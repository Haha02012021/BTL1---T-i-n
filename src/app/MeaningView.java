package app;

import database.Word;
import javafx.geometry.Insets;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MeaningView {
    private Word word;

    public Word getWord() {
        return word;
    }

    public void setWord(Word word) {
        this.word = word;
    }

    static TextFlow pronunText(String pro) {
        Text pronun = new Text(pro);
        TextFlow textFlow = new TextFlow(pronun);
        textFlow.setPadding(new Insets(0, 0, 5, 20));
        return textFlow;
    }

    static TextFlow partOfSpeechText(String partOfSpeech) {
        Text pOS = new Text(partOfSpeech);
        pOS.setFont(Font.font("Cambria", 20));
        pOS.setFill(Color.DARKBLUE);
        TextFlow textFlow = new TextFlow(pOS);
        textFlow.setPadding(new Insets(10, 0, 5, 20));
        return textFlow;
    }

    static TextFlow meaningLineText(String meaningLine) {
        Text text = new Text("| " + meaningLine);
        text.setWrappingWidth(180);
        TextFlow textFlow = new TextFlow(text);
        textFlow.setPadding(new Insets(0, 0, 5, 20));
        return textFlow;
    }

    static TextFlow phraseText(String phrase) {
        Text text = new Text("・" + phrase);
        TextFlow textFlow = new TextFlow(text);
        textFlow.setPadding(new Insets(0, 0, 5, 20));
        return textFlow;
    }

    static TextFlow exampleText(String example) {
        Text text = new Text(example);
        text.setFill(Color.LIGHTBLUE);
        TextFlow textFlow = new TextFlow(text);
        textFlow.setPadding(new Insets(0, 0, 5, 20));
        return textFlow;
    }

    static TextFlow exampleMeaningText(String example) {
        Text text = new Text(example);
        TextFlow textFlow = new TextFlow(text);
        textFlow.setPadding(new Insets(0, 0, 0, 20));
        return textFlow;
    }

    public VBox meaningVBox() {
        VBox meaning = new VBox();
        meaning.setSpacing(4);
        TextFlow pronun = new TextFlow();
        if (word.getPronunciation() != null) pronun.getChildren().add(new Text(word.getPronunciation()));
        meaning.getChildren().add(pronun);
        pronun.setPadding(new Insets(0, 0, 0, 20));
        Scanner sc = new Scanner(word.getMeaning());
        Pattern example = Pattern.compile("\\=([^\\+]+)\\+ ([^\\r\\n]+)");
        while (sc.hasNext()) {
            String line = sc.nextLine();
            if (line.charAt(0) == '*') meaning.getChildren().add(partOfSpeechText(line.substring(2)));
            else if (line.charAt(0) == '-') meaning.getChildren().add(meaningLineText(line.substring(2)));
            else if (line.charAt(0) == '!') meaning.getChildren().add(phraseText(line.substring(1)));
            else if (line.charAt(0) == '=') {
                Matcher m = example.matcher(line);
                if (m.find()) meaning.getChildren().addAll(exampleText(m.group(1)), exampleMeaningText(m.group(2)));
            }
            else meaning.getChildren().add(pronunText(line));
        }
        meaning.setStyle("-fx-font-size: 16; -fx-background-color: white;");
        return meaning;
    }
}
