package uz.urinov.stadium.Auth;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.urinov.stadium.Prifile.ProfileCreateDTO;
import uz.urinov.stadium.Prifile.ProfileResponseDTO;
import uz.urinov.stadium.util.Result;

@Slf4j
@RestController
@RequestMapping("/auth")
@Tag(name = "Auth Controller", description = "Api list for authorization, registration and other ... ")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // CheckUserPhoneRequest
    @PostMapping("/check-user-phone")
    public ResponseEntity<Result> checkUserPhone(@Valid @RequestBody CheckUserPhoneResponse dto) {
        Result result = authService.checkUserPhone(dto);
        return ResponseEntity.status(result.isSuccess() ? 200 : 409).body(result);
    }

    // Profile registration Sms
    @PostMapping("/registration-sms")
    public ResponseEntity<Result> registrationSms(@Valid @RequestBody ProfileCreateDTO dto) {
        Result result = authService.registrationSms(dto);
        return ResponseEntity.status(result.isSuccess() ? 200 : 409).body(result);
    }
    // Profile verifySms
    @PostMapping("/verifySms")
    public ResponseEntity<Result> verifySms(@Valid @RequestBody VerifyDto dto) {
        Result result = authService.verifySms(dto);
        return ResponseEntity.status(result.isSuccess() ? 200 : 409).body(result);
    }
    // Resent sms code
    @PostMapping("/verification/resendSma/{phone}")
    public ResponseEntity<Result> verificationResendSms(@PathVariable String phone) {
        Result result = authService.verificationResendSms(phone);
        return ResponseEntity.status(result.isSuccess() ? 200 : 409).body(result);
    }

    // Profile login
    @PostMapping("/login")
    public HttpEntity<ProfileResponseDTO> loginUser(@RequestBody LoginDto loginDto) {
        ProfileResponseDTO result = authService.loginProfile(loginDto);
        return ResponseEntity.ok().body(result);
    }


}
