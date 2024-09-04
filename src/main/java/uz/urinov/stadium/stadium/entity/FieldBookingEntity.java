package uz.urinov.stadium.stadium.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;
import uz.urinov.stadium.Profile.entity.ProfileEntity;
import uz.urinov.stadium.stadium.enums.BookingStatus;
import uz.urinov.stadium.stadium.enums.Status;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "field_booking")
public class FieldBookingEntity {
    @Id
    @UuidGenerator
    private String id;

    @Column(name = "field_id")
    private Integer fieldId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "field_id",insertable = false, updatable = false)
    private FieldEntity field;

    @Column(name = "customer_id")
    private String customerId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id",insertable = false, updatable = false)
    private ProfileEntity customer;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    private BookingStatus status = BookingStatus.CHECKING;

    @Column(name = "visible")
    private Boolean visible=Boolean.TRUE;

    @Column(name = "create_date")
    private LocalDateTime createDate=LocalDateTime.now();

}
