package uz.urinov.stadium.region.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDate;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RegionResponseDTO {

    private Integer id;

    private String nameUz;

    private String nameEn;

    private String nameRu;

    private String nameKr;

    private String name;

    private Boolean visible;

    private LocalDate createDate;
}
