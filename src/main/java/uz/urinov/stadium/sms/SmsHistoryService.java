package uz.urinov.stadium.sms;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.urinov.stadium.Prifile.ProfileEntity;
import uz.urinov.stadium.Prifile.ProfileRepository;
import uz.urinov.stadium.exp.AppBadException;


import java.time.LocalDateTime;
import java.util.Optional;
@Slf4j
@Service
@RequiredArgsConstructor
public class SmsHistoryService {

    private final ProfileRepository profileRepository;
    private final SmsHistoryRepository smsHistoryRepository;

    public void createSmsHistory(String smsCode, String phone) {
        Optional<ProfileEntity> entityOptionalPhone = profileRepository.findByPhoneAndVisibleTrue(phone);
        if (entityOptionalPhone.isEmpty()) {
            throw new AppBadException("Phone already exists");
        }

        SmsHistoryEntity smsHistoryEntity = new SmsHistoryEntity();
        smsHistoryEntity.setProfileId(entityOptionalPhone.get().getId());
        smsHistoryEntity.setSmsCode(smsCode);
        smsHistoryEntity.setPhone(phone);
        smsHistoryEntity.setCreateDate(LocalDateTime.now());
        smsHistoryRepository.save(smsHistoryEntity);

    }

    public void checkEmailLimit(String phone) {

        LocalDateTime to = LocalDateTime.now();
        LocalDateTime from = to.minusMinutes(2);

        long count = smsHistoryRepository.countByPhoneAndCreateDateBetween(phone, from, to);
        log.warn("Profile phone = {}", phone);
        if (count >= 3) {

            throw new AppBadException("Sms limit reached. Please try after some time");
        }
    }

}
