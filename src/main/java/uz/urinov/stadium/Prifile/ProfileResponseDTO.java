package uz.urinov.stadium.Prifile;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProfileResponseDTO {

    private String id;

    private String name;

    private String surname;

    private String phone;

    private String status;

    private String role;

    private Boolean visible;

    private LocalDateTime createDate;

    private Integer photoId;

    private String jwt;
}
