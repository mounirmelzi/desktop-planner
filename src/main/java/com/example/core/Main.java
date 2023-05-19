package com.example.core;

import com.example.core.exceptions.CreneauLibreDurationException;
import com.example.core.exceptions.DecompositionImpossibleException;
import com.example.core.exceptions.InvalidDateTimeException;
import com.example.core.exceptions.UnscheduledException;
import com.example.core.utils.Pair;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.TreeSet;

public class Main {
    public static void main(String[] args) throws CreneauLibreDurationException, InvalidDateTimeException, UnscheduledException {
        Calendrier calendrier = new Calendrier(new TreeSet<>(List.of(
                new Day(LocalDate.now(), new TreeSet<>(List.of(
                        new CreneauLibre(
                                LocalTime.of(8, 0),
                                LocalTime.of(9, 0)
                        ),
                        new CreneauLibre(
                                LocalTime.of(13, 0),
                                LocalTime.of(14, 5)
                        )
                ))),
                new Day(LocalDate.now().plusDays(1), new TreeSet<>(List.of(
                        new CreneauLibre(
                                LocalTime.of(8, 0),
                                LocalTime.of(8, 30)
                        )
                ))),
                new Day(LocalDate.now().plusDays(2), new TreeSet<>(List.of(
                        new CreneauLibre(
                                LocalTime.of(8, 0),
                                LocalTime.of(8, 30)
                        )
                ))),
                new Day(LocalDate.now().plusDays(3), new TreeSet<>(List.of(
                        new CreneauLibre(
                                LocalTime.of(8, 0),
                                LocalTime.of(8, 30)
                        )
                ))),
                new Day(LocalDate.now().plusDays(4), new TreeSet<>(List.of(
                        new CreneauLibre(
                                LocalTime.of(8, 0),
                                LocalTime.of(9, 0)
                        )
                ))),
                new Day(LocalDate.now().plusDays(5), new TreeSet<>(List.of(
                        new CreneauLibre(
                                LocalTime.of(8, 0),
                                LocalTime.of(9, 0)
                        )
                ))),
                new Day(LocalDate.now().plusDays(6), new TreeSet<>(List.of(
                        new CreneauLibre(
                                LocalTime.of(8, 0),
                                LocalTime.of(9, 0)
                        )
                ))),
                new Day(LocalDate.now().plusDays(7), new TreeSet<>(List.of(
                        new CreneauLibre(
                                LocalTime.of(8, 0),
                                LocalTime.of(9, 0)
                        )
                ))),
                new Day(LocalDate.now().plusDays(8), new TreeSet<>(List.of(
                        new CreneauLibre(
                                LocalTime.of(8, 0),
                                LocalTime.of(9, 0)
                        )
                ))),
                new Day(LocalDate.now().plusDays(9), new TreeSet<>(List.of(
                        new CreneauLibre(
                                LocalTime.of(8, 0),
                                LocalTime.of(9, 0)
                        )
                )))
        )));

        Planning planning = new Planning(LocalDate.now(), LocalDate.now().plusDays(9), calendrier);

        TacheSimple tache = new TacheSimple(
                "tache 1",
                Duration.ofMinutes(10),
                Priority.MEDIUM,
                LocalDateTime.of(
                        LocalDate.now().plusDays(4),
                        LocalTime.of(8, 30)
                )
        );

        tache.setPeriodicity(1);

        tache.planifier(planning, LocalDateTime.now());

        tache.deplanifier(planning);

        for (Day d : planning.getDays()) {
            System.out.println(d.getDate());
            for (Creneau c : d.getCreneaux()) {
                System.out.println(c);
            }
        }
    }
    //endregion
}
