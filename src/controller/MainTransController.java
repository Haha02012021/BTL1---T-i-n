package controller;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import app.ManageApp;
import database.Bookmark;
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
import javafx.scene.control.OverrunStyle;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import views.MeaningView;

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
    @FXML
    private ImageView bookmarkIcon;

    private TextArea editMeaning = new TextArea();

    static Dictionary dictionary = new Dictionary("English");
    private Bookmark bookmark = new Bookmark(dictionary.getLanguage());
    private int indexWord = -1;

    public static void setDictionary(Dictionary dict) {
        dictionary = dict;
    }

    public Dictionary getDictionary() {
        return dictionary;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        typeSearch.setStyle("-fx-font-family: Roboto; -fx-font-size: 16; -fx-background-color: transparent;");
        typeSearch.setPromptText(dictionary.getLanguage());

        typeSearch.setOnAction(event -> {
            String word = typeSearch.textProperty().getValue();
            this.setMeaningBox(word, englishWord, meaningBox, dictionary);
        });

        typeSearch.textProperty().addListener((observableValue, s, t1) -> {
            if(!t1.trim().isEmpty()) {
                searchBox.getChildren().clear();

                ArrayList<Word> words = ManageApp.sameWord(dictionary.getAllWord(), t1.trim().toLowerCase(Locale.ROOT));
                for (int i = 0; i < words.size(); i++) {

                    BorderPane borderPane = new BorderPane();

                    Text eng = new Text(words.get(i).getEnglish());
                    eng.setFont(Font.font("Roboto", FontWeight.BOLD, 14));
                    TextFlow english = new TextFlow(eng);
                    english.setPadding(new Insets(0, 0, 0, 6));

                    borderPane.setLeft(english);

                    Text pronun = new Text(words.get(i).getPronunciation());
                    pronun.setFont(Font.font("Roboto", FontWeight.NORMAL, 14));
                    TextFlow pro = new TextFlow(pronun);
                    pro.setPadding(new Insets(0, 6, 0, 0));

                    borderPane.setRight(pro);

                    String string = words.get(i).getEnglish();
                    Scanner sc = new Scanner(words.get(i).getMeaning());
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

                    Label label = new Label(shortMean);
                    label.setFont(Font.font("Roboto", FontPosture.ITALIC, 13));
                    label.setPrefWidth(340);
                    label.setTextOverrun(OverrunStyle.ELLIPSIS);
                    TextFlow textFlow = new TextFlow(label);
                    textFlow.setTextAlignment(TextAlignment.LEFT);
                    borderPane.setBottom(textFlow);
                    borderPane.setPadding(new Insets(5));

                    if(words.get(i).getEnglish().equals(englishWord.getText()))
                        borderPane.setStyle("-fx-background-color: #99ccff; -fx-border-color: transparent transparent #cccccc transparent; -fx-border-width: 2px;");
                    else borderPane.setStyle("-fx-background-color: #F2F3F4; -fx-border-color: transparent transparent #cccccc transparent; -fx-border-width: 2px;");

                    borderPane.hoverProperty().addListener(((ob, ov, nv) -> {
                        if (nv) {
                            borderPane.setStyle("-fx-background-color: #99ccff; -fx-border-color: transparent transparent #cccccc transparent; -fx-border-width: 2px;");
                        } else {
                            if(!string.equals(englishWord.getText()))
                                borderPane.setStyle("-fx-background-color: #F2F3F4; -fx-border-color: transparent transparent #cccccc transparent; -fx-border-width: 2px;");
                        }
                    }));

                    borderPane.setOnMouseClicked(mouseEvent -> {
                        this.setMeaningBox(string, englishWord, meaningBox, dictionary);
                        for (int j = 0; j < words.size(); j++) {
                            searchBox.getChildren().get(j).setStyle("-fx-background-color: #F2F3F4; -fx-border-color: transparent transparent #cccccc transparent; -fx-border-width: 2px;");
                        }
                        borderPane.setStyle("-fx-background-color: #99ccff; -fx-border-color: transparent transparent #cccccc transparent; -fx-border-width: 2px;");
                    });
                  
                    searchBox.getChildren().add(borderPane);
                }
            } else {
                searchBox.getChildren().clear();
            }
        });
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
        this.setMeaningBox(word, englishWord, meaningBox, dictionary);
    }

    public void editButton(MouseEvent mouseEvent) {
        Word word = dictionary.getWord(indexWord);

        VBox editView = new VBox();
        editView.setMinSize(500, 500);
        editView.setStyle("-fx-background-color: white;");

        Label firstNote = new Label("Sửa theo form sau:");
        firstNote.setFont(Font.font("Roboto", FontWeight.BOLD, 20));
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
        Button removeButton = new Button("Remove Word");
        buttonBox.getChildren().addAll(saveButton, removeButton,cancelButton);

        saveButton.setStyle("-fx-border-radius: 0; -fx-border-color: transparent; -fx-background-color:  #064279; -fx-background-radius: 0;-fx-font-size: 16;");
        cancelButton.setStyle("-fx-border-radius: 0; -fx-border-color: transparent; -fx-background-color:  #064279; -fx-background-radius: 0; -fx-font-size: 16;");
        saveButton.setTextFill(Color.WHITE);
        cancelButton.setTextFill(Color.WHITE);
        removeButton.setStyle("-fx-border-radius: 0; -fx-border-color: transparent; -fx-background-color:  #064279; -fx-background-radius: 0; -fx-font-size: 16;");
        removeButton.setTextFill(Color.WHITE);

        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        buttonBox.setMargin(saveButton, new Insets(0, 10, 0, 0));
        buttonBox.setMargin(removeButton, new Insets(0, 10, 0, 0));
        buttonBox.setPrefHeight(60);

        editMeaning.setPrefSize(TextArea.USE_COMPUTED_SIZE, TextArea.USE_COMPUTED_SIZE);
        editMeaning.setText(ManageApp.wordToString(word));
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
                        printWriter.print(ManageApp.wordToString(w));
                    }
                    printWriter.close();
                } catch (Exception exception) {
                    System.out.println(exception.getMessage());
                }
            } catch (Exception exception) {
                System.out.println(exception.getMessage());
            }
            scanner.close();

            Alert savedAlert = new Alert(Alert.AlertType.INFORMATION);
            savedAlert.setTitle("Confirmation");
            savedAlert.setHeaderText("Saved");
            savedAlert.setContentText("Nghĩa mới đã được lưu!");
            ButtonType okButton = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);

            savedAlert.getButtonTypes().setAll(okButton);
            savedAlert.show();

            this.setMeaningBox(dictionary.getWord(indexWord).getEnglish(), englishWord, meaningBox, dictionary);

            
        });

        removeButton.setOnAction(e -> {
            Alert removeAlert = new Alert(Alert.AlertType.WARNING);
            removeAlert.setTitle("Warning");
            removeAlert.setHeaderText("Warn");
            removeAlert.setContentText("Bấm YES, từ này sẽ bị xóa hoàn toàn. Bạn sẽ không tìm được nó nữa. Bạn có muốn xóa nữa không?");

            ButtonType buttonTypeYES = new ButtonType("YES", ButtonBar.ButtonData.YES);
            ButtonType buttonTypeNO = new ButtonType("NO", ButtonBar.ButtonData.NO);

            removeAlert.getButtonTypes().setAll(buttonTypeYES, buttonTypeNO);

            Optional<ButtonType> result = removeAlert.showAndWait();

            removeAlert.show();

            if(result.get() == buttonTypeYES) {
                String english = dictionary.getWord(indexWord).getEnglish();
                dictionary.getAllWord().remove(indexWord);                
                ArrayList<Word> newDict = dictionary.getAllWord();

                Bookmark bookmark = new Bookmark(dictionary.getLanguage());
                ArrayList<String> bmSrc = bookmark.getBmSrc();

                bmSrc.remove(indexWord);

                try {
                    try (PrintWriter printWriter = new PrintWriter("src/database/bookmarkImg" + dictionary.getLanguage() + ".txt")) {
                        for (String bm: bmSrc) {
                            printWriter.println(bm);
                        }
                        printWriter.close();
                    } catch (Exception ex) {
                        System.out.println(ex.getMessage());
                    }
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }

                Dictionary dict = new Dictionary("bookmark");
                ArrayList<Word> bmWords = dict.getAllWord();

                int indexBm = ManageApp.findWord(bmWords, english);

                if (indexBm != -1) {
                    bmWords.remove(indexBm);

                    try {
                        try (PrintWriter printWriter = new PrintWriter("src/database/bookmark.txt")) {
                            for (Word w: bmWords) {
                                printWriter.println(ManageApp.wordToString(w));
                            }
                            printWriter.close();
                        } catch (Exception ex) {
                            System.out.println(ex.getMessage());
                        }
                    } catch (Exception ex) {
                        System.out.println(ex.getMessage());
                    }
                }

                try {
                    try (PrintWriter printWriter = new PrintWriter("src/database/" + dictionary.getLanguage() + ".txt")) {
                        for (Word w: newDict) {
                            printWriter.print(ManageApp.wordToString(w));
                        }
                        printWriter.close();
                    } catch (Exception exception) {
                        System.out.println(exception.getMessage());
                    }
                } catch (Exception exception) {
                    System.out.println(exception.getMessage());
                }

                Label label = new Label("Từ này đã bị xóa!");
                label.setFont(Font.font("Roboto", FontWeight.MEDIUM, FontPosture.ITALIC, 16));
                label.setPadding(new Insets(10));
                meaningBox.setContent(label);
                removeAlert.close();
            } else {
                removeAlert.close();
            }

            System.out.println(result.get().getText());
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

            System.out.println(result.get().getText());

            cancelAlert.show();

            if(result.get() == buttonTypeOK) {
                this.setMeaningBox(dictionary.getWord(indexWord).getEnglish(), englishWord, meaningBox, dictionary);
                cancelAlert.close();
            }
        });

        editView.getChildren().addAll(firstNote, note, editMeaning, buttonBox);
        meaningBox.setContent(editView);
    }

    public void bookmarkButton(MouseEvent event) {
        Word word = dictionary.getWord(indexWord);
        Dictionary bookmarkDict = new Dictionary("bookmark");
        ArrayList<Word> bmDict = bookmarkDict.getAllWord();
        ArrayList<String> bookmarkSrc = bookmark.getBmSrc();
        String newBm = "";

        for (int i = 0; i < bmDict.size(); i++) {
            if (bmDict.get(i).getEnglish().equals(word.getEnglish())) bmDict.remove(i);
        }

        if (bookmarkSrc.get(indexWord).equals("../image/bookmark-before.png")) {
            word.setBookmark(true);
            newBm = "../image/bookmark.png";
            bmDict.add(word);
        } else {
            bookmarkIcon.setImage(new Image(getClass().getResource("../image/bookmark-before.png").toExternalForm()));
            word.setBookmark(false);
            newBm = "../image/bookmark-before.png";            
        }

        bookmarkIcon.setImage(new Image(getClass().getResource(newBm).toExternalForm()));
        bookmarkSrc.set(indexWord, newBm);
        try {
            try (PrintWriter printWriter = new PrintWriter("src/database/bookmark.txt")) {
                for (Word w: bmDict) {
                    printWriter.print(ManageApp.wordToString(w));
                }
                printWriter.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            try (PrintWriter printWriter = new PrintWriter("src/database/bookmarkImg" + dictionary.getLanguage() + ".txt")) {
                for (String string: bookmarkSrc) {
                    printWriter.println(string);
                }
                printWriter.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        dictionary.setAWord(indexWord, word);
        bookmark.setABm(indexWord, newBm);
    }

    public void speakButton(MouseEvent event) {
        try {
            String text = dictionary.getWord(indexWord).getEnglish();
            text = text.replace(" ", "%20");
            String language = "en-UK";
            if(dictionary.getLanguage().equals("English")) {
                language = "en-UK";
            } else {
                language = "vi-VI";
            }
            Media media = new Media("https://translate.google.com/translate_tts?ie=UTF-8&q=" + text.toLowerCase(Locale.ROOT).trim() + "&tl=" + language + "&total=1&idx=0&textlen=7&client=tw-ob&prev=input&ttsspeed=1");
            MediaPlayer mediaPlayer = new MediaPlayer(media);
            mediaPlayer.play();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public Word setMeaningBox(String word, Label labelEng, ScrollPane scrollPane, Dictionary dictionary) {
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

            bookmarkIcon.setImage(new Image(getClass().getResource(bookmark.getBmSrc().get(indexResultWord)).toExternalForm()));
            Dictionary d = new Dictionary("history");
            ArrayList<Word> words = d.getAllWord();

            for (int i = 0; i < words.size(); i++) {
                if (words.get(i).getEnglish().equals(resultWord.getEnglish())) words.remove(i);
            }

            words.add(resultWord);

            try {
                try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/database/history.txt"))) {
                    for (Word w: words) {
                        writer.write(ManageApp.wordToString(w));
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
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

}
