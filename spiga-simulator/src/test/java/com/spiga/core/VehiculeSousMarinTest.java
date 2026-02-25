package com.spiga.core;

import com.spiga.env.ZoneOperation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class VehiculeSousMarinTest {

    private VehiculeSousMarin sub;
    private ZoneOperation zone;

    @BeforeEach
    public void setUp() {
        sub = new VehiculeSousMarin("U1", new Point3D(500, 500, -50));
        zone = new ZoneOperation(new Point3D(0, 0, -1000), new Point3D(1000, 1000, 1000));
        zone.addCollidable(sub);
    }

    @Test
    public void testUnderwaterMovement() {
        Point3D target = new Point3D(550, 500, -50);
        sub.deplacer(target, zone);

        assertTrue(sub.getPosition().getX() > 500);
        assertEquals(500, sub.getPosition().getY(), 20.0);
        assertEquals(-50, sub.getPosition().getZ(), 0.1);
    }

    @Test
    public void testDepthConstraint() {
        Point3D target = new Point3D(500, 500, 100);
        sub.deplacer(target, zone);

        assertTrue(sub.getPosition().getZ() <= 0);
    }

    @Test
    public void testConstructorClampsDepthAboveSeaLevel() {
        VehiculeSousMarin highSub = new VehiculeSousMarin("U2", new Point3D(100, 100, 80));
        assertEquals(0.0, highSub.getPosition().getZ(), 0.001);
    }
}
