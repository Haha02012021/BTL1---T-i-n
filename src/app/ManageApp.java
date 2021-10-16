package app;

import database.Word;

import java.util.ArrayList;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ManageApp {
    public static int findWord(ArrayList<Word> wordArr, String word) {
        word = word.trim().toLowerCase(Locale.ROOT);
        for (int i = 0; i < wordArr.size(); i++) {
            if (word.equals(wordArr.get(i).getEnglish().toLowerCase())) return i;
        }
        return -1;
    }

    public static ArrayList<Word> sameWord(ArrayList<Word> words, String word) {
        word = word.trim().toLowerCase(Locale.ROOT);
        ArrayList<Word> wordArr = new ArrayList<>();
        Pattern pattern = Pattern.compile("^" + word);
        for (Word w: words) {
            Matcher matcher = pattern.matcher(w.getEnglish().toLowerCase());
            if (matcher.find()) {
                wordArr.add(w);
                if (wordArr.size() == 20) return wordArr;
            }
        }
        return wordArr;
    }

    public static String wordToString(Word word) {
        String result = "@" + word.getEnglish() + " ";
        if (word.getPronunciation() != null) result += word.getPronunciation() + "\n";
        else result += "\n";
        result += (word.getMeaning());
        return result;
    }
}
