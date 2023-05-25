package com.example.core;

import com.example.core.exceptions.CreneauLibreDurationException;
import com.example.core.exceptions.DecompositionImpossibleException;
import com.example.core.exceptions.UnscheduledException;
import com.example.core.utils.Pair;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;
import java.util.TreeSet;

public class CreneauLibre extends Creneau implements IDecomposable<Pair<Tache, LocalTime>, Creneau> {
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
     * decomposer un creneau libre en un creneau occupée par une tache et un autre creneau libre si possible
     * @param decomposer la tache qui va décomposer ce creneau libre et le temp du debut de planification
     * @return TreeSet<Creneau> [CreneauOccupe, CreneauLibre]
     * @throws DecompositionImpossibleException si on ne peut pas décomposer le Creneau Libre
     */
    @Override
    public TreeSet<Creneau> decomposer(@NotNull Pair<Tache, LocalTime> decomposer) throws DecompositionImpossibleException {
        Tache tache = decomposer.getFirst();
        LocalTime startTime = decomposer.getSecond();

        LocalTime heureDecomposition = startTime.plus(tache.getDuree());

        try {
            CreneauOccupe nouveauCreneauOccupe = new CreneauOccupe(startTime, heureDecomposition, tache);
            CreneauLibre nouveauCreneauLibre = new CreneauLibre(heureDecomposition, getHeureFin());

            return new TreeSet<>(List.of(nouveauCreneauOccupe, nouveauCreneauLibre));
        } catch (CreneauLibreDurationException exception) {
            throw new DecompositionImpossibleException();
        }
    }

    /**
     * planifier une tache dans un creneau libre
     * @param tache tache la tache qui va etre planifier dans ce creneau libre
     * @param startTime le temps du début de planification
     * @return TreeSet<Creneau> (CreneauLibre?, CreneauOccupe, CreneauLibre?)
     * @throws UnscheduledException si la durée de la tache est plus grande que la durée de creneau libre
     */
    TreeSet<Creneau> planifier(@NotNull Tache tache, LocalTime startTime) throws UnscheduledException {
        Duration duration = Duration.between(startTime, getHeureFin());
        if (duration.compareTo(tache.getDuree()) < 0)
            throw new UnscheduledException();

        TreeSet<Creneau> creneaux = new TreeSet<>();

        try {
            creneaux.addAll(decomposer(new Pair<>(tache, startTime)));
        } catch (DecompositionImpossibleException e) {
            creneaux.add(new CreneauOccupe(startTime, getHeureFin(), tache));
        }

        try{
            creneaux.add(new CreneauLibre(getHeureDebut(), startTime));
        } catch (CreneauLibreDurationException ignored) {}

        return creneaux;
    }

    //endregion
}
