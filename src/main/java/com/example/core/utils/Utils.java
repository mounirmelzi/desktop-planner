package com.example.core.utils;

import com.example.core.Creneau;
import com.example.core.CreneauOccupe;
import com.example.core.Day;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.TreeSet;

public class Utils {
    public static Object deepCopy(Object object) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(object);
        oos.flush();
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bais);
        return ois.readObject();
    }

    public static LocalDateTime dateTimePairToLocalDateTime(@NotNull Pair<Day, TreeSet<Creneau>> pair) {
        LocalDate date = pair.getFirst().getDate();
        CreneauOccupe creneauOccupe = null;

        for (Creneau c : pair.getSecond()) {
            if (c instanceof CreneauOccupe)
                creneauOccupe = (CreneauOccupe) c;
        }

        assert creneauOccupe != null;
        return LocalDateTime.of(date, creneauOccupe.getHeureDebut());
    }

    public static int compareBetweenDateTimePair(Pair<Day, TreeSet<Creneau>> pair1, Pair<Day, TreeSet<Creneau>> pair2) {
        LocalDateTime localDateTime1 = Utils.dateTimePairToLocalDateTime(pair1);
        LocalDateTime localDateTime2 = Utils.dateTimePairToLocalDateTime(pair2);

        return localDateTime1.compareTo(localDateTime2);
    }

    public static LocalTime stringToLocalTime(String time) {
        try {
            return LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm:ss"));
        } catch (DateTimeParseException e) {
            return null;
        }
    }
}
