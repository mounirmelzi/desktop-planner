package com.example.core;

import com.example.core.exceptions.CreneauLibreDurationException;
import com.example.core.exceptions.DecompositionImpossibleException;

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;
import java.util.TreeSet;

public class CreneauLibre extends Creneau implements IDecomposable<Tache, Creneau> {
    private static Duration dureeMin = Duration.ofMinutes(30);

    public CreneauLibre(LocalTime heureDebut, LocalTime heureFin) throws CreneauLibreDurationException {
        super(heureDebut, heureFin);

        if (this.getDuration().compareTo(CreneauLibre.getDureeMin()) < 0) {
            throw new CreneauLibreDurationException();
        }
    }

    public static Duration getDureeMin() {
        return dureeMin;
    }

    public static void setDureeMin(Duration dureeMin) {
        CreneauLibre.dureeMin = dureeMin;
    }

    /**
     * decomposer un creneau libre en un creneau occupée par une tache et un autre creneau libre
     *
     * @param decomposer la tache qui va etre planifier dans ce creneau libre
     * @return TreeSet<Creneau> [CreneauOccupe, CreneauLibre?]
     * @throws DecompositionImpossibleException si la durée de la tache est plus grande que la durée de creneau libre
     */
    @Override
    public TreeSet<Creneau> decomposer(Tache decomposer) throws DecompositionImpossibleException {
        Duration dureeTache = decomposer.getDuree();
        Duration dureeCreneau = this.getDuration();

        if (dureeCreneau.compareTo(dureeTache) < 0) {
            throw new DecompositionImpossibleException();
        }

        LocalTime heureDebut = this.getHeureDebut();
        LocalTime heureFin = this.getHeureFin();
        LocalTime heureDecomposition = (LocalTime) dureeTache.addTo(heureDebut);

        try {
            CreneauLibre nouveauCreneauLibre = new CreneauLibre(heureDecomposition, heureFin);
            CreneauOccupe nouveauCreneauOccupe = new CreneauOccupe(heureDebut, heureDecomposition, decomposer);

            return new TreeSet<>(List.of(nouveauCreneauOccupe, nouveauCreneauLibre));
        } catch (CreneauLibreDurationException exception) {
            return new TreeSet<>(List.of(
                    new CreneauOccupe(heureDebut, heureFin, decomposer)
            ));
        }
    }

    @Override
    public String toString() {
        return "CreneauLibre{" +
                "heureDebut=" + getHeureDebut() +
                ", heureFin=" + getHeureFin() +
                '}';
    }
}
