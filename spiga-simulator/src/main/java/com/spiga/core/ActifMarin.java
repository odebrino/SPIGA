package com.spiga.core;

import com.spiga.env.ZoneOperation;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract class representing a marine mobile asset.
 */
public abstract class ActifMarin extends ActifMobile {

    private static final double WAYPOINT_TOLERANCE = 5.0;

    public ActifMarin(String id, Point3D position, double vitesseMax, double autonomieMax) {
        super(id, position, vitesseMax, autonomieMax);
    }

    @Override
    public void deplacer(Point3D cible, ZoneOperation zone) {
        if (cible == null || zone == null || !peutSeDeplacer()) {
            return;
        }

        passerEnMissionSiNecessaire();

        if (getCurrentPath().isEmpty()) {
            setCurrentPath(zone.findPath(getPosition(), cible, true));
        }

        Point3D waypoint = cible;
        if (!getCurrentPath().isEmpty()) {
            waypoint = getCurrentPath().get(0);
        }

        double zCible = normaliserProfondeurCible(cible.getZ());
        waypoint = new Point3D(waypoint.getX(), waypoint.getY(), zCible);

        if (estArrive(waypoint, WAYPOINT_TOLERANCE)) {
            if (!getCurrentPath().isEmpty()) {
                getCurrentPath().remove(0);
            }
            return;
        }

        Point3D courant = zone.getCourantMarin();
        Point3D nouvellePosition = avancerVers(waypoint, getVitesseMax(), courant);
        nouvellePosition = normaliserPositionMarine(nouvellePosition);

        if (!zone.isInside(nouvellePosition)) {
            notifierEtatCritique(TypeAlerte.SORTIE_ZONE);
            return;
        }

        if (zone.isCollision(nouvellePosition, this)) {
            notifierEtatCritique(TypeAlerte.COLLISION_IMMINENTE);
            return;
        }

        Point3D terrainProbe = new Point3D(nouvellePosition.getX(), nouvellePosition.getY(), 0);
        if (zone.isLand(terrainProbe)) {
            return;
        }

        setPosition(nouvellePosition);

        double consommation = 1.0;
        if (courant.getX() != 0 || courant.getY() != 0 || courant.getZ() != 0) {
            consommation += 0.5;
        }

        appliquerConsommation(consommation);
    }

    protected double normaliserProfondeurCible(double requestedZ) {
        return requestedZ;
    }

    protected Point3D normaliserPositionMarine(Point3D position) {
        return position;
    }

    @Override
    public List<Point3D> calculerTrajet(Point3D cible) {
        List<Point3D> path = new ArrayList<>();
        path.add(new Point3D(getPosition().getX(), getPosition().getY(), getPosition().getZ()));
        path.add(new Point3D(cible.getX(), cible.getY(), cible.getZ()));
        return path;
    }
}
