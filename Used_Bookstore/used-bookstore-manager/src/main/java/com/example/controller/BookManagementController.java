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

    @FXML private TextField timkiem;
    @FXML private TextField titleInput;
    @FXML private TextField authorInput;
    @FXML private TextField categoryInput;
    @FXML private TextField priceInput;
    @FXML private TextField stockInput;
    @FXML private TextField ratingInput;
    @FXML private HBox inputSection;
    @FXML private Button addBookButton;




    @FXML
    public void initialize() {
        // Bind columns
        colId.setCellValueFactory(data -> data.getValue().idProperty().asObject());
        colTitle.setCellValueFactory(data -> data.getValue().titleProperty());
        colAuthor.setCellValueFactory(data -> data.getValue().authorProperty());
        colCategory.setCellValueFactory(data -> data.getValue().categoryProperty());
        colPrice.setCellValueFactory(data -> data.getValue().priceProperty().asObject());
        colStock.setCellValueFactory(data -> data.getValue().stockProperty().asObject());
        colRating.setCellValueFactory(data -> data.getValue().ratingProperty().asObject());

        // Sample data
        bookList.addAll(
                new Book(1, "Đắc nhân tâm", "Dale Carnegie", "Kỹ năng", 85000, 10, 4.5),
                new Book(2, "Lập trình Java", "Nguyễn Văn A", "Giáo trình", 120000, 5, 4.2),
                new Book(3, "Harry Potter", "J.K. Rowling", "Tiểu thuyết", 95000, 7, 4.8)
        );

        bookTable.setItems(bookList);
        addActionButtons();
    }
//biến toàn cục để lưu sách đang sửa
    private Book bookBeingEdited = null;
    private void addActionButtons() {
        colActions.setCellFactory(col -> new TableCell<>() {
            private final Button btnEdit = new Button("Sửa");
            private final Button btnDelete = new Button("Xóa");
            private final HBox pane = new HBox(5, btnEdit, btnDelete);


            {

                btnEdit.setStyle("-fx-background-color: #f1c40f; -fx-text-fill: white;");

                btnDelete.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");

                btnEdit.setOnAction(event -> {
                    Book book = getTableView().getItems().get(getIndex());
                    bookBeingEdited = book;
                    // Hiện form và đổ dữ liệu vào input
                    inputSection.setVisible(true);
                    inputSection.setManaged(true);
                    titleInput.setText(book.getTitle());
                    authorInput.setText(book.getAuthor());
                    categoryInput.setText(book.getCategory());
                    priceInput.setText(String.valueOf(book.getPrice()));
                    stockInput.setText(String.valueOf(book.getStock()));
                    ratingInput.setText(String.valueOf(book.getRating()));
                    // Đổi nhãn nút thành "Cập nhật"
                    addBookButton.setText("Thêm sách");

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
        String keyword = timkiem.getText().toLowerCase().trim();

        if (keyword.isEmpty()) {
            bookTable.setItems(bookList);
            return;
        }

        ObservableList<Book> filtered = FXCollections.observableArrayList();

        for (Book b : bookList) {
            // Tìm theo tiêu đề, tác giả, hoặc thể loại
            if (b.getTitle().toLowerCase().contains(keyword) ||
                    b.getAuthor().toLowerCase().contains(keyword) ||
                    b.getCategory().toLowerCase().contains(keyword)) {

                filtered.add(b);
            }
        }

        if (filtered.isEmpty()) {
            showAlert("Không tìm thấy", "Không có sách nào khớp với từ khóa.");
        } else {
            bookTable.setItems(filtered);

            StringBuilder details = new StringBuilder();

            for (Book b : filtered) {
                details.append("ID: ").append(b.getId()).append("\n");
                details.append("Tên sách: ").append(b.getTitle()).append("\n");
                details.append("Tác giả: ").append(b.getAuthor()).append("\n");
                details.append("Thể loại: ").append(b.getCategory()).append("\n");
                details.append("Giá: ").append(b.getPrice()).append(" VND\n");
                details.append("Tồn kho: ").append(b.getStock()).append("\n");
                details.append("Đánh giá: ").append(b.getRating()).append("\n");

                if (b.getStock() == 0) {
                    details.append("⚠️ Sách đã hết hàng.\n");
                }

                details.append("-----------------------------\n");
            }

            showAlert("Kết quả tìm kiếm", details.toString());
        }
    }

    @FXML
    private void handleAddBook() {
        if (titleInput.getText().isEmpty() || authorInput.getText().isEmpty()
                || categoryInput.getText().isEmpty() || priceInput.getText().isEmpty()
                || stockInput.getText().isEmpty() || ratingInput.getText().isEmpty()) {
            showAlert("Thiếu thông tin", "Vui lòng nhập đầy đủ.");
            return;
        }

        try {
            if (bookBeingEdited == null) {
                //thêm mới
                Book book = new Book(
                        generateNewId(),
                        titleInput.getText(),
                        authorInput.getText(),
                        categoryInput.getText(),
                        Double.parseDouble(priceInput.getText()),
                        Integer.parseInt(stockInput.getText()),
                        Double.parseDouble(ratingInput.getText())
                );
                bookList.add(book);
                showAlert("Thành công", "Đã thêm sách: " + book.getTitle());
            }else {
                // Cập nhật
                bookBeingEdited.setTitle(titleInput.getText());
                bookBeingEdited.setAuthor(authorInput.getText());
                bookBeingEdited.setCategory(categoryInput.getText());
                bookBeingEdited.setPrice(Double.parseDouble(priceInput.getText()));
                bookBeingEdited.setStock(Integer.parseInt(stockInput.getText()));
                bookBeingEdited.setRating(Double.parseDouble(ratingInput.getText()));
                bookTable.refresh();
                showAlert("Cập nhật", "Đã cập nhật sách: " + bookBeingEdited.getTitle());
            }

            clearInputs();
            bookBeingEdited = null;
            inputSection.setVisible(false);
            inputSection.setManaged(false);

        } catch (NumberFormatException e) {
            showAlert("Sai định dạng", "Giá, tồn kho và đánh giá phải là số.");
        }
    }

    private int generateNewId() {
        int maxId = 0;
        for (Book book : bookList) {
            if (book.getId() > maxId) {
                maxId = book.getId();
            }
        }
        return maxId + 1;
    }

    private void clearInputs() {
        titleInput.clear();
        authorInput.clear();
        categoryInput.clear();
        priceInput.clear();
        stockInput.clear();
        ratingInput.clear();
    }

    @FXML
    private void showAddBookDialog() {
        //hiện phần nhập liệu khi nhấn nút thêm sách
        inputSection.setVisible(true);
        inputSection.setManaged(true);

    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
