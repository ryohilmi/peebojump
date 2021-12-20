package Entity.Enemies;

import TileMap.TileMap;
import Entity.*;
import Entity.PlayerObject.*;

public class Enemy extends MapObject {

	protected int health;
	protected int maxHealth;
	protected boolean dead;
	protected int damage;
	protected int type;

	protected boolean flinching;
	protected long flinchTimer;

	// attack
	protected boolean attackRight;
	protected boolean attackLeft;
	protected int attackWidth;
	protected int attackDelay;
	protected int attackCounter;
	protected int attackAnimDelay;

	// idle
	protected boolean idle;
	protected int idleWidth;
	protected int idleTime;
	protected int idleCounter;
	protected int idleAnimDelay;

	// walk
	protected boolean walk;
	protected int walkWidth;
	protected int walkAnimDelay;
	protected int walkCounter;
	protected int walkMaxDistance;

	// enemy type
	public static final int LANDAK = 0;
	public static final int PLANT = 1;
	public static final int KADAL = 2;

	// enemy action
	protected static final int IDLE = 0;
	protected static final int ATTACK = 1;
	protected static final int WALK = 2;

	public Enemy(TileMap tm) {
		super(tm);
	}

	public boolean isDead() { return dead; }

	public int getDamage() { return damage; }

	public void hit(int damage) {
		if(dead || flinching) return;
		health -= damage;
		if(health < 0) health = 0;
		if(health == 0) dead = true;
		flinching = true;
		flinchTimer = System.nanoTime();
	}

	public int getType() { return type; }

	public void detectPlayer (Player player) {
		if (attackCounter == 0) {
			if (player.getx() <= x + (attackWidth / 2) && player.getx() > x) {
				attackRight = true;
				attackLeft = false;
				attackCounter++;
			} else if (player.getx() >= x - (attackWidth / 2) && player.getx() < x) {
				attackRight = false;
				attackLeft = true;
				attackCounter++;
			}
		}
		else {
			if (attackCounter < attackDelay) { attackCounter++;}
			else { attackCounter = 0; }
		}
	}

	public void idleBuffer() {
		if (idleCounter < idleTime) {
			idleCounter++;
		}
		else if (idleCounter == idleTime) {
			idle = false;
			walk = true;
			idleCounter = 0;
			if (right) {
				right = false;
				left = true;
				facingRight = false;
			} else if (left) {
				right = true;
				left = false;
				facingRight = true;
			}
		}
	}

	public void update() {}

}














