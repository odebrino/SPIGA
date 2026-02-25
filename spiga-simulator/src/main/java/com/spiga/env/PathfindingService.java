package com.spiga.env;

import com.spiga.core.Point3D;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.function.Predicate;

/**
 * Grid-based A* pathfinding service.
 */
public class PathfindingService {
    private final double minX;
    private final double minY;
    private final double maxX;
    private final double maxY;
    private final double cellSize;
    private final int gridWidth;
    private final int gridHeight;

    public PathfindingService(Point3D minCoord, Point3D maxCoord, double cellSize) {
        this.minX = minCoord.getX();
        this.minY = minCoord.getY();
        this.maxX = maxCoord.getX();
        this.maxY = maxCoord.getY();
        this.cellSize = cellSize;

        this.gridWidth = Math.max(1, (int) Math.ceil((maxX - minX) / cellSize));
        this.gridHeight = Math.max(1, (int) Math.ceil((maxY - minY) / cellSize));
    }

    public List<Point3D> findPath(Point3D start, Point3D end, boolean isMarine, Predicate<Point3D> isLandPredicate) {
        Node startNode = toNode(start);
        Node endNode = toNode(end);

        PriorityQueue<Node> openSet = new PriorityQueue<>(Comparator.comparingDouble(node -> node.f));
        Set<String> closedSet = new HashSet<>();
        Map<String, Node> nodes = new HashMap<>();

        startNode.g = 0;
        startNode.h = heuristic(startNode, endNode);
        startNode.f = startNode.h;

        openSet.add(startNode);
        nodes.put(startNode.key(), startNode);

        while (!openSet.isEmpty()) {
            Node current = openSet.poll();
            if (current.key().equals(endNode.key())) {
                List<Point3D> path = reconstructPath(current, end.getZ());
                Point3D last = path.get(path.size() - 1);
                if (Math.abs(last.getX() - end.getX()) > 1e-6
                        || Math.abs(last.getY() - end.getY()) > 1e-6
                        || Math.abs(last.getZ() - end.getZ()) > 1e-6) {
                    path.add(new Point3D(end.getX(), end.getY(), end.getZ()));
                }
                return path;
            }

            closedSet.add(current.key());

            for (Node neighbor : neighbors(current)) {
                if (closedSet.contains(neighbor.key())) {
                    continue;
                }

                Point3D sample = toWorldPoint(neighbor.x, neighbor.y, 0);
                boolean isLand = isLandPredicate.test(sample);
                if (isMarine && isLand) {
                    continue;
                }
                if (!isMarine && !isLand) {
                    continue;
                }

                double tentativeG = current.g + movementCost(current, neighbor);

                Node existing = nodes.get(neighbor.key());
                if (existing == null || tentativeG < existing.g) {
                    Node target = existing == null ? neighbor : existing;
                    target.parent = current;
                    target.g = tentativeG;
                    target.h = heuristic(target, endNode);
                    target.f = target.g + target.h;

                    if (existing == null) {
                        nodes.put(target.key(), target);
                        openSet.add(target);
                    } else {
                        openSet.remove(target);
                        openSet.add(target);
                    }
                }
            }
        }

        List<Point3D> fallback = new ArrayList<>();
        fallback.add(new Point3D(end.getX(), end.getY(), end.getZ()));
        return fallback;
    }

    private Node toNode(Point3D point) {
        int gx = clamp((int) ((point.getX() - minX) / cellSize), 0, gridWidth - 1);
        int gy = clamp((int) ((point.getY() - minY) / cellSize), 0, gridHeight - 1);
        return new Node(gx, gy);
    }

    private int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }

    private double movementCost(Node from, Node to) {
        int dx = Math.abs(from.x - to.x);
        int dy = Math.abs(from.y - to.y);
        return (dx + dy == 2) ? Math.sqrt(2) : 1.0;
    }

    private double heuristic(Node a, Node b) {
        double dx = a.x - b.x;
        double dy = a.y - b.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    private List<Node> neighbors(Node node) {
        List<Node> neighbors = new ArrayList<>(8);
        int[] dx = { 0, 0, 1, -1, 1, 1, -1, -1 };
        int[] dy = { 1, -1, 0, 0, 1, -1, 1, -1 };

        for (int i = 0; i < dx.length; i++) {
            int nx = node.x + dx[i];
            int ny = node.y + dy[i];
            if (nx >= 0 && nx < gridWidth && ny >= 0 && ny < gridHeight) {
                neighbors.add(new Node(nx, ny));
            }
        }

        return neighbors;
    }

    private Point3D toWorldPoint(int gx, int gy, double z) {
        double worldX = minX + gx * cellSize + cellSize / 2.0;
        double worldY = minY + gy * cellSize + cellSize / 2.0;
        return new Point3D(worldX, worldY, z);
    }

    private List<Point3D> reconstructPath(Node node, double z) {
        List<Point3D> points = new ArrayList<>();
        Node current = node;
        while (current != null) {
            points.add(0, toWorldPoint(current.x, current.y, z));
            current = current.parent;
        }
        return points;
    }

    private static class Node {
        private final int x;
        private final int y;
        private double g;
        private double h;
        private double f;
        private Node parent;

        private Node(int x, int y) {
            this.x = x;
            this.y = y;
        }

        private String key() {
            return x + "," + y;
        }
    }
}
