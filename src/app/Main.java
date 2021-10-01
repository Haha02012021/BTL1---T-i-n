package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader root = new FXMLLoader(getClass().getResource("Controller.fxml"));
            Parent r = root.load();
            primaryStage.setTitle("HIHA");
            Scene scene = new Scene(r);

            String path = "https://www.oxfordlearnersdictionaries.com/media/english/uk_pron/t/t_o/t_oth/t_other_1_gb_1.mp3";
            AudioClip audioClip = new AudioClip(path);

            Button button = new Button("Phát");
            button.setOnAction(event -> {
                audioClip.play();
            });
            String s = "t-other";
            if (s.contains(" ")) s = s.replace(" ", "_");
            if (s.contains("-")) s = s.replace("-", "_");
            if (s.contains("'")) s = s.replace("'", "_");
            String[] resArr = new String[s.length()];
            resArr[0] = String.valueOf(s.charAt(0));
            int index = 1;
            for (int i = 1; i < s.length(); i+=2) {
                resArr[index] = resArr[index-1] + s.charAt(i) + s.charAt(i+1);
                index++;
            }
            String res = "";
            for (String s1: resArr) {
                if (s1 != null) res += ("/" + s1);
            }
            System.out.println(res);
            String pathAudioUK = "https://www.oxfordlearnersdictionaries.com/media/english/uk_pron%s_1_gb_1.mp3";
            String pathAudioUK1 = pathAudioUK;
            pathAudioUK1 = pathAudioUK1.replace("%s", res);
            System.out.println(pathAudioUK1);
            StackPane stackPane = new StackPane();
            stackPane.getChildren().add(button);
            Scene scene1 = new Scene(stackPane, 300, 300);

            primaryStage.setScene(scene);
            primaryStage.show();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
}
