package ru.practicum.model.exception;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Exception {
    private String status;
    private String reason;
    private String message;
    private LocalDateTime timestamp;
}
