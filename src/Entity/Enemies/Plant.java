package Entity.Enemies;

import Entity.*;
import TileMap.TileMap;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class Plant extends Enemy {

    // sprites
    private ArrayList<BufferedImage[]> sprites;
    private final int[] numFrames = {
            2, 14
    };

    public Plant(TileMap tm) {

        super(tm);

        type = PLANT;

        // movement
        fallSpeed = 0.2;
        maxFallSpeed = 10.0;

        // size
        width = CONSTWIDTH;
        height = 54;
        attackWidth = CONSTWIDTH * 5;
        idleWidth = width;

        // coliision size
        mincwidth = 20;
        maxcwidth = 80;
        cwidth = mincwidth;
        cheight = 40;

        // attack
        attackDelay = 150;
        attackCounter = 0;

        // health
        health = maxHealth = 2;
        damage = 1;

        // load sprites
        try {
            BufferedImage spritesheet = ImageIO.read(
                    getClass().getResourceAsStream(
                            "/Sprites/Enemies/plant.png"
                    )
			);

            sprites = new ArrayList<BufferedImage[]>();
            for(int i = 0; i < 2; i++) {

                BufferedImage[] bi =
                        new BufferedImage[numFrames[i]];

                for(int j = 0; j < numFrames[i]; j++) {
                    if(i == IDLE) {
                        bi[numFrames[i] - 1 - j] = spritesheet.getSubimage(
                                j * width,
                                i * height,
                                width,
                                height
                        );
                    }
                    else {
                        bi[numFrames[i] - 1 - j] = spritesheet.getSubimage(
                                j * attackWidth,
                                i * height,
                                attackWidth,
                                height
                        );
                    }

                }

                sprites.add(bi);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        animation = new Animation();
        idle = true;
        currentAction = IDLE;
        animation.setFrames(sprites.get(IDLE));
        animation.setDelay(110);

        left = true;
        facingRight = false;

        }

    private void getNextPosition () {
        // falling
        if (falling) {
            dy += fallSpeed;
        }
    }

    public void update () {

        // update position
        getNextPosition();
        checkTileMapCollision();
        setPosition(xtemp, ytemp);

        // return attack
        if(currentAction == ATTACK) {
            if(animation.hasPlayedOnce()) {
                attack = false;
                stretchDone = false;
            }
        }

        // check flinching
        if (flinching) {
            long elapsed =
                    (System.nanoTime() - flinchTimer) / 1000000;
            if (elapsed > 110) {
                flinching = false;
            }
        }

        // check attack
        else if(attack) {
            if(currentAction != ATTACK) {
                currentAction = ATTACK;
                width = attackWidth;
                animation.setFrames(sprites.get(ATTACK));
                animation.setDelay(100);
            }
            if(!stretchDone) stretchCollision();
        }

        // check idle
        else {
            if(currentAction != IDLE) {
                currentAction = IDLE;
                width = idleWidth;
                animation.setFrames(sprites.get(IDLE));
                animation.setDelay(400);
            }
        }

        // update animation
        animation.update();

        //System.out.println("x = " + x);
        //System.out.println("xinverse = " + xinverse);


    }

    public void draw (Graphics2D g){

        //if(notOnScreen()) return;

        setMapPosition();

        super.draw(g);

    }
}























