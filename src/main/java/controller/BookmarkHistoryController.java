package controller;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import app.ManageApp;
import database.Dictionary;
import database.Word;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ButtonBar;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import views.MeaningView;

public class BookmarkHistoryController implements Initializable {
    @FXML
    private ScrollPane meaningBox;
    @FXML
    private Label englishWord;
    @FXML
    private TextField typeSearch;
    @FXML
    private VBox searchBox;
    @FXML
    private HBox titleHBox;
    @FXML
    private VBox searchVBox;
    @FXML
    private Pane volumnPane;
    @FXML
    private AnchorPane contentBox;
    @FXML
    private ImageView bookmarkIcon;

    static Dictionary dictionary = new Dictionary("bookmark");
    private Word wordOfClass = null;

    public static void setDictionary(Dictionary dict) {
        dictionary = dict;
    }

    public static Dictionary getDictionary() {
        return dictionary;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(dictionary.getAllWord().size() > 0)
            setMeaningBox(dictionary.getWord(dictionary.getAllWord().size() - 1).getEnglish(), englishWord, meaningBox, dictionary.getAllWord());
        

        typeSearch.setStyle("-fx-font-family: Roboto; -fx-font-size: 16; -fx-background-color: transparent;");

        typeSearch.setOnAction(event -> {
            String word = typeSearch.textProperty().getValue();
            this.setMeaningBox(word, englishWord, meaningBox, dictionary.getAllWord());
        });

        ArrayList<Word> word = dictionary.getAllWord();
        for (int i = word.size() - 1; i >= 0; i--) {

            BorderPane borderPane = new BorderPane();

            Text eng = new Text(word.get(i).getEnglish());
            eng.setFont(Font.font("Roboto", FontWeight.BOLD, 14));
            TextFlow english = new TextFlow(eng);
            english.setPadding(new Insets(0, 0, 0, 6));

            borderPane.setLeft(english);

            Text pronun = new Text(word.get(i).getPronunciation());
            pronun.setFont(Font.font("Roboto", FontWeight.NORMAL, 14));
            TextFlow pro = new TextFlow(pronun);
            pro.setPadding(new Insets(0, 6, 0, 0));

            borderPane.setRight(pro);

            Scanner sc = new Scanner(word.get(i).getMeaning());
            Pattern example = Pattern.compile("\\=([^\\+]+)\\+ ([^\\r\\n]+)");
            String shortMean = "";
            while (sc.hasNext()) {
                String line = sc.nextLine();
                if (line.charAt(0) == '*') shortMean += (line.substring(1) + ": ");
                else if (line.charAt(0) == '-') shortMean += (line.substring(1) + " ");
                else if (line.charAt(0) == '!') shortMean += (line.substring(1) + " ");
                else if (line.charAt(0) == '=') {
                    Matcher m = example.matcher(line);
                    if (m.find()) shortMean += ("ex: " + m.group(1) + " " + m.group(2) + " ");
                }
                else shortMean += line;
            }
            sc.close();

            Label label = new Label(shortMean);
            label.setFont(Font.font("Roboto", FontPosture.ITALIC, 13));
            label.setPrefWidth(340);
            label.setTextOverrun(OverrunStyle.ELLIPSIS);
            TextFlow textFlow = new TextFlow(label);
            textFlow.setTextAlignment(TextAlignment.LEFT);
            borderPane.setBottom(textFlow);
            borderPane.setPadding(new Insets(5));

            if(word.get(i).getEnglish().equals(englishWord.getText()))
                borderPane.setStyle("-fx-background-color: #99ccff; -fx-border-color: transparent transparent #cccccc transparent; -fx-border-width: 2px;");
            else borderPane.setStyle("-fx-background-color: #F2F3F4; -fx-border-color: transparent transparent #cccccc transparent; -fx-border-width: 2px;");

            String string = word.get(i).getEnglish();
            borderPane.hoverProperty().addListener(((observableValue, aBoolean, t1) -> {
                if (t1) {
                    borderPane.setStyle("-fx-background-color: #99ccff; -fx-border-color: transparent transparent #cccccc transparent; -fx-border-width: 2px;");
                } else {
                    if(!string.equals(englishWord.getText()))
                        borderPane.setStyle("-fx-background-color: #F2F3F4; -fx-border-color: transparent transparent #cccccc transparent; -fx-border-width: 2px;");
                }
            }));

            borderPane.setOnMouseClicked(mouseEvent -> {
                setMeaningBox(string, englishWord, meaningBox, word);
                for (int j = 0; j < word.size(); j++) {
                    searchBox.getChildren().get(j).setStyle("-fx-background-color: #F2F3F4; -fx-border-color: transparent transparent #cccccc transparent; -fx-border-width: 2px;");
                }
                borderPane.setStyle("-fx-background-color: #99ccff; -fx-border-color: transparent transparent #cccccc transparent; -fx-border-width: 2px;");
            });

            searchBox.getChildren().add(borderPane);
        }
    }

