package ru.practicum.model;

import lombok.Data;

@Data
public class Hit {
    private String app;
    private String uri;
    private Long hits;
}
