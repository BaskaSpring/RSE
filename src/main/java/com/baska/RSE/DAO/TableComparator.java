package com.baska.RSE.DAO;

import com.baska.RSE.Models.Types;

import java.util.Comparator;

public class TableComparator implements Comparator<Types> {
    @Override
    public int compare(Types o1, Types o2) {
        return o1.getPriority()-o2.getPriority();
    }
}
