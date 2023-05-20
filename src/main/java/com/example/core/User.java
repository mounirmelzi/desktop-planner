package com.example.core;

import com.example.core.exceptions.InvalidDateTimeException;
import com.example.core.exceptions.UnscheduledException;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public class User implements Serializable {
    //region Attributes

    private String pseudo;
    private Calendrier calendrier;
    private Planning planning;
    private final TreeSet<Tache> taches;
    private final HashSet<Project> projects;
    private Historique historique;
    private HashMap<String, Category> categories;
    private HashMap<Badge, Integer> badges;
    private Duration dureeCreneauLibreMin;
    private int nbrTachesMinParJour;

    //endregion

    //region Constructors

    public User(String pseudo) {
        this.pseudo = pseudo;

        this.calendrier = new Calendrier();
        this.planning = null;
        this.taches = new TreeSet<>();
        this.projects = new HashSet<>();
        this.badges = new HashMap<>();
        this.nbrTachesMinParJour = 3;

        this.dureeCreneauLibreMin = Duration.ofMinutes(30);
        CreneauLibre.setDureeMin(this.dureeCreneauLibreMin);

        this.categories = new HashMap<>();
        categories.put("Studies", new Category("Studies"));
        categories.put("Work", new Category("Work"));
        categories.put("Hobby", new Category("Hobby"));
        categories.put("Sport", new Category("Sport"));
        categories.put("Health", new Category("Health"));
    }

    //endregion

    //region Setter and Getters

    public String getPseudo() {
        return pseudo;
    }

    public Calendrier getCalendrier() {
        return calendrier;
    }

    public Planning getPlanning() {
        return planning;
    }

    public void setCalendrier(Calendrier calendrier) {
        this.calendrier = calendrier;
    }

    public TreeSet<Tache> getTaches() {
        return taches;
    }

    public HashSet<Project> getProjects() {
        return projects;
    }

    public Set<String> getCategories() {
        return categories.keySet();
    }

    public Category getCategorie(String name) {
        return categories.get(name);
    }

    //endregion

    //region Methods

    /**
     * vérifier si un utilisateur a un planning ou non
     * @return true si le planning existe, false si non
     */
    public boolean hasPlanning() {
        return getPlanning() != null;
    }

    /**
     * créer un nouveau planning
     * @param dateDebut la date début de planning
     * @param dateFin la date fin du planning
     * @throws InvalidDateTimeException si la date début du planning est avant today
     */
    public void createPlanning(LocalDate dateDebut, LocalDate dateFin) throws InvalidDateTimeException {
        this.planning = new Planning(dateDebut, dateFin, this.calendrier);
    }

    /**
     * etendre la date fin du planning
     * @param newEndDate la nouvelle date fin de planning
     * @throws InvalidDateTimeException si la nouvelle date fin est avant la date fin ancienne
     */
    public void extendPlanning(@NotNull LocalDate newEndDate) throws InvalidDateTimeException {
        if (newEndDate.isBefore(getPlanning().getDateFin()))
            throw new InvalidDateTimeException();

        getPlanning().setDateFin(newEndDate);
    }

    /**
     * ajouter une tache dans l'ensemble des taches de l'utilisateur
     * @param tache la tache à ajouter
     * @return true si la tache est ajoutée, false si elle existe déjà
     */
    public boolean addTache(@NotNull Tache tache) {
        return this.taches.add(tache);
    }

    /**
     * supprimer une tache de l'ensemble des taches de l'utilisateur
     * @param tache la tache à supprimer
     * @return true si la tache est suprimée, false si non
     */
    public boolean deleteTache(@NotNull Tache tache) {
        tache.deplanifier(getPlanning());
        return this.taches.remove(tache);
    }

    /**
     * planifier automatiquement les taches de l'utilisateur
     * @throws UnscheduledException si les taches ne peut pas etre toutes planifiées
     */
    public void planifierAuto() throws UnscheduledException {
        boolean error = false;

        for (Tache tache : getTaches()) {
            try {
                tache.planifier(planning, LocalDateTime.now());
            } catch (UnscheduledException ignored) {
                error = true;
            }
        }

        if (error)
            throw new UnscheduledException();
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
