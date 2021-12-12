package Entity;

import TileMap.TileMap;

public class Enemy extends MapObject {

	protected int health;
	protected int maxHealth;
	protected boolean dead;
	protected int damage;
	protected int type;

	protected boolean flinching;
	protected long flinchTimer;

	// attack
	protected boolean attack;
	protected int attackWidth;
	protected int attackDelay;
	protected int attackCounter;

	// idle
	protected boolean idle;
	protected int idleWidth;
	protected int idleTime;
	protected int idleCounter;

	// walk
	protected boolean walk;
	protected int walkWidth;

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
				attack = true;
				right = true;
				facingRight = true;
				attackCounter++;
			} else if (player.getx() >= x - (attackWidth / 2) && player.getx() < x) {
				attack = true;
				left = true;
				facingRight = false;
				attackCounter++;
			}
		}
		else {
			if (attackCounter < attackDelay) { attackCounter++; }
			else { attackCounter = 0; }
		}
	}

	public void idleBuffer() {
		if (idleCounter < idleTime) {
			idleCounter++;
		}
		else if (idleCounter == idleTime) {
			idle = false;
			idleCounter = 0;
			if (right && dx == 0) {
				right = false;
				left = true;
				facingRight = false;
			} else if (left && dx == 0) {
				right = true;
				left = false;
				facingRight = true;
			}
		}
	}

	public void update() {}

}














