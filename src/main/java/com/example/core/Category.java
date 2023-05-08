package com.example.core;

import java.io.Serializable;

public class Category implements Serializable {
    //region Attributes

    private String name;
    private String color;

    //endregion

    //region Constructors

    public Category(String name) {
        this.name = name;
        this.color = "WHITE";
    }

    public Category(String name, String color) {
        this(name);
        this.color = color;
    }

    //endregion

    //region Setter and Getters

    //endregion

    //region Methods

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

    //endregion
}
