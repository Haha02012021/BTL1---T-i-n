package controller;

import app.ManageApp;
import app.MeaningView;
import database.Dictionary;
import database.Word;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.*;

import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BookmarkController implements Initializable {
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

    private ListView<String> listView = new ListView<>();
    private String search;
    static Dictionary dictionary = new Dictionary("bookmark");
    static int indexWord;

    private TextArea editMeaning = new TextArea();

    public static void setDictionary(Dictionary dict) {
        dictionary = dict;
    }

    public static Dictionary getDictionary() {
        return dictionary;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        searchBox.setVgrow(listView, Priority.ALWAYS);
        
        listView.setPrefWidth(280);
        listView.setMinHeight(500);
        listView.setPrefHeight(ListView.USE_COMPUTED_SIZE);
        listView.setStyle("-fx-font-size: 16; " +
                          "-fx-font-family: Cambria;");

        typeSearch.setStyle("-fx-font-family: Roboto; -fx-font-size: 16; -fx-background-color: transparent;");

        typeSearch.setOnAction(event -> {
            String word = typeSearch.textProperty().getValue();
            search = word;
            BookmarkController.setMeaningBox(word, englishWord, meaningBox, dictionary.getAllWord());
        });

        searchBox.getChildren().clear();

        listView.getItems().clear();
        listView.setCellFactory(e -> {
            return new CellStyle();
        });

        ArrayList<Word> word = dictionary.getAllWord();
        for (int i = word.size() - 1; i >= 0; i--) {
            String meaning = "";
            meaning += (word.get(i).getEnglish() + "\n");
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

            if (shortMean.length() > 32) shortMean = shortMean.substring(0, 32) + "...";
            meaning += (shortMean + "\n" + word.get(i).getPronunciation());
            listView.getItems().add(meaning);

            sc.close();
        }

        searchBox.getChildren().add(listView);
        listView.getSelectionModel().selectedItemProperty().addListener((observableValue1, s1, t11) -> {
            System.out.println(t11);
            ArrayList<String> strings = separateMeaning(t11);
            search = strings.get(0);
            BookmarkController.setMeaningBox(strings.get(0), englishWord, meaningBox, dictionary.getAllWord());
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
        BookmarkController.setMeaningBox(word, englishWord, meaningBox, dictionary.getAllWord());
    }

    public void bookmarkButton(MouseEvent event) {
        Word word = dictionary.getWord(indexWord);
        Dictionary bookmarkDict = new Dictionary("bookmark");
        ArrayList<Word> bmDict = bookmarkDict.getAllWord();

        for (int i = 0; i < bmDict.size(); i++) {
            if (bmDict.get(i).getEnglish().equals(word.getEnglish())) bmDict.remove(i);
        }

        if (!word.getBookmark()) {
            word.setBookmark(true);
            bmDict.add(word);
        } else {
            word.setBookmark(false);
        }

        try {
            try (PrintWriter printWriter = new PrintWriter("src/database/bookmark.txt")) {
                for (Word w: bmDict) {
                    printWriter.print(wordToString(w));
                    System.out.println("Da ghi");
                }
                printWriter.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        dictionary.setAWord(indexWord, word);
    }



    public void ukSpeak() {

    }

    public void usSpeak() {

    }

    public static Word setMeaningBox(String word, Label labelEng, ScrollPane scrollPane, ArrayList<Word> words) {
        int indexResultWord = ManageApp.findWord(words, word);
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

            writeFile("history", resultWord);
        }
        else  {
            labelEng.setText("Ôi!");
            TextFlow textFlow = new TextFlow(new Text("Không tìm thấy từ này!"));
            textFlow.setPadding(new Insets(10, 10, 10, 10));
            scrollPane.setContent(textFlow);
        }
        return resultWord;
    }


    public static void writeFile(String nameFile, Word word) {
        Dictionary dict = new Dictionary(nameFile);
        ArrayList<Word> words = dict.getAllWord();

        for (int i = 0; i < words.size(); i++) {
            if (words.get(i).getEnglish().equals(word.getEnglish())) words.remove(i);
        }

        words.add(word);

        try {
            try (PrintWriter printWriter = new PrintWriter("src/database/" + nameFile + ".txt")) {
                for (Word w: words) {
                    printWriter.print(wordToString(w));
                }
                printWriter.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static String wordToString(Word word) {
        String result = "@" + word.getEnglish() + " ";
        if (word.getPronunciation() != null) result += word.getPronunciation() + "\n";
        else result += "\n";
        result += word.getMeaning();
        return result;
    }

}
