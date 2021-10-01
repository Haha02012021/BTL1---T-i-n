package app;

import database.Dictionary;
import database.Word;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Controller implements Initializable{
    @FXML
    private ScrollPane meaningBox;
    @FXML
    private Label englishWord;
    @FXML
    private TextField typeSearch;
    @FXML
    private VBox vBox;
    @FXML
    private VBox searchVBox;
    @FXML
    private HBox titleHBox;
    @FXML
    private VBox functionVBox;
    @FXML
    private Text pronunLabel;
    @FXML
    private Pane volumnPane;

    private ListView<String> listView = new ListView<>();

    private Dictionary dictionary = new Dictionary("English");
    private String[] color = {"#0A7BE9", "BEE0FF"};
    private String pathAudioUS = "https://www.oxfordlearnersdictionaries.com/media/english/us_pron%s_1_us_1.mp3";
    private String pathAudioUK = "https://www.oxfordlearnersdictionaries.com/media/english/uk_pron%s_1_gb_1.mp3";
    private String search;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        listView.setMinHeight(490);
        listView.setPadding(new Insets(0));
        listView.setStyle("-fx-control-inner-background: #BEE0FF; -fx-font-size: 16; " +
                "-fx-font-family: Cambria;");

        typeSearch.setOnAction(event -> {
            String word = typeSearch.textProperty().getValue();
            search = word;
            Controller.setMeaningBox(word, englishWord, pronunLabel, meaningBox, dictionary);
        });

        typeSearch.textProperty().addListener((observableValue, s, t1) -> {
            if(!t1.trim().isEmpty()) {
                vBox.getChildren().clear();

                listView.getItems().clear();
                listView.setCellFactory(e -> {
                    return new CellStyle();
                });

                ArrayList<Word> words = ManageApp.sameWord(dictionary.getAllWord(), t1.trim().toLowerCase(Locale.ROOT));
                for (Word word : words) {
                    String meaning = "";
                    meaning += (word.getEnglish() + "\n");
                    Scanner sc = new Scanner(word.getMeaning());
                    Pattern example = Pattern.compile("\\=([^\\+]+)\\+ ([^\\r\\n]+)");
                    while (sc.hasNext()) {
                        String line = sc.nextLine();
                        if (line.charAt(0) == '*') meaning += (line.substring(1) + ": ");
                        else if (line.charAt(0) == '-') meaning += (line.substring(1) + " ");
                        else if (line.charAt(0) == '!') meaning += (line.substring(1) + " ");
                        else if (line.charAt(0) == '=') {
                            Matcher m = example.matcher(line);
                            if (m.find()) meaning += ("ex: " + m.group(1) + " " + m.group(2) + " ");
                        }
                        else meaning += line;
                    }
                    meaning += ("\n" + word.getPronunciation());
                    listView.getItems().add(meaning);
                }

                vBox.getChildren().add(listView);
                listView.getSelectionModel().selectedItemProperty().addListener((observableValue1, s1, t11) -> {
                    System.out.println(t11);
                    ArrayList<String> strings = separateMeaning(t11);
                    search = strings.get(0);
                    Controller.setMeaningBox(strings.get(0), englishWord, pronunLabel, meaningBox, dictionary);
                });

            } else {
                vBox.getChildren().clear();
            }
        });
    }

    class CellStyle extends ListCell<String> {
        @Override
        public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);

            if (item != null) {
                BorderPane borderPane = new BorderPane();
                ArrayList<String> strings = separateMeaning(item);
                TextFlow english = new TextFlow(new Text(strings.get(0)));
                english.setPadding(new Insets(0, 0, 0, 3));
                borderPane.setLeft(new TextFlow(english));
                if(!strings.get(strings.size()-1).equals("null"))
                    borderPane.setRight(new TextFlow(new Text(strings.get(strings.size()-1))));

                String shortMeaning = strings.get(1);
                if (strings.get(1).length() > 32) {
                    shortMeaning = strings.get(1).substring(0, 32) + "...";
                }
                Text label = new Text(shortMeaning);
                TextFlow textFlow = new TextFlow(label);
                borderPane.setBottom(textFlow);
                textFlow.setPadding(new Insets(0, 0, 0, -5));
                borderPane.setStyle("-fx-background-color: white;");
                borderPane.setPadding(new Insets(10));
                setGraphic(borderPane);
            } else setGraphic(null);
        }
    }

    public static ArrayList<String> separateMeaning(String s) {
        Scanner scanner = new Scanner(s);
        ArrayList<String> strings = new ArrayList<>();
        int i = 0;
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            strings.add(line);
        }
        return strings;
    }

    public void ukSpeak() {
        /*String s = search;
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
        String path = pathAudioUK;
        path = path.replace("%s", res);
        try {
            AudioClip audioClip = new AudioClip(path);
            audioClip.play();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }*/
    }

    public void usSpeak() {
        /*pathAudioUS = pathAudioUS.replace("%s", search);
        try {
            AudioClip audioClip = new AudioClip(pathAudioUS);
            pathAudioUS = pathAudioUS.replace(search, "%s");
            audioClip.play();
        }
        catch (Exception e) {
            System.out.println(pathAudioUS);
            pathAudioUS = pathAudioUS.replace(search, "%s");
        }*/
    }

    public void engVietButton(ActionEvent event) {
        vBox.getChildren().clear();
        typeSearch.textProperty().setValue("");
        dictionary = new Dictionary("English");
    }

    public void vietEngButton(ActionEvent event) {
        vBox.getChildren().clear();
        typeSearch.textProperty().setValue("");
        dictionary = new Dictionary("Vietnamese");
    }

    public void moreButton(ActionEvent event) {

    }

    public void colorButton(ActionEvent event) {
        String currentStyle = searchVBox.getStyle();
        String currentListStyle = listView.getStyle();
        String newColor;
        String newListStyle;
        if (currentStyle.contains(color[0])) {
            newColor = currentStyle.replace(color[0], color[1]);
            newListStyle = currentListStyle.replace(color[0], "#" + color[1]);
        }
        else {
            newColor = currentStyle.replace(color[1], color[0]);
            newListStyle = currentListStyle.replace("#" + color[1], color[0]);
        }
        System.out.println(currentStyle);
        searchVBox.setStyle(newColor);
        titleHBox.setStyle(newColor);
        functionVBox.setStyle(newColor);
        listView.setStyle(newListStyle);
    }

    public void savedButton(ActionEvent event) {

    }


    public void searchButton(ActionEvent event) {
        String word = typeSearch.textProperty().getValue();
        search = word;
        Controller.setMeaningBox(word, englishWord, pronunLabel, meaningBox, dictionary);
    }

    public static void setMeaningBox(String word, Label labelEng, Text labelPro, ScrollPane scrollPane, Dictionary dictionary) {
        Word resultWord = ManageApp.findWord(dictionary.getAllWord(), word);
        MeaningView vbox = new MeaningView();
        if(resultWord != null) {
            labelEng.setText(resultWord.getEnglish());
            labelPro.setText(resultWord.getPronunciation());
            vbox.setWord(resultWord);
            scrollPane.setContent(vbox.meaningVBox());
        }
        else  {
            labelEng.setText("Ôi!");
            TextFlow textFlow = new TextFlow(new Text("Không tìm thấy từ này!"));
            textFlow.setPadding(new Insets(10, 10, 10, 10));
            scrollPane.setContent(textFlow);
        }
    }

}
