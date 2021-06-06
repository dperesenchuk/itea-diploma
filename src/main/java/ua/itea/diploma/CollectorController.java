package ua.itea.diploma;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

public interface CollectorController {

    @GetMapping("/collect")
    ResponseEntity<String> collect();
}
