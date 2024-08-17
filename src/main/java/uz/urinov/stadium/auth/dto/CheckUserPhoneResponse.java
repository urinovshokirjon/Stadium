package uz.urinov.stadium.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.urinov.stadium.auth.enums.Status;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckUserPhoneResponse {
    private Status status;
}
