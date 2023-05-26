package com.example.core;

import com.example.core.exceptions.CreneauLibreDurationException;
import com.example.core.exceptions.UnscheduledException;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class Day implements Comparable<Day>, Serializable {
    //region Attributes

    private final LocalDate date;
    private TreeSet<Creneau> creneaux;

    //endregion

    //region Constructors

    public Day(@NotNull LocalDate date) {
        this.date = date;
        this.creneaux = new TreeSet<>();
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
     * verifier s'il y a un creneau occupée dans une journée
     * @return true s'il y a au moins un creneau occupée, false si non
     */
    public boolean hasCreneauxOccupees() {
        for (Creneau creneau : getCreneaux()) {
            if (creneau.isOccupe())
                return true;
        }

        return false;
    }

    /**
     * calculer le nombre des taches complétées dans une journée
     * @return le nombre des taches complétées dans une journée
     */
    public int getCompletedTachesNumber() {
        int counter = 0;

        for (CreneauOccupe creneauOccupe : getCreneauxOccupes()) {
            if (creneauOccupe.getTache().getState() == State.COMPLETED)
                counter++;
        }

        return counter;
    }

    /**
     * calculer le nombre des taches planifiées dans une journée
     * @return le nombre des taches planifiées dans la journée
     */
    public int getTotalTachesNumber() {
        return getCreneauxOccupes().size();
    }

    /**
     * calculer le rendement journalier
     * @return double le rendement journalier
     */
    public double getRendement() {
        return (getTotalTachesNumber() == 0) ? 0.0 : ((double) getCompletedTachesNumber() / getTotalTachesNumber());
    }

    public HashMap<Category, Integer> countCompletedCategory() {
        HashMap<Category, Integer> categoriesCounter = new HashMap<>();

        for (CreneauOccupe creneauOccupe : getCreneauxOccupes()) {
            Tache tache = creneauOccupe.getTache();
            Category category = tache.getCategory();
            if (category == null || !tache.getState().equals(State.COMPLETED))
                continue;

            if (categoriesCounter.containsKey(category)) {
                categoriesCounter.put(category, categoriesCounter.get(category) + 1);
            } else {
                categoriesCounter.put(category, 1);
            }
        }

        return categoriesCounter;
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
                if (c.getHeureDebut().equals(creneauLibre.getHeureFin())) {
                    try {
                        CreneauLibre toAdd = new CreneauLibre(creneauLibre.getHeureDebut(), c.getHeureFin());
                        creneaux.remove(c);
                        creneaux.add(toAdd);
                        return true;
                    } catch (CreneauLibreDurationException ignored) {}
                }

                if (c.getHeureFin().equals(creneauLibre.getHeureDebut())) {
                    if (it.hasNext() && !(creneauLibre.isAvant(it.next())))
                        return false;

                    try {
                        CreneauLibre toAdd = new CreneauLibre(c.getHeureDebut(), creneauLibre.getHeureFin());
                        creneaux.remove(c);
                        creneaux.add(toAdd);
                        return true;
                    } catch (CreneauLibreDurationException ignored) {}
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

    /**
     * supprimer une tache d'une journée
     * @param planificationDateTime la date + l'heure debut du créneau occupé dans lequel la tache est planifiée
     * @return true si la tache est supprimée, false si non
     */
    public boolean deleteTache(@NotNull LocalDateTime planificationDateTime) {
        if (!planificationDateTime.toLocalDate().isEqual(getDate()))
            return false;

        try {
            Creneau toDelete = getCreneaux().floor(new CreneauLibre(
                    planificationDateTime.toLocalTime(),
                    planificationDateTime.toLocalTime().plus(CreneauLibre.getDureeMin())
            ));

            if (toDelete == null || !toDelete.isOccupe())
                return false;

            if (!planificationDateTime.equals(LocalDateTime.of(getDate(), toDelete.getHeureDebut())))
                return false;

            creneaux.remove(toDelete);

            try {
                ajouterCreneauLibre(new CreneauLibre(toDelete.getHeureDebut(), toDelete.getHeureFin()));
            } catch (CreneauLibreDurationException ignored) {}
            return true;
        } catch (CreneauLibreDurationException e) {
            return false;
        }
    }

    /**
     * deplanifier les taches des creneauOccupe non blockés d'un planning
     * @param planning le planning a liberer ses CreneauxOccupés non blockés
     */
    void libererCreneauxOccupes(Planning planning) {
        for (CreneauOccupe creneauOccupe : getCreneauxOccupes()) {
            if (creneauOccupe.isBlocked())
                continue;

            Tache tache = creneauOccupe.getTache();
            tache.deplanifier(planning);
        }
    }

    /**
     * chercher l'heure début du créneau occupée qui contient une tache
     * @param tache la tache qu'on veut chercher l'heure début de son créneau occupé
     * @return LocalTime
     */
    public LocalTime searchForTachePlanificationTime(Tache tache) {
        for (CreneauOccupe creneauOccupe : getCreneauxOccupes()) {
            if (tache == creneauOccupe.getTache())
                return creneauOccupe.getHeureDebut();
        }

        return null;
    }

    /**
     * parcourir treeSet des creneaux et recherche le premier creneauOccupe
     * @return le premier CreneauOccupe si trouvé sinon null
     */
    public CreneauOccupe getPremierCreneauOccupe() {
        CreneauOccupe premierCreneauOccupe = null;
        for (Creneau creneau : creneaux) {
            if (creneau instanceof CreneauOccupe) {
                premierCreneauOccupe = (CreneauOccupe) creneau;
                break;
            }
        }
        return premierCreneauOccupe ;
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
