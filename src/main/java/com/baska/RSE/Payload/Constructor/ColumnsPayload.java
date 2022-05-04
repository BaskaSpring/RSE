package com.baska.RSE.Payload.Constructor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ColumnsPayload {
    private String name;
    private String type;
    private List<EnumsValuePayload> enumsValue;
}
