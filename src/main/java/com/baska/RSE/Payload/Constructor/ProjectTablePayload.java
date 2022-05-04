package com.baska.RSE.Payload.Constructor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectTablePayload {

    private String name;
    private Set<RolePayload> roles;
    private List<ColumnsPayload> columnsPayloads;

}
