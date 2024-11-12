package ru.practicum;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@AllArgsConstructor
@Slf4j
public class EWMController {
    private final StatClient statClient;
    private static final String APP_NAME = "";

    @PostMapping("")
    public void saveHit(HttpServletRequest request) {
        statClient.post(APP_NAME, request);
    }
}
