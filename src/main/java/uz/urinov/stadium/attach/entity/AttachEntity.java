package uz.urinov.stadium.attach.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import uz.urinov.stadium.Profile.entity.ProfileEntity;
import uz.urinov.stadium.stadium.entity.FieldEntity;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "attach")
public class AttachEntity {
    @Id
    private String id;

    @Column(name = "original_name")
    private String originalName;

    @Column
    private String path;

    @Column
    private Long size;

    @Column
    private String extension;

    @Column(name = "created_date")
    private LocalDateTime createdData = LocalDateTime.now();

}
