package controller;

import java.net.URL;
import java.util.ResourceBundle;

import database.Dictionary;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;

public class InfoController implements Initializable {
    @FXML
    private Text englishNumbers;
    @FXML
    private Text vietnameseNumbers;

    private Dictionary vietDict = new Dictionary("Vietnamese");
    private Dictionary engDict = new Dictionary("English");

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        int engSize = engDict.getAllWord().size();
        int vietSize = vietDict.getAllWord().size();
        englishNumbers.setText("Số lượng từ: " + engSize);
        vietnameseNumbers.setText("Số lượng từ: " + vietSize);
    }
}