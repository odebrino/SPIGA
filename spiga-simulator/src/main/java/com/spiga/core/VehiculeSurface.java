package com.spiga.core;

/**
 * Class representing a surface vessel (boat).
 */
public class VehiculeSurface extends ActifMarin {

    public VehiculeSurface(String id, Point3D position) {
        super(id, new Point3D(position.getX(), position.getY(), 0), 60.0, 150.0);
    }

    @Override
    public void envoyerMessage(String destinataire, String message) {
        System.out.println(getId() + " (Surface) envoie à " + destinataire + ": " + message);
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
        System.out.println("ALERTE SURFACE " + getId() + ": " + type);
    }

    @Override
    public void setPosition(Point3D position) {
        super.setPosition(new Point3D(position.getX(), position.getY(), 0));
    }

    @Override
    protected double normaliserProfondeurCible(double requestedZ) {
        return 0;
    }

    @Override
    protected Point3D normaliserPositionMarine(Point3D position) {
        return new Point3D(position.getX(), position.getY(), 0);
    }
}
