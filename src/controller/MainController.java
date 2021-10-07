package controller;

import database.Dictionary;
import database.Word;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MainController implements Initializable{
    @FXML
    private VBox functionVBox;
    @FXML
    private HBox contentBox;


    private String[] color = {"#0A7BE9", "#064279"};
    static Word meanedWord;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            FXMLLoader mainTransFxmlLoader = new FXMLLoader(getClass().getResource("../views/MainTrans.fxml"));
            Parent mainTransParent = mainTransFxmlLoader.load();
            contentBox.getChildren().add(mainTransParent);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
    }

    public void engVietButton(MouseEvent mouseEvent) {
        MainTransController.setDictionary(new Dictionary("English"));
        try {
            FXMLLoader mainTransFxmlLoader = new FXMLLoader(getClass().getResource("../views/MainTrans.fxml"));
            Parent mainTransParent = mainTransFxmlLoader.load();
            contentBox.getChildren().clear();
            contentBox.getChildren().add(mainTransParent);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
    }

    public void vietEngButton(MouseEvent mouseEvent) {
        MainTransController.setDictionary(new Dictionary("Vietnamese"));
        try {
            FXMLLoader mainTransFxmlLoader = new FXMLLoader(getClass().getResource("../views/MainTrans.fxml"));
            Parent mainTransParent = mainTransFxmlLoader.load();
            contentBox.getChildren().clear();
            contentBox.getChildren().add(mainTransParent);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
    }

    public void moreButton(MouseEvent event) {
        try {
            FXMLLoader ggtransFxmlLoader = new FXMLLoader(getClass().getResource("../views/Ggtrans.fxml"));
            Parent ggtransParent = ggtransFxmlLoader.load();

            contentBox.getChildren().clear();
            contentBox.getChildren().add(ggtransParent);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
    }



    public static ArrayList<Word> bookmarkButton(MouseEvent mouseEvent) {
        ArrayList<Word> bmWords = new ArrayList<>();
        if(!bmWords.contains(meanedWord)) bmWords.add(meanedWord);
        return bmWords;
    }

    public void colorButton(MouseEvent event) {
        
    }

}

