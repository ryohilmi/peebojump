package Entity.PlayerObject;

import Entity.Animation;
import Entity.MapObject;
import TileMap.TileMap;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Objects;

public class Lightning extends MapObject {

	private int positionVault;
	private boolean hit = false;
	private boolean remove;
	private BufferedImage[] sprites;

	public Lightning(TileMap tm, boolean playerFacingRight) {

		super(tm);

		// position
		positionVault = 100;

		// size
		width = CONSTWIDTH * 9;
		height = 64;

		// coliision size
		mincwidth = 10;
		maxcwidth = CONSTWIDTH * 4 + 12;
		cwidth = mincwidth;
		cheight = 36;
		stretchSpeed = 5;
		stretchDone = false;

		// direction
		if(playerFacingRight) {
			facingRight = true;
			right = true;
			left = false;
			x += positionVault;
		}
		else {
			facingRight = false;
			right = false;
			left = true;
			x -= positionVault;
		}



		// load sprites
		try {
			BufferedImage spritesheet = ImageIO.read(
					Objects.requireNonNull(getClass().getResourceAsStream(
							"/Sprites/Player/lightning.png"
					))
			);

			sprites = new BufferedImage[19];
			for(int i = 0; i < sprites.length; i++) {
				sprites[sprites.length - 1 - i] = spritesheet.getSubimage(
						i * width,
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

	public void setHit() { hit = true; }

	public boolean shouldRemove() { return remove; }

	public void update() {
		checkTileMapCollision();
		int collide_r = tileMap.getType((int)((y - cheight / 2) / tileSize) + 1, (int)(x + cwidth) / tileSize);
		int collide_l = tileMap.getType(((int)(y - cheight / 2) / tileSize) + 1, (int)((x - cwidth) / tileSize));
		if (facingRight) {
			if(collide_r == 1)
				remove = true;
		} else {
			if (collide_l == 1)
				remove = true;
		}
		setPosition(xtemp, ytemp);

		if (!stretchDone) {
			stretchCollision();
		}

		animation.update();
		if (animation.hasPlayedOnce() || hit || collided_with_tile) {
			remove = true;
			dx = 0;
			cwidth = 0;
			cheight = 0;
		}
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

