package com.example.core;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Month;
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

    public TreeSet<Day> getDaysOfMonth(int year, Month month) {
        Day start = new Day(LocalDate.of(year, month, 1));
        Day end = new Day(LocalDate.of(year, month, month.maxLength()));

        return (TreeSet<Day>) getDays().subSet(start, true, end, true);
    }

    public Day getDayByDate(LocalDate date) {
        Day day = new Day(date);
        day = getDays().floor(day);
        return ((day != null) && (date.isEqual(day.getDate()))) ? day : null;
    }

    public boolean addDay(Day day) {
        return days.add(day);
    }

    //endregion
}
