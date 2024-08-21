package uz.urinov.stadium.stadium.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.urinov.stadium.auth.enums.Language;
import uz.urinov.stadium.stadium.dto.FieldCreateDto;
import uz.urinov.stadium.stadium.service.FieldService;
import uz.urinov.stadium.util.Result;

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

}
