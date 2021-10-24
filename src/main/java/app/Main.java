package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader root = new FXMLLoader(Main.class.getResource("views/Main.fxml"));
            Parent r = root.load();
            primaryStage.setTitle("HIHA");
            Scene scene = new Scene(r);

            Button button = new Button("Click");
            StackPane stackPane = new StackPane();
            stackPane.getChildren().add(button);


            scene.getStylesheets().add(getClass().getResource("css/style.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.getIcons().add(new Image(getClass().getResource("image/happy.png").toExternalForm()));
            primaryStage.show();
        }
        catch (Exception e) {
            System.out.println("error: " + e);
        }

    }
}
