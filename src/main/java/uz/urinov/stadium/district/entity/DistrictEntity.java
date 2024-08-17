package uz.urinov.stadium.district.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import uz.urinov.stadium.Profile.entity.ProfileEntity;
import uz.urinov.stadium.region.entity.RegionEntity;

import java.time.LocalDate;

@Setter
@Getter
@Entity
@Table(name = "district")
public class DistrictEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "region_id")
    private Integer regionId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id",insertable = false, updatable = false)
    private RegionEntity region;

    @Column(name = "name_uz", length = 50, unique = true)
    private String nameUz;

    @Column(name = "name_ru", length = 50, unique = true)
    private String nameRu;

    @Column(name = "name_en", length = 50, unique = true)
    private String nameEn;

    @Column(name = "visible")
    private Boolean visible=Boolean.TRUE;

    @Column(name = "create_date")
    private LocalDate createDate=LocalDate.now();

}
