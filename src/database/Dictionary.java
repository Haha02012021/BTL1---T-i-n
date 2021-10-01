package database;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Dictionary {
    private ArrayList<Word> dict = new ArrayList<>(109000);

    public Dictionary(String language) {
        try {
            File file = new File(getClass().getResource(language + ".txt").toURI()); // tạo đường dẫn tương đối
            Scanner sc = new Scanner(file);

            String line;
            Matcher m;
            Word word = null;
            Pattern pattern = Pattern.compile("@([^/]+)(/[^/]+/)?"); // tạo pattern để match dòng đầu của mỗi từ

            while (sc.hasNext()) {
                line = sc.nextLine();
                m = pattern.matcher(line);
                if (m.find()) {
                    // tạo đối tượng word mới và thêm vào dict (quy tắc tham chiếu)
                    word = new Word(m.group(1).trim(), m.group(2), "");
                    this.dict.add(word);
                } else word.addMeaning(line); //lúc này word là từ cuối cùng trong dict. Method addMeaning sẽ thêm dòng vào thuộc tính meaning
            }

            sc.close();
        } catch (Exception e) {
            // do method getClass() và toURI là các method có throws nên phải handle exception
            // các file của mình về cơ bản là ko thể lỗi được nên chỉ để handle cơ bản như vậy thôi
            System.out.println(e.getMessage());
        }
    }

    public ArrayList<Word> getAllWord() {
        return this.dict;
    }

    public Word getWord(int index) {
        return this.dict.get(index);
    }
}