package controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import app.ManageApp;
import database.Dictionary;
import database.Word;
import javafx.css.Match;
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

        Pattern pattern = Pattern.compile("/[^/]+/");
        Matcher m = pattern.matcher(newPronun);

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
                int index = ManageApp.findWord(dict, newWord.trim());
                Word word = new Word(newWord, newPronun, newMeaning, false);
                if (!newPronun.equals("") && !m.find()) {
                    Alert confimAlert1 = new Alert(Alert.AlertType.CONFIRMATION);
                    confimAlert1.setTitle("BÁO");
                    confimAlert1.setHeaderText("BÁOOO");
                    confimAlert1.setContentText("Định dạng phát âm không đúng! \nBạn có muốn tự động tạo định dạng /từ/ không? \nBấm YES để đồng ý. \nChọn NO, phát âm mới của bạn sẽ bị xóa!");
        
                    ButtonType buttonTypeYES = new ButtonType("YES", ButtonBar.ButtonData.YES);
                    ButtonType buttonTypeNO = new ButtonType("NO", ButtonBar.ButtonData.NO);
        
                    confimAlert1.getButtonTypes().setAll(buttonTypeYES, buttonTypeNO);
    
                    Optional<ButtonType> result = confimAlert1.showAndWait();
                
                    confimAlert1.show();
    
                    if(result.get() == buttonTypeYES) {
                        typePronun.setText("/" + newPronun + "/");
                        newPronun = "/" + newPronun + "/";
                        confimAlert1.close();
                    } else if (result.get() == buttonTypeNO) {
                        newPronun = "";
                        confimAlert1.close();
                    }
                }

                if (index == -1) {
                    word.setPronunciation(newPronun);
                    dict.add(word);
                    Collections.sort(dict);
        
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
                        word.setPronunciation(newPronun);
                        dict.set(index, word);
                        confimAlert.close();
                    } else if (result.get() == buttonTypeNO) {
                        dict.set(index, new Word(newWord, dictionary.getWord(index).getPronunciation().replace("\n", "") + "\n" + newPronun.replace("\n", "") + "\n", dictionary.getWord(index).getMeaning() + newMeaning + "\n", false));
                        confimAlert.close();
                    }
                }

                dictionary.writeToDictionary(dict);

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
