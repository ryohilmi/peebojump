package Utility;

import java.io.*;
import java.nio.file.Files;
import java.util.List;

public class SaveData {

    public static int LEVEL1 = 1;
    public static int LEVEL2 = 2;
    public static int LEVEL3 = 3;

    public static String savePath = System.getProperty("user.dir") + "\\save.txt";

    public static void createIfNotExist() {
        File save = new File(savePath);

        if (!isFileExists(save)) {
            try {
                save.createNewFile();

                writeToFile(save, "highscore\n0.0\n0.0\n0.0\nlatestlevel\n1");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean isFileExists(File file) {
        return file.exists() && !file.isDirectory();
    }

    public static void writeToFile(File file, String text) {
        try {
            FileWriter writer = new FileWriter(file, true);
            writer.write(text);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String readHighScore(int level) {
        try {
            return Files.readAllLines(new File(savePath).toPath()).get(level);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "0.0";
    }

    public static int readLatestLevel() {
        try {
            return Integer.parseInt(Files.readAllLines(new File(savePath).toPath()).get(5));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return 1;
    }

    public static void writeHighScore(int level, String score) {
        writeToLine(new File(savePath), score, level);
    }

    public static void writeLatestLevel(int level) {
        writeToLine(new File(savePath), String.valueOf(level), 5);
    }

    public static void writeToLine(File file, String text, int lineNumber) {
        try {
            List<String> lines = Files.readAllLines(file.toPath());
            lines.set(lineNumber, text);
            Files.write(file.toPath(), lines);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
