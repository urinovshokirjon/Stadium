package uz.urinov.stadium.stadium.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.urinov.stadium.auth.enums.Language;
import uz.urinov.stadium.stadium.dto.StadiumCreateDto;
import uz.urinov.stadium.stadium.dto.StadiumResponseDto;
import uz.urinov.stadium.stadium.service.StadiumService;
import uz.urinov.stadium.util.Result;

import java.util.List;

@SecurityRequirement(name = "Authorization")
@RequiredArgsConstructor
@RestController
@RequestMapping("/stadium")
public class StadiumController {
    private final StadiumService stadiumService;

    // 1. Create stadium (ADMIN,OWNER)
    @PostMapping("/owr/create")
    public ResponseEntity<Result> createStadium(@Valid @RequestBody StadiumCreateDto dto,
                                                @RequestHeader(value = "Accept-Language") Language lang) {
        Result result = stadiumService.createStadium(dto, lang);
        return ResponseEntity.status(result.isSuccess() ? HttpStatus.CREATED : HttpStatus.CONFLICT).body(result);
    }


    // 2. Update stadium (ADMIN,OWNER)
    @PutMapping("/owr/update/{id}")
    public ResponseEntity<Result> updateStadium(@PathVariable int id, @Valid @RequestBody StadiumCreateDto dto,
                                                @RequestHeader(value = "Accept-Language") Language lang) {

        Result result = stadiumService.updateStadium(id, dto, lang);
        return ResponseEntity.status(result.isSuccess() ? HttpStatus.OK : HttpStatus.CONFLICT).body(result);
    }

    // 3. Delete stadium (ADMIN,OWNER)
    @PutMapping("/owr/delete/{id}")
    public ResponseEntity<Result> deleteStadium(@PathVariable("id") int stadiumId,
                                                @RequestHeader(value = "Accept-Language") Language lang) {

        Result result = stadiumService.deleteStadium(stadiumId, lang);
        return ResponseEntity.status(result.isSuccess() ? HttpStatus.OK : HttpStatus.CONFLICT).body(result);
    }

    // 4. Region Stadium List stadium
    @GetMapping("/region-id-stadium-list")
    public ResponseEntity<List<StadiumResponseDto>> regionIdStadiumList(@RequestParam(defaultValue = "0") int page,
                                                                        @RequestParam(defaultValue = "2") int size,
                                                                        @RequestParam int regionId,
                                                                        @RequestParam Double lat,
                                                                        @RequestParam Double lon,
                                                                        @RequestHeader(value = "Accept-Language") Language lang) {
        List<StadiumResponseDto> result = stadiumService.regionStadiumList(regionId, lang, page - 1, size, lat, lon);
        return ResponseEntity.ok().body(result);
    }


    // 5. Closest Stadium List stadium (eng yaqin)
    @GetMapping("/closest-stadium-list")
    public ResponseEntity<List<StadiumResponseDto>> closestStadiumList(@RequestParam(defaultValue = "0") int page,
                                                                       @RequestParam(defaultValue = "2") int size,
                                                                       @RequestParam Double lat,
                                                                       @RequestParam Double lon,
                                                                       @RequestHeader(value = "Accept-Language") Language lang) {
        List<StadiumResponseDto> result = stadiumService.closestStadiumList(lang, page - 1, size, lat, lon);
        return ResponseEntity.ok().body(result);
    }


}
