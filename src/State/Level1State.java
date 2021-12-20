package State;

import Entity.*;
import Entity.Enemies.*;
import Entity.PlayerObject.*;
import Entity.Balloon;
import Main.GamePanel;
import TileMap.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Level1State extends State {
    private TileMap tileMap;
    private Background bg;
    private HUD hud;

    private ArrayList<Enemy> enemies;

    private Player player;
    private Balloon balloon;

    private boolean show_hitbox;

    public Level1State(StateManager gsm) {
        this.stateManager = gsm;
        show_hitbox = false;
        init();
    }

    public void init() {
        tileMap = new TileMap(32);
        tileMap.loadTiles("/Tilesets/tilepeebo.png");
        tileMap.loadMap("/Maps/better1.map");
        tileMap.setPosition(0, 0);
        tileMap.setTween(1);

        bg = new Background("/Backgrounds/background.png");

        player = new Player(tileMap);
        player.setPosition(900, 2525);

        hud = new HUD(player);

        populateEnemies();

        balloon = new Balloon(tileMap);
        balloon.setPosition(3104, 160);
    }

    private void populateEnemies() {
        enemies = new ArrayList<Enemy>();

        // enemy spawn
        Landak l;
        Kadal k;
        Plant p;

        Point[] LandakPoint = new Point[] {
                new Point(2432, 2528),
                new Point(2816, 1664),
                new Point(2176, 1888)
        };
        for(int i = 0; i < LandakPoint.length; i++) {
            l = new Landak(tileMap);
            l.setPosition(LandakPoint[i].x, LandakPoint[i].y);
            enemies.add(l);
        }

        Point[] KadalPoint = new Point[]{
                new Point(1696, 1952),
                new Point(1312, 1856),
                new Point(704, 1856)
        };

        for (int i = 0; i < KadalPoint.length; i++)
        {
            k = new Kadal(tileMap);
            k.setPosition(KadalPoint[i].x, KadalPoint[i].y);
            enemies.add(k);
        }

        Point[] PlantPoint = new Point[]{
                new Point(1376, 1088),
                new Point(2528, 768)
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
        // update balloon
        balloon.update();
        if(player.intersects(balloon)) {
            // TODO: game end
            player.hit(100);
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
