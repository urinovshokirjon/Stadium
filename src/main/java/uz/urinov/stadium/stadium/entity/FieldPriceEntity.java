package uz.urinov.stadium.stadium.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import uz.urinov.stadium.Profile.entity.ProfileEntity;

import java.time.LocalDate;

@Setter
@Getter
@Entity
@Table(name = "field_price", uniqueConstraints = {@UniqueConstraint(columnNames = {"field_id", "owner_id"})
})
public class FieldPriceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "average_rating")
    private Integer averageRating = 0;

    @Column(name = "owner_id")
    private String ownerId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id",insertable = false, updatable = false)
    private ProfileEntity owner;

    @Column(name = "field_id")
    private Integer fieldId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "field_id",insertable = false, updatable = false)
    private FieldEntity field;

    @Column(name = "visible")
    private Boolean visible=Boolean.TRUE;

    @Column(name = "create_date")
    private LocalDate createDate=LocalDate.now();

}
