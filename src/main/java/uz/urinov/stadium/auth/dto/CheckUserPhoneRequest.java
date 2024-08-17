package uz.urinov.stadium.auth.dto;

import jakarta.validation.constraints.Pattern;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CheckUserPhoneRequest {

    @NonNull
    @Pattern(regexp = "^\\+998\\d{9}$", message = "Noto'g'ri telefon raqami")
    private String phone;
}
