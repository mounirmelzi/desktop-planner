package com.example.core;

import java.io.*;
import java.util.HashMap;

public class User implements Serializable {

    //region Attributes

    private HashMap<Badge, Integer> badges;
    private String pseudo;
    private Calendrier calendrier;
    private HashMap<Category, String> categoriesColor;

    //endregion

    //region Constructors

    public User(String pseudo) {
        this.pseudo = pseudo;
        this.badges = new HashMap<>();
        this.calendrier = new Calendrier();

        // init categoriesColor to the default color
        categoriesColor = new HashMap<>();
        for (Category category : Category.values()) categoriesColor.put(category, Category.getDefaultColor());
    }

    public User(String pseudo, Calendrier calendrier) {
        this(pseudo);
        this.calendrier = calendrier;
    }

    public User(String pseudo, Calendrier calendrier, HashMap<Badge, Integer> badges) {
        this(pseudo, calendrier);
        this.badges = badges;
    }

    public User(String pseudo, Calendrier calendrier, HashMap<Badge, Integer> badges, HashMap<Category, String> categoriesColor) {
        this(pseudo, calendrier, badges);
        this.categoriesColor = categoriesColor;
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
            this.badges = user.badges;
            this.categoriesColor = user.categoriesColor;
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

    @Override
    public String toString() {
        return "User{" +
                "badges=" + badges +
                ", pseudo='" + pseudo + '\'' +
                ", calendrier=" + calendrier +
                ", categoriesColor=" + categoriesColor +
                '}';
    }
}
