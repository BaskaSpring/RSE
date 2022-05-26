package com.baska.RSE.Payload.Tables;

import com.baska.RSE.Models.Types;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class PropPayload {

    Map<Long,String> values =  new HashMap<>();

}
