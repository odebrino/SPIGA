package com.spiga.env;

import com.spiga.core.Point3D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Encapsulates terrain classification logic (land vs sea).
 */
public class TerrainService {
    private final List<ZoneOperation.Island> islands;

    public TerrainService() {
        this.islands = new ArrayList<>();
    }

    public List<ZoneOperation.Island> getIslands() {
        return Collections.unmodifiableList(islands);
    }

    public void addIsland(ZoneOperation.Island island) {
        if (island != null) {
            islands.add(island);
        }
    }

    public boolean isLand(Point3D point) {
        for (ZoneOperation.Island island : islands) {
            if (island.contains(point.getX(), point.getY())) {
                return true;
            }
        }
        return false;
    }
}
