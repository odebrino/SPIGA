package com.spiga.core;

import com.spiga.env.ZoneOperation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ActifMarinTest {

    private ActifMarin boat;
    private ZoneOperation zone;

    @BeforeEach
    public void setUp() {
        boat = new VehiculeSurface("S1", new Point3D(500, 500, 0));
        zone = new ZoneOperation(new Point3D(0, 0, 0), new Point3D(1000, 1000, 1000));
        zone.addCollidable(boat);
    }

    @Test
    public void testMovementOnWater() {
        Point3D initial = new Point3D(boat.getPosition().getX(), boat.getPosition().getY(), boat.getPosition().getZ());
        Point3D target = new Point3D(550, 500, 0);

        boat.deplacer(target, zone);

        assertTrue(boat.getPosition().distanceTo(initial) > 0);
        assertFalse(zone.isLand(boat.getPosition()));
    }

    @Test
    public void testDoesNotEnterLand() {
        boat.setPosition(new Point3D(460, 460, 0));
        Point3D target = new Point3D(300, 300, 0);

        boat.deplacer(target, zone);

        assertFalse(zone.isLand(boat.getPosition()));
    }
}
