package uz.urinov.stadium.Prifile;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "profile")
public class ProfileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 50)
    private String name;

    @Column(length = 50)
    private String surname;


    @Column(length = 50, unique = true)
    private String phone;

    @Column(length = 50)
    private String password;

    @Enumerated(EnumType.STRING)
    private ProfileStatus status;

    @Enumerated(EnumType.STRING)
    private ProfileRole role;

    @Column(name = "visible")
    private Boolean visible=Boolean.TRUE;

    @Column(name = "create_date")
    private LocalDateTime createDate=LocalDateTime.now();

    @Column(name = "photo_id",unique = true)
    private String photoId;

}
