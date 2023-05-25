package com.example.core;

import com.example.core.exceptions.UnscheduledException;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class TacheSimple extends Tache {
    //region Attributes

    private int periodicity;

    //endregion

    //region Constructors

    public TacheSimple(String nom, Duration duree, Priority priority, LocalDateTime deadline) {
        super(nom, duree, priority, deadline);
        periodicity = 0;
    }

    public TacheSimple(String nom, Duration duree, Priority priority, LocalDateTime deadline, Category category) {
        super(nom, duree, priority, deadline, category);
        periodicity = 0;
    }

    public TacheSimple(String nom, Duration duree, Priority priority, LocalDateTime deadline, Category category, State state) {
        super(nom, duree, priority, deadline, category, state);
        periodicity = 0;
    }

    //endregion

    //region Setter and Getters

    public int getPeriodicity() {
        return periodicity;
    }

    public void setPeriodicity(int periodicity) {
        this.periodicity = Math.max(periodicity, 0);
    }

    //endregion

    //region Methods

    /**
     * vérifier si une tache simple est planifiée ou non
     * @return true si la tache n'est pas planifiée, false si non
     */
    @Override
    public boolean isUnscheduled() {
        return getPlanificationDateTime() == null;
    }

    /**
     * verifier si une tache simple est periodique ou non
     * @return true si la tache simple est periodique, false si non
     */
    @Override
    public boolean isPeriodique() {
        return getPeriodicity() > 0;
    }

    /**
     * planifier une tache automatiquement dans un planning
     * @param planning le planning dans lequel la tache sera planifiée
     * @param startDateTime la journée et le temps du début de planification
     * @return LocalDateTime
     * @throws UnscheduledException si la tache ne peut pas etre planifiée dans le planning
     */
    @Override
    public LocalDateTime planifier(@NotNull Planning planning, LocalDateTime startDateTime) throws UnscheduledException {
        if (!this.isUnscheduled())
            return this.getPlanificationDateTime();

        if (periodicity == 0) {
            LocalDateTime infos = planning.planifier(this, startDateTime);
            setPlanificationDateTime(infos);
            return infos;
        } else if (periodicity < 0)
            throw new UnscheduledException();

        LocalDateTime firstDateTime = planning.planifier(this, startDateTime);
        LocalDateTime savedDeadline = getDeadline();

        startDateTime = firstDateTime.plusDays(getPeriodicity()).toLocalDate().atStartOfDay();

        while (!startDateTime.isAfter(savedDeadline) && !startDateTime.toLocalDate().isAfter(planning.getDateFin())) {
            if (startDateTime.toLocalDate().isAfter(savedDeadline.toLocalDate()))
                setDeadline(startDateTime.toLocalDate().atTime(LocalTime.MAX));
            else setDeadline(savedDeadline);

            try {
                planning.planifier(this, startDateTime);
            } catch (UnscheduledException ignored) {}
            startDateTime = startDateTime.toLocalDate().plusDays(getPeriodicity()).atStartOfDay();
        }

        setDeadline(savedDeadline);
        setPlanificationDateTime(firstDateTime);
        return firstDateTime;
    }

    /**
     * déplanifier une tache simple
     * @param planning le planning où la tache simple est planifiée
     */
    @Override
    public void deplanifier(Planning planning) {
        if (isUnscheduled())
            return;

        if (periodicity == 0) {
            Day day = planning.getDayByDate(getPlanificationDateTime().toLocalDate());
            day.deleteTache(getPlanificationDateTime());
            setPlanificationDateTime(null);
            return;
        } else if (periodicity < 0)
            return;

        LocalDate planificationDate = this.getPlanificationDateTime().toLocalDate();
        while (!getDeadline().isBefore(planificationDate.atStartOfDay()) && !planificationDate.isAfter(planning.getDateFin())) {
            Day day = planning.getDayByDate(planificationDate);
            if (day != null) {
                LocalTime planificationTime = day.searchForTachePlanificationTime(this);
                if (planificationTime != null)
                    day.deleteTache(LocalDateTime.of(planificationDate, planificationTime));
            }

            planificationDate = planificationDate.plusDays(periodicity);
        }

        setPlanificationDateTime(null);
    }

    //endregion
}
