package com.example.core;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Comparator;

public abstract class Tache implements Comparable<Tache>, Serializable {
    protected String nom;
    protected Duration duree;
    protected Priority priority;
    protected LocalDateTime deadline;
    protected Category category;
    protected State state;

    @Override
    public int compareTo(@NotNull Tache other) {
        Comparator<Tache> priorityComparator = new PriorityComparator();
        Comparator<Tache> deadlineComparator = new DeadlineComparator();

        return priorityComparator.reversed()
                .thenComparing(deadlineComparator)
                .thenComparing((tache1, tache2) -> tache1.nom.compareTo(tache2.nom))
                .compare(this, other);
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


    public static class PriorityComparator implements Comparator<Tache> {
        @Override
        public int compare(Tache t1, Tache t2) {
            return t1.priority.compareTo(t2.priority);
        }
    }

    public static class DeadlineComparator implements Comparator<Tache> {
        @Override
        public int compare(Tache t1, Tache t2) {
            return t1.deadline.compareTo(t2.deadline);
        }
    }
}
