package uz.urinov.stadium.stadium.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.urinov.stadium.auth.enums.Language;
import uz.urinov.stadium.stadium.dto.FieldCreateDto;
import uz.urinov.stadium.stadium.dto.FieldResponseDto;
import uz.urinov.stadium.stadium.service.FieldService;
import uz.urinov.stadium.util.Result;

import java.util.List;

@SecurityRequirement(name = "Authorization")
@RestController
@RequestMapping("/field")
@RequiredArgsConstructor
public class FieldController {

    private final FieldService fieldService;

    // Field created
    @PostMapping("/created")
    public ResponseEntity<Result> createField(@RequestBody FieldCreateDto dto,
                                              @RequestHeader(value = "Accept-Language", defaultValue = "UZ") Language lang) {
        Result result = fieldService.createField(dto,lang);
        return ResponseEntity.status(result.isSuccess() ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST).body(result);
    }

    // Field update
    @PutMapping("/update/{fieldId}")
    public ResponseEntity<Result> updateField(@PathVariable(value = "fieldId") int fieldId, @Valid @RequestBody FieldCreateDto dto,
                                              @RequestHeader(value = "Accept-Language", defaultValue = "UZ") Language lang) {
        Result result = fieldService.updateField(fieldId,dto,lang);
        return ResponseEntity.status(result.isSuccess() ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST).body(result);
    }

    // Field delete
    @PutMapping("/delete/{fieldId}")
    public ResponseEntity<Result> deleteField(@PathVariable(value = "fieldId") int fieldId,
                                              @RequestHeader(value = "Accept-Language", defaultValue = "UZ") Language lang) {
        Result result = fieldService.deleteField(fieldId,lang);
        return ResponseEntity.status(result.isSuccess() ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST).body(result);
    }

    // get id Field
    @GetMapping("/getIdField/{fieldId}")
    public ResponseEntity<FieldResponseDto> getIdField(@PathVariable(value = "fieldId") int fieldId,
                                                  @RequestHeader(value = "Accept-Language", defaultValue = "UZ") Language lang) {
        FieldResponseDto result = fieldService.getIdField(fieldId,lang);
        return ResponseEntity.ok(result);
    }

    // Get by field List
    @GetMapping("/getFieldListStadium/{stadiumId}")
    public ResponseEntity<List<FieldResponseDto>> getFieldListStadium(@PathVariable(value = "stadiumId") int stadiumId,
                                                               @RequestHeader(value = "Accept-Language", defaultValue = "UZ") Language lang) {
        List<FieldResponseDto> result = fieldService.getFieldListStadium(stadiumId,lang);
        return ResponseEntity.ok(result);
    }

    // Field rating
    @PostMapping("/rating")
    public ResponseEntity<Result> ratingField(@RequestParam int fieldId,
                                              @RequestParam int rating,
                                              @RequestHeader(value = "Accept-Language", defaultValue = "UZ") Language lang) {
        Result result = fieldService.ratingField(fieldId,rating,lang);
        return ResponseEntity.status(result.isSuccess() ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST).body(result);
    }

}