    public static ArrayList<String> separateMeaning(String s) {
        Scanner scanner = new Scanner(s);
        ArrayList<String> strings = new ArrayList<>();
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            strings.add(line);
        }
        scanner.close();
        return strings;
    }

    public void searchButton(MouseEvent event) {
        String word = typeSearch.textProperty().getValue();
        this.setMeaningBox(word, englishWord, meaningBox, dictionary.getAllWord());
    }

    public void speakButton(MouseEvent event) {
        try {
            String text = wordOfClass.getEnglish();
            text = text.replace(" ", "%20");
            try {
                URL speakURL = new URL("https://translate.google.com/translate_tts?ie=UTF-8&q=" + text.toLowerCase(Locale.ROOT).trim() + "&tl=" + detectLang(text) + "&total=1&idx=0&textlen=7&client=tw-ob&prev=input&ttsspeed=1");
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

            Media media = new Media("https://translate.google.com/translate_tts?ie=UTF-8&q=" + text.toLowerCase(Locale.ROOT).trim() + "&tl=" + detectLang(text) + "&total=1&idx=0&textlen=7&client=tw-ob&prev=input&ttsspeed=1");
            MediaPlayer mediaPlayer = new MediaPlayer(media);
            mediaPlayer.play();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public Word setMeaningBox(String word, Label labelEng, ScrollPane scrollPane, ArrayList<Word> words) {
        Word resultWord = ManageApp.findBHWord(words, word);

        MeaningView vbox = new MeaningView();
        if(resultWord != null) {
            wordOfClass = resultWord;
            labelEng.setText(resultWord.getEnglish());
            vbox.setWord(resultWord);
            scrollPane.setContent(vbox.meaningVBox());
        }
        else  {
            labelEng.setText("Ôi!");
            Text text = new Text("Không tìm thấy từ này!");
            text.setFont(Font.font("Roboto", FontWeight.NORMAL, FontPosture.ITALIC, 16));
            TextFlow textFlow = new TextFlow(text);
            textFlow.setPadding(new Insets(10, 10, 10, 10));
            scrollPane.setContent(textFlow);
        }
        return resultWord;
    }

    public static String detectLang(String word) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("https://microsoft-translator-text.p.rapidapi.com/Detect?api-version=3.0"))
            .header("content-type", "application/json")
            .header("x-rapidapi-host", "microsoft-translator-text.p.rapidapi.com")
            .header("x-rapidapi-key", "c60baaed7fmsh89132cf0bf904e8p1dd3d4jsn598a8fe828a1")
            .method("POST", HttpRequest.BodyPublishers.ofString("[\r\n{\r\n\"Text\": \"" + word + "\"\r\n}\r\n]"))
            .build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

        JSONParser parser = new JSONParser();

        String result = "";

        try {
            Object object = parser.parse(response.body());
            JSONArray array = (JSONArray) object;

            JSONObject object2 = (JSONObject)array.get(0);

            result = object2.get("language").toString();
        } catch (ParseException parseException) {
            System.out.println(parseException);
        }

        if (result.equals("en")) result = "en-UK";
        else result = "vi-VI";

        return result;
    }
}
