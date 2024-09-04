package uz.urinov.stadium.stadium.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import uz.urinov.stadium.Profile.entity.ProfileEntity;
import uz.urinov.stadium.attach.entity.AttachEntity;
import uz.urinov.stadium.stadium.enums.Status;

import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "field")
public class FieldEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name",length = 50)
    private String name;

    @Column(name = "description",columnDefinition = "text")
    private String description;

    @Column(name = "average_rating")
    private Double averageRating = 0D;

    @Column(name = "rating_count")
    private Integer ratingCount = 0;

    @Column(name = "owner_id")
    private String ownerId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id",insertable = false, updatable = false)
    private ProfileEntity owner;

    @Column(name = "fieldType_id")
    private Integer fieldTypeId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fieldType_id",insertable = false, updatable = false)
    private FieldTypeEntity fieldType;

    @Column(name = "stadium_id")
    private Integer stadiumId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stadium_id",insertable = false, updatable = false)
    private StadiumEntity stadium;

    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVE;


    @Column(name = "visible")
    private Boolean visible=Boolean.TRUE;

    @Column(name = "create_date")
    private LocalDate createDate=LocalDate.now();
}
