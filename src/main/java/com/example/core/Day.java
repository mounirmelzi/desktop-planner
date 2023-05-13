package com.example.core;

import com.example.core.exceptions.CreneauLibreDurationException;
import com.example.core.exceptions.UnscheduledException;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Iterator;
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

        Iterator<Creneau> it = creneaux.iterator();
        while (it.hasNext()) {
            Creneau c = it.next();

            if (!(creneauLibre.isAvant(c) || creneauLibre.isApres(c)))
                return false;

            if (c.isLibre()) {
                if (c.getHeureDebut() == creneauLibre.getHeureFin()) {
                    c.setHeureDebut(creneauLibre.getHeureDebut());
                    return true;
                }

                if (c.getHeureFin() == creneauLibre.getHeureDebut()) {
                    if (it.hasNext() && !(creneauLibre.isAvant(it.next())))
                        return false;

                    c.setHeureFin(creneauLibre.getHeureFin());
                    return true;
                }
            }

            if (c.isApres(creneauLibre))
                break;
        }

        creneaux.add(creneauLibre);
        return true;
    }

    /**
     * planifier une tache automatiquement dans une journée
     * @param tache la tache qui va etre planifier dans cette journée
     * @param startDateTime la journée et le temps du début de planification
     * @return TreeSet<Creneau> (CreneauLibre?, CreneauOccupe, CreneauLibre?)
     * @throws UnscheduledException si la tache ne peut pas etre planifiée dans aucun creneau libre
     */
    TreeSet<Creneau> planifier(@NotNull Tache tache, @NotNull LocalDateTime startDateTime) throws UnscheduledException {
        LocalDate startDate = startDateTime.toLocalDate();
        LocalTime startTime = startDateTime.toLocalTime();

        boolean isToday = date.isEqual(startDate);

        for (CreneauLibre creneauLibre : getCreneauxLibres()) {
            if (!isToday || startTime.isBefore(creneauLibre.getHeureDebut()))
                startTime = creneauLibre.getHeureDebut();

            if (!tache.checkDeadline(this, startTime))
                break;

            if (isToday && !startTime.isBefore(creneauLibre.getHeureFin()))
                continue;

            try {
                TreeSet<Creneau> creneaux = creneauLibre.planifier(tache, startTime);

                this.creneaux.remove(creneauLibre);
                this.creneaux.addAll(creneaux);
                return creneaux;
            } catch (UnscheduledException ignored) {}
        }

        throw new UnscheduledException();
    }

    /**
     * planifier une tache manuellement dans un créneau libre d'une journée
     * @param tache la tache qui va etre planifier dans cette journée
     * @param time le temps du début de créneau libre dans lequel on va planifier la tache
     * @return TreeSet<Creneau> (CreneauOccupe, CreneauLibre?)
     * @throws UnscheduledException si la tache ne peut pas etre planifiée dans le creneau libre
     */
    TreeSet<Creneau> planifierManuellement(Tache tache, LocalTime time) throws UnscheduledException {
        CreneauLibre creneauLibre;

        try {
            creneauLibre = new CreneauLibre(time, LocalTime.MAX);
        } catch (CreneauLibreDurationException e) {
            throw new UnscheduledException();
        }

        creneauLibre = this.getCreneauxLibres().floor(creneauLibre);

        if (creneauLibre == null || !creneauLibre.getHeureDebut().equals(time))
            throw new UnscheduledException();

        if (!tache.checkDeadline(this, creneauLibre.getHeureDebut()))
            throw new UnscheduledException();

        TreeSet<Creneau> creneaux = creneauLibre.planifier(tache, time);
        this.creneaux.remove(creneauLibre);
        this.creneaux.addAll(creneaux);
        return creneaux;
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
