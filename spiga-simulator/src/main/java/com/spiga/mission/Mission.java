package com.spiga.mission;

import com.spiga.core.ActifMobile;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Mission {
    private final String id;
    private ObjectifMission objectif;
    private StatutMission statut;
    private final List<ActifMobile> actifsAssignes;

    public Mission(String id, ObjectifMission objectif) {
        this.id = id;
        this.objectif = objectif;
        this.statut = StatutMission.PLANIFIEE;
        this.actifsAssignes = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void assignerActif(ActifMobile actif) {
        if (actif == null || actifsAssignes.contains(actif)) {
            return;
        }
        this.actifsAssignes.add(actif);
    }

    public List<ActifMobile> getActifsAssignes() {
        return Collections.unmodifiableList(actifsAssignes);
    }

    public void demarrer() {
        this.statut = StatutMission.EN_COURS;
        for (ActifMobile actif : actifsAssignes) {
            actif.demarrer();
        }
    }

    public void terminer() {
        this.statut = StatutMission.TERMINEE;
        for (ActifMobile actif : actifsAssignes) {
            actif.arreter();
        }
    }

    public StatutMission getStatut() {
        return statut;
    }

    public ObjectifMission getObjectif() {
        return objectif;
    }

    public void setObjectif(ObjectifMission objectif) {
        this.objectif = objectif;
    }
}
