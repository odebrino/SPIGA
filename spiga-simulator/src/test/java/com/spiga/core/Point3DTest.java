package com.spiga.core;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class Point3DTest {

    @Test
    public void testEqualsAndHashCode() {
        Point3D p1 = new Point3D(10.0, 20.0, 30.0);
        Point3D p2 = new Point3D(10.0, 20.0, 30.0);

        assertEquals(p1, p2);
        assertEquals(p1.hashCode(), p2.hashCode());
    }

    @Test
    public void testDistanceTo() {
        Point3D origin = new Point3D(0, 0, 0);
        Point3D p = new Point3D(3, 4, 12);

        assertEquals(13.0, origin.distanceTo(p), 0.0001);
    }
}
