package com.spiga.core;

import com.spiga.env.ZoneOperation;
import java.util.ArrayList;
import java.util.List;

/**
 * Class representing a land-based vehicle.
 */
public class VehiculeTerrestre extends ActifMobile {

    private static final double WAYPOINT_TOLERANCE = 5.0;

    public VehiculeTerrestre(String id, Point3D position) {
        super(id, new Point3D(position.getX(), position.getY(), 0), 50.0, 120.0);
    }

    @Override
    public void setPosition(Point3D position) {
        super.setPosition(new Point3D(position.getX(), position.getY(), 0));
    }

    @Override
    public void envoyerMessage(String destinataire, String message) {
        System.out.println(getId() + " (Terrestre) envoie à " + destinataire + ": " + message);
    }

    @Override
    public void demarrer() {
        super.demarrer();
    }

    @Override
    public void arreter() {
        super.arreter();
    }

    @Override
    public void recharger(double quantite) {
        setAutonomieActuelle(Math.min(getAutonomieMax(), getAutonomieActuelle() + quantite));
    }

    @Override
    public void ravitailler() {
        recharger(100);
    }

    @Override
    public void notifierEtatCritique(TypeAlerte type) {
        System.out.println("ALERTE TERRESTRE " + getId() + ": " + type);
    }

    @Override
    public void deplacer(Point3D cible, ZoneOperation zone) {
        if (cible == null || zone == null || !peutSeDeplacer()) {
            return;
        }

        passerEnMissionSiNecessaire();

        Point3D target2D = new Point3D(cible.getX(), cible.getY(), 0);
        if (!zone.isLand(target2D)) {
            return;
        }

        if (getCurrentPath().isEmpty()) {
            setCurrentPath(zone.findPath(getPosition(), target2D, false));
            if (getCurrentPath().isEmpty()) {
                return;
            }
        }

        Point3D next = getCurrentPath().get(0);
        Point3D waypoint = new Point3D(next.getX(), next.getY(), 0);

        if (estArrive(waypoint, WAYPOINT_TOLERANCE)) {
            if (!getCurrentPath().isEmpty()) {
                getCurrentPath().remove(0);
            }
            return;
        }

        Point3D nextPosition = avancerVers(waypoint, getVitesseMax(), new Point3D(0, 0, 0));
        Point3D newPos = new Point3D(nextPosition.getX(), nextPosition.getY(), 0);

        if (!zone.isInside(newPos)) {
            notifierEtatCritique(TypeAlerte.SORTIE_ZONE);
            return;
        }

        if (zone.isCollision(newPos, this)) {
            notifierEtatCritique(TypeAlerte.COLLISION_IMMINENTE);
            return;
        }

        if (!zone.isLand(newPos)) {
            return;
        }

        setPosition(newPos);
        appliquerConsommation(1.0);
    }

    @Override
    public List<Point3D> calculerTrajet(Point3D cible) {
        List<Point3D> path = new ArrayList<>();
        path.add(new Point3D(getPosition().getX(), getPosition().getY(), 0));
        path.add(new Point3D(cible.getX(), cible.getY(), 0));
        return path;
    }
}
