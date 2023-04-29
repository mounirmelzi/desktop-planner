package com.example.core;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.TreeSet;

public class Day implements Comparable<Day>, Serializable {
    private LocalDate date;
    private final TreeSet<Creneau> creneaux = new TreeSet<>();
    private static int nbrTachesRealiseesMin;

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
    public int compareTo(@NotNull Day other) {
        return date.compareTo(other.date);
    }
}
