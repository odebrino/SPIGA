package com.spiga.core;

import java.util.Objects;

/**
 * Class representing a point in 3D space.
 */
public class Point3D {
    private static final double EPSILON = 1e-6;

    private double x;
    private double y;
    private double z;

    public Point3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public double distanceTo(Point3D other) {
        double dx = x - other.x;
        double dy = y - other.y;
        double dz = z - other.z;
        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Point3D)) {
            return false;
        }
        Point3D point3D = (Point3D) o;
        return Math.abs(point3D.x - x) < EPSILON
                && Math.abs(point3D.y - y) < EPSILON
                && Math.abs(point3D.z - z) < EPSILON;
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                Math.round(x / EPSILON),
                Math.round(y / EPSILON),
                Math.round(z / EPSILON));
    }

    @Override
    public String toString() {
        return "Point3D{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }
}
