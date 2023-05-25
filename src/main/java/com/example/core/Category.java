package com.example.core;

import java.io.Serializable;

public class Category implements Serializable {
    //region Attributes

    private String name;
    private String color;
    static final String defaultColor = "#FFFFFF";

    //endregion

    //region Constructors

    public Category(String name, String color) {
        this.name = name;
        this.setColor(color);
    }

    //endregion

    //region Setter and Getters

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = (color == null) ? defaultColor : color;
    }

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
