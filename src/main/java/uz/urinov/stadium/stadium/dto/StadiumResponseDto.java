package uz.urinov.stadium.stadium.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import uz.urinov.stadium.district.dto.DistrictResponseDTO;

import java.time.LocalDate;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StadiumResponseDto {

    private Integer id;

    private DistrictResponseDTO district;

    private String description;

    private Double lat;

    private Double lon;

    private Long likeCount;

    private Long dislikeCount;

    private List<String> photolist;

    private List<FieldResponseDto> fieldResponseDtoList;

    private Boolean visible;

    private LocalDate createDate;
}
