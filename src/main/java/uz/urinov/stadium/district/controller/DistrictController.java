package uz.urinov.stadium.district.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.urinov.stadium.auth.enums.Language;
import uz.urinov.stadium.district.dto.DistrictCreateDTO;
import uz.urinov.stadium.district.dto.DistrictResponseDTO;
import uz.urinov.stadium.district.service.DistrictService;
import uz.urinov.stadium.util.Result;

import java.util.List;

@SecurityRequirement(name = "Authorization")
@RestController
@RequestMapping("/district")
public class DistrictController {

    @Autowired
    private DistrictService regionService;

    // 1. Region create (ADMIN)
    @PostMapping("/adm/create")
    public ResponseEntity<Result> createDistrict(@Valid @RequestBody DistrictCreateDTO regionDto,
                                                              @RequestHeader(value = "Accept-Language", defaultValue = "UZ") Language lang) {
        Result result = regionService.createDistrict(regionDto, lang);
        return ResponseEntity.status(result.isSuccess() ? HttpStatus.CREATED : HttpStatus.CONFLICT).body(result);
    }

    // 2. District update (ADMIN)
    @PutMapping("/adm/update/{id}")
    public ResponseEntity<Result> updateDistrict(@Valid @RequestBody DistrictCreateDTO regionDto,
                                                 @RequestHeader(value = "Accept-Language", defaultValue = "UZ") Language lang,
                                                 @PathVariable("id") int id) {
        Result result = regionService.updateDistrict(regionDto, id, lang);
        return ResponseEntity.status(result.isSuccess() ? HttpStatus.OK : HttpStatus.CONFLICT).body(result);
    }

    // 3. District list (ADMIN)
    @GetMapping("/adm/list")
    public ResponseEntity<List<DistrictResponseDTO>> getDistrictList() {
        List<DistrictResponseDTO> regionDtoList = regionService.getDistrictList();
        return ResponseEntity.status(HttpStatus.OK).body(regionDtoList);
    }

    // 4. District delete (ADMIN)
    @DeleteMapping("/adm/delete/{id}")
    public ResponseEntity<Result> deleteDistrict(@PathVariable int id,
                                                 @RequestHeader(value = "Accept-Language", defaultValue = "UZ") Language lang) {
        Result result = regionService.deleteDistrict(id, lang);
        return ResponseEntity.status(result.isSuccess() ? HttpStatus.OK : HttpStatus.CONFLICT).body(result);
    }

    // 5. District By Lang
    @GetMapping("/lang")
    public ResponseEntity<List<DistrictResponseDTO>> getDistrictByLang2(@RequestHeader(value = "Accept-Language", defaultValue = "UZ") Language lang) {
        List<DistrictResponseDTO> regionLangDtoList = regionService.getDistrictByLang(lang);
        return ResponseEntity.status(HttpStatus.OK).body(regionLangDtoList);
    }

    // 6. District Region  By Lang
    @GetMapping("/region-lang/{id}")
    public ResponseEntity<List<DistrictResponseDTO>> getDistrictRegionId(@RequestHeader(value = "Accept-Language", defaultValue = "UZ") Language lang,
                                                                        @PathVariable("id") int regionId) {
        List<DistrictResponseDTO> regionLangDtoList = regionService.getDistrictRegionId(regionId,lang);
        return ResponseEntity.status(HttpStatus.OK).body(regionLangDtoList);
    }

    // 7. District id  By Lang
    @GetMapping("/district-lang/{id}")
    public ResponseEntity<DistrictResponseDTO> getDistrictId(@RequestHeader(value = "Accept-Language", defaultValue = "UZ") Language lang,
                                                                         @PathVariable("id") int districtId) {
        DistrictResponseDTO regionLangDtoList = regionService.getDistrictId(districtId,lang);
        return ResponseEntity.status(HttpStatus.OK).body(regionLangDtoList);
    }


}
