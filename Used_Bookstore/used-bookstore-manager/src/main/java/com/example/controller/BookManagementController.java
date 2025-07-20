package com.example.controller;

import com.example.model.Book;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

public class BookManagementController {

    @FXML
    private TextField searchField;

    @FXML
    private TableView<Book> bookTable;

    @FXML
    private TableColumn<Book, Integer> colId;
    @FXML
    private TableColumn<Book, String> colTitle;
    @FXML
    private TableColumn<Book, String> colAuthor;
    @FXML
    private TableColumn<Book, String> colCategory;
    @FXML
    private TableColumn<Book, Double> colPrice;
    @FXML
    private TableColumn<Book, Integer> colStock;
    @FXML
    private TableColumn<Book, Double> colRating;
    @FXML
    private TableColumn<Book, Void> colActions;

    private final ObservableList<Book> bookList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Setup column bindings
        colId.setCellValueFactory(data -> data.getValue().idProperty().asObject());
        colTitle.setCellValueFactory(data -> data.getValue().titleProperty());
        colAuthor.setCellValueFactory(data -> data.getValue().authorProperty());
        colCategory.setCellValueFactory(data -> data.getValue().categoryProperty());
        colPrice.setCellValueFactory(data -> data.getValue().priceProperty().asObject());
        colStock.setCellValueFactory(data -> data.getValue().stockProperty().asObject());
        colRating.setCellValueFactory(data -> data.getValue().ratingProperty().asObject());

        // Add mock data
        bookList.addAll(
            new Book(1, "Đắc nhân tâm", "Dale Carnegie", "Kỹ năng", 85000, 10, 4.5),
            new Book(2, "Lập trình Java", "Nguyễn Văn A", "Giáo trình", 120000, 5, 4.2),
            new Book(3, "Harry Potter", "J.K. Rowling", "Tiểu thuyết", 95000, 7, 4.8)
        );

        bookTable.setItems(bookList);
        addActionButtons();
    }

    private void addActionButtons() {
        colActions.setCellFactory(col -> new TableCell<>() {
            private final Button btnEdit = new Button("Sửa");
            private final Button btnDelete = new Button("Xóa");
            private final HBox pane = new HBox(5, btnEdit, btnDelete);

            {
                btnEdit.setStyle("-fx-background-color: #f39c12; -fx-text-fill: white;");
                btnDelete.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");

                btnEdit.setOnAction(event -> {
                    Book book = getTableView().getItems().get(getIndex());
                    showAlert("Chỉnh sửa", "Sửa sách: " + book.getTitle());
                });

                btnDelete.setOnAction(event -> {
                    Book book = getTableView().getItems().get(getIndex());
                    bookList.remove(book);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : pane);
            }
        });
    }

    @FXML
    private void searchBook() {
        String keyword = searchField.getText().toLowerCase();
        if (keyword.isEmpty()) {
            bookTable.setItems(bookList);
        } else {
            ObservableList<Book> filtered = FXCollections.observableArrayList();
            for (Book b : bookList) {
                if (b.getTitle().toLowerCase().contains(keyword) || b.getAuthor().toLowerCase().contains(keyword)) {
                    filtered.add(b);
                }
            }
            bookTable.setItems(filtered);
        }
    }

    @FXML
    private void showAddBookDialog() {
        showAlert("Thêm sách", "Chức năng thêm sách sẽ được cập nhật sau.");
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
