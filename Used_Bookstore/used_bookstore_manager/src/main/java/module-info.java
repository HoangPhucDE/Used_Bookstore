module com.mycompany.used_bookstore_manager {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.mycompany.used_bookstore_manager to javafx.fxml;
    exports com.mycompany.used_bookstore_manager;
}
