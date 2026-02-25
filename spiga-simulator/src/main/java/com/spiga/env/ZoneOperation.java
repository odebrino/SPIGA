package com.spiga.env;

import com.spiga.core.Point3D;
import java.util.List;

/**
 * Represents the operational zone of the simulation.
 * Manages environment, terrain, collisions and pathfinding access.
 */
public class ZoneOperation {
    private static final double DEFAULT_CELL_SIZE = 20.0;

    private final Point3D minCoord;
    private final Point3D maxCoord;
    private Point3D vent;
    private double precipitations;
    private Point3D courantMarin;

    private final TerrainService terrainService;
    private final CollisionService collisionService;
    private final PathfindingService pathfindingService;

    public ZoneOperation(Point3D minCoord, Point3D maxCoord) {
        this.minCoord = minCoord;
        this.maxCoord = maxCoord;

        this.vent = new Point3D(0, 0, 0);
        this.precipitations = 0;
        this.courantMarin = new Point3D(0, 0, 0);

        this.terrainService = new TerrainService();
        this.collisionService = new CollisionService();
        this.pathfindingService = new PathfindingService(minCoord, maxCoord, DEFAULT_CELL_SIZE);

        initializeDefaultIslands();
    }

    private void initializeDefaultIslands() {
        terrainService.addIsland(new Island(300, 300, 150, 150, true));
        terrainService.addIsland(new Island(700, 700, 200, 300, false));
    }

    /**
     * Represents a land mass in the operational map.
     */
    public static class Island {
        private final double x;
        private final double y;
        private final double w;
        private final double h;
        private final boolean circle;

        public Island(double x, double y, double w, double h, boolean circle) {
            this.x = x;
            this.y = y;
            this.w = w;
            this.h = h;
            this.circle = circle;
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }

        public double getW() {
            return w;
        }

        public double getH() {
            return h;
        }

        public boolean isCircle() {
            return circle;
        }

        public boolean contains(double px, double py) {
            if (circle) {
                double dx = px - x;
                double dy = py - y;
                return Math.sqrt(dx * dx + dy * dy) <= w;
            }

            // Rectangle centered at x,y
            return px >= x - w / 2.0 && px <= x + w / 2.0 && py >= y - h / 2.0 && py <= y + h / 2.0;
        }
    }

    public List<Island> getIslands() {
        return terrainService.getIslands();
    }

    public boolean isLand(Point3D point) {
        return terrainService.isLand(point);
    }

    public Point3D getVent() {
        return vent;
    }

    public void setVent(Point3D vent) {
        this.vent = vent;
    }

    public double getPrecipitations() {
        return precipitations;
    }

    public void setPrecipitations(double precipitations) {
        this.precipitations = precipitations;
    }

    public Point3D getCourantMarin() {
        return courantMarin;
    }

    public void setCourantMarin(Point3D courantMarin) {
        this.courantMarin = courantMarin;
    }

    public void addObstacle(Obstacle obstacle) {
        collisionService.addObstacle(obstacle);
    }

    public void addCollidable(Collidable collidable) {
        collisionService.addCollidable(collidable);
    }

    public void removeCollidable(Collidable collidable) {
        collisionService.removeCollidable(collidable);
    }

    public boolean isCollision(Point3D point) {
        return collisionService.isCollision(point);
    }

    public boolean isCollision(Point3D point, Collidable ignoreMe) {
        return collisionService.isCollision(point, ignoreMe);
    }

    public boolean isInside(Point3D point) {
        return point.getX() >= minCoord.getX() && point.getX() <= maxCoord.getX()
                && point.getY() >= minCoord.getY() && point.getY() <= maxCoord.getY()
                && point.getZ() >= minCoord.getZ() && point.getZ() <= maxCoord.getZ();
    }

    /**
     * Finds a path from start to end.
     *
     * @param start start point
     * @param end end point
     * @param isMarine true for marine vehicles, false for land vehicles
     * @return computed path, or a fallback path containing end
     */
    public List<Point3D> findPath(Point3D start, Point3D end, boolean isMarine) {
        return pathfindingService.findPath(start, end, isMarine, this::isLand);
    }
}
