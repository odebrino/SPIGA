package com.spiga.core;

import com.spiga.env.ZoneOperation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CollisionTest {

    private ZoneOperation zone;
    private DroneReconnaissance drone1;
    private DroneReconnaissance drone2;

    @BeforeEach
    public void setUp() {
        zone = new ZoneOperation(new Point3D(0, 0, 0), new Point3D(1000, 1000, 1000));
        drone1 = new DroneReconnaissance("D1", new Point3D(100, 100, 50));
        drone2 = new DroneReconnaissance("D2", new Point3D(200, 100, 50));

        zone.addCollidable(drone1);
        zone.addCollidable(drone2);
    }

    @Test
    public void testCollisionDetection() {
        Point3D target = new Point3D(100, 100, 50);

        for (int i = 0; i < 20; i++) {
            drone2.deplacer(target, zone);
        }

        double dist = Math.sqrt(
                Math.pow(drone1.getPosition().getX() - drone2.getPosition().getX(), 2)
                        + Math.pow(drone1.getPosition().getY() - drone2.getPosition().getY(), 2));

        assertTrue(dist >= 10.0, "Distance should be maintained to avoid collision. Current dist: " + dist);
    }

    @Test
    public void testNoCollisionDifferentZ() {
        DroneReconnaissance drone3 = new DroneReconnaissance("D3", new Point3D(200, 100, 100));
        zone.addCollidable(drone3);

        Point3D target = new Point3D(100, 100, 100);

        for (int i = 0; i < 20; i++) {
            drone3.deplacer(target, zone);
        }

        assertEquals(100.0, drone3.getPosition().getX(), 1.0);
        assertEquals(100.0, drone3.getPosition().getY(), 1.0);
        assertEquals(100.0, drone3.getPosition().getZ(), 1.0);
    }
}
