package TileMap;

import Main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Objects;

public class Background {

    private BufferedImage image;

    private double x;
    private double y;
    private double dx;
    private double dy;

    public Background(String s) {

        try {
            image = ImageIO.read(
                    Objects.requireNonNull(getClass().getResourceAsStream(s))
            );
        }
        catch(Exception e) {
            e.printStackTrace();
        }

    }

    public void setPosition(double x, double y) {
        this.x = x % GamePanel.WIDTH;
        this.y = y % GamePanel.HEIGHT;
    }

    public void setVector(double dx, double dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public void update() {
        x += dx;
        y += dy;

        if (Math.abs(x) == GamePanel.WIDTH) {
            x = 0;
        }

        if (Math.abs(y) == GamePanel.HEIGHT) {
            y = 0;
        }
    }

    public void draw(Graphics2D g) {

        g.drawImage(image, (int)x, (int)y, null);

        if(x < 0) {
            g.drawImage(image, (int)x + GamePanel.WIDTH, (int)y, null);
        }
        if(x > 0) {
            g.drawImage(image, (int)x - GamePanel.WIDTH, (int)y, null);
        }
    }

}







