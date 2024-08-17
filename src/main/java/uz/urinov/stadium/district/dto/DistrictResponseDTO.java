package uz.urinov.stadium.district.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDate;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DistrictResponseDTO {

    private Integer id;

    private Integer regionId;

    private String nameUz;

    private String nameRu;

    private String nameEn;

    private String name;

    private Boolean visible;

    private LocalDate createDate;
}
