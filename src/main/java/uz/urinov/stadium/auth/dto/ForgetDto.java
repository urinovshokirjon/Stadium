package uz.urinov.stadium.auth.dto;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ForgetDto {
    @NonNull
    @Pattern(regexp = "^\\+998\\d{9}$", message = "Noto'g'ri telefon raqami")
    private String phone;

    @NonNull
    private String smsCode;

    @NonNull
    private String newPassword;
}
