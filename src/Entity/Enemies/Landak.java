package Entity.Enemies;

import Entity.Animation;
import TileMap.TileMap;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Landak extends Enemy {

	// sprites
	private BufferedImage[] sprites;

	public Landak(TileMap tm) {

		super(tm);
		time_weight = 4;
		type = LANDAK;

		// movement
		moveSpeed = 0.3;
		maxSpeed = 0.3;
		fallSpeed = 0.2;
		maxFallSpeed = 10.0;

		// size
		width = CONSTWIDTH;
		height = 32;

		// collision size
		cwidth = 24;
		cheight = 20;

		// walk
		walkAnimDelay = 110;

		// health
		health = maxHealth = 2;
		damage = 1;

		// load sprites
		try {

			BufferedImage spritesheet = ImageIO.read(
					getClass().getResourceAsStream(
							"/Sprites/Enemies/landak.png"
					)
			);

			sprites = new BufferedImage[7];
			for (int i = 0; i < sprites.length; i++) {
				sprites[sprites.length - 1 - i] = spritesheet.getSubimage(
						i * width,
						0,
						width,
						height
				);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		walk = true;
		currentAction = WALK;
		animation = new Animation();
		animation.setFrames(sprites);
		animation.setDelay(walkAnimDelay);

		right = true;
		facingRight = true;

	}

	private void getNextPosition () {

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

		// check flinching
		if (flinching) {
			long elapsed =
					(System.nanoTime() - flinchTimer) / 1000000;
			if (elapsed > 400) {
				flinching = false;
			}
		}

		// check wall
		if (right && dx == 0) {
			right = false;
			left = true;
			facingRight = false;
		} else if (left && dx == 0) {
			right = true;
			left = false;
			facingRight = true;
		}

		// update animation
		animation.update();

	}

	public void draw (Graphics2D g){

		//if(notOnScreen()) return;

		setMapPosition();

		super.draw(g);

	}
}























