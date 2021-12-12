package Entity.Player;

import Entity.Animation;
import Entity.MapObject;
import TileMap.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Objects;

public class Lightning extends MapObject {

	private int expires = -35;
	private boolean hit;
	private boolean remove;
	private BufferedImage[] sprites;

	public Lightning(TileMap tm, boolean right) {
		
		super(tm);
		
		facingRight = right;
		
		moveSpeed = 2.5;
		if(right) dx = moveSpeed;
		else dx = -moveSpeed;
		
		width = 80;
		height = 64;
		cwidth = 30;
		cheight = 20;
		// load sprites
		try {
			BufferedImage spritesheet = ImageIO.read(
					Objects.requireNonNull(getClass().getResourceAsStream(
							"/Sprites/Player/lightning.gif"
					))
			);

			sprites = new BufferedImage[16];
			for(int i = 0; i < sprites.length; i++) {
				sprites[i] = spritesheet.getSubimage(
					i * 100,
					0,
					width,
					height
				);
			}

			animation = new Animation();
			animation.setFrames(sprites);
			animation.setDelay(35);
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void setHit() {
		if(hit) return;
		hit = true;
		dx = 0;
	}
	
	public boolean shouldRemove() { return remove; }
	
	public void update() {
		
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		
		if(dx == 0 && !hit) {
			setHit();
		}
		
		animation.update();
		if (animation.hasPlayedOnce()) {
			remove = true;
		}

		// buat nentuin apakah lightning expired (setelah 35 frame)
		if( expires == 1 || hit) {
			dx = 0;
			cwidth = 0;
			cheight = 0;
		}
		expires++;
	}
	
	public void draw(Graphics2D g) {
		setMapPosition();
		super.draw(g);
	}

	// TODO: implement ini setelah class Level udh ada
	public void showHitbox(boolean flag) {
		hitboxFlag = flag;
	}
}


















