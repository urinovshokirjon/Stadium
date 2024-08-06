package uz.urinov.stadium.sms;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;
import uz.urinov.stadium.Prifile.ProfileEntity;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "sms_history")
public class SmsHistoryEntity {

    @Id
    @UuidGenerator
    private String id;

    @NotBlank
    private String smsCode;

    @NotBlank
    private String phone;

    @Column(name = "profile_id")
    private String profileId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", insertable = false, updatable = false)
    private ProfileEntity profile;

    @Column(name = "create_date")
    private LocalDateTime createDate=LocalDateTime.now();

}
