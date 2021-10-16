package database;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Dictionary {
    private ArrayList<Word> dict = new ArrayList<>();
    private String language;

    public Dictionary(String language) {
        this.language = language;

        String line;
        Matcher m;
        Word word = null;
        Pattern pattern = Pattern.compile("@([^/]+)(/[^/]+/)?"); // tạo pattern để match dòng đầu của mỗi từ

        try {
            try (FileReader fr = new FileReader("src/database/" + language + ".txt")) {
                Scanner reader = new Scanner(fr);
                while(reader.hasNextLine()) {
                    line = reader.nextLine();
                    m = pattern.matcher(line);
                    if (m.find()) {
                        // tạo đối tượng word mới và thêm vào dict (quy tắc tham chiếu)
                        word = new Word(m.group(1).trim(), m.group(2), "", false);
                        this.dict.add(word);
                    } else word.addMeaning(line); //lúc này word là từ cuối cùng trong dict. Method addMeaning sẽ thêm dòng vào thuộc tính meaning
                }

                reader.close();
            }
        } catch (Exception exception) {
            System.out.println(exception);
        }
    }

    public String getLanguage() {
        return this.language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public ArrayList<Word> getAllWord() {
        return this.dict;
    }

    public Word getWord(int index) {
        return this.dict.get(index);
    }

    public void setAWord(int indexWord, Word word) {
        this.dict.set(indexWord, word);
    }
}