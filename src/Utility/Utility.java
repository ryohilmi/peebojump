package Utility;

import java.awt.*;

public class Utility {

    public static void centeredText(Graphics2D g, String string,
                                    int width, int height, Font font, Color color) {

        g.setFont(font);
        g.setColor(color);

        int stringWidth = (int) g.getFontMetrics().getStringBounds(string, g).getWidth();
        int stringHeight = (int) g.getFontMetrics().getStringBounds(string, g).getHeight();

        int horizontalCenter = width / 2 - stringWidth / 2;
        int verticalCenter = height / 2 - stringHeight / 2;

        g.drawString(string, horizontalCenter, verticalCenter);
    }

    public static void horizontalCenteredText(Graphics2D g, String string,
                                      int width, int y, Font font, Color color) {

        g.setFont(font);
        g.setColor(color);

        int stringWidth = (int) g.getFontMetrics().getStringBounds(string, g).getWidth();

        int horizontalCenter = width / 2 - stringWidth / 2;

        g.drawString(string, horizontalCenter, y);
    }

    public static void verticalCenteredText(Graphics2D g, String string,
                                            int x, int height, Font font, Color color) {

        g.setFont(font);
        g.setColor(color);

        int stringHeight = (int) g.getFontMetrics().getStringBounds(string, g).getHeight();

        int verticalCenter = height / 2 - stringHeight / 2;

        g.drawString(string, x, verticalCenter);
    }



}
