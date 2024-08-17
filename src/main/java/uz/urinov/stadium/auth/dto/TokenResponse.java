package uz.urinov.stadium.auth.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TokenResponse {
    String accessToken;
    String refreshToken;
}
