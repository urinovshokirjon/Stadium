package uz.urinov.stadium.stadium.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import uz.urinov.stadium.Profile.entity.ProfileEntity;

import java.util.List;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FieldCreateDto {

    @NotBlank(message = "Name bo'sh bo'lishi mumkin emas")
    @Size(min = 3, max = 50, message = "Berilgan field (Name) ning uzunligi 3 va 50 orasida bo'lishi kerak")
    private String name;

    @NotBlank(message = "Description bo'sh bo'lishi mumkin emas")
    @Size(min = 3, message = "Field haqida to'liqroq ma'lumot bo'lishi kerak")
    private String description;

    @NotNull(message = " Field type id bo'sh bo'lishi mumkin emas")
    @Min(value = 1, message = "Field type id  ning qiymati minimal 1 bo'lsin")
    private Integer fieldTypeId;

    @NotNull(message = " Stadium id bo'sh bo'lishi mumkin emas")
    @Min(value = 1, message = "Stadium id  ning qiymati minimal 1 bo'lsin")
    private Integer stadiumId;

    @NotNull
    private List<String> photoList;

}
