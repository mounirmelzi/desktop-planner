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

    public static void main(String[] args) throws CreneauLibreDurationException {
        System.out.println("hee");
    }



    /*
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

        TacheDecomposable tache = new TacheDecomposable(
                "tache 1",
                Duration.ofHours(3),
                Priority.MEDIUM,
                LocalDateTime.of(
                        LocalDate.now().plusDays(9),
                        LocalTime.of(8, 30)
                )
        );

        try {
            TreeSet<TacheSimple> tttt = tache.decomposer(new Pair<>(planning, LocalDateTime.of(
                    LocalDate.now().plusDays(0),
                    LocalTime.of(7, 0)
            )));

            tache.setChildren(tttt);
            System.out.println("done");

        } catch (DecompositionImpossibleException e) {
            throw new RuntimeException(e);
        }

        tache.planifier(planning, LocalDateTime.of(
                LocalDate.now().plusDays(0),
                LocalTime.of(7, 0)
        ));




        for (Day d : planning.getDays()) {
            System.out.println(d.getDate());
            for (Creneau c : d.getCreneaux()) {
                System.out.println(c);
            }
        }
    }
    */

    /*
    public static void main(String[] args) throws CreneauLibreDurationException, InvalidDateTimeException, UnscheduledException, DecompositionImpossibleException {
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
                                LocalTime.of(9, 0)
                        )
                )))
        )));

        Planning planning = new Planning(LocalDate.now(), LocalDate.now().plusDays(1), calendrier);

        TacheDecomposable tache = new TacheDecomposable(
                "tache 1",
                Duration.ofMinutes(180), // duree
                Priority.MEDIUM,
                LocalDateTime.of( //Deadline
                        LocalDate.now().plusDays(4),
                        LocalTime.of(9, 0)
                )
        );

        LocalDateTime dateTime = LocalDateTime.of( // startDateTime
                LocalDate.now(),
                LocalTime.of(8, 0)
        );

        TreeSet<TacheSimple> tt = tache.decomposer(new Pair<>(planning, dateTime));

        for (TacheSimple t : tt) {
            System.out.println(t);
        }

        System.out.println();

        for (TacheSimple t : tt) {
            t.planifier(planning, dateTime);
        }

        for (Day d : planning.getDays()) {
            System.out.println(d.getDate());
            for (Creneau c : d.getCreneaux()) {
                System.out.println(c);
            }
        }
    }
    */

    //region test de la planification
    /*
    public static void main(String[] args) throws CreneauLibreDurationException, InvalidDateTimeException, UnscheduledException {
        Calendrier calendrier = new Calendrier(new TreeSet<>(List.of(
                new Day(LocalDate.now(), new TreeSet<>(List.of(
                        new CreneauLibre(
                                LocalTime.of(8, 0),
                                LocalTime.of(11, 0)
                        ),
                        new CreneauLibre(
                                LocalTime.of(13, 0),
                                LocalTime.of(14, 0)
                        )
                ))),
                new Day(LocalDate.now().plusDays(1), new TreeSet<>(List.of(
                        new CreneauLibre(
                                LocalTime.of(8, 0),
                                LocalTime.of(9, 0)
                        )
                )))
        )));

        Planning planning = new Planning(LocalDate.now(), LocalDate.now().plusDays(1), calendrier);

        TacheDecomposable tache = new TacheDecomposable(
                "tache 1",
                Duration.ofMinutes(30),
                Priority.MEDIUM,
                LocalDateTime.of(
                        LocalDate.now().plusDays(2),
                        LocalTime.of(10, 0)
                )
        );

        TacheDecomposable tache2 = new TacheDecomposable(
                "tache 2",
                Duration.ofMinutes(30),
                Priority.MEDIUM,
                LocalDateTime.of(
                        LocalDate.now().plusDays(2),
                        LocalTime.of(10, 0)
                )
        );

        planning.planifier(tache, LocalDateTime.of(
                LocalDate.now().plusDays(0),
                LocalTime.of(10, 45)
        ));

        planning.planifier(tache2, LocalDateTime.of(
                LocalDate.now().plusDays(0),
                LocalTime.of(13, 0)
        ));


        for (Day d : planning.getDays()) {
            System.out.println(d.getDate());
            for (Creneau c : d.getCreneaux()) {
                System.out.println(c);
            }
        }
    }
    */
    //endregion
}
