package ru.practicum;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.util.List;
import java.util.Map;

@Service
public class StatClient extends BaseClient {

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

    public ResponseEntity<Object> post(StatsRequestDto statsRequestDto) {
        return post("/hit", statsRequestDto);
    }

}