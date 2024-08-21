package uz.urinov.stadium.stadium.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDate;
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FieldTypeResponseDto {

    private Integer id;

    private Integer orderNumber;

    private String nameUz;

    private String nameEn;

    private String nameRu;

    private String nameKr;

    private String name;

    private Boolean visible;

    private LocalDate createDate;
}
