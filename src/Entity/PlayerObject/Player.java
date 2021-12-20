package Entity.PlayerObject;

import Audio.AudioPlayer;
import TileMap.*;
import Entity.*;
import Entity.Enemies.*;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;


public class Player extends MapObject {

    // player stuff
    private int health;
    private final int maxHealth;
    private int fire;
    private final int maxFire;
    private boolean dead;
    private boolean flinching;
    private long flinchTimer;

    // lightning properties
    private boolean firing;
    private final int fireCost;
    private final int lightningDmg;
    private final ArrayList<Lightning> lightnings;

    // scratch
    private boolean scratching;
    private final int scratchDamage;
    private final int scratchRange;

    // animations
    private ArrayList<BufferedImage[]> sprites;
    private final int[] numFrames = {
            2, 6, 4, 5, 3
    };

    // animation actions
    private static final int IDLE = 0;
    private static final int IDLE_H = 42;
    private static final int IDLE_W = 32;

    private static final int WALKING = 1;
    private static final int WALKING_H = 42;
    private static final int WALKING_W = 33;

    private static final int JUMPING = 2;
    private static final int JUMPING_H = 56;
    private static final int JUMPING_W = 38;

    private static final int SCRATCHING = 3;
    private static final int SCRATCHING_H = 45;
    private static final int SCRATCHING_W = 60;

    private static final int LIGHTNING = 4;
    private static final int LIGHTNING_H = 42;
    private static final int LIGHTNING_W = 32;

    // deprecated
    // private static final int FALLING = 6;

    private final HashMap<String, AudioPlayer> sfx;

