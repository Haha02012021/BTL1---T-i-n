package controller;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.ButtonBar;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;

public class GgtransController implements Initializable{
    @FXML
    private TextArea originTextArea;
    @FXML
    private TextArea translatedTextArea;
    @FXML
    private Label fromEng;
    @FXML
    private Label fromViet;
    @FXML
    private Label toEng;
    @FXML
    private Label toViet;

    String fromLang = "en";
    String toLang = "vi";

    private static final String styleNormal = "-fx-background-color: transparent;";
    private static final String styleFocus = "-fx-background-color:  #064279;";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fromEng.setOnMouseClicked(e -> {
            fromEng.setStyle(styleFocus);
            fromViet.setStyle(styleNormal);
            toEng.setStyle(styleNormal);
            toViet.setStyle(styleFocus);

            fromLang = "en";
            toLang = "vi";
            String oldText = translatedTextArea.getText();
            originTextArea.setText(oldText);
            translatedTextArea.setText(this.translateText(oldText));
        });

        fromViet.setOnMouseClicked(e -> {
            fromEng.setStyle(styleNormal);
            fromViet.setStyle(styleFocus);
            toEng.setStyle(styleFocus);
            toViet.setStyle(styleNormal);

            fromLang = "vi";
            toLang = "en";
            String oldText = translatedTextArea.getText();
            originTextArea.setText(oldText);
            translatedTextArea.setText(this.translateText(oldText));
        });

        toEng.setOnMouseClicked(e -> {
            fromEng.setStyle(styleNormal);
            fromViet.setStyle(styleFocus);
            toEng.setStyle(styleFocus);
            toViet.setStyle(styleNormal);

            fromLang = "vi";
            toLang = "en";
            String oldText = translatedTextArea.getText();
            originTextArea.setText(oldText);
            translatedTextArea.setText(this.translateText(oldText));
        });

        toViet.setOnMouseClicked(e -> {
            fromEng.setStyle(styleFocus);
            fromViet.setStyle(styleNormal);
            toEng.setStyle(styleNormal);
            toViet.setStyle(styleFocus);

            fromLang = "en";
            toLang = "vi";
            String oldText = translatedTextArea.getText();
            originTextArea.setText(oldText);
            translatedTextArea.setText(this.translateText(oldText));
        });
    }

    public void translate(MouseEvent event) {
        String oldText = originTextArea.getText();
        translatedTextArea.setText(this.translateText(oldText));
    }

    public void exchange(MouseEvent event) {
        if (fromLang.equals("en")) {
            fromEng.setStyle(styleNormal);
            fromViet.setStyle(styleFocus);
            toEng.setStyle(styleFocus);
            toViet.setStyle(styleNormal);

            fromLang = "vi";
            toLang = "en";
        } else {
            fromEng.setStyle(styleFocus);
            fromViet.setStyle(styleNormal);
            toEng.setStyle(styleNormal);
            toViet.setStyle(styleFocus);

            fromLang = "en";
            toLang = "vi";
        }

        String oldText = translatedTextArea.getText();
        originTextArea.setText(oldText);
        translatedTextArea.setText(this.translateText(oldText));
    }

    public void fromVolume(MouseEvent event) {
        try {
            String text = originTextArea.getText();
            text = text.replace(" ", "%20");
            String language = "en-UK";
            if(fromLang.equals("en")) {
                language = "en-UK";
            } else {
                language = "vi-VI";
            }
            try {
                URL speakURL = new URL("https://translate.google.com/translate_tts?ie=UTF-8&q=" + text.toLowerCase(Locale.ROOT).trim() + "&tl=" + language + "&total=1&idx=0&textlen=7&client=tw-ob&prev=input&ttsspeed=1");
                URLConnection connection = speakURL.openConnection();
                connection.connect();
            } catch (Exception e) {
                //TODO: handle exception
                Alert savedAlert = new Alert(Alert.AlertType.ERROR);
                savedAlert.setTitle("Confirmation");
                savedAlert.setHeaderText("OH NO!");
                savedAlert.setContentText("Hãy kết nối Internet để thực hiện chức năng này!");
                ButtonType okButton = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);

                savedAlert.getButtonTypes().setAll(okButton);
                savedAlert.show();
                return;
            }

            Media media = new Media("https://translate.google.com/translate_tts?ie=UTF-8&q=" + text.toLowerCase(Locale.ROOT).trim() + "&tl=" + language + "&total=1&idx=0&textlen=7&client=tw-ob&prev=input&ttsspeed=1");
            MediaPlayer mediaPlayer = new MediaPlayer(media);
            mediaPlayer.play();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void toVolume(MouseEvent event) {
        try {
            String text = translatedTextArea.getText();
            text = text.replace(" ", "%20");
            String language = "en-UK";
            if(toLang.equals("en")) {
                language = "en-UK";
            } else {
                language = "vi-VI";
            }
            Media media = new Media("https://translate.google.com/translate_tts?ie=UTF-8&q=" + text.toLowerCase(Locale.ROOT).trim() + "&tl=" + language + "&total=1&idx=0&textlen=7&client=tw-ob&prev=input&ttsspeed=1");
            MediaPlayer mediaPlayer = new MediaPlayer(media);
            Status status = mediaPlayer.getStatus();

            if (status == Status.HALTED || status == Status.UNKNOWN)
            {
                Alert savedAlert = new Alert(Alert.AlertType.ERROR);
                savedAlert.setTitle("Confirmation");
                savedAlert.setHeaderText("OH NO!");
                savedAlert.setContentText("Hãy kết nối Internet để thực hiện chức năng này!");
                ButtonType okButton = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);

                savedAlert.getButtonTypes().setAll(okButton);
                savedAlert.show();
                return;
            } else {
                mediaPlayer.play();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static String translatedTranslate(String oriText, String fromLang, String toLang) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://microsoft-translator-text.p.rapidapi.com/translate?to=" + toLang + "&api-version=3.0&from" + fromLang + "&profanityAction=NoAction&textType=plain"))
                .header("content-type", "application/json")
                .header("x-rapidapi-host", "microsoft-translator-text.p.rapidapi.com")
                .header("x-rapidapi-key", "c60baaed7fmsh89132cf0bf904e8p1dd3d4jsn598a8fe828a1")
                .method("POST", HttpRequest.BodyPublishers.ofString("[\r\n{\r\n\"Text\": \"" + oriText + "\"\r\n}\r\n]"))
                .build();
        HttpResponse<String> response;
        try {
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "Vui lòng kết nối Internet để thực hiện chức năng này!";
        }
        //System.out.println(response.body());

        JSONParser parser = new JSONParser();

        String result = "";

        try {
            Object object = parser.parse(response.body());
            JSONArray array = (JSONArray) object;

            JSONObject object2 = (JSONObject)array.get(0);

            JSONArray translations = (JSONArray)object2.get("translations");

            JSONObject object3 = (JSONObject)translations.get(0);

            //System.out.println("text: " + object3.get("text"));
            result = object3.get("text").toString();
            //System.out.println("to: " + object3.get("to"));
        } catch (ParseException parseException) {
            System.out.println(parseException);
        }

        return result;
    }
    public String translateText(String oldText) {
        Scanner scanner = new Scanner(oldText);
        String newText = "";

        while(scanner.hasNextLine()) {
            String line = scanner.nextLine();
            try {
                newText += (translatedTranslate(line, fromLang, toLang) + "\n");
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        scanner.close();

        return newText;
    }

}
