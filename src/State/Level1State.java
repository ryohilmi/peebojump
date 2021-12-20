package State;

import Entity.HUD;
import Entity.PlayerObject.*;
import Main.GamePanel;
import TileMap.Background;
import TileMap.TileMap;

import java.awt.*;
import java.awt.event.KeyEvent;

public class Level1State extends State {
    private TileMap tileMap;
    private Background bg;
    private HUD hud;

    private Player player;

    public Level1State(StateManager gsm) {
        this.stateManager = gsm;
        init();
    }

    public void init() {
        tileMap = new TileMap(32);
        tileMap.loadTiles("/Tilesets/tile.png");
        tileMap.loadMap("/Maps/temp.map");
        tileMap.setPosition(0, 0);
        tileMap.setTween(1);

		/* tileMap = new TileMap(30);
		tileMap.loadTiles("/Tilesets/grasstileset.gif");
		tileMap.loadMap("/Maps/level1-1.map");
		tileMap.setPosition(0, 0);
		tileMap.setTween(1); */

        bg = new Background("/Backgrounds/background.png");

        player = new Player(tileMap);
        player.setPosition(50, 0);

        hud = new HUD(player);

        populateEnemies();

    }

    private void populateEnemies() {

    }

    public void update() {

        // update player
        player.update();
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
