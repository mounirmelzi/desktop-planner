package com.example.core;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public enum Badge {
    GOOD("Good"), VERY_GOOD("Very Good"), EXCELLENT("Excellent");

    private final String name;

    Badge(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @NotNull
    @Contract(pure = true)
    @Override
    public String toString() {
        return "Badge{" +
                "name='" + name + '\'' +
                '}';
    }
}
