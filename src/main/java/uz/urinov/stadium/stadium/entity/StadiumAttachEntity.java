package uz.urinov.stadium.stadium.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;
import uz.urinov.stadium.Profile.entity.ProfileEntity;
import uz.urinov.stadium.attach.entity.AttachEntity;

import java.time.LocalDate;

@Setter
@Getter
@Entity
@Table(name = "stadium_attach")
public class StadiumAttachEntity {
    @Id
    @UuidGenerator
    private String id;

    @Column(name = "owner_id")
    private String ownerId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id",insertable = false, updatable = false)
    private ProfileEntity owner;

    @Column(name = "attach_id")
    private String attachId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attach_id",insertable = false, updatable = false)
    private AttachEntity attach;

    @Column(name = "stadium_id")
    private Integer stadiumId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stadium_id",insertable = false, updatable = false)
    private StadiumEntity stadium;

    @Column(name = "create_date")
    private LocalDate createDate=LocalDate.now();
}
