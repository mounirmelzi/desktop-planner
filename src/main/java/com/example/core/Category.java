package com.example.core;

import java.io.Serializable;

public class Category implements Serializable {
    private String name;
    private String color;

    public Category(String name) {
        this.name = name;
        this.color = "WHITE";
    }

    public Category(String name, String color) {
        this(name);
        this.color = color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return name.equals(category.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
