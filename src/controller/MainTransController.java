package controller;

import app.ManageApp;
import app.MeaningView;
import database.Dictionary;
import database.Word;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.*;

import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainTransController implements Initializable {
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

    private TextArea editMeaning = new TextArea();

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
        searchBox.setVgrow(listView, Priority.ALWAYS);

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
                searchBox.getChildren().clear();

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

                searchBox.getChildren().add(listView);
                listView.getSelectionModel().selectedItemProperty().addListener((observableValue1, s1, t11) -> {
                    System.out.println(t11);
                    ArrayList<String> strings = separateMeaning(t11);
                    search = strings.get(0);
                    MainTransController.setMeaningBox(strings.get(0), englishWord, meaningBox, dictionary);
                });

            } else {
                searchBox.getChildren().clear();
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
        Word word = dictionary.getWord(indexWord);

        VBox editView = new VBox();
        editView.setMinSize(500, 500);
        editView.setStyle("-fx-background-color: white;");

        Label firstNote = new Label("Sửa theo form sau:");
        firstNote.setFont(Font.font("Roboto", FontWeight.BOLD, 20));;
        Label note = new Label("- Sửa từ loại: * <từ loại> \n" + 
                                "- Sửa phát âm: /<phát âm>/ \n" +
                                "- Sửa nghĩa: - <nghĩa> \n" +
                                "- Sửa từ ghép: !<từ ghép> \n" +
                                "- Sửa ví dụ: = <ví dụ> \n" +
                                "- Sửa nghĩa ví dụ: =<ví dụ>+ <nghĩa ví dụ>");
        note.setFont(Font.font("Roboto", FontWeight.LIGHT, FontPosture.ITALIC, 16));
        note.setPrefHeight(200);
        

        HBox buttonBox = new HBox();
        Button saveButton = new Button("Save");
        Button cancelButton = new Button("Cancel");
        buttonBox.getChildren().addAll(saveButton, cancelButton);

        saveButton.setStyle("-fx-border-radius: 0; -fx-border-color: transparent; -fx-background-color:  #064279; -fx-background-radius: 0;-fx-font-size: 16;");
        cancelButton.setStyle("-fx-border-radius: 0; -fx-border-color: transparent; -fx-background-color:  #064279; -fx-background-radius: 0; -fx-font-size: 16;");
        saveButton.setTextFill(Color.WHITE);
        cancelButton.setTextFill(Color.WHITE);
        
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        buttonBox.setMargin(saveButton, new Insets(0, 10, 0, 0));
        buttonBox.setPrefHeight(60);

        editMeaning.setPrefSize(TextArea.USE_COMPUTED_SIZE, TextArea.USE_COMPUTED_SIZE);
        editMeaning.setText(wordToString(word));
        editMeaning.setFont(Font.font("Roboto", FontWeight.NORMAL, 16));
        editMeaning.setStyle("-fx-background-border: 0; -fx-border-color: grey; -fx-display-caret:true;");
        editMeaning.setWrapText(true);

        editView.setMargin(firstNote, new Insets(10, 10, 10, 10));
        editView.setMargin(note, new Insets(0, 15, 15, 15));
        editView.setMargin(editMeaning, new Insets(0, 15, 15, 15));
        editView.setMargin(buttonBox, new Insets(0, 15, 15, 15));
        editView.setVgrow(editMeaning, Priority.ALWAYS);

        saveButton.setOnAction(e -> {
            String editedMeaning = editMeaning.getText();
            Scanner scanner = new Scanner(editedMeaning);

            Matcher m;
            Word editedWord = null;
            Pattern pattern = Pattern.compile("@([^/]+)(/[^/]+/)?"); // tạo pattern để match dòng đầu của mỗi từ

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                m = pattern.matcher(line);
                if (m.find()) {
                    // tạo đối tượng word mới và thêm vào dict (quy tắc tham chiếu)
                    editedWord = new Word(m.group(1).trim(), m.group(2), "", false);
                } else {
                    editedWord.addMeaning(line); //lúc này word là từ cuối cùng trong dict. Method addMeaning sẽ thêm dòng vào thuộc tính meaning
                }
            }
            dictionary.setAWord(indexWord, editedWord);
            ArrayList<Word> words = dictionary.getAllWord();

            try {
                try (PrintWriter printWriter = new PrintWriter("src/database/" + dictionary.getLanguage() + ".txt")) {
                    for (Word w: words) {
                        printWriter.print(wordToString(w));
                    }
                    printWriter.close();
                } catch (Exception exception) {
                    System.out.println(exception.getMessage());
                }
            } catch (Exception exception) {
                System.out.println(exception.getMessage());
            }
            scanner.close();

            Alert savedAlert = new Alert(Alert.AlertType.CONFIRMATION);
            savedAlert.setTitle("Confirmation");
            savedAlert.setHeaderText("Saved");
            savedAlert.setContentText("Nghĩa mới đã được lưu!");
            ButtonType okButton = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);

            savedAlert.getButtonTypes().setAll(okButton);


            Optional<ButtonType> result = savedAlert.showAndWait();

            if(result.get() == okButton) {
                MainTransController.setMeaningBox(dictionary.getWord(indexWord).getEnglish(), englishWord, meaningBox, dictionary);
            }

            System.out.println(result.get().getText());
            savedAlert.show();
        });

        cancelButton.setOnAction(e -> {
            Alert cancelAlert = new Alert(Alert.AlertType.WARNING);
            cancelAlert.setTitle("Warning");
            cancelAlert.setHeaderText("Warn");
            cancelAlert.setContentText("Phần bạn sửa sẽ không được lưu! Bấm Cancel để trở lại màn hình edit!");

            ButtonType buttonTypeOK = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
            ButtonType buttonTypeCancel = new ButtonType("CANCEL", ButtonBar.ButtonData.CANCEL_CLOSE);

            cancelAlert.getButtonTypes().setAll(buttonTypeOK, buttonTypeCancel);

            Optional<ButtonType> result = cancelAlert.showAndWait();

            if(result.get() == buttonTypeOK) {
                MainTransController.setMeaningBox(dictionary.getWord(indexWord).getEnglish(), englishWord, meaningBox, dictionary);
            }

            System.out.println(result.get().getText());

            cancelAlert.show();
        });

        editView.getChildren().addAll(firstNote, note, editMeaning, buttonBox);
        meaningBox.setContent(editView);
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

            Dictionary dict = new Dictionary("history");
            ArrayList<Word> words = dict.getAllWord();

            for (int i = 0; i < words.size(); i++) {
                if (words.get(i).getEnglish().equals(resultWord.getEnglish())) words.remove(i);
            }

            words.add(resultWord);

            try {
                try (PrintWriter printWriter = new PrintWriter("src/database/history.txt")) {
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
        else  {
            labelEng.setText("Ôi!");
            TextFlow textFlow = new TextFlow(new Text("Không tìm thấy từ này!"));
            textFlow.setPadding(new Insets(10, 10, 10, 10));
            scrollPane.setContent(textFlow);
        }
        return resultWord;
    }

    public static String wordToString(Word word) {
        String result = "@" + word.getEnglish() + " ";
        if (word.getPronunciation() != null) result += word.getPronunciation() + "\n";
        else result += "\n";
        result += (word.getMeaning());
        return result;
    }

}
