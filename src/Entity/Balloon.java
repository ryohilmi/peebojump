package Entity;

import TileMap.TileMap;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Objects;

public class Balloon extends MapObject {
    private boolean is_touched;
    private BufferedImage[] sprite;

    public Balloon(TileMap tm) {
        super(tm);
        facingRight = true;
        width = 32;
        height = 64;
        cwidth = 22;
        cheight = 22;
        setRectModifier(-4, 17);
        try {
            BufferedImage spritesheet = ImageIO.read(
                    Objects.requireNonNull(getClass().getResourceAsStream(
                            "/Sprites/Balloon.png"
                    ))
            );

            sprite = new BufferedImage[4];
            for (int i = 0; i < sprite.length; i++) {
                sprite[i] = spritesheet.getSubimage(
                        width * i, 0,
                        width, height
                );
            }
            animation = new Animation();
            animation.setFrames(sprite);
            animation.setDelay(200);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update() {
        checkTileMapCollision();
        setPosition(xtemp, ytemp);
        animation.update();
    }

    public void showHitbox(boolean flag) {
        hitboxFlag = flag;
    }

    @Override
    public void draw(Graphics2D g) {
        setMapPosition();
        super.draw(g);
    }
}
