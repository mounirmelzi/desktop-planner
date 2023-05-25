package com.example.core;

import com.example.core.exceptions.InvalidDateTimeException;
import com.example.core.exceptions.UnscheduledException;
import com.example.core.utils.Utils;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class User implements Serializable {
    //region Attributes

    private String pseudo;
    private final Calendrier calendrier;
    private Planning planning;
    private final TreeSet<Tache> taches;
    private final HashSet<Project> projects;
    private final Historique historique;
    private final TreeMap<String, Category> categories;
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
        this.historique = new Historique();
        this.setNbrTachesMinParJour(5);
        this.setDureeCreneauLibreMin(Duration.ofMinutes(30));

        this.categories = new TreeMap<>();
        this.addCategory("Studies", "#ff000044");
        this.addCategory("Work", "#0000ff44");
        this.addCategory("Hobby", "#00ff0044");
        this.addCategory("Sport", "#0ff00044");
        this.addCategory("Health", "#000ff044");
    }

    //endregion

    //region Setter and Getters

    public String getPseudo() {
        return pseudo;
    }

    public boolean setPseudo(String pseudo) {
        try {
            User.load(pseudo);
            return false;
        } catch (IOException | ClassNotFoundException ignored) {}

        String oldPseudo = this.pseudo;

        File file = new File(String.format(
                "data%susers%s%s.bin", File.separator, File.separator, oldPseudo
        ));

        this.pseudo = pseudo;

        try {
            this.save();
            boolean deleted = file.delete();
            return true;
        } catch (IOException e) {
            this.pseudo = oldPseudo;
            return false;
        }
    }

    public Calendrier getCalendrier() {
        return calendrier;
    }

    public Historique getHistorique() { return historique; }

    public Planning getPlanning() {
        return planning;
    }

    public TreeSet<Tache> getTaches() {
        return taches;
    }

    public HashSet<Project> getProjects() {
        return projects;
    }

    public Project getProject(String name) {
        for (Project project : getProjects()) {
            if (name.equals(project.getNom()))
                return project;
        }

        return null;
    }

    public Set<String> getCategories() {
        return categories.keySet();
    }

    public Category getCategorie(String name) {
        return categories.get(name);
    }

    public void addCategory(String name, String color) {
        if (color == null) {
            categories.put(name, new Category(name));
            return;
        }

        categories.put(name, new Category(name, color));
    }

    public int getNbrTachesMinParJour() {
        return nbrTachesMinParJour;
    }

    public void setNbrTachesMinParJour(int nbrTachesMinParJour) {
        this.nbrTachesMinParJour = nbrTachesMinParJour;
        if (planning != null)
            planning.setNbrTachesMinParJour(nbrTachesMinParJour);
    }

    public Duration getDureeCreneauLibreMin() {
        return dureeCreneauLibreMin;
    }

    public void setDureeCreneauLibreMin(Duration dureeCreneauLibreMin) {
        this.dureeCreneauLibreMin = dureeCreneauLibreMin;
        CreneauLibre.setDureeMin(dureeCreneauLibreMin);
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
        this.planning.setNbrTachesMinParJour(this.getNbrTachesMinParJour());
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

    public void archiverCurrentPlanning() {
        if (this.planning == null)
            return;

        try {
            Planning deepCopyOfCurrentPlanning = (Planning) Utils.deepCopy(this.planning);
            HashSet<Project> deepCopyOfProjects = (HashSet<Project>) Utils.deepCopy(this.projects);
            historique.archive(deepCopyOfCurrentPlanning, deepCopyOfProjects);

            for (Tache tache : this.getTaches())
                tache.deplanifier(this.planning);

            for (Project project : this.getProjects())
                project.deplanifier(this.planning);

            this.planning = null;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
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
     * ajouter un projet dans l'ensemble des projets de l'utilisateur
     * @param project le project à ajouter
     * @return true si le project est ajoutée, false si il existe déjà
     */
    public boolean addProject(@NotNull Project project) {
        return this.projects.add(project);
    }

    /**
     * supprimer un projet de l'ensemble des projets de l'utilisateur
     * @param project le projet à supprimer
     * @return true si le projet est suprimée, false si non
     */
    public boolean deleteProject(@NotNull Project project) {
        project.deplanifier(getPlanning());
        return this.projects.remove(project);
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

    public void replanifier() throws UnscheduledException {
        planning.libererCreneauxOccupes();
        planifierAuto();
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
