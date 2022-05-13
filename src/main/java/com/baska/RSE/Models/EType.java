package com.baska.RSE.Models;

public enum EType {
    STRING("Строка"),
    NUMBER("Число"),
    BOOLEAN("Булево"),
    ENUM("Перечисление"),
    DATE("Дата");

    private String value;

    EType(final String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return this.getValue();
    }
}
