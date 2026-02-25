package com.spiga.ui;

import com.spiga.core.ActifMarin;
import com.spiga.core.ActifMobile;
import com.spiga.core.EtatOperationnel;
import com.spiga.core.Point3D;
import com.spiga.core.VehiculeTerrestre;
import com.spiga.env.ZoneOperation;
import com.spiga.mission.GestionnaireEssaim;
import java.util.ArrayList;
import java.util.List;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class SimulationView extends Pane {
    private static final double SCALE = 0.9;
    private static final double SELECTION_RADIUS = 20;
    private static final double ARRIVAL_TOLERANCE = 5.0;

    private final ZoneOperation zone;
    private final GestionnaireEssaim gestionnaire;
    private final Canvas canvas;

    private AnimationTimer timer;
    private final List<ActifMobile> selectedAssets = new ArrayList<>();
    private Runnable onUpdate;
    private long lastUpdate = 0;

    public SimulationView(ZoneOperation zone, GestionnaireEssaim gestionnaire) {
        this.zone = zone;
        this.gestionnaire = gestionnaire;
        this.canvas = new Canvas(900, 900);
        getChildren().add(canvas);

        widthProperty().addListener(evt -> draw());
        heightProperty().addListener(evt -> draw());

        canvas.setOnMouseClicked(event -> {
            double mx = event.getX();
            double my = event.getY();

            if (event.getButton() == MouseButton.PRIMARY) {
                boolean clickedAsset = handleSelection(mx, my);
                if (!clickedAsset && !selectedAssets.isEmpty()) {
                    handleMoveCommand(mx, my);
                }
            } else if (event.getButton() == MouseButton.SECONDARY) {
                handleMoveCommand(mx, my);
            }
        });
    }

    public void setOnUpdate(Runnable onUpdate) {
        this.onUpdate = onUpdate;
    }

    public void startSimulation() {
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (now - lastUpdate >= 100_000_000) {
                    update();
                    draw();
                    if (onUpdate != null) {
                        onUpdate.run();
                    }
                    lastUpdate = now;
                }
            }
        };
        timer.start();
    }

    private boolean handleSelection(double mx, double my) {
        for (ActifMobile actif : gestionnaire.getFlotte()) {
            double x = actif.getPosition().getX() * SCALE;
            double y = actif.getPosition().getY() * SCALE;

            if (Math.abs(mx - x) < SELECTION_RADIUS && Math.abs(my - y) < SELECTION_RADIUS) {
                if (selectedAssets.contains(actif)) {
                    selectedAssets.remove(actif);
                } else {
                    selectedAssets.add(actif);
                }
                draw();
                return true;
            }
        }
        return false;
    }

    private void handleMoveCommand(double mx, double my) {
        if (selectedAssets.isEmpty()) {
            return;
        }

        double wx = mx / SCALE;
        double wy = my / SCALE;

        for (ActifMobile asset : selectedAssets) {
            double wz = asset.getPosition().getZ();
            Point3D target = new Point3D(wx, wy, wz);

            if (asset instanceof ActifMarin && zone.isLand(new Point3D(wx, wy, 0))) {
                continue;
            }

            if (asset instanceof VehiculeTerrestre && !zone.isLand(new Point3D(wx, wy, 0))) {
                continue;
            }

            asset.setTarget(target);
        }
    }

    private void update() {
        for (ActifMobile actif : gestionnaire.getFlotte()) {
            Point3D target = actif.getTarget();
            if (target == null) {
                continue;
            }

            actif.deplacer(target, zone);

            if (actif.getPosition().distanceTo(target) < ARRIVAL_TOLERANCE) {
                actif.setTarget(null);
                actif.setEtat(EtatOperationnel.AU_SOL);
            }
        }
    }

    private void draw() {
        GraphicsContext gc = canvas.getGraphicsContext2D();

        gc.setFill(Color.LIGHTBLUE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        gc.setFill(Color.LIGHTGREEN);
        for (ZoneOperation.Island island : zone.getIslands()) {
            if (island.isCircle()) {
                gc.fillOval(
                        island.getX() * SCALE - island.getW() * SCALE,
                        island.getY() * SCALE - island.getW() * SCALE,
                        island.getW() * SCALE * 2,
                        island.getW() * SCALE * 2);
            } else {
                gc.fillRect(
                        (island.getX() - island.getW() / 2.0) * SCALE,
                        (island.getY() - island.getH() / 2.0) * SCALE,
                        island.getW() * SCALE,
                        island.getH() * SCALE);
            }
        }

        for (ActifMobile actif : gestionnaire.getFlotte()) {
            drawAsset(gc, actif);
        }
    }

    private void drawAsset(GraphicsContext gc, ActifMobile actif) {
        double x = actif.getPosition().getX() * SCALE;
        double y = actif.getPosition().getY() * SCALE;
        String type = actif.getClass().getSimpleName();

        gc.setFill(getColorForType(actif));

        if (selectedAssets.contains(actif)) {
            gc.setStroke(Color.RED);
            gc.setLineWidth(2);
            gc.strokeOval(x - 15, y - 15, 30, 30);
        }

        if (type.contains("Reconnaissance")) {
            gc.fillPolygon(new double[] { x, x - 10, x + 10 }, new double[] { y - 10, y + 10, y + 10 }, 3);
        } else if (type.contains("Logistique")) {
            gc.fillRect(x - 8, y - 8, 16, 16);
            gc.setStroke(Color.BLACK);
            gc.strokeOval(x - 12, y - 12, 10, 10);
            gc.strokeOval(x + 2, y - 12, 10, 10);
            gc.strokeOval(x - 12, y + 2, 10, 10);
            gc.strokeOval(x + 2, y + 2, 10, 10);
        } else if (type.contains("Surface")) {
            gc.fillPolygon(
                    new double[] { x - 12, x + 12, x + 6, x - 6 },
                    new double[] { y - 6, y - 6, y + 6, y + 6 },
                    4);
        } else if (type.contains("SousMarin")) {
            gc.fillOval(x - 14, y - 7, 28, 14);
            gc.strokeLine(x, y - 7, x, y - 14);
        } else if (type.contains("Terrestre")) {
            gc.fillRect(x - 10, y - 6, 20, 12);
            gc.setFill(Color.BLACK);
            gc.fillOval(x - 8, y + 6, 4, 4);
            gc.fillOval(x + 4, y + 6, 4, 4);
            gc.fillOval(x - 8, y - 8, 4, 4);
            gc.fillOval(x + 4, y - 8, 4, 4);
        } else {
            gc.fillOval(x - 5, y - 5, 10, 10);
        }

        gc.setFill(Color.BLACK);
        gc.fillText(actif.getId(), x + 12, y);
    }

    private Color getColorForType(ActifMobile actif) {
        String type = actif.getClass().getSimpleName();
        if (type.contains("Reconnaissance")) {
            return Color.BLUE;
        }
        if (type.contains("Logistique")) {
            return Color.GREEN;
        }
        if (type.contains("Surface")) {
            return Color.ORANGE;
        }
        if (type.contains("SousMarin")) {
            return Color.DARKBLUE;
        }
        return Color.RED;
    }
}
