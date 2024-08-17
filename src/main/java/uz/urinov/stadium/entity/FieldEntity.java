package uz.urinov.stadium.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import uz.urinov.stadium.Profile.entity.ProfileEntity;

import java.time.LocalDate;

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

    @Column(name = "owner_id")
    private String ownerId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id",insertable = false, updatable = false)
    private ProfileEntity owner;


    @Column(name = "visible")
    private Boolean visible=Boolean.TRUE;

    @Column(name = "create_date")
    private LocalDate createDate=LocalDate.now();
}
