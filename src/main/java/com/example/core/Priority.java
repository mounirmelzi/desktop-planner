package com.example.core;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public enum Priority {
    LOW("Low"), MEDIUM("Medium"), HIGH("High");

    private final String name;

    Priority(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @NotNull
    @Contract(pure = true)
    @Override
    public String toString() {
        return "Priority{" +
                "name='" + name + '\'' +
                '}';
    }
}
