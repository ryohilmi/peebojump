package Entity.Enemies;

import Entity.Animation;
import Entity.Enemies.Enemy;
import TileMap.TileMap;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Kadal extends Enemy {

    // sprites
    private ArrayList<BufferedImage[]> sprites;
    private final int[] numFrames = {
            2, 9, 9
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
        attackWidth = CONSTWIDTH * 9;
        idleWidth = width;
        walkWidth = width;

        // collision size
        mincwidth = 20;
        maxcwidth = 154;
        cwidth = mincwidth;
        cheight = 36;
        stretchSpeed = 3;

        // attack
        attackDelay = 150;
        attackCounter = 0;
        attackAnimDelay = 100;

        // idle
        idleTime = 200;
        idleCounter = 0;
        idleAnimDelay = 150;

        // walk
        walkAnimDelay = 50;
        walkCounter = 0;
        walkMaxDistance = 100;

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
        animation.setDelay(walkAnimDelay);

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

        // cannot walk when idle
        if(currentAction == IDLE) {
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
        if((currentAction == ATTACK)) {
            if(animation.hasPlayedOnce()) {
                // return to the previous action
                if(previousAction == IDLE) { idle = true; }
                else if(previousAction == WALK) { walk = true; }
                // make attack false
                if(attackRight) { attackRight = false; }
                else if(attackLeft) { attackLeft = false; }
                stretchDone = false;

                // stretch fault
                cwidth = mincwidth;
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
        if((attackRight && right && facingRight) || (attackLeft && left && !facingRight)) {
            if (currentAction != ATTACK) {
                // action transition
                previousAction = currentAction;
                if(previousAction == IDLE) { idle = false; }
                else if(previousAction == WALK) { walk = false; }

                // set currentAction ATTACK
                currentAction = ATTACK;
                width = attackWidth;
                animation.setFrames(sprites.get(ATTACK));
                animation.setDelay(attackAnimDelay);
            }
            if (!stretchDone) stretchCollision();
        }

        // check walk
        if(walk) {
            if(walkCounter < walkMaxDistance) {
                idle = false;
                if (currentAction != WALK) {
                    currentAction = WALK;
                    width = walkWidth;
                    animation.setFrames(sprites.get(WALK));
                    animation.setDelay(walkAnimDelay);
                }
                walkCounter++;
            }
            else if(walkCounter == walkMaxDistance) {
                walk = false;
                idle = true;
                walkCounter = 0;

            }
        }

        // check idle
        if(idle){
            walk = false;
            if(currentAction != IDLE) {
                currentAction = IDLE;
                width = idleWidth;
                animation.setFrames(sprites.get(IDLE));
                animation.setDelay(idleAnimDelay);
            }
            idleBuffer();
        }

        // update animation
        animation.update();

        //System.out.println("walk = " + walk);
        //System.out.println("idle = " + idle);

    }

    public void draw (Graphics2D g){

        setMapPosition();

        super.draw(g);

    }
}