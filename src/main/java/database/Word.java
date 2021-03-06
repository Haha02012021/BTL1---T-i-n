package database;

public class Word implements Comparable<Word>{
    private String english;
    private String pronunciation;
    private String meaning;
    private boolean bookmark;

    public Word(String english, String pronunciation, String meaning, boolean bookmark) {
        this.english = english;
        this.pronunciation = pronunciation;
        this.meaning = meaning;
        this.bookmark = bookmark;
    }

    public boolean getBookmark() {
        return this.bookmark;
    }

    public void setBookmark(boolean bookmark) {
        this.bookmark = bookmark;
    }

    public String getEnglish() {
        return this.english;
    }

    public String getPronunciation() {
        return this.pronunciation;
    }

    public String getMeaning() {
        return this.meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public void addMeaning(String line) {
        this.meaning += line + "\n";
    }


    @Override
    public int compareTo(Word o) {
        return this.getEnglish().compareTo(o.getEnglish());
    }

    public void setEnglish(String english) {
        this.english = english;
    }
    public void setPronunciation(String pronunciation) {
        this.pronunciation = pronunciation;
    }

    public boolean isBookmark() {
        return this.bookmark;
    }

}