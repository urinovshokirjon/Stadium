package uz.urinov.stadium.sms;

import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface SmsHistoryRepository extends CrudRepository<SmsHistoryEntity,String> {

    Optional<SmsHistoryEntity> findBySmsCodeAndPhone(String message, String phone);

    Optional<SmsHistoryEntity> findTopByPhoneOrderByCreateDateDesc(String phone);

    // countByEmailAndCreateDateBetween
    Long countByPhoneAndCreateDateBetween(String phone, LocalDateTime from, LocalDateTime to);
}
