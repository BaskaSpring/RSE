package com.baska.RSE.DAO;

import com.baska.RSE.Models.TableColumn;

import java.util.Comparator;

public class TableComparator implements Comparator<TableColumn> {
    @Override
    public int compare(TableColumn o1, TableColumn o2) {
        return o1.getPriority()-o2.getPriority();
    }
}
