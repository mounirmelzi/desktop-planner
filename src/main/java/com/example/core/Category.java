package com.example.core;

import java.util.HashMap;

public enum Category {
    STUDIES,
    WORK,
    HOBBY,
    SPORT,
    HEALTH;

    private final HashMap<User, String> color = new HashMap<>();
    private static final String defaultColor = "WHITE";

    public String getColor(User user) {
        String color = this.color.get(user);

        if (color == null) {
            return defaultColor;
        }

        return color;
    }

    public void setColor(User user, String color) {
        this.color.put(user, color);
    }
}