    public Player(TileMap tm) {

        super(tm);

        width = 32;
        height = 42;
        cwidth = 30;
        cheight = 30;

        moveSpeed = 0.3;
        maxSpeed = 1.6;
        stopSpeed = 0.4;
        fallSpeed = 0.15;
        maxFallSpeed = 4.0;
        jumpStart = -4.8;
        stopJumpSpeed = 0.3;

        facingRight = true;

        health = maxHealth = 5;
        fire = maxFire = 2500;

        fireCost = 1000;
        lightningDmg = 5;
        lightnings = new ArrayList<>();

        scratchDamage = 8;
        scratchRange = 40;

        // load sprites
        try {
            BufferedImage spritesheet = ImageIO.read(
                    Objects.requireNonNull(getClass().getResourceAsStream(
                            "/Sprites/Player/peebo.png"
                    ))
            );

            sprites = new ArrayList<>();
            for (int i = 0; i < 5; i++) {

                BufferedImage[] bi =
                        new BufferedImage[numFrames[i]];

                for(int j = 0; j < numFrames[i]; j++) {
                    if (i == IDLE) {
                        bi[j] = spritesheet.getSubimage(
                                0,
                                0,
                                IDLE_W,
                                IDLE_H
                        );
                    }
                    else if (i == WALKING) {
                        bi[j] = spritesheet.getSubimage(
                                j * WALKING_W,
                                IDLE_H,
                                WALKING_W,
                                WALKING_H
                        );
                    }
                    else if (i == JUMPING) {
                        bi[j] = spritesheet.getSubimage(
                                j * JUMPING_W,
                                IDLE_H + WALKING_H,
                                JUMPING_W,
                                JUMPING_H
                        );
                    }
                    else if (i == SCRATCHING) {
                        bi[j] = spritesheet.getSubimage(
                                j * SCRATCHING_W,
                                IDLE_H + WALKING_H + JUMPING_H,
                                SCRATCHING_W,
                                SCRATCHING_H
                        );
                    }
                    else if (i == LIGHTNING) { bi[j] = spritesheet.getSubimage(
                                j * LIGHTNING_W,
                                IDLE_H + WALKING_H + JUMPING_H + SCRATCHING_H,
                                LIGHTNING_W,
                                LIGHTNING_H
                        );
                    }
                }

                sprites.add(bi);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        animation = new Animation();
        currentAction = IDLE;
        animation.setFrames(sprites.get(WALKING));
        animation.setDelay(400);

        sfx = new HashMap<>();
        sfx.put("jump", new AudioPlayer("/SFX/jump.mp3"));
        sfx.put("scratch", new AudioPlayer("/SFX/scratch.mp3"));
        sfx.put("range", new AudioPlayer("/SFX/range.mp3"));

    }

    public int getHealth() {
        return health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getFire() {
        return fire;
    }

    public int getMaxFire() {
        return maxFire;
    }

    public void setFiring() {
        firing = true;
    }

    public void setScratching() {
        scratching = true;
    }

    public void checkAttack(ArrayList<Enemy> enemies) {

        // nge loop setiap enemy
        for (Enemy e : enemies) {

            // scratch atk
            if (scratching) {
                if (facingRight) {
                    if (
                            e.getx() > x &&
                                    e.getx() < x + scratchRange &&
                                    e.gety() > y - (height >> 1) &&
                                    e.gety() < y + (height >> 1)
                    ) {
                        e.hit(scratchDamage);
                    }
                } else {
                    if (
                            e.getx() < x &&
                                    e.getx() > x - scratchRange &&
                                    e.gety() > y - (height >> 1) &&
                                    e.gety() < y + (height >> 1)
                    ) {
                        e.hit(scratchDamage);
                    }
                }
            }

            // cek bila range atk kena
            for (Lightning lightning : lightnings) {
                if (lightning.intersects(e)) {
                    e.hit(lightningDmg);
                    lightning.setHit();
                    break;
                }
            }

            // check enemy collision
            if (intersects(e)) {
                hit(e.getDamage());
            }

        }

    }

    public void hit(int damage) {
        if (flinching) return;
        health -= damage;
        if (health < 0) health = 0;
        if (health == 0) dead = true;
        flinching = true;
        flinchTimer = System.nanoTime();
    }

    private void getNextPosition() {
        // movement
        if (left) {
            dx -= moveSpeed;
            if (dx < -maxSpeed) {
                dx = -maxSpeed;
            }
        } else if (right) {
            dx += moveSpeed;
            if (dx > maxSpeed) {
                dx = maxSpeed;
            }
        } else {
            if (dx > 0) {
                dx -= stopSpeed;
                if (dx < 0) {
                    dx = 0;
                }
            } else if (dx < 0) {
                dx += stopSpeed;
                if (dx > 0) {
                    dx = 0;
                }
            }
        }

        // pas nge atk gabisa gerak kecuali airborne
        if ((currentAction == SCRATCHING || currentAction == LIGHTNING) &&
                !(jumping || falling)) {
            dx = 0;
        }

        if (jumping && !falling) {
            sfx.get("jump").play();
            dy = jumpStart;
            falling = true;
        }

        if (falling) {
            dy += fallSpeed;
            if (dy > 0) jumping = false;
            if (dy < 0 && !jumping) dy += stopJumpSpeed;
            if (dy > maxFallSpeed) dy = maxFallSpeed;

        }

    }

    public void update() {

        // update position
        getNextPosition();
        checkTileMapCollision();
        setPosition(xtemp, ytemp);

        // kalo udah atk, di berhentiin
        if (currentAction == SCRATCHING) {
            if (animation.hasPlayedOnce()) scratching = false;
        }
        if (currentAction == LIGHTNING) {
            if (animation.hasPlayedOnce()) firing = false;
        }

        // lightning atk
        fire += 1;
        if (fire > maxFire) fire = maxFire;
        if (firing && currentAction != LIGHTNING) {
            if (fire > fireCost) {
                fire -= fireCost;
                Lightning lightning = new Lightning(tileMap, facingRight);
                lightning.setPosition(x, y - 10);
                lightnings.add(lightning);
            }
        }

        // update lightning
        for (int i = 0; i < lightnings.size(); i++) {
            lightnings.get(i).update();
            if (lightnings.get(i).shouldRemove()) {
                lightnings.remove(i);
                i--;
            }
        }

        // cek kedap kedip
        if (flinching) {
            long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
            if (elapsed > 1000) {
                flinching = false;
            }
        }

        // set animation
        if (scratching) {
            if (currentAction != SCRATCHING) {
                sfx.get("scratch").play();
                currentAction = SCRATCHING;
                animation.setFrames(sprites.get(SCRATCHING));
                animation.setDelay(50);
                width = SCRATCHING_W;
                height = SCRATCHING_H;
            }
        } else if (firing) {
            if (currentAction != LIGHTNING) {
                sfx.get("range").play();
                currentAction = LIGHTNING;
                animation.setFrames(sprites.get(LIGHTNING));
                animation.setDelay(100);
                height = LIGHTNING_H;
                width = LIGHTNING_W;
            }
        }
        else if (dy < 0) {
            if (currentAction != JUMPING) {
                currentAction = JUMPING;
                animation.setFrames(sprites.get(JUMPING));
                animation.setDelay(-1);
                width = JUMPING_W;
            }
        } else if (left || right) {
            if (currentAction != WALKING) {
                currentAction = WALKING;
                animation.setFrames(sprites.get(WALKING));
                animation.setDelay(40);
                width = WALKING_W;
                height = WALKING_H;
            }
        } else {
            if (currentAction != IDLE) {
                currentAction = IDLE;
                animation.setFrames(sprites.get(IDLE));
                animation.setDelay(400);
                width = IDLE_W;
            }
        }

        animation.update();

        // set direction
        if (currentAction != SCRATCHING && currentAction != LIGHTNING) {
            if (right) facingRight = true;
            if (left) facingRight = false;
        }

    }

    public void draw(Graphics2D g) {

        setMapPosition();

        // draw lightning
        for (Lightning lightning : lightnings) {
            lightning.draw(g);
        }

        // draw player
        if (flinching) {
            long elapsed =
                    (System.nanoTime() - flinchTimer) / 1000000;
            if (elapsed / 100 % 2 == 0) {
                return;
            }
        }

        super.draw(g);

    }
    // TODO: implement ini setelah class Level udh ada
    public void showHitbox(boolean flag) {
        hitboxFlag = flag;
    }
}
















