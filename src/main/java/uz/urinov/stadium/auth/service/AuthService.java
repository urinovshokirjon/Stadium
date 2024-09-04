package uz.urinov.stadium.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Service;
import uz.urinov.stadium.auth.dto.*;
import uz.urinov.stadium.auth.enums.Language;
import uz.urinov.stadium.auth.enums.StatusAuth;
import uz.urinov.stadium.Profile.dto.ProfileCreateDTO;
import uz.urinov.stadium.Profile.dto.ProfileResponseDTO;
import uz.urinov.stadium.Profile.entity.ProfileEntity;
import uz.urinov.stadium.Profile.enums.ProfileStatus;
import uz.urinov.stadium.Profile.repository.ProfileRepository;
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
            response.setStatus(StatusAuth.NOT_FOUND);
            return response;
        }
        ProfileEntity profileEntity = optionalProfile.get();
        if (!profileEntity.getVisible()) {
            response.setStatus(StatusAuth.BLOCKED);
            return response;
        }
        if (profileEntity.getStatus().equals(ProfileStatus.INACTIVE)) {
            response.setStatus(StatusAuth.INACTIVE);
            return response;
        }
        response.setStatus(StatusAuth.ACTIVE);
        return response;

    }

    // Profile registration Sms
    public Result registrationSms(ProfileCreateDTO dto, Language lang) {
        Optional<ProfileEntity> optionalProfile = profileRepository.findByPhone(dto.getPhone());
        if (optionalProfile.isPresent()) {
            log.warn("Ismi name = {}, phone = {}", dto.getName(), dto.getPhone());
            String message=rbms.getMessage("phone.exists",null, new Locale(lang.name()));
            return new Result(message, false);
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
        String response=rbms.getMessage("active.do",null, new Locale(lang.name()));
        return new Result(response, true);

    }

    // Profile verifySms
    public Result verifySms(VerifyDto dto, Language lang) {
        Optional<SmsHistoryEntity> bySmsCodeAndPhone = smsHistoryRepository.findBySmsCodeAndPhone(dto.getSmsCode(), dto.getPhone());
        if (bySmsCodeAndPhone.isEmpty()) {
            String response=rbms.getMessage("password.wrong",null, new Locale(lang.name()));
            return new Result(response, false);
        }
        ProfileEntity entity = checkPhone(dto.getPhone(),lang);
        entity.setStatus(ProfileStatus.ACTIVE);
        profileRepository.save(entity);
        String response=rbms.getMessage("active",null, new Locale(lang.name()));
        return new Result(entity.getName()+" "+response, true);
    }

    // Resent sms code
    public Result verificationResendSms(String phone,Language lang) {

        ProfileEntity profileEntity = checkPhone(phone,lang);

        if (!profileEntity.getVisible() || !profileEntity.getStatus().equals(ProfileStatus.INACTIVE)) {
            String response=rbms.getMessage("registration.not",null, new Locale(lang.name()));
            throw new AppBadException(response);
        }
        smsHistoryService.checkEmailLimit(profileEntity.getPhone());
        String smsCode = RandomUtil.getRandomSmsCode();
//        String smsCode = "Bu Eskiz dan test";    // TODO: Phone ga code ketadigan qilish kerak;
        smsService.sendSms(profileEntity.getPhone(), smsCode);
        String response=rbms.getMessage("active.do",null, new Locale(lang.name()));
        return new Result(response, true);
    }


    // Profile login
    public ProfileResponseDTO loginProfile(LoginDto loginDto,Language lang) {
        String password = MD5Util.getMD5(loginDto.getPassword());
        Optional<ProfileEntity> profileEntityOptional = profileRepository.findByPhoneAndPasswordAndVisibleTrueAndStatusActive(loginDto.getUsername(), password);
        if (profileEntityOptional.isEmpty()) {
            log.warn("Profile phone = {}, password = {},", loginDto.getUsername(), password);
            String response=rbms.getMessage("password.wrong",null, new Locale(lang.name()));
            throw new AppBadException(response);
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
    public Result forget(CheckUserPhoneRequest dto,Language lang) {

        ProfileEntity profileEntity = checkPhone(dto.getPhone(),lang);
        log.info("Ismi name = {}, phone = {}", profileEntity.getName(), profileEntity.getPhone());

        // Sms yuborish methodini chaqiramiz;
        String message = RandomUtil.getRandomSmsCode();
//        String smsCode = "Bu Eskiz dan test";    // TODO: Phone ga code ketadigan qilish kerak;
        smsService.sendSms(profileEntity.getPhone(), message);
        String response=rbms.getMessage("active.do",null, new Locale(lang.name()));
        return new Result(response, true);
    }

    // Forget User password update Request
    public Result forgetUpdatePassword(ForgetDto dto,Language lang) {
        ProfileEntity profileEntity = checkPhone(dto.getPhone(),lang);

        Optional<SmsHistoryEntity> bySmsCodeAndPhone = smsHistoryRepository.findBySmsCodeAndPhone(dto.getSmsCode(), dto.getPhone());
        if (bySmsCodeAndPhone.isEmpty()) {
            String response=rbms.getMessage("message.or.phone.wrong",null, new Locale(lang.name()));
            return new Result(response, false);
        }
        profileEntity.setPassword(MD5Util.getMD5(dto.getNewPassword()));
        profileRepository.save(profileEntity);
        String response=rbms.getMessage("do.not.you.password",null, new Locale(lang.name()));
        return new Result(response, true);
    }

    public ProfileEntity checkPhone(String phone,Language lang) {
        return profileRepository.findByPhoneAndVisibleTrue(phone).orElseThrow(() -> {
            log.warn("Profile not found id : {}", phone);
            String response=rbms.getMessage("item.not.found",null, new Locale(lang.name()));
            throw new AppBadException(response);
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
