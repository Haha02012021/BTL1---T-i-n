package controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.sound.midi.SysexMessage;
import javax.swing.text.StyledEditorKit.BoldAction;

import app.ManageApp;
import app.MeaningView;
import database.Dictionary;
import database.Word;
import javafx.css.converter.FontConverter.FontStyleConverter;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;

public class MainTransController implements Initializable {
    @FXML
    private ScrollPane meaningBox;
    @FXML
    private Label englishWord;
    @FXML
    private TextField typeSearch;
    @FXML
    private VBox vBox;
    @FXML
    private HBox titleHBox;
    @FXML
    private VBox searchVBox;
    @FXML
    private Pane volumnPane;

    private ListView<String> listView = new ListView<>();
    private String search;
    static Dictionary dictionary = new Dictionary("English");
    static int indexWord;

    public static void setDictionary(Dictionary dict) {
        dictionary = dict;
    }

    public static Dictionary getDictionary() {
        return dictionary;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        listView.setPrefWidth(280);
        listView.setMinHeight(500);
        listView.setStyle("-fx-font-size: 16; " +
                          "-fx-font-family: Cambria;");

        typeSearch.setStyle("-fx-font-family: Roboto; -fx-font-size: 16; -fx-background-color: transparent;");

        typeSearch.setOnAction(event -> {
            String word = typeSearch.textProperty().getValue();
            search = word;
            MainTransController.setMeaningBox(word, englishWord, meaningBox, dictionary);
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

                    if (shortMean.length() > 32) shortMean = shortMean.substring(0, 32) + "...";
                    meaning += (shortMean + "\n" + word.getPronunciation());
                    listView.getItems().add(meaning);
                }

                vBox.getChildren().add(listView);
                listView.getSelectionModel().selectedItemProperty().addListener((observableValue1, s1, t11) -> {
                    System.out.println(t11);
                    ArrayList<String> strings = separateMeaning(t11);
                    search = strings.get(0);
                    MainTransController.setMeaningBox(strings.get(0), englishWord, meaningBox, dictionary);
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
                Text eng = new Text(strings.get(0));
                eng.setFont(Font.font("Roboto", FontWeight.BOLD, 14));
                TextFlow english = new TextFlow(eng);
                english.setPadding(new Insets(0, 0, 0, 6));
                borderPane.setLeft(new TextFlow(english));
                if(!strings.get(strings.size()-1).equals("null")) {
                    String pronun = strings.get(strings.size()-1);
                    if (pronun.length() > 10) pronun = pronun.substring(0, 10).trim() + ".../";
                    borderPane.setRight(new TextFlow(new Text(pronun)));
                }
                    

                String shortMeaning = strings.get(1);
                Text label = new Text(shortMeaning);
                TextFlow textFlow = new TextFlow(label);
                textFlow.setTextAlignment(TextAlignment.LEFT);
                borderPane.setBottom(textFlow);
                borderPane.setStyle("-fx-background-color: white;");
                borderPane.setPadding(new Insets(3));
                setGraphic(borderPane);
            } else setGraphic(null);
        }
    }

    public static ArrayList<String> separateMeaning(String s) {
        Scanner scanner = new Scanner(s);
        ArrayList<String> strings = new ArrayList<>();
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            strings.add(line);
        }
        return strings;
    }

    public void searchButton(MouseEvent event) {
        String word = typeSearch.textProperty().getValue();
        search = word;
        MainTransController.setMeaningBox(word, englishWord, meaningBox, dictionary);
    }

    public void editButton(MouseEvent mouseEvent) {
        
    }

    public void bookmarkButton(MouseEvent event) {

    }

    public void ukSpeak() {

    }

    public void usSpeak() {

    }

    public static Word setMeaningBox(String word, Label labelEng, ScrollPane scrollPane, Dictionary dictionary) {
        int indexResultWord = ManageApp.findWord(dictionary.getAllWord(), word);
        Word resultWord;
        if (indexResultWord != -1) {
            resultWord = dictionary.getWord(indexResultWord);
            indexWord = indexResultWord;
        }
        else resultWord = null;
        MeaningView vbox = new MeaningView();
        if(resultWord != null) {
            labelEng.setText(resultWord.getEnglish());
            vbox.setWord(resultWord);
            scrollPane.setContent(vbox.meaningVBox());
        }
        else  {
            labelEng.setText("Ôi!");
            TextFlow textFlow = new TextFlow(new Text("Không tìm thấy từ này!"));
            textFlow.setPadding(new Insets(10, 10, 10, 10));
            scrollPane.setContent(textFlow);
        }
        return resultWord;
    }

}
