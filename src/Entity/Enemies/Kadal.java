package Entity.Enemies;

import Entity.Animation;
import Entity.Enemy;
import Entity.Player;
import TileMap.TileMap;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Kadal extends Enemy {

    // sprites
    private ArrayList<BufferedImage[]> sprites;
    private final int[] numFrames = {
            2, 11, 9
    };

    public Kadal(TileMap tm) {

        super(tm);

        type = KADAL;

        // movement
        moveSpeed = 0.3;
        maxSpeed = 0.3;
        fallSpeed = 0.2;
        maxFallSpeed = 10.0;

        // size
        width = CONSTWIDTH;
        height = 48;
        attackWidth = 340;
        idleWidth = width;
        walkWidth = width;

        // collision size
        mincwidth = 20;
        maxcwidth = 80;
        cwidth = mincwidth;
        cheight = 36;

        // attack
        attackDelay = 150;
        attackCounter = 0;

        // idle
        idleTime = 200;
        idleCounter = 0;

        // health
        health = maxHealth = 2;
        damage = 1;

        // load sprites
        try {
            BufferedImage spritesheet = ImageIO.read(
                    getClass().getResourceAsStream(
                            "/Sprites/Enemies/kadal.png"
                    )
            );

            sprites = new ArrayList<BufferedImage[]>();
            for(int i = 0; i < 3; i++) {

                BufferedImage[] bi =
                        new BufferedImage[numFrames[i]];

                for(int j = 0; j < numFrames[i]; j++) {
                    if(i == ATTACK) {
                        bi[numFrames[i] - 1 - j] = spritesheet.getSubimage(
                                j * attackWidth,
                                i * height,
                                attackWidth,
                                height
                        );
                    }
                    else if(i == IDLE || i == WALK){
                        bi[numFrames[i] - 1 - j] = spritesheet.getSubimage(
                                j * width,
                                i * height,
                                width,
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
        walk = true;
        currentAction = WALK;
        animation.setFrames(sprites.get(WALK));
        animation.setDelay(110);

        left = true;
        facingRight = false;

    }

    private void getNextPosition () {
        // movement
        if (left && walk) {
            dx -= moveSpeed;
            if (dx < -maxSpeed) {
                dx = -maxSpeed;
            }
        } else if (right && walk) {
            dx += moveSpeed;
            if (dx > maxSpeed) {
                dx = maxSpeed;
            }
        }

        // cannot walk when attack
        if(currentAction == ATTACK) {
            dx = 0;
        }
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
                if(previousAction == IDLE) { idle = true; }
                else if(previousAction == WALK) { walk = true; }
                attack = false;
                stretchDone = false;
            }
        }

        // check wall
        if ((right && dx == 0) || (left && dx == 0)) {
            if(!idle) {
                idle = true;
                walk = false;
            }
            if(idle) {
                idleBuffer();
                if(!idle) {
                    walk = true;
                }
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
        //else if(attack) {
        //    if(currentAction != ATTACK) {
        //        previousAction = currentAction;
        //        currentAction = ATTACK;
        //        width = attackWidth;
        //        animation.setFrames(sprites.get(ATTACK));
        //        animation.setDelay(500);
        //    }
        //    if(!stretchDone) stretchCollision();
        //}

        // check walk
        else if(walk) {
            if(currentAction != WALK) {
                currentAction = WALK;
                width = walkWidth;
                animation.setFrames(sprites.get(WALK));
                animation.setDelay(100);
            }
        }

        // check idle
        else if(idle){
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