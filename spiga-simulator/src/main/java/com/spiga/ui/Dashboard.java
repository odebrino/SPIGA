package com.spiga.ui;

import com.spiga.core.ActifMobile;
import com.spiga.core.Point3D;
import com.spiga.mission.GestionnaireEssaim;
import com.spiga.mission.ObjectifMission;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class Dashboard extends VBox {
    private final GestionnaireEssaim gestionnaire;
    private ListView<String> assetList;

    private ComboBox<String> typeSelect;
    private TextField idInput;
    private TextField xInput;
    private TextField yInput;
    private TextField zInput;

    private AssetCreator assetCreator;
    private BiConsumer<Point3D, ObjectifMission> onSetTarget;
    private GroupMissionCreator onCreateGroupMission;

    public interface AssetCreator {
        void create(String type, String id, Point3D position);
    }

    public interface GroupMissionCreator {
        void create(List<String> assetIds, Point3D target, ObjectifMission objectif);
    }

    public Dashboard(GestionnaireEssaim gestionnaire) {
        this(gestionnaire, null);
    }

    public Dashboard(GestionnaireEssaim gestionnaire, AssetCreator assetCreator) {
        this.gestionnaire = gestionnaire;
        this.assetCreator = assetCreator;

        setPadding(new Insets(10));
        setSpacing(10);
        setStyle("-fx-background-color: #f0f0f0; -fx-border-color: #ccc;");
        setPrefWidth(300);

        buildHeader();
        buildCreationSection();
        buildFleetSection();
        buildTargetSection();
    }

    private void buildHeader() {
        Label title = new Label("Dashboard");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        getChildren().add(title);
    }

    private void buildCreationSection() {
        VBox creationBox = new VBox(5);
        creationBox.setStyle("-fx-border-color: #aaa; -fx-padding: 5;");
        creationBox.getChildren().add(new Label("Add New Asset:"));

        typeSelect = new ComboBox<>();
        typeSelect.getItems().addAll(
                "DroneReconnaissance",
                "DroneLogistique",
                "VehiculeSurface",
                "VehiculeSousMarin",
                "VehiculeTerrestre");
        typeSelect.getSelectionModel().selectFirst();

        idInput = new TextField();
        idInput.setPromptText("ID (e.g. D1)");

        HBox posBox = new HBox(5);
        xInput = new TextField("100");
        xInput.setPrefWidth(60);
        yInput = new TextField("100");
        yInput.setPrefWidth(60);
        zInput = new TextField("50");
        zInput.setPrefWidth(60);
        posBox.getChildren().addAll(new Label("X:"), xInput, new Label("Y:"), yInput, new Label("Z:"), zInput);

        Button addButton = new Button("Add Asset");
        addButton.setOnAction(e -> handleAddAsset());

        creationBox.getChildren().addAll(typeSelect, idInput, posBox, addButton);
        getChildren().add(creationBox);
    }

    private void buildFleetSection() {
        getChildren().add(new Label("Current Assets:"));

        assetList = new ListView<>();
        assetList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        update();
        getChildren().add(assetList);

        Button refreshBtn = new Button("Refresh List");
        refreshBtn.setOnAction(e -> update());
        getChildren().add(refreshBtn);
    }

    private void buildTargetSection() {
        VBox targetBox = new VBox(5);
        targetBox.setStyle("-fx-border-color: #aaa; -fx-padding: 5;");
        targetBox.getChildren().add(new Label("Set Target:"));

        HBox targetPosBox = new HBox(5);
        TextField txInput = new TextField("500");
        txInput.setPrefWidth(60);
        TextField tyInput = new TextField("500");
        tyInput.setPrefWidth(60);
        TextField tzInput = new TextField("0");
        tzInput.setPrefWidth(60);
        targetPosBox.getChildren().addAll(new Label("X:"), txInput, new Label("Y:"), tyInput, new Label("Z:"), tzInput);

        ComboBox<ObjectifMission> missionTypeSelect = new ComboBox<>();
        missionTypeSelect.getItems().setAll(ObjectifMission.values());
        missionTypeSelect.getSelectionModel().select(ObjectifMission.RECONNAISSANCE);

        Button setTargetBtn = new Button("Set Target");
        setTargetBtn.setOnAction(e -> {
            try {
                double x = Double.parseDouble(txInput.getText());
                double y = Double.parseDouble(tyInput.getText());
                double z = Double.parseDouble(tzInput.getText());

                Point3D target = new Point3D(x, y, z);
                ObjectifMission objectif = missionTypeSelect.getValue();
                List<String> selectedIds = getSelectedAssetIds();

                if (!selectedIds.isEmpty()) {
                    if (onCreateGroupMission != null) {
                        onCreateGroupMission.create(selectedIds, target, objectif);
                    }
                } else if (onSetTarget != null) {
                    onSetTarget.accept(target, objectif);
                }
            } catch (NumberFormatException ex) {
                showAlert("Invalid target coordinates!");
            }
        });

        targetBox.getChildren().addAll(targetPosBox, missionTypeSelect, setTargetBtn);
        getChildren().add(targetBox);
    }

    public void setOnCreateGroupMission(GroupMissionCreator callback) {
        this.onCreateGroupMission = callback;
    }

    public void setAssetCreator(AssetCreator creator) {
        this.assetCreator = creator;
    }

    public void setOnSetTarget(BiConsumer<Point3D, ObjectifMission> onSetTarget) {
        this.onSetTarget = onSetTarget;
    }

    private void handleAddAsset() {
        if (assetCreator == null) {
            return;
        }

        try {
            String type = typeSelect.getValue();
            String id = idInput.getText();
            double x = Double.parseDouble(xInput.getText());
            double y = Double.parseDouble(yInput.getText());
            double z = Double.parseDouble(zInput.getText());

            if (id == null || id.isBlank()) {
                showAlert("ID is required!");
                return;
            }

            assetCreator.create(type, id, new Point3D(x, y, z));
            idInput.clear();
        } catch (NumberFormatException ex) {
            showAlert("Invalid coordinates!");
        }
    }

    private List<String> getSelectedAssetIds() {
        List<String> selectedItems = new ArrayList<>(assetList.getSelectionModel().getSelectedItems());
        List<String> selectedIds = new ArrayList<>();

        for (String item : selectedItems) {
            if (item != null && !item.isEmpty()) {
                selectedIds.add(item.split(" ")[0]);
            }
        }

        return selectedIds;
    }

    public void update() {
        List<String> selectedIds = getSelectedAssetIds();

        assetList.getItems().clear();
        for (ActifMobile actif : gestionnaire.getFlotte()) {
            String status = String.format("%s [%s] - %s (Autonomie: %.1f%%)",
                    actif.getId(),
                    actif.getClass().getSimpleName(),
                    actif.getEtat(),
                    (actif.getAutonomieActuelle() / actif.getAutonomieMax()) * 100);
            assetList.getItems().add(status);
        }

        assetList.getSelectionModel().clearSelection();
        for (int i = 0; i < assetList.getItems().size(); i++) {
            String item = assetList.getItems().get(i);
            String id = item.split(" ")[0];
            if (selectedIds.contains(id)) {
                assetList.getSelectionModel().select(i);
            }
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
