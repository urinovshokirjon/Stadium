package uz.urinov.stadium.stadium.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.urinov.stadium.auth.enums.Language;
import uz.urinov.stadium.stadium.dto.BookingFieldCreateDto;
import uz.urinov.stadium.stadium.dto.BookingResponseDto;
import uz.urinov.stadium.stadium.enums.BookingStatus;
import uz.urinov.stadium.stadium.service.FieldBookingService;
import uz.urinov.stadium.util.Result;

import java.util.List;

@RestController
@RequestMapping("/booking")
@RequiredArgsConstructor
public class FieldBookingController {
    private final FieldBookingService fieldBookingService;

    // Field order
    @PostMapping("/order")
    public ResponseEntity<Result> fieldBookingOrder(@Valid @RequestBody BookingFieldCreateDto dto,
                                                    @RequestHeader(value = "Accept-Language", defaultValue = "UZ") Language lang) {
        Result response = fieldBookingService.fieldBookingOrder(dto, lang);
        return ResponseEntity.status(response.isSuccess() ? 201 : 409).body(response);
    }

    // Field cancellation user
    @PutMapping("/cancellation-user/{id}")
    public ResponseEntity<Result> fieldBookingIdCancellationUser(@PathVariable(value = "id") String bookingId,
                                                                 @RequestHeader(value = "Accept-Language", defaultValue = "UZ") Language lang) {
        Result response = fieldBookingService.fieldBookingIdCancellationUser(bookingId, lang);
        return ResponseEntity.status(response.isSuccess() ? 201 : 409).body(response);
    }

    // Field cancellation owner
    @PutMapping("/cancellation-owner/{id}")
    public ResponseEntity<Result> fieldBookingIdCancellationOwner(@PathVariable(value = "id") String bookingId,
                                                                  @RequestHeader(value = "Accept-Language", defaultValue = "UZ") Language lang) {
        Result response = fieldBookingService.fieldBookingIdCancellationOwner(bookingId, lang);
        return ResponseEntity.status(response.isSuccess() ? 201 : 409).body(response);
    }

    // Field order user
    @GetMapping("/get-order-user")
    public ResponseEntity<List<BookingResponseDto>> getOrderUser(@RequestParam(defaultValue = "BOOKED") BookingStatus status,
                                                                 @RequestHeader(value = "Accept-Language", defaultValue = "UZ") Language lang) {
        List<BookingResponseDto> response = fieldBookingService.getOrderUser(status, lang);
        return ResponseEntity.ok().body(response);

    }

    // Field order owner field id
    @GetMapping("/own/get-order-owner-field-id/{fieldId}")
    public ResponseEntity<List<BookingResponseDto>> getOrderOwnerFieldId(@PathVariable(value = "fieldId") Integer fieldId,
                                                                         @RequestHeader(value = "Accept-Language", defaultValue = "UZ") Language lang) {
        List<BookingResponseDto> response = fieldBookingService.getOrderOwnerFieldId(fieldId, lang);
        return ResponseEntity.ok().body(response);

    }

    // Field order owner stadium id
    @GetMapping("/own/get-order-owner-stadium-id/{stadiumId}")
    public ResponseEntity<List<BookingResponseDto>> getOrderOwnerStadiumId(@PathVariable(value = "stadiumId") Integer stadiumId,
                                                                           @RequestHeader(value = "Accept-Language", defaultValue = "UZ") Language lang) {
        List<BookingResponseDto> response = fieldBookingService.getOrderOwnerStadiumId2(stadiumId, lang);
        return ResponseEntity.ok().body(response);

    }


}
