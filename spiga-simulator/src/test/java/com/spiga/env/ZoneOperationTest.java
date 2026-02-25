package com.spiga.env;

import com.spiga.core.Point3D;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ZoneOperationTest {

    private ZoneOperation zone;

    @BeforeEach
    public void setUp() {
        zone = new ZoneOperation(new Point3D(0, 0, -1000), new Point3D(1000, 1000, 1000));
    }

    @Test
    public void testIsLand() {
        assertTrue(zone.isLand(new Point3D(300, 300, 0)));
        assertTrue(zone.isLand(new Point3D(400, 300, 0)));
        assertFalse(zone.isLand(new Point3D(500, 500, 0)));
    }

    @Test
    public void testRectangularIslandIsCentered() {
        assertTrue(zone.isLand(new Point3D(700, 700, 0)));
        assertTrue(zone.isLand(new Point3D(800, 850, 0))); // boundary point
        assertFalse(zone.isLand(new Point3D(810, 700, 0))); // outside width
    }

    @Test
    public void testFindPathMarine() {
        Point3D start = new Point3D(100, 100, 0);
        Point3D end = new Point3D(500, 500, 0);

        List<Point3D> path = zone.findPath(start, end, true);

        assertFalse(path.isEmpty());
        for (Point3D p : path) {
            assertFalse(zone.isLand(p), "Marine path should not cross land at " + p);
        }
    }

    @Test
    public void testFindPathLand() {
        Point3D start = new Point3D(300, 300, 0);
        Point3D end = new Point3D(350, 350, 0);

        List<Point3D> path = zone.findPath(start, end, false);

        assertFalse(path.isEmpty());
        for (Point3D p : path) {
            assertTrue(zone.isLand(p), "Land path should not cross water at " + p);
        }
    }

    @Test
    public void testFindPathLandReturnsEmptyWhenTargetIsWater() {
        Point3D start = new Point3D(300, 300, 0);
        Point3D end = new Point3D(500, 500, 0);

        List<Point3D> path = zone.findPath(start, end, false);

        assertTrue(path.isEmpty());
    }

    @Test
    public void testFindPathMarineReturnsEmptyWhenTargetIsLand() {
        Point3D start = new Point3D(100, 100, 0);
        Point3D end = new Point3D(300, 300, 0);

        List<Point3D> path = zone.findPath(start, end, true);

        assertTrue(path.isEmpty());
    }

    @Test
    public void testIsCollision() {
        Obstacle obs = new Obstacle(new Point3D(500, 500, 50), 10);
        zone.addObstacle(obs);

        assertTrue(zone.isCollision(new Point3D(500, 500, 50)));
        assertFalse(zone.isCollision(new Point3D(600, 600, 50)));
    }
}
