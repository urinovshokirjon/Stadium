package uz.urinov.stadium.Auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.urinov.stadium.Prifile.*;
import uz.urinov.stadium.exp.AppBadException;
import uz.urinov.stadium.sms.SmsHistoryEntity;
import uz.urinov.stadium.sms.SmsHistoryRepository;
import uz.urinov.stadium.sms.SmsHistoryService;
import uz.urinov.stadium.sms.SmsService;
import uz.urinov.stadium.util.JWTUtil;
import uz.urinov.stadium.util.MD5Util;
import uz.urinov.stadium.util.RandomUtil;
import uz.urinov.stadium.util.Result;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final ProfileRepository profileRepository;
    private final SmsService smsService;
    private final SmsHistoryRepository smsHistoryRepository;
    private final SmsHistoryService smsHistoryService;

    // CheckUserPhoneRequest
    public Result checkUserPhone(CheckUserPhoneResponse dto) {
        Boolean existsByPhone = profileRepository.existsByPhone(dto.getPhone());
        if (existsByPhone) {
            return new Result("Bunday telefon mavjud. Loginga jo'nating",true);
        }
        return new Result("Bunday telefon mavjud emas. Registrationga jo'nating",false);
    }

    // Profile registration Sms
    public Result registrationSms(ProfileCreateDTO dto) {
        Boolean existsByPhone = profileRepository.existsByPhone(dto.getPhone());
        if(existsByPhone) {
            log.warn("Ismi name = {}, phone = {}", dto.getName(), dto.getPhone());
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
        String smsCode = "Bu Eskiz dan test";    // TODO: Phone ga code ketadigan qilish kerak;
        smsService.sendSms(dto.getPhone(), smsCode);
        return new Result("Muvaffaqiyatli ro'yxatdan o'tdingiz. Akkounting ACTIVE qilish uchun telefoningizga borgan sms code tasdiqlang", true);

    }

    // Profile verifySms
    public Result verifySms( VerifyDto dto) {
        Optional<SmsHistoryEntity> bySmsCodeAndPhone = smsHistoryRepository.findBySmsCodeAndPhone(dto.getSmsCode(), dto.getPhone());
        if(bySmsCodeAndPhone.isEmpty()) {
            return new Result("Telefon phone yoki smsCode noto'g'ri", false);
        }
        Optional<ProfileEntity> entityOptional = profileRepository.findByPhoneAndVisibleTrue(dto.getPhone());
        if(entityOptional.isEmpty()) {
            return new Result("Telefonsizning telefon raqamingiz bloclangan ", false);
        }
        ProfileEntity entity = entityOptional.get();
        entity.setStatus(ProfileStatus.ACTIVE);
        profileRepository.save(entity);
        return new Result("Profile ACTIVE holatga o'tdi", true);
    }

    // Resent sms code
    public Result verificationResendSms(String phone) {

        Optional<ProfileEntity> profileEntityOptional = profileRepository.findByPhoneAndVisibleTrue(phone);
        if (profileEntityOptional.isEmpty()) {
            throw new AppBadException("Phone not exists");
        }

        ProfileEntity profileEntity = profileEntityOptional.get();

        if (!profileEntity.getVisible() || !profileEntity.getStatus().equals(ProfileStatus.INACTIVE)) {
            throw new AppBadException("Registration not completed");
        }
        smsHistoryService.checkEmailLimit(profileEntity.getPhone());
//        String smsCode = UUID.randomUUID().toString();
        String smsCode = "Bu Eskiz dan test";    // TODO: Phone ga code ketadigan qilish kerak;
        smsService.sendSms(profileEntity.getPhone(), smsCode);
        return new Result("To complete your registration please verify your phone.", true);
    }


    // Profile login
    public ProfileResponseDTO loginProfile(LoginDto loginDto) {
        String password = MD5Util.getMD5(loginDto.getPassword());
        Optional<ProfileEntity> profileEntityOptional = profileRepository.findByPhoneAndPasswordAndVisibleTrueAndStatusActive(loginDto.getUsername(), password);
        if (profileEntityOptional.isEmpty()) {
            log.warn("Profile phone = {}, password = {},", loginDto.getUsername(), password);
            return null;
        }
        ProfileEntity profileEntity = profileEntityOptional.get();
        ProfileResponseDTO profileResponseDTO = new ProfileResponseDTO();
        profileResponseDTO.setId(profileEntity.getId());
        profileResponseDTO.setPhone(profileEntity.getPhone());
        profileResponseDTO.setRole(profileEntity.getRole().toString());
        profileResponseDTO.setStatus(profileEntity.getStatus().toString());
        profileResponseDTO.setJwt(JWTUtil.encode(profileEntity.getId(),profileEntity.getPhone(),profileEntity.getRole()));
        return profileResponseDTO;

    }


}
