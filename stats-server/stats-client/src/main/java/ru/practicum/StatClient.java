package ru.practicum;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
public class StatClient extends BaseClient {
    private static final DateTimeFormatter CUSTOM_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final String API_PREFIX = "";

    @Autowired
    public StatClient(@Value("${stats.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> get(String from, String to, List<String> uris, Boolean isUnique) {
        Map<String, Object> parameters = Map.of(
                "from", from,
                "to", to,
                "uris", uris,
                "isUnique",isUnique
        );
        return get("/stats?from={from}&to={to}&uris={uris}$isUnique={isUnique}", parameters);
    }

    public ResponseEntity<Object> post(String appName, HttpServletRequest request) {
        System.out.println("HttpServletRequest " + request);
        StatsRequestDto statsRequestDto = new StatsRequestDto(appName,
                request.getRequestURI(),
                request.getRemoteAddr(),
                LocalDateTime.now().format(CUSTOM_FORMATTER));
        return post("/hit", statsRequestDto);
    }

}