package com.example.core;

public enum Priority {
    LOW("Low"), MEDIUM("Medium"), HIGH("High");

    //region attributs

    private final String name;

    //endregion

    //region constructeur

    Priority(String name) {
        this.name = name;
    }

    //endregion

    //region setters, getters

    public static Priority getByName(String name) {
        if (name == null)
            return null;

        for (Priority priority : Priority.values()) {
            if (priority.getName().equals(name))
                return priority;
        }

        return null;
    }

    public String getName() {
        return name;
    }

    //endregion
}
