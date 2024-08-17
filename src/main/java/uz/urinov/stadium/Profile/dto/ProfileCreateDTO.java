package uz.urinov.stadium.Profile.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import uz.urinov.stadium.Profile.enums.ProfileRole;

@Getter
@Setter
public class ProfileCreateDTO {

    @Size(min = 3, max = 50, message = "Berilgan name uzunligi 3 va 50 orasida bo'lishi kerak")
    @NotBlank(message = "name bo'sh bo'lishi mumkin emas")
    private String name;

    @Size(min = 3, max = 50, message = "Berilgan surname uzunligi 3 va 50 orasida bo'lishi kerak")
    @NotBlank(message = "Surname bo'sh bo'lishi mumkin emas")
    private String surname;


    @Pattern(regexp = "^\\+998\\d{9}$", message = "Noto'g'ri telefon raqami")
    @NotBlank(message = "Phone bo'sh bo'lishi mumkin emas")
    private String phone;


    @Size(min = 4, max = 50, message = "Berilgan password uzunligi 3 va 50 orasida bo'lishi kerak")
    @NotBlank(message = "Password bo'sh bo'lishi mumkin emas")
    private String password;

    @NotNull
    private ProfileRole role;



}
