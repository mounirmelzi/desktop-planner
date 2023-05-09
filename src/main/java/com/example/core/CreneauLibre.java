package com.example.core;

import com.example.core.exceptions.CreneauLibreDurationException;
import com.example.core.exceptions.DecompositionImpossibleException;
import com.example.core.exceptions.UnscheduledException;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;
import java.util.TreeSet;

public class CreneauLibre extends Creneau implements IDecomposable<Tache, Creneau> {
    //region Attributes

    private static Duration dureeMin = Duration.ofMinutes(30);

    //endregion

    //region Constructors

    public CreneauLibre(LocalTime heureDebut, LocalTime heureFin) throws CreneauLibreDurationException {
        super(heureDebut, heureFin);

        if (getDuration().compareTo(CreneauLibre.getDureeMin()) < 0) {
            throw new CreneauLibreDurationException();
        }
    }

    //endregion

    //region Setter and Getters

    public static Duration getDureeMin() {
        return dureeMin;
    }

    public static void setDureeMin(Duration dureeMin) {
        CreneauLibre.dureeMin = dureeMin;
    }

    //endregion

    //region Methods

    /**
     * decomposer un creneau libre en un creneau occupée par une tache et un autre creneau libre
     * @param decomposer la tache qui va décomposer ce creneau libre
     * @return TreeSet<Creneau> [CreneauOccupe, CreneauLibre]
     * @throws DecompositionImpossibleException si on ne peut pas décomposer le Creneau Libre
     */
    @Override
    public TreeSet<Creneau> decomposer(Tache decomposer) throws DecompositionImpossibleException {
        if (decomposer == null) {
            throw new DecompositionImpossibleException();
        }

        LocalTime heureDecomposition = (LocalTime) decomposer.getDuree().addTo(getHeureDebut());

        try {
            CreneauLibre nouveauCreneauLibre = new CreneauLibre(heureDecomposition, getHeureFin());
            CreneauOccupe nouveauCreneauOccupe = new CreneauOccupe(getHeureDebut(), heureDecomposition, decomposer);

            return new TreeSet<>(List.of(nouveauCreneauOccupe, nouveauCreneauLibre));
        } catch (CreneauLibreDurationException exception) {
            throw new DecompositionImpossibleException();
        }
    }

    /**
     * planifier une tache dans un creneau libre
     * @param tache la tache qui va etre planifier dans ce creneau libre
     * @return TreeSet<Creneau> (CreneauOccupe, CreneauLibre?)
     * @throws UnscheduledException si la durée de la tache est plus grande que la durée de creneau libre
     */
    public TreeSet<Creneau> planifier(@NotNull Tache tache) throws UnscheduledException {
        if (getDuration().compareTo(tache.getDuree()) < 0) {
            throw new UnscheduledException();
        }

        try {
            return decomposer(tache);
        } catch (DecompositionImpossibleException e) {
            return new TreeSet<>(List.of(new CreneauOccupe(getHeureDebut(), getHeureFin(), tache)));
        }
    }

    @Override
    public String toString() {
        return "CreneauLibre{" +
                "heureDebut=" + getHeureDebut() +
                ", heureFin=" + getHeureFin() +
                '}';
    }

    //endregion
}
