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
public class ColumnsPayload {

    @NotBlank(message = "длина имени не может быть пустым")
    @NotEmpty(message = "длина имени не может быть пустым")
    @Size(min=2,max=15,message = "длина имени не может быть меньше 2 и больше 15 символов")
    public String name;

    @NotBlank(message = "длина типа не может быть пустым")
    @NotEmpty(message = "длина типа не может быть пустым")
    public String type;

}
