package com.spiga.core;

/**
 * Class representing a submarine.
 */
public class VehiculeSousMarin extends ActifMarin {

    public VehiculeSousMarin(String id, Point3D position) {
        super(id, sanitizeDepth(position), 40.0, 200.0);
    }

    private static Point3D sanitizeDepth(Point3D position) {
        return new Point3D(position.getX(), position.getY(), Math.min(position.getZ(), 0));
    }

    @Override
    public void setPosition(Point3D position) {
        super.setPosition(new Point3D(position.getX(), position.getY(), Math.min(position.getZ(), 0)));
    }

    @Override
    public void envoyerMessage(String destinataire, String message) {
        System.out.println(getId() + " (Sous-marin) envoie à " + destinataire + ": " + message);
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
        setAutonomieActuelle(getAutonomieMax());
    }

    @Override
    public void notifierEtatCritique(TypeAlerte type) {
        System.out.println("ALERTE SOUS-MARIN " + getId() + ": " + type);
    }

    @Override
    protected double normaliserProfondeurCible(double requestedZ) {
        return Math.min(requestedZ, 0);
    }

    @Override
    protected Point3D normaliserPositionMarine(Point3D position) {
        return new Point3D(position.getX(), position.getY(), Math.min(position.getZ(), 0));
    }
}
