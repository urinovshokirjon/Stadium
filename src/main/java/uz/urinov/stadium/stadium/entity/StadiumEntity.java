package uz.urinov.stadium.stadium.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import uz.urinov.stadium.Profile.entity.ProfileEntity;
import uz.urinov.stadium.attach.entity.AttachEntity;
import uz.urinov.stadium.district.entity.DistrictEntity;
import uz.urinov.stadium.region.entity.RegionEntity;
import uz.urinov.stadium.stadium.enums.Status;

import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "stadium")
public class StadiumEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name",length = 50)
    private String name;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Column(name = "lat")
    private Double lat;

    @Column(name = "lon")
    private Double lon;

    @Column(name = "district_id")
    private Integer districtId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "district_id", insertable = false, updatable = false)
    private DistrictEntity district;

    @Column(name = "profile_id")
    private String profileId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", insertable = false, updatable = false)
    private ProfileEntity ownerId;

    @Enumerated(EnumType.STRING)
    private Status status = Status.INACTIVE;

    @Column(name = "visible")
    private Boolean visible = Boolean.FALSE;

    @Column(name = "create_date")
    private LocalDate createDate = LocalDate.now();

}
