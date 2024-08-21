package uz.urinov.stadium.stadium.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;
import uz.urinov.stadium.Profile.entity.ProfileEntity;
import uz.urinov.stadium.attach.entity.AttachEntity;

import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "field_attach")
public class FieldAttachEntity {
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

    @Column(name = "field_id")
    private Integer fieldId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "field_id",insertable = false, updatable = false)
    private FieldEntity field;

    @Column(name = "create_date")
    private LocalDate createDate=LocalDate.now();
}
