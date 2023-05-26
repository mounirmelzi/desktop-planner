package com.example.core;

import com.example.core.exceptions.UnscheduledException;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;

public abstract class Tache implements IPlanifiable, Comparable<Tache>, Serializable {
    //region Attributes

    private String nom;
    private Duration duree;
    private Priority priority;
    private LocalDateTime deadline;
    private Category category;
    private State state;
    private LocalDateTime planificationDateTime;

    //endregion

    //region Constructors

    public Tache(String nom, Duration duree, Priority priority, LocalDateTime deadline) {
        this.nom = nom;
        this.duree = duree;
        this.priority = priority;
        this.deadline = deadline;
        this.category = null;
        this.state = State.IN_PROGRESS;
        this.planificationDateTime = null;
    }

    public Tache(String nom, Duration duree, Priority priority, LocalDateTime deadline, Category category) {
        this(nom, duree, priority, deadline);
        this.category = category;
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

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Duration getDuree() {
        return duree;
    }

    public void setDuree(Duration duree) {
        this.duree = duree;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getColor() {
        if (getCategory() == null)
            return Category.defaultColor;

        return getCategory().getColor();
    }

    public State getState() {
        return state;
    }

    public void setState(State state, Planning planning) {
        if (planning == null) {
            this.state = state;
            return;
        }

        if (state == State.COMPLETED && this.state != State.COMPLETED) {
            planning.updateTacheCompletedCounter(1);
        } else if (state != State.COMPLETED && this.state == State.COMPLETED) {
            planning.updateTacheCompletedCounter(-1);
        }

        this.state = state;
    }

    public LocalDateTime getPlanificationDateTime() {
        return planificationDateTime;
    }

    public void setPlanificationDateTime(LocalDateTime planificationDateTime) {
        this.planificationDateTime = planificationDateTime;
    }

    //endregion

    //region Methods

    /**
     * vérifier si le deadline d'une tache est respecté si elle est planifier dans un créneau libre d'une journée donnée
     * @param day une journée
     * @param heureDebut l'heure de début de planification dans le créneau libre
     * @return true si le deadline est respecté, false si non
     */
    public boolean checkDeadline(@NotNull Day day, LocalTime heureDebut) {
        if (day.getDate().isAfter(deadline.toLocalDate()))
            return false;

        if (heureDebut == null)
            return true;

        if (day.getDate().isEqual(deadline.toLocalDate())) {
            if (heureDebut.isAfter(deadline.toLocalTime()))
                return false;

            if (heureDebut.plus(getDuree()).isAfter(deadline.toLocalTime()))
                return false;
        }

        return true;
    }

    /**
     * vérifier si une tache est planifiée ou non
     * @return true si la tache n'est pas planifiée, false si non
     */
    public abstract boolean isUnscheduled();

    /**
     * verifier si une tache est periodique ou non
     * @return true si la tache est periodique, false si non
     */
    public abstract boolean isPeriodique();

    /**
     * planifier une tache manuellement dans un planning
     * @param planning le planning dans lequel la tache sera planifiée
     * @param date la date de la journée où on va planifier la tache
     * @param time le temps du début de créneau libre dans lequel on va planifier la tache
     * @return LocalDateTime
     * @throws UnscheduledException si la tache ne peut pas etre planifiée
     */
    public LocalDateTime planifierManuellement(@NotNull Planning planning, LocalDate date, LocalTime time) throws UnscheduledException {
        LocalDateTime planificationDateTime = planning.planifierManuellement(this, date, time);
        setPlanificationDateTime(planificationDateTime);
        return planificationDateTime;
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

    //endregion

    //region Classes Internes

    /**
     * sert a comparer la priorité entre deux taches
     */
    public static class PriorityComparator implements Comparator<Tache> {
        @Override
        public int compare(@NotNull Tache t1, @NotNull Tache t2) {
            return t1.priority.compareTo(t2.priority);
        }
    }

    /**
     * sert a comparer entre les deadlines de deux taches
     */
    public static class DeadlineComparator implements Comparator<Tache> {
        @Override
        public int compare(@NotNull Tache t1, @NotNull Tache t2) {
            return t1.deadline.compareTo(t2.deadline);
        }
    }

    //endregion
}
