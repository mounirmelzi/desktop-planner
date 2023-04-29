package com.example.core;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public enum State {
    NOT_REALIZED("Not Realized"),
    COMPLETED("Completed"),
    IN_PROGRESS("In Progress"),
    CANCELLED("Cancelled"),
    DELAYED("Delayed");

    private final String name;

    State(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @NotNull
    @Contract(pure = true)
    @Override
    public String toString() {
        return "State{" +
                "name='" + name + '\'' +
                '}';
    }
}
