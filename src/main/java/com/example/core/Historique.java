package com.example.core;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class Historique implements Serializable {
    //region Attributes

    private TreeMap<LocalDateTime, Planning> historique;

    //endregion

    //region Constructors

    public Historique() {
        this.historique = new TreeMap<>(Comparator.reverseOrder());
    }

    //endregion

    //region Setter and Getters

    public TreeMap<LocalDateTime, Planning> getHistorique() {
        return historique;
    }

    //endregion

    //region Methods

    public int getSize() {
        return historique.size();
    }

    public void archive(Planning planning) {
        historique.put(LocalDateTime.now(), planning);
    }

    public Planning[] getAllPlannings() {
        return historique.values().toArray(new Planning[0]);
    }

    public Planning getPlaningByDate(LocalDateTime archiveDateTime) {
        return historique.get(archiveDateTime);
    }

    public Planning getPlanningByIndex(int index) {
        if (index < 0 || index >= getSize())
            return null;

        try {
            return getAllPlannings()[index];
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    public Planning deletePlaningByDate(LocalDateTime archiveDateTime) {
        return historique.remove(archiveDateTime);
    }

    public Planning deletePlanningByIndex(int index) {
        if (index < 0 || index >= getSize())
            return null;

        ArrayList<LocalDateTime> keys = new ArrayList<>(historique.keySet());
        LocalDateTime key = keys.get(index);

        return historique.remove(key);
    }

    public Planning restore() {
        Map.Entry<LocalDateTime, Planning> firstEntry = historique.firstEntry();
        historique.remove(firstEntry.getKey());
        return firstEntry.getValue();
    }

    public LocalDateTime getDateArchivage (Planning p) {
        LocalDateTime key = null ;
        for (Map.Entry<LocalDateTime, Planning> entry : historique.entrySet()) {
            if (entry.getValue().equals(p)) {
                key = entry.getKey();
                break;
            }
        }
        return key;
    }

    public Map.Entry<LocalDateTime, Planning> getLastPlanningArchive () {
        return historique.firstEntry();
    }

    //endregion
}
