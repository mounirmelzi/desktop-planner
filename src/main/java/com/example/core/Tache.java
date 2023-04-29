package com.example.core;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDate;

public abstract class Tache implements Comparable<Tache>, Serializable {
    protected String nom;
    protected Duration duree;
    protected Priority priority;
    protected LocalDate deadline;
    protected Category category;
    protected State state;

    @Override
    public int compareTo(@NotNull Tache o) {
        int cmp = priority.compareTo(o.priority);
        if (cmp != 0) {
            return cmp;
        }

        return deadline.compareTo(o.deadline);
    }

    @Override
    public String toString() {
        return "Tache{" +
                "nom='" + nom + '\'' +
                ", duree=" + duree +
                ", priority=" + priority +
                ", deadline=" + deadline +
                ", category=" + category +
                ", state=" + state +
                '}';
    }
}
