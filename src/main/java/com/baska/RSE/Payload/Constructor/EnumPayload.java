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
public class EnumPayload {

    @NotBlank(message = "длина имени не может быть пустым")
    @NotEmpty(message = "длина имени не может быть пустым")
    @Size(min=2,max=50,message = "длина имени не может быть меньше 2 и больше 50 символов")
    public String name;
}
