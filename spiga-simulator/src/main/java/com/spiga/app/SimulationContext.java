package com.spiga.app;

import com.spiga.env.ZoneOperation;
import com.spiga.mission.GestionnaireEssaim;

/**
 * Shared application context holding fleet manager and simulation zone.
 */
public class SimulationContext {
    private final GestionnaireEssaim gestionnaire;
    private final ZoneOperation zone;

    public SimulationContext(GestionnaireEssaim gestionnaire, ZoneOperation zone) {
        this.gestionnaire = gestionnaire;
        this.zone = zone;
    }

    public GestionnaireEssaim getGestionnaire() {
        return gestionnaire;
    }

    public ZoneOperation getZone() {
        return zone;
    }
}
