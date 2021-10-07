package app;

import controller.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;
import com.sun.speech.freetts.*;
import com.sun.speech.freetts.VoiceManager;
import com.sun.speech.freetts.Voice;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader root = new FXMLLoader(getClass().getResource("../views/MainController.fxml"));
            Parent r = root.load();
            primaryStage.setTitle("HIHA");
            Scene scene = new Scene(r);

            Button button = new Button("Click");
            StackPane stackPane = new StackPane();
            stackPane.getChildren().add(button);
            Scene scene1 = new Scene(stackPane, 300, 300);

            button.setOnAction(e -> {
                System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
                VoiceManager voiceManager = VoiceManager.getInstance();
                Voice voice = voiceManager.getVoice("kevin16");
                voice.allocate();
                voice.speak("please speak something");
        
            });

            //,cmudict04,freetts-jsapi10

            scene.getStylesheets().add(getClass().getResource("../css/listView.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.show();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
}
