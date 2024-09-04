package uz.urinov.stadium.stadium.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookingFieldCreateDto {

    @NotNull(message = " Field id bo'sh bo'lishi mumkin emas")
    @Min(value = 1, message = "Field id  ning qiymati minimal 1 bo'lsin")
    private Integer fieldId;

    @NotNull
    private LocalDateTime startTime;

    @NotNull
    private LocalDateTime endTime;


}
