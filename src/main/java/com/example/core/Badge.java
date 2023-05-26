package com.example.core;

public enum Badge {
    GOOD("Good"), VERY_GOOD("Very Good"), EXCELLENT("Excellent");

    //region attributs
    private final String name;
    //endregion

    //region constructeur
    Badge(String name) {
        this.name = name;
    }
    //endregion

    //region getters,setters
    public String getName() {
        return name;
    }
    //endregion
}
