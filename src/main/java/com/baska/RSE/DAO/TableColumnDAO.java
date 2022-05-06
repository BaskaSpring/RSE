package com.baska.RSE.DAO;

import com.baska.RSE.Models.TableColumn;
import com.baska.RSE.Repositories.TableColumnRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TableColumnDAO {

    @Autowired
    TableColumnRepository tableColumnRepository;

    public TableColumn save(TableColumn tableColumn){
        return tableColumnRepository.save(tableColumn);
    }
}
