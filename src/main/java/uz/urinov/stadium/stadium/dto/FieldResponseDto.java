package uz.urinov.stadium.stadium.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FieldResponseDto {

    private Integer stadiumId;

    private Integer id;

    private String name;

    private String description;

    private Double rating;

    private List<String> photolist;

    private FieldTypeResponseDto fieldTypeResponseDto;

    private Boolean visible;

    private LocalDate createDate;
}
