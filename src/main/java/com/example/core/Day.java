package com.example.core;

import com.example.core.exceptions.UnscheduledException;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class Day implements Comparable<Day>, Serializable {
    //region Attributes

    private LocalDate date;
    private TreeSet<Creneau> creneaux;

    //endregion

    //region Constructors

    public Day(@NotNull LocalDate date) {
        this.date = date;
        this.creneaux = new TreeSet<>();
    }

    public Day(@NotNull LocalDate date, TreeSet<Creneau> creneaux) {
        this(date);
        this.creneaux = creneaux;
    }

    //endregion

    //region Setter and Getters

    public TreeSet<CreneauLibre> getCreneauxLibres() {
        return creneaux.stream()
                .filter(creneau -> creneau instanceof CreneauLibre)
                .map(creneau -> (CreneauLibre) creneau)
                .collect(Collectors.toCollection(TreeSet::new));
    }

    public TreeSet<CreneauOccupe> getCreneauxOccupes() {
        return creneaux.stream()
                .filter(creneau -> creneau instanceof CreneauOccupe)
                .map(creneau -> (CreneauOccupe) creneau)
                .collect(Collectors.toCollection(TreeSet::new));
    }

    public TreeSet<Creneau> getCreneaux() {
        return creneaux;
    }

    public LocalDate getDate() {
        return date;
    }

    //endregion

    //region Methods

    /**
     * verifier s'il y a un creneau dans une journée
     * @return true s'il y a au moins un creneau, false si non
     */
    public boolean hasCreneaux() {
        return !creneaux.isEmpty();
    }

    /**
     * ajouter un creneau libre dans les creneaux de la journée
     * @param creneauLibre le creneau libre à ajouter
     * @return boolean: true si le creneau est ajouté avec succès, false si non
     */
    public boolean ajouterCreneauLibre(CreneauLibre creneauLibre) {
        if (creneauLibre == null)
            return false;

        for (Creneau c : creneaux) {
            if (!(creneauLibre.isAvant(c) || creneauLibre.isApres(c))) {
                return false;
            }
        }

        creneaux.add(creneauLibre);
        return true;
    }

    /**
     * planifier une tache automatiquement dans une journée
     * @param tache la tache qui va etre planifier dans cette journée
     * @param startDateTime la journée et le temps du début de planification
     * @return TreeSet<Creneau> (CreneauOccupe, CreneauLibre?)
     * @throws UnscheduledException si la tache ne peut pas etre planifiée dans aucun creneau libre
     */
    public TreeSet<Creneau> planifier(@NotNull Tache tache, @NotNull LocalDateTime startDateTime) throws UnscheduledException {
        if (date.isBefore(startDateTime.toLocalDate()))
            throw new UnscheduledException();

        boolean isToday = date.isEqual(startDateTime.toLocalDate());

        for (CreneauLibre creneauLibre : getCreneauxLibres()) {
            if (!tache.checkDeadline(this, startDateTime.toLocalTime()))
                break;

            if (isToday && !creneauLibre.getHeureFin().isAfter(startDateTime.toLocalTime()))
                continue;

            try {
                TreeSet<Creneau> creneaux;
                if (isToday)
                    creneaux = creneauLibre.planifier(tache, startDateTime.toLocalTime());
                else
                    creneaux = creneauLibre.planifier(tache);

                this.creneaux.remove(creneauLibre);
                this.creneaux.addAll(creneaux);
                return creneaux;
            } catch (UnscheduledException ignored) {}
        }

        throw new UnscheduledException();
    }

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

    //endregion
}
