package uz.urinov.stadium.exp;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class HandlerController {
    @ExceptionHandler(AppBadException.class)
    public ResponseEntity<String> handler(AppBadException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
