package uz.urinov.stadium.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Service;
import uz.urinov.stadium.auth.dto.*;
import uz.urinov.stadium.auth.enums.Language;
import uz.urinov.stadium.auth.enums.Status;
import uz.urinov.stadium.Profile.dto.ProfileCreateDTO;
import uz.urinov.stadium.Profile.dto.ProfileResponseDTO;
import uz.urinov.stadium.Profile.entity.ProfileEntity;
import uz.urinov.stadium.Profile.enums.ProfileStatus;
import uz.urinov.stadium.Profile.repository.ProfileRepository;
import uz.urinov.stadium.config.RecourseBundleConfig;
import uz.urinov.stadium.exp.AppBadException;
import uz.urinov.stadium.sms.entity.SmsHistoryEntity;
import uz.urinov.stadium.sms.repository.SmsHistoryRepository;
import uz.urinov.stadium.sms.service.SmsHistoryService;
import uz.urinov.stadium.sms.service.SmsService;
import uz.urinov.stadium.util.*;

import java.util.Locale;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final ProfileRepository profileRepository;
    private final SmsService smsService;
    private final SmsHistoryRepository smsHistoryRepository;
    private final SmsHistoryService smsHistoryService;
    private final ResourceBundleMessageSource rbms;

    // CheckUserPhoneRequest
    public CheckUserPhoneResponse checkUserPhone(CheckUserPhoneRequest dto) {
        CheckUserPhoneResponse response = new CheckUserPhoneResponse();
        Optional<ProfileEntity> optionalProfile = profileRepository.findByPhone(dto.getPhone());

        if (optionalProfile.isEmpty()) {
            response.setStatus(Status.NOT_FOUND);
            return response;
        }
        ProfileEntity profileEntity = optionalProfile.get();
        if (!profileEntity.getVisible()) {
            response.setStatus(Status.BLOCKED);
            return response;
        }
        if (profileEntity.getStatus().equals(ProfileStatus.INACTIVE)) {
            response.setStatus(Status.INACTIVE);
            return response;
        }
        response.setStatus(Status.ACTIVE);
        return response;

    }

    // Profile registration Sms
    public Result registrationSms(ProfileCreateDTO dto, Language lang) {
        Optional<ProfileEntity> optionalProfile = profileRepository.findByPhone(dto.getPhone());
        if (optionalProfile.isPresent()) {
            log.warn("Ismi name = {}, phone = {}", dto.getName(), dto.getPhone());
            String message=rbms.getMessage("phone.exists",null, new Locale(lang.name()));
            return new Result("Bunday telefon  oldin ro'yxatga olingan", false);
        }
        ProfileEntity entity = new ProfileEntity();
        entity.setName(dto.getName());
        entity.setSurname(dto.getSurname());
        entity.setPhone(dto.getPhone());
        entity.setPassword(MD5Util.getMD5(dto.getPassword()));
        entity.setRole(dto.getRole());
        entity.setStatus(ProfileStatus.INACTIVE);
        profileRepository.save(entity);

        log.info("Ismi name = {}, phone = {}", dto.getName(), dto.getPhone());

        // Sms yuborish methodini chaqiramiz;
        String message = RandomUtil.getRandomSmsCode();
//        String smsCode = "Bu Eskiz dan test";    // TODO: Phone ga code ketadigan qilish kerak;
        smsService.sendSms(dto.getPhone(), message);
        return new Result("Muvaffaqiyatli ro'yxatdan o'tdingiz. Akkounting ACTIVE qilish uchun telefoningizga borgan sms code tasdiqlang", true);

    }

    // Profile verifySms
    public Result verifySms(VerifyDto dto) {
        Optional<SmsHistoryEntity> bySmsCodeAndPhone = smsHistoryRepository.findBySmsCodeAndPhone(dto.getSmsCode(), dto.getPhone());
        if (bySmsCodeAndPhone.isEmpty()) {
            return new Result("Telefon phone yoki smsCode noto'g'ri", false);
        }
        ProfileEntity entity = checkPhone(dto.getPhone());
        entity.setStatus(ProfileStatus.ACTIVE);
        profileRepository.save(entity);
        return new Result("Profile ACTIVE holatga o'tdi", true);
    }

    // Resent sms code
    public Result verificationResendSms(String phone) {

        ProfileEntity profileEntity = checkPhone(phone);

        if (!profileEntity.getVisible() || !profileEntity.getStatus().equals(ProfileStatus.INACTIVE)) {
            throw new AppBadException("Registration not completed");
        }
        smsHistoryService.checkEmailLimit(profileEntity.getPhone());
        String smsCode = RandomUtil.getRandomSmsCode();
//        String smsCode = "Bu Eskiz dan test";    // TODO: Phone ga code ketadigan qilish kerak;
        smsService.sendSms(profileEntity.getPhone(), smsCode);
        return new Result("To complete your registration please verify your phone.", true);
    }


    // Profile login
    public ProfileResponseDTO loginProfile(LoginDto loginDto) {
        String password = MD5Util.getMD5(loginDto.getPassword());
        Optional<ProfileEntity> profileEntityOptional = profileRepository.findByPhoneAndPasswordAndVisibleTrueAndStatusActive(loginDto.getUsername(), password);
        if (profileEntityOptional.isEmpty()) {
            log.warn("Profile phone = {}, password = {},", loginDto.getUsername(), password);
            throw new AppBadException("Profile phone or password is incorrect");
        }
        ProfileEntity profileEntity = profileEntityOptional.get();
        ProfileResponseDTO profileResponseDTO = new ProfileResponseDTO();
        profileResponseDTO.setId(profileEntity.getId());
        profileResponseDTO.setPhone(profileEntity.getPhone());
        profileResponseDTO.setRole(profileEntity.getRole().toString());
        profileResponseDTO.setStatus(profileEntity.getStatus().toString());
        profileResponseDTO.setDayJwt(JWTUtil.encodeDay(profileEntity.getId(), profileEntity.getPhone(), profileEntity.getRole()));
        profileResponseDTO.setMonthJwt(JWTUtil.encodeMonth(profileEntity.getId(), profileEntity.getPhone(), profileEntity.getRole()));
        return profileResponseDTO;

    }

    // ForgetUserPhoneRequest
    public Result forget(CheckUserPhoneRequest dto) {

        ProfileEntity profileEntity = checkPhone(dto.getPhone());
        log.info("Ismi name = {}, phone = {}", profileEntity.getName(), profileEntity.getPhone());

        // Sms yuborish methodini chaqiramiz;
        String message = RandomUtil.getRandomSmsCode();
//        String smsCode = "Bu Eskiz dan test";    // TODO: Phone ga code ketadigan qilish kerak;
        smsService.sendSms(profileEntity.getPhone(), message);
        return new Result("Muvaffaqiyatli ro'yxatdan o'tdingiz. Akkounting ACTIVE qilish uchun telefoningizga borgan sms code tasdiqlang", true);
    }

    // Forget User password update Request
    public Result forgetUpdatePassword(ForgetDto dto) {
        ProfileEntity profileEntity = checkPhone(dto.getPhone());

        Optional<SmsHistoryEntity> bySmsCodeAndPhone = smsHistoryRepository.findBySmsCodeAndPhone(dto.getSmsCode(), dto.getPhone());
        if (bySmsCodeAndPhone.isEmpty()) {
            return new Result("Telefon phone yoki smsCode noto'g'ri", false);
        }
        profileEntity.setPassword(MD5Util.getMD5(dto.getNewPassword()));
        profileRepository.save(profileEntity);
        return new Result("Parolingizni esdan chiqarmang", true);
    }

    public ProfileEntity checkPhone(String phone) {
        return profileRepository.findByPhoneAndVisibleTrue(phone).orElseThrow(() -> {
            log.warn("Profile not found id : {}", phone);
            throw new AppBadException("Bunday profile topilmadi");
        });
    }

    public TokenResponse refreshToken(RefreshTokenRequest request) {
        JwtDTO decode = JWTUtil.decode(request.getRefreshToken());
        if (decode.isRefreshToken()) {
            String accesseToken = JWTUtil.encodeDay(decode.getId(), decode.getUsername(), decode.getRole());
            String refreshToken = JWTUtil.encodeMonth(decode.getId(), decode.getUsername(), decode.getRole());
            return new TokenResponse(accesseToken, refreshToken);
        }
        return null;
    }
}
