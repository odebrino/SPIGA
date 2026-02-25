package com.spiga.core;

import com.spiga.env.ZoneOperation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class VehiculeTerrestreTest {

    private VehiculeTerrestre car;
    private ZoneOperation zone;

    @BeforeEach
    public void setUp() {
        car = new VehiculeTerrestre("C1", new Point3D(300, 300, 0));
        zone = new ZoneOperation(new Point3D(0, 0, 0), new Point3D(1000, 1000, 1000));
        zone.addCollidable(car);
    }

    @Test
    public void testMovementOnLand() {
        Point3D initial = new Point3D(car.getPosition().getX(), car.getPosition().getY(), car.getPosition().getZ());
        Point3D target = new Point3D(310, 310, 0);

        car.deplacer(target, zone);

        assertTrue(car.getPosition().distanceTo(initial) > 0);
        assertTrue(zone.isLand(car.getPosition()));
    }

    @Test
    public void testCannotMoveIntoWater() {
        Point3D initial = new Point3D(car.getPosition().getX(), car.getPosition().getY(), car.getPosition().getZ());
        Point3D waterTarget = new Point3D(0, 0, 0);

        car.deplacer(waterTarget, zone);

        assertEquals(initial, car.getPosition());
        assertTrue(zone.isLand(car.getPosition()));
    }
}
