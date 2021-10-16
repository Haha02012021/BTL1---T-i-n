package database;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

public class Bookmark {
    private ArrayList<String> bmSrc = new ArrayList<>();
     
    public Bookmark(String language) {
        bmSrc.clear();
        try {
            try (FileReader fr = new FileReader("src/database/bookmarkImg" + language + ".txt")) {
                Scanner reader = new Scanner(fr);
                String line;
                while (reader.hasNextLine()) {
                    line = reader.nextLine();
                    bmSrc.add(line);
                }
                reader.close();
                fr.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public ArrayList<String> getBmSrc() {
        return this.bmSrc;
    }

    public void setABm(int index, String path) {
        this.bmSrc.set(index, path);
    }
    
}
