package com.example.controller;

import com.example.model.Employee;
import javafx.collections.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class EmployeeManagementController {
    @FXML private TableView<Employee> employeeTable;
    @FXML private TableColumn<Employee, String> colId;
    @FXML private TableColumn<Employee, String> colName;
    @FXML private TableColumn<Employee, String> colEmail;
    @FXML private TableColumn<Employee, String> colPhone;
    @FXML private TableColumn<Employee, String> colRole;
    @FXML private TableColumn<Employee, Void> colActions;
    @FXML private TextField searchField;

    private final ObservableList<Employee> employeeList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Setup column bindings
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("role"));

        loadMockData();
        employeeTable.setItems(employeeList);
        addActionButtons();
    }

    private void loadMockData() {
        employeeList.addAll(
            new Employee("E001", "Nguyễn Văn A", "a@gmail.com", "0901234567", "Admin"),
            new Employee("E002", "Trần Thị B", "b@gmail.com", "0912345678", "Nhân viên"),
            new Employee("E003", "Lê Văn C", "c@gmail.com", "0923456789", "Quản lý")
        );
    }

    private void addActionButtons() {
        colActions.setCellFactory(param -> new TableCell<>() {
            private final Button btnDelete = new Button("Xóa");
            {
                btnDelete.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");
                btnDelete.setOnAction(e -> {
                    Employee employee = getTableView().getItems().get(getIndex());
                    employeeList.remove(employee);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(btnDelete);
                }
            }
        });
    }

    @FXML
    private void searchEmployee() {
        String keyword = searchField.getText().toLowerCase();
        if (keyword.isEmpty()) {
            employeeTable.setItems(employeeList);
            return;
        }

        ObservableList<Employee> filtered = FXCollections.observableArrayList();
        for (Employee emp : employeeList) {
            if (emp.getName().toLowerCase().contains(keyword) || emp.getEmail().toLowerCase().contains(keyword)) {
                filtered.add(emp);
            }
        }
        employeeTable.setItems(filtered);
    }

    @FXML
    private void showAddEmployeeDialog() {
        // Simple inline add (no popup), replace with real dialog if needed
        int nextId = employeeList.size() + 1;
        Employee newEmp = new Employee("E00" + nextId, "Nhân viên mới " + nextId,
                "new" + nextId + "@mail.com", "09" + nextId + "7890", "Nhân viên");
        employeeList.add(newEmp);
    }
}
