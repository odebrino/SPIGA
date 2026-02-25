package com.spiga.ui;

import com.spiga.app.SimulationBootstrap;
import com.spiga.app.SimulationContext;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainApp extends Application {

    private SimulationContext context;
    private SimulationBootstrap bootstrap;
    private SimulationView simulationView;
    private Dashboard dashboard;

    @Override
    public void start(Stage primaryStage) {
        bootstrap = new SimulationBootstrap();
        context = bootstrap.createDemoContext();

        simulationView = new SimulationView(context.getZone(), context.getGestionnaire());
        dashboard = new Dashboard(context.getGestionnaire());

        simulationView.setOnUpdate(dashboard::update);

        dashboard.setOnSetTarget((target, objectif) -> {
            bootstrap.assignGlobalTarget(context, target);
            System.out.println("Global target set to: " + target + " (" + objectif + ")");
        });

        dashboard.setOnCreateGroupMission((ids, target, objectif) -> {
            System.out.println("Creating group mission for assets: " + ids + " objective: " + objectif);
            bootstrap.createGroupMission(context, ids, target, objectif);
        });

        dashboard.setAssetCreator((type, id, pos) -> {
            try {
                bootstrap.createAndRegisterAsset(context, type, id, pos);
                dashboard.update();
                System.out.println("Added asset: " + id);
            } catch (IllegalArgumentException ex) {
                showError(ex.getMessage());
            }
        });

        BorderPane root = new BorderPane();
        root.setCenter(simulationView);
        root.setRight(dashboard);

        Scene scene = new Scene(root, 1400, 900);
        primaryStage.setTitle("SPIGA Simulator - JavaFX");
        primaryStage.setScene(scene);
        primaryStage.show();

        simulationView.startSimulation();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
