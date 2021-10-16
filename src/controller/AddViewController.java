package controller;

import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;
import java.util.ResourceBundle;

import app.ManageApp;
import database.Bookmark;
import database.Dictionary;
import database.Word;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.AnchorPane;

public class AddViewController implements Initializable{
    @FXML
    private TextField typeWord;
    @FXML
    private TextField typePronun;
    @FXML
    private TextArea addArea;
    @FXML
    private ComboBox selectBox;
    @FXML
    private AnchorPane addView;

    private String[] language = {"Tiếng Anh", "Tiếng Việt"};

    @Override 
    public void initialize(URL url, ResourceBundle resourceBundle) {
        typeWord.setStyle("-fx-font-family: Roboto; -fx-font-size: 16; -fx-background-color: transparent;");
        selectBox.getItems().addAll(language);
    }

    public void saveButton() {
        String newWord = typeWord.getText();
        String newMeaning = addArea.getText();
        String newPronun = typePronun.getText();
        String nameFile = "";

        try {
            if(selectBox.getValue().equals(language[0])) {
                nameFile = "English";
            } else if (selectBox.getValue().equals(language[1])) {
                nameFile = "Vietnamese";
            }
    
            if(newWord.equals("")) {
                Alert resetAlert = new Alert(AlertType.ERROR);
                resetAlert.setTitle("Warning");
                resetAlert.setHeaderText("Warn");
                resetAlert.setContentText("Vui lòng nhập Từ mới!");
    
                ButtonType buttonTypeOK = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
    
                resetAlert.getButtonTypes().setAll(buttonTypeOK);
            
                resetAlert.show();
            } else if (!(nameFile.equals("") && newWord.equals(""))) {
                Dictionary dictionary = new Dictionary(nameFile);
                ArrayList<Word> dict = dictionary.getAllWord();
                int index = ManageApp.findWord(dict, newWord.toLowerCase().trim());
                Word word = new Word(newWord, newPronun, newMeaning, false);
                if (index == -1) {
                    dict.add(word);
                    Collections.sort(dict);

                    int indexWord = ManageApp.findWord(dict, word.getEnglish());

                    Bookmark bookmark = new Bookmark(nameFile);
                    ArrayList<String> bmSrc = bookmark.getBmSrc();

                    bmSrc.add(indexWord, "../image/bookmark-before.png");

                    try {
                        try (PrintWriter printWriter = new PrintWriter("src/database/bookmarkImg" + nameFile + ".txt")) {
                            for (String bm: bmSrc) {
                                printWriter.println(bm);
                            }
                            printWriter.close();
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
        
                } else {
                    Alert confimAlert = new Alert(Alert.AlertType.CONFIRMATION);
                    confimAlert.setTitle("BÁO");
                    confimAlert.setHeaderText("BÁOOO");
                    confimAlert.setContentText("Từ này đã có trong từ điển! Bạn muốn thay thế cả từ không? Bấm NO nghĩa bạn vừa thêm sẽ được lưu vào sau nghĩa có sẵn!");
        
                    ButtonType buttonTypeYES = new ButtonType("YES", ButtonBar.ButtonData.YES);
                    ButtonType buttonTypeNO = new ButtonType("NO", ButtonBar.ButtonData.NO);
        
                    confimAlert.getButtonTypes().setAll(buttonTypeYES, buttonTypeNO);

                    Optional<ButtonType> result = confimAlert.showAndWait();
                
                    confimAlert.show();

                    if(result.get() == buttonTypeYES) {
                        dict.set(index, word);
                        confimAlert.close();
                    } else {
                        dict.set(index, new Word(newWord, dictionary.getWord(index).getPronunciation(), dictionary.getWord(index).getMeaning() + newMeaning, false));
                        confimAlert.close();
                    }
                }

                try {
                    try (PrintWriter printWriter = new PrintWriter("src/database/" + nameFile + ".txt")) {
                        for (Word w: dict) {
                            printWriter.println(ManageApp.wordToString(w));
                        }
    
                        Alert savedAlert = new Alert(Alert.AlertType.INFORMATION);
                        savedAlert.setTitle("Confirmation");
                        savedAlert.setHeaderText("Saved");
                        savedAlert.setContentText("Từ mới đã được lưu!");
                        ButtonType okButton = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
    
                        savedAlert.getButtonTypes().setAll(okButton);

                        Optional<ButtonType> result = savedAlert.showAndWait();
                        savedAlert.show();

                        if(result.get().getButtonData() == ButtonData.OK_DONE) {
                            savedAlert.close();
                        }
                        printWriter.close(); 
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        } catch (Exception exception) {
            if(newWord.equals("")) {
                Alert resetAlert = new Alert(Alert.AlertType.ERROR);
                resetAlert.setTitle("Warning");
                resetAlert.setHeaderText("Warn");
                resetAlert.setContentText("Vui lòng chọn Từ điển và nhập Từ mới!");
    
                ButtonType buttonTypeOK = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
    
                resetAlert.getButtonTypes().setAll(buttonTypeOK);
            
                resetAlert.show();
            } else {
                Alert resetAlert = new Alert(Alert.AlertType.ERROR);
                resetAlert.setTitle("Warning");
                resetAlert.setHeaderText("Warn");
                resetAlert.setContentText("Vui lòng nhập chọn Từ điển!");
        
                ButtonType buttonTypeOK = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        
                resetAlert.getButtonTypes().setAll(buttonTypeOK);
                
                resetAlert.show();
            }
        }

    }

    public void resetButton() {
        Alert resetAlert = new Alert(Alert.AlertType.WARNING);
        resetAlert.setTitle("Warning");
        resetAlert.setHeaderText("Warn");
        resetAlert.setContentText("Bạn có chắc chắn muốn reset không?");

        ButtonType buttonTypeYES = new ButtonType("YES", ButtonBar.ButtonData.YES);
        ButtonType buttonTypeNO = new ButtonType("NO", ButtonBar.ButtonData.NO);

        resetAlert.getButtonTypes().setAll(buttonTypeYES, buttonTypeNO);
        
        Optional<ButtonType> result = resetAlert.showAndWait();

        resetAlert.show();

        if(result.get() == buttonTypeYES) {
            typeWord.setText("");
            typePronun.setText("");
            addArea.setText("");
            resetAlert.close();
        } else {
            resetAlert.close();
        }
    }
}
