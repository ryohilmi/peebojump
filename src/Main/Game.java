package Main;

import Utility.SaveData;

import javax.swing.*;

public class Game {

    public static void main(String[] args) {
        // Audio terkadang akan fail buat di load
        // possible cause: karena versi sdk / setting
        // message: unsupported format
        JFrame window = new JFrame("PeeboJump");
        window.setContentPane(new GamePanel());
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.pack();
        window.setVisible(true);

        SaveData.createIfNotExist();
    }
}
