package com.example.core;

import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeSet;

public class User implements Serializable {
    //region Attributes

    private final TreeSet<Tache> taches = new TreeSet<>(); // les taches no programmées : unscheduled (l'ensemble des taches)
    private final HashSet<Project> projects = new HashSet<>();
    private String pseudo;
    private Calendrier calendrier;
    private Planning planning;
    private Historique historique;
    private HashSet<Category> categories;
    private HashMap<Badge, Integer> badges;
    private Duration dureeCreneauLibreMin;
    private int nbrTachesMinParJour;

    //endregion

    //region Constructors

    public User(String pseudo) {
        this.pseudo = pseudo;

        this.calendrier = new Calendrier();
        this.planning = null;
        this.badges = new HashMap<>();
        this.nbrTachesMinParJour = 3;

        this.dureeCreneauLibreMin = Duration.ofMinutes(30);
        CreneauLibre.setDureeMin(this.dureeCreneauLibreMin);

        this.categories = new HashSet<>(Arrays.asList(
                new Category("Studies"),
                new Category("Work"),
                new Category("Hobby"),
                new Category("Sport"),
                new Category("Health")
        ));
    }

    //endregion

    //region Setter and Getters

    public String getPseudo() {
        return pseudo;
    }

    public Calendrier getCalendrier() {
        return calendrier;
    }

    public void setCalendrier(Calendrier calendrier) {
        this.calendrier = calendrier;
    }

    //endregion

    //region Methods

    public void creerPlanning () {
        // TODO: creer un planning: attacher a ce planning le calendrier de l'utilisateur
    }

    public void planifierAuto() {
        // TODO: planifier l'ensemble des taches this.taches automatiquement dans this.planning
    }

    /**
     * Read an User object from .bin file
     *
     * @param pseudo user's pseudo
     * @return User object
     */
    @NotNull
    public static User load(@NotNull String pseudo) throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream(String.format(
                "data%susers%s%s.bin", File.separator, File.separator, pseudo
        ));
        ObjectInputStream ois = new ObjectInputStream(fis);

        User user = (User) ois.readObject();

        ois.close();
        fis.close();

        CreneauLibre.setDureeMin(user.dureeCreneauLibreMin);
        return user;
    }

    /**
     * Save an User object in .bin file
     */
    public void save() throws IOException {
        // create the save folder if it doesn't exist
        File folder = new File(String.format("data%susers", File.separator));
        if (!folder.exists()) {
            boolean success = folder.mkdirs();
        }

        // save the User object
        FileOutputStream fos = new FileOutputStream(String.format(
                "data%susers%s%s.bin", File.separator, File.separator, this.pseudo
        ));
        ObjectOutputStream oos = new ObjectOutputStream(fos);

        oos.writeObject(this);

        oos.close();
        fos.close();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return pseudo.equals(user.pseudo);
    }

    @Override
    public int hashCode() {
        return pseudo.hashCode();
    }

    //endregion
}
