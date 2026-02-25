package com.spiga.app;

import com.spiga.core.ActifMobile;
import com.spiga.core.ActifMarin;
import com.spiga.core.DroneLogistique;
import com.spiga.core.DroneReconnaissance;
import com.spiga.core.Point3D;
import com.spiga.core.VehiculeSousMarin;
import com.spiga.core.VehiculeSurface;
import com.spiga.core.VehiculeTerrestre;
import com.spiga.env.ZoneOperation;
import com.spiga.mission.GestionnaireEssaim;
import com.spiga.mission.Mission;
import com.spiga.mission.MissionReconnaissance;
import com.spiga.mission.ObjectifMission;
import java.util.List;
import java.util.Optional;

/**
 * Application-level bootstrap and orchestration helpers.
 */
public class SimulationBootstrap {
    private final AssetFactory assetFactory;

    public SimulationBootstrap() {
        this.assetFactory = new AssetFactory();
    }

    public SimulationContext createEmptyContext() {
        GestionnaireEssaim gestionnaire = new GestionnaireEssaim();
        ZoneOperation zone = new ZoneOperation(new Point3D(0, 0, -1000), new Point3D(1000, 1000, 1000));
        return new SimulationContext(gestionnaire, zone);
    }

    public SimulationContext createDemoContext() {
        SimulationContext context = createEmptyContext();

        registerAsset(context, new DroneReconnaissance("D1", new Point3D(100, 100, 50)));
        registerAsset(context, new DroneLogistique("D2", new Point3D(200, 200, 50)));
        registerAsset(context, new VehiculeSurface("S1", new Point3D(100, 800, 0)));
        registerAsset(context, new VehiculeSousMarin("U1", new Point3D(800, 100, -50)));
        registerAsset(context, new VehiculeTerrestre("C1", new Point3D(300, 300, 0)));

        return context;
    }

    public void registerAsset(SimulationContext context, ActifMobile asset) {
        context.getGestionnaire().ajouterActif(asset);
        context.getZone().addCollidable(asset);
    }

    public ActifMobile createAndRegisterAsset(SimulationContext context, String type, String id, Point3D position) {
        Optional<String> validationError = validateSpawn(type, position, context.getZone());
        if (validationError.isPresent()) {
            throw new IllegalArgumentException(validationError.get());
        }

        ActifMobile asset = assetFactory.createAsset(type, id, position);
        registerAsset(context, asset);
        return asset;
    }

    public ActifMobile createAssetFromConsoleChoice(String choice, String id) {
        return assetFactory.createAssetFromConsoleChoice(choice, id);
    }

    public Optional<String> validateSpawn(String type, Point3D position, ZoneOperation zone) {
        boolean isMarine = type.contains("Surface") || type.contains("SousMarin");
        boolean isLandVehicle = "VehiculeTerrestre".equals(type);
        boolean isLand = zone.isLand(position);

        if (isMarine && isLand) {
            return Optional.of("Marine assets cannot spawn on land!");
        }
        if (isLandVehicle && !isLand) {
            return Optional.of("Land vehicles cannot spawn in sea!");
        }

        return Optional.empty();
    }

    public void assignGlobalTarget(SimulationContext context, Point3D target) {
        if (target == null) {
            return;
        }

        for (ActifMobile actif : context.getGestionnaire().getFlotte()) {
            if (isTargetCompatible(actif, target, context.getZone())) {
                actif.setTarget(normalizeTargetForAsset(actif, target));
            } else {
                actif.setTarget(null);
            }
        }
    }

    public Mission createGroupMission(
            SimulationContext context,
            List<String> ids,
            Point3D target,
            ObjectifMission objectif) {

        Mission mission = new MissionReconnaissance("Mission-" + System.currentTimeMillis());
        mission.setObjectif(objectif == null ? ObjectifMission.RECONNAISSANCE : objectif);

        if (ids == null || ids.isEmpty()) {
            mission.demarrer();
            return mission;
        }

        for (String id : ids) {
            ActifMobile asset = context.getGestionnaire().getActifParId(id);
            if (asset != null && isTargetCompatible(asset, target, context.getZone())) {
                mission.assignerActif(asset);
                asset.setTarget(normalizeTargetForAsset(asset, target));
            }
        }

        mission.demarrer();
        return mission;
    }

    private boolean isTargetCompatible(ActifMobile asset, Point3D target, ZoneOperation zone) {
        if (asset == null || target == null || zone == null) {
            return false;
        }

        Point3D terrainProbe = new Point3D(target.getX(), target.getY(), 0);
        if (asset instanceof ActifMarin && zone.isLand(terrainProbe)) {
            return false;
        }
        if (asset instanceof VehiculeTerrestre && !zone.isLand(terrainProbe)) {
            return false;
        }

        return true;
    }

    private Point3D normalizeTargetForAsset(ActifMobile asset, Point3D target) {
        if (asset instanceof VehiculeTerrestre || asset instanceof VehiculeSurface) {
            return new Point3D(target.getX(), target.getY(), 0);
        }
        if (asset instanceof VehiculeSousMarin) {
            return new Point3D(target.getX(), target.getY(), Math.min(target.getZ(), 0));
        }
        return new Point3D(target.getX(), target.getY(), target.getZ());
    }
}
