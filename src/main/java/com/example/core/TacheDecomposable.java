package com.example.core;

import com.example.core.exceptions.DecompositionImpossibleException;
import com.example.core.exceptions.UnscheduledException;
import com.example.core.utils.Pair;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.TreeSet;

public class TacheDecomposable extends Tache implements IDecomposable<Pair<Planning, LocalDateTime>, TacheSimple> {
    //region Attributes

    private TreeSet<TacheSimple> children;

    //endregion

    //region Constructors

    public TacheDecomposable(String nom, Duration duree, Priority priority, LocalDateTime deadline) {
        super(nom, duree, priority, deadline);
        children = new TreeSet<>();
    }

    public TacheDecomposable(String nom, Duration duree, Priority priority, LocalDateTime deadline, Category category) {
        super(nom, duree, priority, deadline, category);
        children = new TreeSet<>();
    }

    public TacheDecomposable(String nom, Duration duree, Priority priority, LocalDateTime deadline, State state) {
        super(nom, duree, priority, deadline, state);
        children = new TreeSet<>();
    }

    public TacheDecomposable(String nom, Duration duree, Priority priority, LocalDateTime deadline, Category category, State state) {
        super(nom, duree, priority, deadline, category, state);
        children = new TreeSet<>();
    }

    //endregion

    //region Setter and Getters

    public TreeSet<TacheSimple> getChildren() {
        return children;
    }

    public void setChildren(TreeSet<TacheSimple> children) {
        this.children = children;
    }

    @Override
    public LocalDateTime getPlanificationDateTime() {
        if (!hasChildren())
            return super.getPlanificationDateTime();

        LocalDateTime min = null;
        for (TacheSimple tache : getChildren()) {
            if (tache.getPlanificationDateTime() == null)
                continue;

            if (min == null || tache.getPlanificationDateTime().isBefore(min))
                min = tache.getPlanificationDateTime();
        }

        return min;
    }

    //endregion

    //region Methods

    /**
     * vérifier si une tache décomposable a des sous taches
     * @return true si elle décomposée (a des sous taches), false si non
     */
    public boolean hasChildren() {
        return !children.isEmpty();
    }

    @Override
    public boolean isPeriodique() {
        return false;
    }

    /**
     * vérifier si une tache décomposable est planifiée ou non
     * @return true si la tache n'est pas planifiée, false si non
     */
    @Override
    public boolean isUnscheduled() {
        if (!this.hasChildren())
            return getPlanificationDateTime() == null;

        for (TacheSimple tacheSimple : getChildren()) {
            if (tacheSimple.isUnscheduled())
                return true;
        }

        return false;
    }

    /**
     * planifier une tache décomposable automatiquement dans un planning
     * @param planning le planning dans lequel la tache sera planifiée
     * @param startDateTime la journée et le temps du début de planification
     * @return LocalDateTime
     * @throws UnscheduledException si la tache ne peut pas etre planifiée dans le planning
     */
    @Override
    public LocalDateTime planifier(Planning planning, LocalDateTime startDateTime) throws UnscheduledException {
        if (!this.isUnscheduled())
            return this.getPlanificationDateTime();

        if (!hasChildren()) {
            LocalDateTime infos = planning.planifier(this, startDateTime);
            setPlanificationDateTime(infos);
            return infos;
        }

        boolean error = false;
        LocalDateTime max = getPlanificationDateTime();
        for (TacheSimple tache : getChildren()) {
            try {
                LocalDateTime temp = tache.planifier(planning, startDateTime);

                if (max == null || temp.isAfter(max))
                    max = temp;
            } catch (UnscheduledException e) {
                error = true;
            }
        }

        if (error) throw new UnscheduledException();
        setPlanificationDateTime(max);
        return max;
    }

    /**
     * planifier une tache manuellement dans un planning
     * @param planning le planning dans lequel la tache sera planifiée
     * @param date la date de la journée où on va planifier la tache
     * @param time le temps du début de créneau libre dans lequel on va planifier la tache
     * @return LocalDateTime
     * @throws UnscheduledException si la tache ne peut pas etre planifiée
     */
    @Override
    public LocalDateTime planifierManuellement(Planning planning, LocalDate date, LocalTime time) throws UnscheduledException {
        LocalDateTime planificationDateTime = super.planifierManuellement(planning, date, time);
        this.getChildren().clear();
        return planificationDateTime;
    }

    /**
     * décomposer une tache décomposable en des taches simples si possible
     * @param decomposer le planning pour lequel la tache sera décomposée et le temp du debut de décomposée (planification)
     * @return TreeSet<TacheSimple>
     * @throws DecompositionImpossibleException si la décomposition n'est pas possible
     */
    @Override
    public TreeSet<TacheSimple> decomposer(@NotNull Pair<Planning, LocalDateTime> decomposer) throws DecompositionImpossibleException {
        if (hasChildren() || !isUnscheduled())
            throw new DecompositionImpossibleException();

        Planning planning = decomposer.getFirst();
        LocalDateTime startDateTime = decomposer.getSecond();

        if (startDateTime == null)
            startDateTime = LocalDateTime.now().plusHours(1);

        LocalDate startDate = startDateTime.toLocalDate();
        LocalTime startTime = startDateTime.toLocalTime();

        if (planning.getDateFin().isBefore(startDate))
            throw new DecompositionImpossibleException();

        int subTacheCount = 1;
        Duration saveDuration = getDuree();
        TreeSet<TacheSimple> tachesSimples = new TreeSet<>();


        for (Day day : planning.getDays(startDate, getDeadline().toLocalDate())) {
            boolean isToday = startDate.isEqual(day.getDate());

            for (CreneauLibre creneauLibre : day.getCreneauxLibres()) {
                if (!isToday || startTime.isBefore(creneauLibre.getHeureDebut()))
                    startTime = creneauLibre.getHeureDebut();

                if (!checkDeadline(day, startTime)) {
                    setDuree(saveDuration);
                    throw new DecompositionImpossibleException();
                }

                if (isToday && !startTime.isBefore(creneauLibre.getHeureFin()))
                    continue;

                Duration duration = Duration.between(startTime, creneauLibre.getHeureFin());
                if (getDuree().compareTo(duration) <= 0)
                {
                    tachesSimples.add(new TacheSimple(getNom() + " [" + subTacheCount + "]", getDuree(), getPriority(), getDeadline(), getCategory(), getState()));
                    setDuree(saveDuration);
                    return tachesSimples;
                } else {
                    tachesSimples.add(new TacheSimple(getNom() + " [" + subTacheCount + "]", duration, getPriority(), getDeadline(), getCategory(), getState()));
                    setDuree(getDuree().minus(duration));
                    subTacheCount++;

                    if (!getDuree().isPositive()) {
                        setDuree(saveDuration);
                        return tachesSimples;
                    }
                }
            }
        }

        setDuree(saveDuration);
        throw new DecompositionImpossibleException();
    }

    @Override
    public void deplanifier(Planning planning) {
        if (isUnscheduled())
            return;

        if (!hasChildren()) {
            Day day = planning.getDayByDate(getPlanificationDateTime().toLocalDate());
            day.deleteTache(getPlanificationDateTime());
            setPlanificationDateTime(null);
            return;
        }

        for (TacheSimple tacheSimple : getChildren()) {
            tacheSimple.deplanifier(planning);
        }

        setPlanificationDateTime(null);
    }

    @Override
    public String toString() {
        return "TacheDecomposable{children=" + children + ", super=" + super.toString() + "}";
    }

    //endregion
}
