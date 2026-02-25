package com.spiga.core;

import com.spiga.env.Collidable;
import com.spiga.env.ZoneOperation;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract base class for all mobile assets in the simulation.
 */
public abstract class ActifMobile
        implements Deplacable, Rechargeable, Communicable, Pilotable, Alertable, Collidable {

    private static final double EPSILON = 1e-6;

    private final String id;
    private Point3D position;
    private final double vitesseMax;
    private final double autonomieMax;
    private double autonomieActuelle;
    private EtatOperationnel etat;
    private Point3D target;
    private List<Point3D> currentPath;
    private double radius;

    public ActifMobile(String id, Point3D position, double vitesseMax, double autonomieMax) {
        this.id = id;
        this.position = clonePoint(position);
        this.vitesseMax = vitesseMax;
        this.autonomieMax = autonomieMax;
        this.autonomieActuelle = autonomieMax;
        this.etat = EtatOperationnel.AU_SOL;
        this.target = null;
        this.currentPath = new ArrayList<>();
        this.radius = 5.0;
    }

    @Override
    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public Point3D getTarget() {
        return target;
    }

    public void setTarget(Point3D target) {
        this.target = target == null ? null : clonePoint(target);
        this.currentPath.clear();
    }

    public List<Point3D> getCurrentPath() {
        return currentPath;
    }

    public void setCurrentPath(List<Point3D> path) {
        this.currentPath = path == null ? new ArrayList<>() : path;
    }

    public String getId() {
        return id;
    }

    public Point3D getPosition() {
        return position;
    }

    public void setPosition(Point3D position) {
        this.position = clonePoint(position);
    }

    public double getVitesseMax() {
        return vitesseMax;
    }

    public double getAutonomieMax() {
        return autonomieMax;
    }

    public double getAutonomieActuelle() {
        return autonomieActuelle;
    }

    public void setAutonomieActuelle(double autonomieActuelle) {
        this.autonomieActuelle = Math.max(0, Math.min(autonomieMax, autonomieActuelle));
    }

    public EtatOperationnel getEtat() {
        return etat;
    }

    public void setEtat(EtatOperationnel etat) {
        this.etat = etat;
    }

    @Override
    public void demarrer() {
        if (etat == EtatOperationnel.EN_PANNE || etat == EtatOperationnel.EN_MAINTENANCE) {
            return;
        }
        if (etat == EtatOperationnel.AU_SOL) {
            etat = EtatOperationnel.EN_MISSION;
        }
    }

    @Override
    public void arreter() {
        this.etat = EtatOperationnel.AU_SOL;
        this.target = null;
        this.currentPath.clear();
    }

    @Override
    public void notifierEtatCritique(TypeAlerte type) {
        System.out.println("ALERTE [" + id + "]: " + type);
    }

    @Override
    public void envoyerMessage(String destinataire, String message) {
        System.out.println("Transmission de " + id + " à " + destinataire + ": " + message);
    }

    @Override
    public void recharger(double quantite) {
        this.autonomieActuelle = Math.min(this.autonomieMax, this.autonomieActuelle + quantite);
    }

    @Override
    public void ravitailler() {
        this.autonomieActuelle = this.autonomieMax;
    }

    protected boolean peutSeDeplacer() {
        return etat != EtatOperationnel.EN_PANNE && autonomieActuelle > 0;
    }

    protected void passerEnMissionSiNecessaire() {
        if (etat == EtatOperationnel.AU_SOL) {
            etat = EtatOperationnel.EN_MISSION;
        }
    }

    protected boolean estArrive(Point3D cible, double tolerance) {
        return position.distanceTo(cible) <= tolerance;
    }

    protected Point3D avancerVers(Point3D cible, double vitesse, Point3D derive) {
        double dx = cible.getX() - position.getX();
        double dy = cible.getY() - position.getY();
        double dz = cible.getZ() - position.getZ();

        double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);
        if (distance < EPSILON) {
            return clonePoint(position);
        }

        double step = Math.min(vitesse, distance);
        double nx = dx / distance;
        double ny = dy / distance;
        double nz = dz / distance;

        return new Point3D(
                position.getX() + nx * step + derive.getX(),
                position.getY() + ny * step + derive.getY(),
                position.getZ() + nz * step + derive.getZ());
    }

    protected void appliquerConsommation(double consommation) {
        setAutonomieActuelle(autonomieActuelle - consommation);
        if (autonomieActuelle <= 0) {
            autonomieActuelle = 0;
            etat = EtatOperationnel.EN_PANNE;
            notifierEtatCritique(TypeAlerte.BATTERIE_FAIBLE);
        }
    }

    protected Point3D clonePoint(Point3D point) {
        if (point == null) {
            return new Point3D(0, 0, 0);
        }
        return new Point3D(point.getX(), point.getY(), point.getZ());
    }

    @Override
    public abstract void deplacer(Point3D cible, ZoneOperation zone);

    @Override
    public abstract List<Point3D> calculerTrajet(Point3D cible);
}
