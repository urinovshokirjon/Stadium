package uz.urinov.stadium.attach.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.urinov.stadium.attach.dto.AttachDTO;
import uz.urinov.stadium.attach.service.AttachService;

@RestController
@RequestMapping("/attach")
@RequiredArgsConstructor
public class AttachController {
    private final AttachService attachService;

    // imag ni yuklash
    @PostMapping("/upload")
    public ResponseEntity<AttachDTO> upload(@RequestParam("file") MultipartFile file) {
        AttachDTO response = attachService.saveAttach(file);
        return ResponseEntity.ok().body(response);
    }

    // imag ni ochish
    @PostMapping(value = "/open_general/{attachId}",produces = MediaType.ALL_VALUE)
    public byte[] openGeneral(@PathVariable("attachId") String attachId) {
        return attachService.openGeneral(attachId);
    }

    // imag ni  yuklab olish
    @PostMapping("/download/{attachId}")
    public ResponseEntity<Resource> download(@PathVariable("attachId") String attachId) {
        Resource file = attachService.download(attachId);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);

    }
}
