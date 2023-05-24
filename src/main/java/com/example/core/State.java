package com.example.core;

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

    public static State getStateByName(String name) {
        if (name == null)
            return null;

        for (State state : State.values()) {
            if (state.getName().equals(name))
                return state;
        }

        return null;
    }

    public String getName() {
        return name;
    }
}
