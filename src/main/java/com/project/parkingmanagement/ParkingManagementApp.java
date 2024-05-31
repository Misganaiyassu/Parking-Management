package com.project.parkingmanagement;

import javafx.application.Application;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ParkingManagementApp extends Application {

    private HashMap<Integer, ParkingSlot> parkingSlots;
    private TableView<ParkingSlot> parkingSlotsTable;

    private Stage primaryStage;


    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        showLoginForm();
    }

    private void showLoginForm() {
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(10));

        Label usernameLabel = new Label("Username:");
        TextField usernameTextField = new TextField();
        gridPane.add(usernameLabel, 0, 0);
        gridPane.add(usernameTextField, 1, 0);

        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();
        gridPane.add(passwordLabel, 0, 1);
        gridPane.add(passwordField, 1, 1);

        Button loginButton = new Button("Login");
        loginButton.setOnAction(event -> {
            if (validateCredentials(usernameTextField.getText(), passwordField.getText())) {
                showParkingManagement();
            } else {
                showAlert(Alert.AlertType.ERROR, "Invalid Credentials", "The username or password is incorrect.");
            }
        });
        gridPane.add(loginButton, 1, 2);

        primaryStage.setTitle("Login");
        primaryStage.setScene(new Scene(gridPane, 400, 200));
        primaryStage.show();
    }

    private boolean validateCredentials(String username, String password) {
        // Replace these values with your actual credentials
        String correctUsername = "admin";
        String correctPassword = "password";

        return username.equals(correctUsername) && password.equals(correctPassword);
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showParkingManagement() {
        parkingSlots = new HashMap<>();
        initializeParkingSlots();

        VBox root = new VBox(10);
        root.setPadding(new Insets(10));

        Button findSlotBtn = new Button("Find Available Slot");
        findSlotBtn.setOnAction(event -> findAvailableSlot());

        TextField licensePlateTextField = new TextField();
        licensePlateTextField.setPromptText("Enter license plate");

        Button occupySlotBtn = new Button("Occupy Slot");
        occupySlotBtn.setOnAction(event -> occupySlot(licensePlateTextField.getText()));

        Button releaseSlotBtn = new Button("Release Slot");
        releaseSlotBtn.setOnAction(event -> releaseSlot());

        parkingSlotsTable = createParkingSlotsTable();
        updateParkingSlotsTable();

        root.getChildren().addAll(findSlotBtn, licensePlateTextField, occupySlotBtn, releaseSlotBtn, parkingSlotsTable);

        primaryStage.setTitle("Parking Management System");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();

    }

    private void initializeParkingSlots() {
        for (int i = 1; i <= 100; i++) {
            parkingSlots.put(i, new ParkingSlot(i, false));
        }
    }

    private void findAvailableSlot() {
        Optional<ParkingSlot> availableSlot = parkingSlots.values().stream()
                .filter(slot -> !slot.isOccupied())
                .findFirst();

        if (availableSlot.isPresent()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Available Slot");
            alert.setHeaderText("Available Parking Slot Found");
            alert.setContentText("Slot ID: " + availableSlot.get().getSlotId());
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Available Slots");
            alert.setHeaderText("No Parking Slots Available");
            alert.setContentText("All parking slots are occupied.");
            alert.showAndWait();
        }
    }

    private void occupySlot(String licensePlate) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Occupy Slot");
        dialog.setHeaderText("Enter Slot ID to Occupy");
        dialog.setContentText("Slot ID:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(slotIdStr -> {
            try {
                int slotId = Integer.parseInt(slotIdStr);
                ParkingSlot slot = parkingSlots.get(slotId);

                if (slot == null || slot.isOccupied()) {
                    showAlert(Alert.AlertType.ERROR,  "Cannot Occupy Slot", "The slot is already occupied or invalid.");
                } else {
                    slot.setOccupied(true);
                    slot.setLicensePlate(licensePlate);
                    updateParkingSlotsTable();
                }
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Cannot Occupy Slot", "Please enter a valid slot ID.");
            }
        });
    }

    private void releaseSlot() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Release Slot");
        dialog.setHeaderText("Enter Slot ID to Release");
        dialog.setContentText("Slot ID:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(slotIdStr -> {
            if (slotIdStr.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Input Error", "Slot ID cannot be empty.");
            } else {
                try {
                    int slotId = Integer.parseInt(slotIdStr);
                    ParkingSlot slot = parkingSlots.get(slotId);

                    if (slot == null || !slot.isOccupied()) {
                        showAlert(Alert.AlertType.ERROR, "Slot Not Occupied", "The slot is not occupied or invalid.");
                    } else {
                        slot.setOccupied(false);
                        slot.setLicensePlate("");
                        updateParkingSlotsTable();
                    }
                } catch (NumberFormatException e) {
                    showAlert(Alert.AlertType.ERROR, "Input Error", "Invalid Slot ID. Please enter a valid number.");
                }
            }
        });
    }

    private TableView<ParkingSlot> createParkingSlotsTable() {
//        TableView<ParkingSlot> tableView = new TableView<>();
//
//        TableColumn<ParkingSlot, Integer> slotIdColumn = new TableColumn<>("Slot ID");
//        slotIdColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().getSlotId()));
//        slotIdColumn.setPrefWidth(150);
//
//        TableColumn<ParkingSlot, String> statusColumn = new TableColumn<>("Status");
//        statusColumn.setCellValueFactory(param -> {
//            String status = param.getValue().isOccupied() ? "Occupied" : "Available";
//            return new ReadOnlyObjectWrapper<>(status);
//        });
//        statusColumn.setPrefWidth(150);
//
//        tableView.getColumns().addAll(slotIdColumn, statusColumn);
//        return tableView;

        TableColumn<ParkingSlot, Integer> slotIdColumn = new TableColumn<>("Slot ID");
        slotIdColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().getSlotId()));
        slotIdColumn.setMinWidth(100);

        TableColumn<ParkingSlot, Boolean> occupiedColumn = new TableColumn<>("Occupied");
        occupiedColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().isOccupied()));
        occupiedColumn.setMinWidth(100);

        TableColumn<ParkingSlot, String> licensePlateColumn = new TableColumn<>("License Plate");
        licensePlateColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().getLicensePlate()));
        licensePlateColumn.setMinWidth(200);

        TableView<ParkingSlot> table = new TableView<>();
        table.getColumns().addAll(slotIdColumn, occupiedColumn, licensePlateColumn);

        return table;
    }

    private void updateParkingSlotsTable() {
        ObservableList<ParkingSlot> slots = FXCollections.observableArrayList(parkingSlots.values());
        parkingSlotsTable.setItems(slots);
    }

    private List<ParkingSlot> getOccupiedSlots() {
        return parkingSlots.values().stream()
                .filter(ParkingSlot::isOccupied)
                .collect(Collectors.toList());
    }


    public static void main(String[] args) {
        launch(args);
    }

}
