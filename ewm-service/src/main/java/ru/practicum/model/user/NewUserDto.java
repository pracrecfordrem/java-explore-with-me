package ru.practicum.model.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewUserDto {
    @NotBlank(message = "Поле name не может быть пустым")
    @Size(min = 2, max = 250)
    private String name;
    @NotBlank(message = "Поле email не может быть пустым")
    @Size(min = 6, max = 254)
    @Email
    private String email;
}
