package app;

import database.Dictionary;
import database.Word;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ManageApp {
    public static int findWord(ArrayList<Word> wordArr, String word) {
        word = word.trim();
        int low = 0;
        int high = wordArr.size() - 1;
        int mid;

        while (low <= high) {
            mid = (low + high) / 2;

            if (wordArr.get(mid).getEnglish().compareTo(word) < 0) {
                low = mid + 1;
            } else if (wordArr.get(mid).getEnglish().compareTo(word) > 0) {
                high = mid - 1;
            } else {
                return mid;
            }
        }
        return -1;
    }

    public static Word findBHWord(ArrayList<Word> wordArr, String word) {
        word = word.trim().toLowerCase(Locale.ROOT);
        for (Word w: wordArr) {
            if (w.getEnglish().toLowerCase().equals(word)) {
                return w;
            }
        }
        return null;
    }

    public static ArrayList<Word> suggestWord(ArrayList<Word> words, String word) {
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
        if (word.getPronunciation() != null) result += cleanEnterLast(word.getPronunciation());
        else result += "\n";
        result += cleanEnterLast(word.getMeaning());
        return result;
    }

    public static String cleanEnterLast(String string) {
        Scanner scanner = new Scanner(string);
        String res = "";
        while(scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.length() != 0) res += (line + "\n");
        }
        scanner.close();
        return res;
    }
}
