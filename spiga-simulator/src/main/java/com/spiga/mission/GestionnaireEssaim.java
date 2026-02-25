package com.spiga.mission;

import com.spiga.core.ActifMobile;
import com.spiga.core.EtatOperationnel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Manages the fleet of mobile assets.
 */
public class GestionnaireEssaim {
    private final List<ActifMobile> flotte;

    public GestionnaireEssaim() {
        this.flotte = new ArrayList<>();
    }

    public void ajouterActif(ActifMobile actif) {
        if (actif != null && !flotte.contains(actif)) {
            this.flotte.add(actif);
        }
    }

    public List<ActifMobile> getFlotte() {
        return Collections.unmodifiableList(flotte);
    }

    public List<ActifMobile> getActifsDisponibles() {
        return flotte.stream()
                .filter(a -> a.getEtat() == EtatOperationnel.AU_SOL)
                .collect(Collectors.toList());
    }

    public ActifMobile suggererActif(double minAutonomie) {
        return getActifsDisponibles().stream()
                .filter(a -> a.getAutonomieActuelle() >= minAutonomie)
                .findFirst()
                .orElse(null);
    }

    public ActifMobile getActifParId(String id) {
        return flotte.stream().filter(a -> a.getId().equals(id)).findFirst().orElse(null);
    }
}
