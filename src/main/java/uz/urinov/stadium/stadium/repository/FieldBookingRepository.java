package uz.urinov.stadium.stadium.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.urinov.stadium.stadium.entity.FieldBookingEntity;
import uz.urinov.stadium.stadium.enums.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface FieldBookingRepository extends JpaRepository<FieldBookingEntity, String> {

    @Query(value = "SELECT * FROM field_booking " +
            " WHERE field_id = ?1 " +
            " AND status  IN (?4) " +
            " AND( " +
            "( ?2 >= start_time AND ?2 < end_time )" +
            " OR (?3 > start_time AND ?3 <= end_time )" +
            " OR (?2 <= start_time AND ?3 >= end_time)" +
            ")", nativeQuery = true)
    List<FieldBookingEntity> findByFieldIdAndStartTimeBeforeAndEndTimeAfter(Integer fieldId, LocalDateTime startTime, LocalDateTime endTime,List<String> statusList);


    Optional<FieldBookingEntity> findByIdAndVisibleTrue(String id);

     List<FieldBookingEntity> findAllByCustomerIdAndStatusAndVisibleTrue(String customerId, BookingStatus status);
     List<FieldBookingEntity> findAllByFieldIdAndVisibleTrue(Integer fieldId);

    @Query("SELECT fb FROM FieldBookingEntity fb " +
            "WHERE fb.id = ?1 AND fb.customerId = ?2 AND " +
            "fb.status IN (?3) " +
            "ORDER BY fb.createDate")
    Optional<FieldBookingEntity> fieldBookingIdCancellationUser(String id, String customerId, List<String> statusList);

    @Query("SELECT fb FROM FieldBookingEntity fb " +
            "WHERE fb.id = ?1 AND " +
            "fb.status IN (?2) " +
            "ORDER BY fb.createDate")
    Optional<FieldBookingEntity> fieldBookingIdCancellationOwner(String id, List<String> statusList);




    List<FieldBookingEntity> findAllByFieldIdInAndVisibleTrue(List<Integer> list);

}
