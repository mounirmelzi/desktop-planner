package com.example.core;

import java.io.*;
import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class User implements Serializable {

    //region Attributes

    private String pseudo;
    private Calendrier calendrier;
    private HashSet<Category> categories;
    private HashMap<Badge, Integer> badges;
    private Duration dureeCreneauLibreMin;
    private int nbrTachesMinParJour;

    //endregion

    //region Constructors

    public User(String pseudo) {
        this.pseudo = pseudo;

        this.calendrier = new Calendrier();
        this.badges = new HashMap<>();
        this.dureeCreneauLibreMin = Duration.ofMinutes(30);
        this.nbrTachesMinParJour = 3;

        this.categories = new HashSet<>(Arrays.asList(
                new Category("Studies"),
                new Category("Work"),
                new Category("Hobby"),
                new Category("Sport"),
                new Category("Health")
        ));
    }

    //endregion

    //region Methods

    /**
     * Read an User object from .bin file
     *
     * @param pseudo user's pseudo
     * @return User object
     */
    public static User load(String pseudo) {
        User user = null;

        try {
            FileInputStream fis = new FileInputStream(String.format(
                    "data%susers%s%s.bin", File.separator, File.separator, pseudo
            ));
            ObjectInputStream ois = new ObjectInputStream(fis);

            user = (User) ois.readObject();

            ois.close();
            fis.close();
        } catch (FileNotFoundException ex) {
            System.out.println("Le fichier qui contient l'utilisateur n'existe pas pour le charcher");
        } catch (EOFException ex) {
            System.out.println("Le fichier qui contient l'utilisateur est endommagé");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return user;
    }

    /**
     * Save an User object in .bin file
     */
    public void save() {
        try {
            FileOutputStream fos = new FileOutputStream(String.format(
                    "data%susers%s%s.bin", File.separator, File.separator, this.pseudo
            ));
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            oos.writeObject(this);

            oos.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Read an User data from .bin file
     */
    public void load() {
        try {
            FileInputStream fis = new FileInputStream(String.format(
                    "data%susers%s%s.bin", File.separator, File.separator, this.pseudo
            ));
            ObjectInputStream ois = new ObjectInputStream(fis);

            User user = (User) ois.readObject();

            ois.close();
            fis.close();

            this.pseudo = user.pseudo;
            this.calendrier = user.calendrier;
            this.categories = user.categories;
            this.badges = user.badges;
            this.dureeCreneauLibreMin = user.dureeCreneauLibreMin;
            this.nbrTachesMinParJour = user.nbrTachesMinParJour;
        } catch (FileNotFoundException ex) {
            System.out.println("Le fichier qui contient l'utilisateur n'existe pas pour le charcher");
        } catch (EOFException ex) {
            System.out.println("Le fichier qui contient l'utilisateur est endommagé");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //endregion

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
}
