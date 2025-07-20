package com.example.controller;

import com.example.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

public class UserManagementController {

    @FXML private TextField searchField;
    @FXML private TableView<User> userTable;
    @FXML private TableColumn<User, Integer> colId;
    @FXML private TableColumn<User, String> colUsername;
    @FXML private TableColumn<User, String> colEmail;
    @FXML private TableColumn<User, String> colRole;
    @FXML private TableColumn<User, String> colStatus;
    @FXML private TableColumn<User, Void> colActions;

    private final ObservableList<User> userList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getId()).asObject());
        colUsername.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getUsername()));
        colEmail.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getEmail()));
        colRole.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getRole()));
        colStatus.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getStatus()));

        addActionButtonsToTable();

        loadMockData();
    }

    private void loadMockData() {
        userList.addAll(
            new User(1, "admin", "admin@example.com", "Admin", "Hoạt động"),
            new User(2, "user1", "user1@example.com", "Người dùng", "Bị khóa"),
            new User(3, "mod123", "mod@example.com", "Mod", "Hoạt động")
        );
        userTable.setItems(userList);
    }

    private void addActionButtonsToTable() {
        colActions.setCellFactory(new Callback<>() {
            @Override
            public TableCell<User, Void> call(final TableColumn<User, Void> param) {
                return new TableCell<>() {
                    private final Button editBtn = new Button("Sửa");
                    private final Button deleteBtn = new Button("Xóa");
                    private final HBox pane = new HBox(10, editBtn, deleteBtn);

                    {
                        editBtn.setStyle("-fx-background-color: #f39c12; -fx-text-fill: white;");
                        deleteBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");

                        editBtn.setOnAction(event -> showEditUserDialog(getTableView().getItems().get(getIndex())));
                        deleteBtn.setOnAction(event -> userList.remove(getTableView().getItems().get(getIndex())));
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        setGraphic(empty ? null : pane);
                    }
                };
            }
        });
    }

    @FXML
    private void showAddUserDialog() {
        Dialog<User> dialog = new Dialog<>();
        dialog.setTitle("Thêm người dùng mới");

        ButtonType addButtonType = new ButtonType("Thêm", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        GridPane grid = createUserForm(null);
        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogBtn -> {
            if (dialogBtn == addButtonType) {
                TextField usernameField = (TextField) grid.lookup("#usernameField");
                TextField emailField = (TextField) grid.lookup("#emailField");
                ComboBox<String> roleBox = (ComboBox<String>) grid.lookup("#roleBox");
                ComboBox<String> statusBox = (ComboBox<String>) grid.lookup("#statusBox");

                int newId = userList.size() + 1;
                return new User(newId,
                        usernameField.getText(),
                        emailField.getText(),
                        roleBox.getValue(),
                        statusBox.getValue());
            }
            return null;
        });

        dialog.showAndWait().ifPresent(userList::add);
    }

    private void showEditUserDialog(User user) {
        Dialog<User> dialog = new Dialog<>();
        dialog.setTitle("Chỉnh sửa người dùng");

        ButtonType saveButtonType = new ButtonType("Lưu", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        GridPane grid = createUserForm(user);
        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogBtn -> {
            if (dialogBtn == saveButtonType) {
                TextField usernameField = (TextField) grid.lookup("#usernameField");
                TextField emailField = (TextField) grid.lookup("#emailField");
                ComboBox<String> roleBox = (ComboBox<String>) grid.lookup("#roleBox");
                ComboBox<String> statusBox = (ComboBox<String>) grid.lookup("#statusBox");

                user.setUsername(usernameField.getText());
                user.setEmail(emailField.getText());
                user.setRole(roleBox.getValue());
                user.setStatus(statusBox.getValue());
                userTable.refresh();
            }
            return null;
        });

        dialog.showAndWait();
    }

    private GridPane createUserForm(User user) {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new javafx.geometry.Insets(20, 150, 10, 10));

        TextField usernameField = new TextField();
        usernameField.setId("usernameField");
        usernameField.setPromptText("Tên đăng nhập");

        TextField emailField = new TextField();
        emailField.setId("emailField");
        emailField.setPromptText("Email");

        ComboBox<String> roleBox = new ComboBox<>();
        roleBox.setId("roleBox");
        roleBox.getItems().addAll("Admin", "Người dùng", "Mod");

        ComboBox<String> statusBox = new ComboBox<>();
        statusBox.setId("statusBox");
        statusBox.getItems().addAll("Hoạt động", "Bị khóa");

        if (user != null) {
            usernameField.setText(user.getUsername());
            emailField.setText(user.getEmail());
            roleBox.setValue(user.getRole());
            statusBox.setValue(user.getStatus());
        }

        grid.add(new Label("Tên đăng nhập:"), 0, 0);
        grid.add(usernameField, 1, 0);
        grid.add(new Label("Email:"), 0, 1);
        grid.add(emailField, 1, 1);
        grid.add(new Label("Vai trò:"), 0, 2);
        grid.add(roleBox, 1, 2);
        grid.add(new Label("Trạng thái:"), 0, 3);
        grid.add(statusBox, 1, 3);

        return grid;
    }

    @FXML
    private void handleSearch() {
        String keyword = searchField.getText().toLowerCase();
        if (keyword.isEmpty()) {
            userTable.setItems(userList);
            return;
        }
        ObservableList<User> filtered = FXCollections.observableArrayList();
        for (User u : userList) {
            if (u.getUsername().toLowerCase().contains(keyword)
                    || u.getEmail().toLowerCase().contains(keyword)
                    || u.getRole().toLowerCase().contains(keyword)) {
                filtered.add(u);
            }
        }
        userTable.setItems(filtered);
    }
}
