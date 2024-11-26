package ru.practicum.model.compilation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class NewCompilationDto {
    private List<Long> events;
    private Boolean pinned;
    @NotNull
    @NotBlank
    @Size(min = 1, max = 50)
    private String title;
}
