package com.example.core;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Objects;

public abstract class Creneau implements Comparable<Creneau>, Serializable {
    protected LocalTime heureDebut;
    protected LocalTime heureFin;

    public Creneau(LocalTime heureDebut, LocalTime heureFin) {
        this.heureDebut = heureDebut;
        this.heureFin = heureFin;
    }

    public Duration getDuration() {
        return Duration.between(heureDebut, heureFin);
    }

    @Override
    public int compareTo(@NotNull Creneau o) {
        return heureDebut.compareTo(o.heureDebut);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Creneau creneau = (Creneau) o;
        return Objects.equals(heureDebut, creneau.heureDebut) && Objects.equals(heureFin, creneau.heureFin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(heureDebut, heureFin);
    }

    @Override
    public String toString() {
        return "Creneau{" +
                "heureDebut=" + heureDebut +
                ", heureFin=" + heureFin +
                '}';
    }
}
