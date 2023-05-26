package com.example.core;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

public class Historique implements Serializable {
    //region Attributes

    private final TreeMap<LocalDateTime, Planning> historiquePlannings ;
    private final TreeMap<LocalDateTime, HashSet<Project>> historiqueProjets ;

    //endregion

    //region Constructors

    public Historique() {
        this.historiquePlannings = new TreeMap<>(Comparator.reverseOrder());
        this.historiqueProjets = new TreeMap<>(Comparator.reverseOrder()) ;
    }

    //endregion

    //region Setter and Getters

    public TreeMap<LocalDateTime, Planning> getHistoriquePlannings() {
        return historiquePlannings;
    }
    public TreeMap<LocalDateTime, HashSet<Project>> getHistoriqueProjets() {
        return  historiqueProjets ;
    }
    public HashSet<Project> getProjetsByDate(LocalDateTime date) {
        return getHistoriqueProjets().get(date);
    }
    //endregion

    //region Methods
    /**
     * recherche une tache dans les projets archivés a une date precise
     * @param date date de l'archivage des projet
     * @param tache la tache que l'on cherche
     * @return Projet ou se trouve la tache, si elle n'y est nul part retourne null
     */
     public Project rechercheTacheDansProjets(LocalDateTime date, Tache tache) {
        for (Project projet: getProjetsByDate(date) ) {
            if (projet.hasTache(tache)) { return projet;}
        }
        return null;
    }

    /**
     * retourne le nombre des plannings achivés
     * @return int le nombre de plannings archivés de l'utilisateur
     */
    public int getNbPlannings() {
        return historiquePlannings.size();
    }

    /**
     * ajoute a la collection de l'archive un planning et une collection de projet a archiver, date de l'archivage correspont a la date actuelle
     * @param planning le planning à archiver
     * @param projects HashSet des projets à archiver
     */
    public void archive(Planning planning, HashSet<Project> projects) {
        LocalDateTime now = LocalDateTime.now();
        historiquePlannings.put(now, planning);
        historiqueProjets.put(now, projects);
    }

    /**
     * recherche l date de l'archivage d'un planning pour l'utilisateur associé
     * @param p le planning que l'on cherche sa date d'archivage
     * @return LocalDateTime la date de l'archivage si le planning est archivé, sinon null
     */
    public LocalDateTime getDateArchivage (Planning p) {
        LocalDateTime key = null ;
        for (Map.Entry<LocalDateTime, Planning> entry : historiquePlannings.entrySet()) {
            if (entry.getValue().equals(p)) {
                key = entry.getKey();
                break;
            }
        }

        return key;
    }

    /**
     * retourne le dernier planning ajouté à la collection des plannings archivés
     * @return entry <LocalDateTime, Planning> ou planning est le planning archivé, et la clé est la date de son archivage
     */
    public Map.Entry<LocalDateTime, Planning> getLastPlanningArchive () {
        return historiquePlannings.firstEntry();
    }

    /**
     * calculer le nombre de projets completes archivés dans une date precis
     * @param date date de l'archivage des projets
     * @return int le nombre des projets completes
     */
    public int getNbProjetsCompletes(LocalDateTime date) {
        if (getProjetsByDate(date) == null)
            return 0;

        int cpt = 0;
        for (Project projet: getProjetsByDate(date) ) {
            if (projet.getState()==State.COMPLETED) { cpt++;}
        }
        return cpt;
    }

    //endregion
}
