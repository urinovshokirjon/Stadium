package uz.urinov.stadium.stadium.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import uz.urinov.stadium.district.dto.DistrictResponseDTO;
import uz.urinov.stadium.stadium.enums.Status;

import java.time.LocalDate;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StadiumResponseMiniDto {

    private Integer id;

    private String name;

    private Double lat;

    private Double lon;

}
