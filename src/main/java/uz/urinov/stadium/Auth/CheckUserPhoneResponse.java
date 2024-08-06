package uz.urinov.stadium.Auth;

import jakarta.validation.constraints.Pattern;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CheckUserPhoneResponse {

    @NonNull
    @Pattern(regexp = "^\\+998\\d{9}$", message = "Noto'g'ri telefon raqami")
    private String phone;
}
