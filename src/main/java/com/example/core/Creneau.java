package com.example.core;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalTime;

public abstract class Creneau implements Comparable<Creneau>, Serializable {
    protected LocalTime heureDebut;
    protected LocalTime heureFin;

    public Duration getDuration() {
        return Duration.between(heureDebut, heureFin);
    }

    @Override
    public int compareTo(@NotNull Creneau o) {
        return heureDebut.compareTo(o.heureDebut);
    }

    @Override
    public String toString() {
        return "Creneau{" +
                "heureDebut=" + heureDebut +
                ", heureFin=" + heureFin +
                '}';
    }
}
