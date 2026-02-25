package com.spiga.core;

import com.spiga.env.ZoneOperation;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract class representing an aerial mobile asset.
 */
public abstract class ActifAerien extends ActifMobile {

    public ActifAerien(String id, Point3D position, double vitesseMax, double autonomieMax) {
        super(id, position, vitesseMax, autonomieMax);
    }

    @Override
    public void deplacer(Point3D cible, ZoneOperation zone) {
        if (cible == null || zone == null || !peutSeDeplacer()) {
            return;
        }

        passerEnMissionSiNecessaire();

        Point3D vent = zone.getVent();
        Point3D derive = new Point3D(vent.getX() * 0.1, vent.getY() * 0.1, 0);
        Point3D nouvellePosition = avancerVers(cible, getVitesseMax(), derive);

        if (!zone.isInside(nouvellePosition)) {
            notifierEtatCritique(TypeAlerte.SORTIE_ZONE);
            return;
        }

        if (zone.isCollision(nouvellePosition, this)) {
            notifierEtatCritique(TypeAlerte.COLLISION_IMMINENTE);
            return;
        }

        setPosition(nouvellePosition);

        double consommation = 1.0;
        if (zone.getPrecipitations() > 0) {
            consommation += zone.getPrecipitations() * 0.1;
        }
        if (vent.getX() != 0 || vent.getY() != 0 || vent.getZ() != 0) {
            consommation += 0.5;
        }

        appliquerConsommation(consommation);
    }

    @Override
    public List<Point3D> calculerTrajet(Point3D cible) {
        List<Point3D> path = new ArrayList<>();
        path.add(new Point3D(getPosition().getX(), getPosition().getY(), getPosition().getZ()));
        path.add(new Point3D(cible.getX(), cible.getY(), cible.getZ()));
        return path;
    }
}
