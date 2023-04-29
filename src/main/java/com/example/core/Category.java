package com.example.core;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public enum Category {
    STUDIES("Studies"), WORK("Work"), HOBBY("Hobby"), SPORT("Sport"), HEALTH("Health");

    private final String name;

    Category(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @NotNull
    @Contract(pure = true)
    public static String getDefaultColor() {
        return "WHITE";
    }

    @NotNull
    @Contract(pure = true)
    @Override
    public String toString() {
        return "Category{" +
                "name='" + name + '\'' +
                '}';
    }
}
