package com.baska.RSE.DAO;

import com.baska.RSE.Models.EnumValues;
import com.baska.RSE.Repositories.EnumValuesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EnumValuesDAO {

    @Autowired
    EnumValuesRepository enumValuesRepository;

    public EnumValues save(EnumValues enumValues) {
        return enumValuesRepository.save(enumValues);
    }
}
