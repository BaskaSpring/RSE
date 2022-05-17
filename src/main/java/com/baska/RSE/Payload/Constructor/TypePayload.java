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
public class TypePayload {

    @NotBlank(message = "длина имени не может быть пустым")
    @NotEmpty(message = "длина имени не может быть пустым")
    @Size(min=3,max=35,message = "длина имени не может быть меньше 3 и больше 35 символов")
    public String name;

    @NotBlank(message = "длина типа не может быть пустым")
    @NotEmpty(message = "длина типа не может быть пустым")
    public String type;

}
