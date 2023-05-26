package com.example.core;

public enum State {
    NOT_REALIZED("Not Realized"),
    COMPLETED("Completed"),
    IN_PROGRESS("In Progress"),
    CANCELLED("Cancelled"),
    DELAYED("Delayed");

    //region attributs
    private final String name;
    //endregion

    //region Constructeur
    State(String name) {
        this.name = name;
    }
    //endregion

    // region setters, getters
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

    //endregion
}
