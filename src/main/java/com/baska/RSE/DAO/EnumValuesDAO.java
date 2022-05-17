package com.baska.RSE.DAO;

import com.baska.RSE.Models.EnumValues;
import com.baska.RSE.Repositories.EnumValuesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class EnumValuesDAO {

    @Autowired
    EnumValuesRepository enumValuesRepository;

    public List<EnumValues> getAll(){
        return enumValuesRepository.getAll();
    }

    public EnumValues save(EnumValues enumValues) {
        return enumValuesRepository.save(enumValues);
    }

    public EnumValues getById(Long enumId) {
        Optional<EnumValues> enumValue = enumValuesRepository.findById(enumId);
        return enumValue.orElse(null);
    }
}
