package com.example.core;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class Historique implements Serializable {
    //region Attributes

    private TreeMap<LocalDateTime, Planning> historiquePlannings ;
    private TreeMap<LocalDateTime, HashSet<Project>> historiqueProjets ;

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
   //endregion

    //region Methods

    public HashSet<Project> getProjetsByDate(LocalDateTime date) {
        return getHistoriqueProjets().get(date);
    }
    public Project rechercheTacheDansProjets(LocalDateTime date, Tache tache) {
        for (Project projet: getProjetsByDate(date) ) {
            if (projet.hasTache(tache)) { return projet;}
        }
        return null;
    }
    public int getNbPlannings() {
        return historiquePlannings.size();
    }

    public void archive(Planning planning, HashSet<Project> projects) {
        LocalDateTime now = LocalDateTime.now();
        historiquePlannings.put(now, planning);
        historiqueProjets.put(now, projects);
    }

    public Planning[] getAllPlannings() {
        return historiquePlannings.values().toArray(new Planning[0]);
    }

    public Planning getPlaningByDate(LocalDateTime archiveDateTime) {
        return historiquePlannings.get(archiveDateTime);
    }

    public Planning getPlanningByIndex(int index) {
        if (index < 0 || index >= getNbPlannings())
            return null;

        try {
            return getAllPlannings()[index];
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    public Planning deletePlaningByDate(LocalDateTime archiveDateTime) {
        return historiquePlannings.remove(archiveDateTime);
    }

    public Planning deletePlanningByIndex(int index) {
        if (index < 0 || index >= getNbPlannings())
            return null;

        ArrayList<LocalDateTime> keys = new ArrayList<>(historiquePlannings.keySet());
        LocalDateTime key = keys.get(index);

        return historiquePlannings.remove(key);
    }

    public Planning restore() {
        Map.Entry<LocalDateTime, Planning> firstEntry = historiquePlannings.firstEntry();
        historiquePlannings.remove(firstEntry.getKey());
        return firstEntry.getValue();
    }

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

    public Map.Entry<LocalDateTime, Planning> getLastPlanningArchive () {
        return historiquePlannings.firstEntry();
    }

    public int getNbProjetsCompletes(LocalDateTime date) {
        int cpt = 0;
        for (Project projet: getProjetsByDate(date) ) {
            if (projet.getState()==State.COMPLETED) { cpt++;}
        }
        return cpt;
    }

    //endregion
}
