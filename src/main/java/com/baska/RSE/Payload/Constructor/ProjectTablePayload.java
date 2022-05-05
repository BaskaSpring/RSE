package com.baska.RSE.Payload.Constructor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectTablePayload {

    @NotBlank(message = "поле не может быть пустым")
    @NotEmpty(message = "поле не может быть пустым")
    @Size(min=5, max = 75, message = "количество символов должно быть между 5 и 75")
    private String name;

}
