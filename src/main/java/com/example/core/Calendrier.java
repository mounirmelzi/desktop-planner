package com.example.core;

import java.io.Serializable;
import java.util.TreeSet;

public class Calendrier implements Serializable {
    //region Attributes

    private TreeSet<Day> days;

    //endregion

    //region Constructors

    public Calendrier() {
        this.days = new TreeSet<>();
    }

    public Calendrier(TreeSet<Day> days) {
        this.days = days;
    }

    //endregion

    //region Setter and Getters

    public TreeSet<Day> getDays() {
        return days;
    }

    //endregion

    //region Methods

    //endregion
}
