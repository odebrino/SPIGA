package com.spiga.app;

import com.spiga.core.ActifMobile;
import com.spiga.core.DroneLogistique;
import com.spiga.core.DroneReconnaissance;
import com.spiga.core.Point3D;
import com.spiga.core.VehiculeSousMarin;
import com.spiga.core.VehiculeSurface;
import com.spiga.core.VehiculeTerrestre;

/**
 * Factory responsible for creating assets from UI/CLI inputs.
 */
public class AssetFactory {

    public ActifMobile createAsset(String type, String id, Point3D position) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Asset ID is required.");
        }
        if (position == null) {
            throw new IllegalArgumentException("Asset position is required.");
        }

        switch (type) {
            case "DroneReconnaissance":
                return new DroneReconnaissance(id, position);
            case "DroneLogistique":
                return new DroneLogistique(id, position);
            case "VehiculeSurface":
                return new VehiculeSurface(id, position);
            case "VehiculeSousMarin":
                return new VehiculeSousMarin(id, position);
            case "VehiculeTerrestre":
                return new VehiculeTerrestre(id, position);
            default:
                throw new IllegalArgumentException("Unknown asset type: " + type);
        }
    }

    public ActifMobile createAssetFromConsoleChoice(String choice, String id) {
        switch (choice) {
            case "1":
                return createAsset("DroneReconnaissance", id, new Point3D(100, 100, 50));
            case "2":
                return createAsset("DroneLogistique", id, new Point3D(200, 200, 50));
            case "3":
                return createAsset("VehiculeSurface", id, new Point3D(100, 800, 0));
            case "4":
                return createAsset("VehiculeSousMarin", id, new Point3D(800, 100, -50));
            case "5":
                return createAsset("VehiculeTerrestre", id, new Point3D(300, 300, 0));
            default:
                throw new IllegalArgumentException("Unknown type option.");
        }
    }
}
