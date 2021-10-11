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
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.sound.midi.VoiceStatus;

public class MainController implements Initializable{
    @FXML
    private VBox functionVBox;
    @FXML
    private HBox allViewBox;


    private String[] color = {"#0A7BE9", "#064279"};
    static Word meanedWord;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            FXMLLoader mainTransFxmlLoader = new FXMLLoader(getClass().getResource("../views/MainTrans.fxml"));
            Parent mainTransParent = mainTransFxmlLoader.load();
            allViewBox.getChildren().add(mainTransParent);
            allViewBox.setHgrow(mainTransParent, Priority.ALWAYS);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
    }

    public void engVietButton(MouseEvent mouseEvent) {
        MainTransController.setDictionary(new Dictionary("English"));
        try {
            FXMLLoader mainTransFxmlLoader = new FXMLLoader(getClass().getResource("../views/MainTrans.fxml"));
            Parent mainTransParent = mainTransFxmlLoader.load();
            allViewBox.getChildren().remove(1);
            allViewBox.getChildren().add(mainTransParent);
            allViewBox.setHgrow(allViewBox.getChildren().get(1), Priority.ALWAYS);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
    }

    public void vietEngButton(MouseEvent mouseEvent) {
        MainTransController.setDictionary(new Dictionary("Vietnamese"));
        try {
            FXMLLoader mainTransFxmlLoader = new FXMLLoader(getClass().getResource("../views/MainTrans.fxml"));
            Parent mainTransParent = mainTransFxmlLoader.load();
            allViewBox.getChildren().remove(1);
            allViewBox.getChildren().add(mainTransParent);
            allViewBox.setHgrow(allViewBox.getChildren().get(1), Priority.ALWAYS);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
    }

    public void moreButton(MouseEvent event) {
        try {
            FXMLLoader mainTransFxmlLoader = new FXMLLoader(getClass().getResource("../views/Ggtrans.fxml"));
            Parent mainTransParent = mainTransFxmlLoader.load();
            allViewBox.getChildren().remove(1);
            allViewBox.getChildren().add(mainTransParent);
            allViewBox.setHgrow(allViewBox.getChildren().get(1), Priority.ALWAYS);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
    }

    public void historyBox(MouseEvent event) {
        BookmarkController.setDictionary(new Dictionary("history"));
        try {
            FXMLLoader mainTransFxmlLoader = new FXMLLoader(getClass().getResource("../views/History.fxml"));
            Parent mainTransParent = mainTransFxmlLoader.load();
            allViewBox.getChildren().remove(1);
            allViewBox.getChildren().add(mainTransParent);
            allViewBox.setHgrow(allViewBox.getChildren().get(1), Priority.ALWAYS);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
    }

    public void bookmarkBox(MouseEvent event) {
        BookmarkController.setDictionary(new Dictionary("bookmark"));
        try {
            FXMLLoader mainTransFxmlLoader = new FXMLLoader(getClass().getResource("../views/Bookmark.fxml"));
            Parent mainTransParent = mainTransFxmlLoader.load();
            allViewBox.getChildren().remove(1);
            allViewBox.getChildren().add(mainTransParent);
            allViewBox.setHgrow(allViewBox.getChildren().get(1), Priority.ALWAYS);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
    }

    public void colorButton(MouseEvent event) {
        
    }

}

