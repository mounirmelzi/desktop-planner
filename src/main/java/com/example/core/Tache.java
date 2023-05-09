package com.example.core;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Comparator;

public abstract class Tache implements IPlanifiable, Comparable<Tache>, Serializable {
    //region Attributes

    private String nom;
    private Duration duree;
    private Priority priority;
    private LocalDateTime deadline;
    private Category category;
    private State state;

    //endregion

    //region Constructors

    public Tache(String nom, Duration duree, Priority priority, LocalDateTime deadline) {
        this.nom = nom;
        this.duree = duree;
        this.priority = priority;
        this.deadline = deadline;
    }

    public Tache(String nom, Duration duree, Priority priority, LocalDateTime deadline, Category category) {
        this(nom, duree, priority, deadline);
        this.category = category;
    }

    public Tache(String nom, Duration duree, Priority priority, LocalDateTime deadline, State state) {
        this(nom, duree, priority, deadline);
        this.state = state;
    }

    public Tache(String nom, Duration duree, Priority priority, LocalDateTime deadline, Category category, State state) {
        this(nom, duree, priority, deadline);
        this.category = category;
        this.state = state;
    }

    //endregion

    //region Setter and Getters

    public String getNom() {
        return nom;
    }

    public Duration getDuree() {
        return duree;
    }

    public Priority getPriority() {
        return priority;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public Category getCategory() {
        return category;
    }

    public State getState() {
        return state;
    }

    //endregion

    //region Methods

    /**
     * vérifier si le deadline d'une tache est respecté si elle est planifier dans un créneau libre d'une journée donnée
     * @param day une journée
     * @param creneauLibre un créneau libre
     * @return true si le deadline est respecté, false si non
     */
    public boolean checkDeadline(@NotNull Day day, CreneauLibre creneauLibre) {
        if (day.getDate().isAfter(deadline.toLocalDate()))
            return false;

        if (creneauLibre == null)
            return true;

        if (day.getDate().isEqual(deadline.toLocalDate())) {
            if (creneauLibre.getHeureDebut().isAfter(deadline.toLocalTime()))
                return false;

            if (creneauLibre.getHeureDebut().plus(getDuree()).isAfter(deadline.toLocalTime()))
                return false;
        }

        return true;
    }

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

    //endregion


    public static class PriorityComparator implements Comparator<Tache> {
        @Override
        public int compare(@NotNull Tache t1, @NotNull Tache t2) {
            return t1.priority.compareTo(t2.priority);
        }
    }

    public static class DeadlineComparator implements Comparator<Tache> {
        @Override
        public int compare(@NotNull Tache t1, @NotNull Tache t2) {
            return t1.deadline.compareTo(t2.deadline);
        }
    }
}
