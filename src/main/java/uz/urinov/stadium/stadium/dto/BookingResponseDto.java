package uz.urinov.stadium.stadium.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import uz.urinov.stadium.stadium.enums.BookingStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookingResponseDto {

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private BookingStatus status;


    private StadiumResponseMiniDto stadiumResponseMiniDto;
    private FieldResponseMiniDto fieldResponseMiniDto;

    private LocalDateTime createDate;

}
