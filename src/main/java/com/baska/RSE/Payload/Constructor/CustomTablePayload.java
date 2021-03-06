package com.baska.RSE.Payload.Constructor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomTablePayload {

    @Size(min=3, max = 75, message = "количество символов должно быть между 3 и 75")
    private String name;

    private String bool;

}
