package com.spiga.core;

import com.spiga.env.ZoneOperation;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ActifAerienTest {

    @Test
    public void testDeplacement() {
        ZoneOperation zone = new ZoneOperation(new Point3D(0, 0, 0), new Point3D(1000, 1000, 1000));
        ActifAerien drone = new DroneReconnaissance("D1", new Point3D(0, 0, 0));

        drone.demarrer();

        Point3D target = new Point3D(100, 0, 0);
        drone.deplacer(target, zone);

        assertEquals(100.0, drone.getPosition().getX(), 0.01);
        assertEquals(0.0, drone.getPosition().getY(), 0.01);
        assertEquals(0.0, drone.getPosition().getZ(), 0.01);
    }

    @Test
    public void testConsumption() {
        ZoneOperation zone = new ZoneOperation(new Point3D(0, 0, 0), new Point3D(1000, 1000, 1000));
        ActifAerien drone = new DroneReconnaissance("D1", new Point3D(0, 0, 0));
        double initialAutonomy = drone.getAutonomieMax();

        drone.deplacer(new Point3D(100, 0, 0), zone);

        assertTrue(drone.getAutonomieActuelle() < initialAutonomy);
    }
}
