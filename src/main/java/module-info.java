module app {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires java.net.http;
    requires json.simple;

    opens app to javafx.fxml;
    opens controller to javafx.fxml;
    exports app;
}
