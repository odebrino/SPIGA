package com.spiga.env;

import com.spiga.core.Point3D;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles collision checks against static obstacles and dynamic assets.
 */
public class CollisionService {
    private final List<Obstacle> obstacles;
    private final List<Collidable> collidables;

    public CollisionService() {
        this.obstacles = new ArrayList<>();
        this.collidables = new ArrayList<>();
    }

    public void addObstacle(Obstacle obstacle) {
        if (obstacle != null) {
            obstacles.add(obstacle);
        }
    }

    public void addCollidable(Collidable collidable) {
        if (collidable != null && !collidables.contains(collidable)) {
            collidables.add(collidable);
        }
    }

    public void removeCollidable(Collidable collidable) {
        collidables.remove(collidable);
    }

    public boolean isCollision(Point3D point) {
        return isCollision(point, null);
    }

    public boolean isCollision(Point3D point, Collidable ignoreMe) {
        for (Obstacle obstacle : obstacles) {
            if (obstacle.contains(point)) {
                return true;
            }
        }

        for (Collidable candidate : collidables) {
            if (candidate == ignoreMe) {
                continue;
            }

            if (Math.abs(candidate.getPosition().getZ() - point.getZ()) >= 0.1) {
                continue;
            }

            double dx = candidate.getPosition().getX() - point.getX();
            double dy = candidate.getPosition().getY() - point.getY();
            double distance = Math.sqrt(dx * dx + dy * dy);
            double pointRadius = ignoreMe != null ? ignoreMe.getRadius() : 5.0;

            if (distance < candidate.getRadius() + pointRadius) {
                return true;
            }
        }

        return false;
    }
}
