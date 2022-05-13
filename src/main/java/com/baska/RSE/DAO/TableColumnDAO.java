package com.baska.RSE.DAO;

import com.baska.RSE.Models.Types;
import com.baska.RSE.Repositories.TableColumnRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class TableColumnDAO {

    @Autowired
    TableColumnRepository tableColumnRepository;

    public Types save(Types tableValue){
        return tableColumnRepository.save(tableValue);
    }


    public Types getById(Long id){
        Optional<Types> tableColumn = tableColumnRepository.findById(id);
        return tableColumn.orElse(null);
    }

    public void delete(Types tableValue){
        tableColumnRepository.delete(tableValue);
    }
}
