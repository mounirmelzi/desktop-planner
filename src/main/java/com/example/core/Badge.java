package com.example.core;

public enum Badge {
    GOOD("Good"), VERY_GOOD("Very Good"), EXCELLENT("Excellent");

    private final String name;

    Badge(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
