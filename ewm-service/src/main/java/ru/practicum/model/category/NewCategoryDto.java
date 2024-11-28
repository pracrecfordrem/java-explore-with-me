package ru.practicum.model.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class NewCategoryDto {
    @NotBlank
    @Size(max = 50, message = "Имя категории содержать не более 50 символов")
    private String name;
}
