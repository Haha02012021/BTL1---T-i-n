package controller;

import database.Dictionary;
import database.Word;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import app.Main;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable{
    @FXML
    private VBox functionVBox;
    @FXML
    private HBox allViewBox;
    @FXML
    private Pane engVietPane;
    @FXML
    private Pane vietEngPane;
    @FXML
    private Pane historyPane;
    @FXML
    private Pane bookmarkPane;
    @FXML
    private Pane morePane;
    @FXML
    private Pane addPane;
    @FXML
    private Pane infoPane;


    static Word meanedWord;
    private String styleFocusHoverButton = "-fx-background-color:  #0A72C2; -fx-background-radius: 5; -fx-border-color:  #084E91; -fx-border-radius: 5;-fx-background-insets: -5;";
    private String styleNormalButton = "-fx-background-color:  #0A72C2; -fx-background-radius: 5;";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        engVietPane.setStyle(styleFocusHoverButton);

        Label noteText = new Label("Lịch sử tra từ");
        noteText.setLayoutX(60);
        noteText.setLayoutY(historyPane.getLayoutY());
        
        historyPane.hoverProperty().addListener((ChangeListener<Boolean>) (observable, oldValue, newValue) -> {
            if (newValue) noteText.setVisible(true);
            else noteText.setVisible(false);
        });
        try {
            FXMLLoader mainTransFxmlLoader = new FXMLLoader(Main.class.getResource("views/MainTrans.fxml"));
            Parent mainTransParent = mainTransFxmlLoader.load();
            allViewBox.getChildren().add(mainTransParent);
            allViewBox.setHgrow(mainTransParent, Priority.ALWAYS);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
    }

    public void engVietButton(MouseEvent mouseEvent) {
        engVietPane.setStyle(styleFocusHoverButton);
        vietEngPane.setStyle(styleNormalButton);
        historyPane.setStyle(styleNormalButton);
        bookmarkPane.setStyle(styleNormalButton);
        morePane.setStyle(styleNormalButton);
        addPane.setStyle(styleNormalButton);
        infoPane.setStyle(styleNormalButton);
        MainTransController.setDictionary(new Dictionary("English"));
        try {
            FXMLLoader mainTransFxmlLoader = new FXMLLoader(Main.class.getResource("views/MainTrans.fxml"));
            Parent mainTransParent = mainTransFxmlLoader.load();
            allViewBox.getChildren().remove(1);
            allViewBox.getChildren().add(mainTransParent);
            allViewBox.setHgrow(allViewBox.getChildren().get(1), Priority.ALWAYS);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
    }

    public void vietEngButton(MouseEvent mouseEvent) {
        engVietPane.setStyle(styleNormalButton);
        vietEngPane.setStyle(styleFocusHoverButton);
        historyPane.setStyle(styleNormalButton);
        bookmarkPane.setStyle(styleNormalButton);
        morePane.setStyle(styleNormalButton);
        addPane.setStyle(styleNormalButton);
        infoPane.setStyle(styleNormalButton);
        MainTransController.setDictionary(new Dictionary("Vietnamese"));
        try {
            FXMLLoader mainTransFxmlLoader = new FXMLLoader(Main.class.getResource("views/MainTrans.fxml"));
            Parent mainTransParent = mainTransFxmlLoader.load();
            allViewBox.getChildren().remove(1);
            allViewBox.getChildren().add(mainTransParent);
            allViewBox.setHgrow(allViewBox.getChildren().get(1), Priority.ALWAYS);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
    }

    public void moreButton(MouseEvent event) {
        engVietPane.setStyle(styleNormalButton);
        vietEngPane.setStyle(styleNormalButton);
        historyPane.setStyle(styleNormalButton);
        bookmarkPane.setStyle(styleNormalButton);
        morePane.setStyle(styleFocusHoverButton);
        addPane.setStyle(styleNormalButton);
        infoPane.setStyle(styleNormalButton);
        try {
            FXMLLoader mainTransFxmlLoader = new FXMLLoader(Main.class.getResource("views/Ggtrans.fxml"));
            Parent mainTransParent = mainTransFxmlLoader.load();
            allViewBox.getChildren().remove(1);
            allViewBox.getChildren().add(mainTransParent);
            allViewBox.setHgrow(allViewBox.getChildren().get(1), Priority.ALWAYS);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
    }

    public void historyBox(MouseEvent event) {
        engVietPane.setStyle(styleNormalButton);
        vietEngPane.setStyle(styleNormalButton);
        historyPane.setStyle(styleFocusHoverButton);
        bookmarkPane.setStyle(styleNormalButton);
        morePane.setStyle(styleNormalButton);
        addPane.setStyle(styleNormalButton);
        BookmarkHistoryController.setDictionary(new Dictionary("history"));
        try {
            FXMLLoader mainTransFxmlLoader = new FXMLLoader(Main.class.getResource("views/History.fxml"));
            Parent mainTransParent = mainTransFxmlLoader.load();
            allViewBox.getChildren().remove(1);
            allViewBox.getChildren().add(mainTransParent);
            allViewBox.setHgrow(allViewBox.getChildren().get(1), Priority.ALWAYS);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
    }

    public void bookmarkBox(MouseEvent event) {
        engVietPane.setStyle(styleNormalButton);
        vietEngPane.setStyle(styleNormalButton);
        historyPane.setStyle(styleNormalButton);
        bookmarkPane.setStyle(styleFocusHoverButton);
        morePane.setStyle(styleNormalButton);
        addPane.setStyle(styleNormalButton);
        infoPane.setStyle(styleNormalButton);
        BookmarkHistoryController.setDictionary(new Dictionary("bookmark"));
        try {
            FXMLLoader mainTransFxmlLoader = new FXMLLoader(Main.class.getResource("views/Bookmark.fxml"));
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

    public void addButton(MouseEvent event) {
        engVietPane.setStyle(styleNormalButton);
        vietEngPane.setStyle(styleNormalButton);
        historyPane.setStyle(styleNormalButton);
        bookmarkPane.setStyle(styleNormalButton);
        morePane.setStyle(styleNormalButton);
        addPane.setStyle(styleFocusHoverButton);
        infoPane.setStyle(styleNormalButton);
        try {
            FXMLLoader mainTransFxmlLoader = new FXMLLoader(Main.class.getResource("views/AddView.fxml"));
            Parent mainTransParent = mainTransFxmlLoader.load();
            allViewBox.getChildren().remove(1);
            allViewBox.getChildren().add(mainTransParent);
            allViewBox.setHgrow(allViewBox.getChildren().get(1), Priority.ALWAYS);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
    }
    
    public void infoButton(MouseEvent event) {
        engVietPane.setStyle(styleNormalButton);
        vietEngPane.setStyle(styleNormalButton);
        historyPane.setStyle(styleNormalButton);
        bookmarkPane.setStyle(styleNormalButton);
        morePane.setStyle(styleNormalButton);
        addPane.setStyle(styleNormalButton);
        infoPane.setStyle(styleFocusHoverButton);
        try {
            FXMLLoader mainTransFxmlLoader = new FXMLLoader(Main.class.getResource("views/Information.fxml"));
            Parent mainTransParent = mainTransFxmlLoader.load();
            allViewBox.getChildren().remove(1);
            allViewBox.getChildren().add(mainTransParent);
            allViewBox.setHgrow(allViewBox.getChildren().get(1), Priority.ALWAYS);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
    }

}

