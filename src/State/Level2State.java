package State;

import Entity.Balloon;
import Entity.Enemies.Enemy;
import Entity.Enemies.Kadal;
import Entity.Enemies.Landak;
import Entity.Enemies.Plant;
import Entity.Explosion;
import Entity.HUD;
import Entity.PlayerObject.Player;
import Main.GamePanel;
import TileMap.*;
import Utility.SaveData;
import Utility.Time;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Level2State extends State {
    private TileMap tileMap;
    private Background bg;
    private HUD hud;

    private ArrayList<Enemy> enemies;
    private ArrayList<Explosion> explosions;

    private Player player;
    private Balloon balloon;

    private boolean show_hitbox;
    private final Time time;

    public Level2State(StateManager gsm) {
        this.stateManager = gsm;
        show_hitbox = false;
        time = new Time();
        init();
    }

    public void init() {
        tileMap = new TileMap(32);
        tileMap.loadTiles("/Tilesets/tilepeebo.png");
        tileMap.loadMap("/Maps/better2.map");
        tileMap.setPosition(0, 0);
        tileMap.setTween(1);

        bg = new Background("/Backgrounds/backgroundlevel.png");

        player = new Player(tileMap, time);
        player.setPosition(3000, 3025);

        hud = new HUD(player, time);

        populateEnemies();

        balloon = new Balloon(tileMap);
        balloon.setPosition(64, 96);
        time.start();
    }

    private void populateEnemies() {
        enemies = new ArrayList<Enemy>();

        // enemy spawn
        Landak l;
        Kadal k;
        Plant p;

        Point[] LandakPoint = new Point[] {
                new Point(736, 2976),
                new Point(192, 2880),
                new Point(288, 2400),
                new Point(96, 2176)
        };
        for(int i = 0; i < LandakPoint.length; i++) {
            l = new Landak(tileMap);
            l.setPosition(LandakPoint[i].x, LandakPoint[i].y);
            enemies.add(l);
        }

        Point[] KadalPoint = new Point[]{
                new Point(512, 2176),
                new Point(288, 1920),
                new Point(704, 1856),
                new Point(2752, 992)
        };

        for (int i = 0; i < KadalPoint.length; i++)
        {
            k = new Kadal(tileMap);
            k.setPosition(KadalPoint[i].x, KadalPoint[i].y);
            enemies.add(k);
        }

        Point[] PlantPoint = new Point[]{
                new Point(2912, 992),
                new Point(1984, 832),
                new Point(576, 352)
        };

        for (int i = 0; i < PlantPoint.length; i++)
        {
            p = new Plant(tileMap);
            p.setPosition(PlantPoint[i].x, PlantPoint[i].y);
            enemies.add(p);
        };
    }

    public void update() {
        player.showHitbox(show_hitbox);
        balloon.showHitbox(show_hitbox);
        // update player
        player.update();

        if (player.getHealth() <= 0) {
            stateManager.setState(StateManager.DEATHSTATE);
        }

        // update attack
        player.checkAttack(enemies);
        // update enemy
        for(int i = 0; i < enemies.size(); i++) {
            Enemy e = enemies.get(i);
            e.update();
            if(e.isDead()) {
                enemies.remove(i);
                i--;
                explosions.add(
                        new Explosion(e.getx(), e.gety()));
            }
            if(e.getType() != e.LANDAK){
                e.detectPlayer(player);
            }
        }
        // update balloon
        balloon.update();
        if(player.intersects(balloon)) {
            SaveData.writeHighScore(1, String.valueOf(time.getSecond())+"."+String.valueOf(time.getMilisecond()));
            stateManager.setState(StateManager.LEVEL3STATE);
        }
        tileMap.setPosition(
                (GamePanel.WIDTH >> 1) - player.getx(),
                (GamePanel.HEIGHT >> 1) - player.gety()
        );

        // set background
        bg.setPosition(tileMap.getx(), tileMap.gety());

    }

    public void draw(Graphics2D g) {

        // draw bg
        bg.draw(g);

        // draw tilemap
        tileMap.draw(g);

        // draw player
        player.draw(g);

        hud.draw(g);

        // draw balon
        balloon.draw(g);

        // draw enemies
        for(int i = 0; i < enemies.size(); i++) {
            enemies.get(i).draw(g);
        }
    }

    public void keyPressed(int k) {
        if(k == KeyEvent.VK_LEFT) player.setLeft(true);
        if(k == KeyEvent.VK_RIGHT) player.setRight(true);
//        if(k == KeyEvent.VK_UP) player.setUp(true);
        if(k == KeyEvent.VK_UP) player.setJumping(true);
        if(k == KeyEvent.VK_DOWN) player.setDown(true);
        if(k == KeyEvent.VK_W) player.setJumping(true);
        if(k == KeyEvent.VK_Z) player.setScratching();
        if(k == KeyEvent.VK_X) player.setFiring();
        if(k == KeyEvent.VK_H) show_hitbox = !show_hitbox;
    }

    public void keyReleased(int k) {
        if(k == KeyEvent.VK_LEFT) player.setLeft(false);
        if(k == KeyEvent.VK_RIGHT) player.setRight(false);
//        if(k == KeyEvent.VK_UP) player.setUp(false);
        if(k == KeyEvent.VK_UP) player.setJumping(false);
        if(k == KeyEvent.VK_DOWN) player.setDown(false);
        if(k == KeyEvent.VK_W) player.setJumping(false);
    }
}
