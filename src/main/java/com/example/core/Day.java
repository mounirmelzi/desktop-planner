package com.example.core;

import java.time.LocalDate;

public class Day implements Comparable<Day> {
    LocalDate date;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Day day = (Day) o;
        return date.equals(day.date);
    }

    @Override
    public int hashCode() {
        return date.hashCode();
    }


    @Override
    public int compareTo(Day other) {
        return date.compareTo(other.date);
    }
}
