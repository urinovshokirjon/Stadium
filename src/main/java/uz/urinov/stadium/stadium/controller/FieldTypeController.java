package uz.urinov.stadium.stadium.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.urinov.stadium.auth.enums.Language;
import uz.urinov.stadium.stadium.dto.FieldTypeCreateDto;
import uz.urinov.stadium.stadium.dto.FieldTypeResponseDto;
import uz.urinov.stadium.stadium.service.FieldTypeService;
import uz.urinov.stadium.util.Result;

import java.util.List;

@SecurityRequirement(name = "Authorization")
@RestController
@RequestMapping("/field-type")
@RequiredArgsConstructor
public class FieldTypeController {
    private final FieldTypeService fieldTypeService;

    // 1. Create fieldType (ADMIN)
    @PostMapping("/adm/create")
    public ResponseEntity<Result> createFieldType(@RequestBody FieldTypeCreateDto dto,
                                                  @RequestHeader(value = "Accept-Language") Language lang) {
        Result result = fieldTypeService.createFieldType(dto, lang);
        return ResponseEntity.status(result.isSuccess() ? HttpStatus.CREATED : HttpStatus.CONFLICT).body(result);
    }


    // 2. Update fieldType (ADMIN)
    @PutMapping("/adm/update/{id}")
    public ResponseEntity<Result> updateFieldType(@PathVariable int id, @RequestBody FieldTypeCreateDto dto,
                                                 @RequestHeader(value = "Accept-Language") Language lang) {

        Result result = fieldTypeService.updateFieldType(id, dto, lang);
        return ResponseEntity.status(result.isSuccess() ? HttpStatus.OK : HttpStatus.CONFLICT).body(result);
    }

    // 3. FieldType list (ADMIN)
    @GetMapping("/adm/list")
    public ResponseEntity<List<FieldTypeResponseDto>> getCategoryList() {
        List<FieldTypeResponseDto> fieldTypeDtoList = fieldTypeService.getFieldTypeList();
        return ResponseEntity.status(HttpStatus.OK).body(fieldTypeDtoList);
    }

    // 4. Delete fieldType (ADMIN)
    @DeleteMapping("/adm/delete/{id}")
    public ResponseEntity<Result> deleteFieldType(@PathVariable int id,
                                               @RequestHeader(value = "Accept-Language") Language lang) {

        Result result = fieldTypeService.deleteFieldType(id,lang);
        return ResponseEntity.status(result.isSuccess() ? HttpStatus.OK : HttpStatus.CONFLICT).body(result);
    }

    // 5. FieldType By Lang
    @GetMapping("/lang")
    public ResponseEntity<List<FieldTypeResponseDto>> getFieldTypeLang(@RequestHeader(value = "Accept-Language") Language lang) {
        List<FieldTypeResponseDto> fieldTypeLangDtoList = fieldTypeService.getFieldTypeLang(lang);
        return ResponseEntity.status(HttpStatus.OK).body(fieldTypeLangDtoList);
    }


}
