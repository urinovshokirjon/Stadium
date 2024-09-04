package uz.urinov.stadium.stadium.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.urinov.stadium.auth.enums.Language;
import uz.urinov.stadium.stadium.dto.FieldResponseDto;
import uz.urinov.stadium.stadium.dto.StadiumResponseDto;
import uz.urinov.stadium.stadium.entity.StadiumEntity;
import uz.urinov.stadium.stadium.enums.Status;
import uz.urinov.stadium.stadium.service.CheckService;

import java.util.List;

@SecurityRequirement(name = "Authorization")
@RestController
@RequestMapping("/check")
@RequiredArgsConstructor
public class CheckController {
    private final CheckService checkService;

    // List status stadium
    @GetMapping("/list-status-stadium")
    public ResponseEntity<List<StadiumResponseDto>> listStatusStadium(@RequestParam(defaultValue = "0") int page,
                                                                      @RequestParam(defaultValue = "2") int size,
                                                                      @RequestParam(defaultValue = "INACTIVE") Status status,
                                                                      @RequestHeader(value = "Accept-Language") Language lang) {
        List<StadiumResponseDto> listStatus = checkService.listStatusStadium(page - 1, size, status, lang);
        return ResponseEntity.ok().body(listStatus);
    }

    // List status field
    @GetMapping("/list-status-field")
    public ResponseEntity<List<FieldResponseDto>> listStatusField(@RequestParam(defaultValue = "0") int page,
                                                                  @RequestParam(defaultValue = "2") int size,
                                                                  @RequestParam(defaultValue = "INACTIVE") Status status,
                                                                  @RequestHeader(value = "Accept-Language") Language lang) {
        List<FieldResponseDto> listStatus = checkService.listStatusField(page - 1, size, status, lang);
        return ResponseEntity.ok().body(listStatus);
    }

    // Status stadium
    @GetMapping("/status-stadium")
    public ResponseEntity<StadiumResponseDto> statusStadium(@RequestParam int id,
                                                            @RequestParam(defaultValue = "INACTIVE") Status status,
                                                            @RequestHeader(value = "Accept-Language") Language lang) {
        StadiumResponseDto response = checkService.statusStadium(id, status,lang);
        return ResponseEntity.ok().body(response);
    }

}
