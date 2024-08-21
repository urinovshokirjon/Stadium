package uz.urinov.stadium.auth.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.urinov.stadium.auth.dto.*;
import uz.urinov.stadium.auth.enums.Language;
import uz.urinov.stadium.auth.service.AuthService;
import uz.urinov.stadium.Profile.dto.ProfileCreateDTO;
import uz.urinov.stadium.Profile.dto.ProfileResponseDTO;
import uz.urinov.stadium.util.Result;

@Slf4j
@RestController
@RequestMapping("/auth/mobile/client/v1/account")
@Tag(name = "Auth Controller", description = "Api list for authorization, registration and other ... ")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // CheckUserPhoneRequest
    @PostMapping("/check")
    public ResponseEntity<CheckUserPhoneResponse> checkUserPhone(@Valid @RequestBody CheckUserPhoneRequest dto) {
        CheckUserPhoneResponse result = authService.checkUserPhone(dto);
        return ResponseEntity.ok().body(result);
    }

    // Profile registration Sms
    @PostMapping("/registration-sms")
    public ResponseEntity<Result> registrationSms(@Valid @RequestBody ProfileCreateDTO dto,
                                                  @RequestHeader(value = "Accept-Language",defaultValue = "UZ") Language lang) {
        Result result = authService.registrationSms(dto,lang);
        return ResponseEntity.status(result.isSuccess() ? 200 : 409).body(result);
    }

    // Profile verifySms
    @PostMapping("/verifySms")
    public ResponseEntity<Result> verifySms(@Valid @RequestBody VerifyDto dto,
                                            @RequestHeader(value = "Accept-Language",defaultValue = "UZ") Language lang) {
        Result result = authService.verifySms(dto,lang);
        return ResponseEntity.status(result.isSuccess() ? 200 : 409).body(result);
    }

    // Resent sms code
    @PostMapping("/verification/resendSma/{phone}")
    public ResponseEntity<Result> verificationResendSms(@PathVariable String phone,
                                                        @RequestHeader(value = "Accept-Language",defaultValue = "UZ") Language lang) {
        Result result = authService.verificationResendSms(phone,lang);
        return ResponseEntity.status(result.isSuccess() ? 200 : 409).body(result);
    }

    // Profile login
    @PostMapping("/login")
    public HttpEntity<ProfileResponseDTO> loginUser(@RequestBody LoginDto loginDto,
                                                    @RequestHeader(value = "Accept-Language",defaultValue = "UZ") Language lang) {
         ProfileResponseDTO result = authService.loginProfile(loginDto,lang);
        return ResponseEntity.ok().body(result);
    }
    // Profile login
    @PostMapping("/refresh-token")
    public HttpEntity<TokenResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
        TokenResponse result = authService.refreshToken(request);
        return ResponseEntity.ok().body(result);
    }

    // Forget User password Request
    @PostMapping("/forget")
    public ResponseEntity<Result> forget(@Valid @RequestBody CheckUserPhoneRequest dto,
                                         @RequestHeader(value = "Accept-Language",defaultValue = "UZ") Language lang) {
        Result result = authService.forget(dto,lang);
        return ResponseEntity.status(result.isSuccess() ? 200 : 409).body(result);
    }

    // Forget User password update Request
    @PostMapping("/forget-update-password")
    public ResponseEntity<Result> forgetUpdatePassword(@Valid @RequestBody ForgetDto dto,
                                                       @RequestHeader(value = "Accept-Language",defaultValue = "UZ") Language lang) {
        Result result = authService.forgetUpdatePassword(dto,lang);
        return ResponseEntity.status(result.isSuccess() ? 200 : 409).body(result);
    }


}
