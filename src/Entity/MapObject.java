package Entity;

import Main.GamePanel;
import TileMap.TileMap;
import TileMap.Tile;

import java.awt.Rectangle;

public abstract class MapObject {

	protected boolean hitboxFlag = false; // TODO: remove pas production
	// tile stuff
	protected TileMap tileMap;
	protected int tileSize;
	protected double xmap;
	protected double ymap;

	// position and vector
	protected double x;
	protected double y;
	protected double dx;
	protected double dy;

	// dimensions
	protected static final int CONSTWIDTH = 32;
	protected int width;
	protected int height;

	// collision box
	protected int cwidth;
	protected int cheight;
	protected int mincwidth;
	protected int maxcwidth;

	// collision
	protected int currRow;
	protected int currCol;
	protected double xdest;
	protected double ydest;
	protected double xtemp;
	protected double ytemp;
	protected int xinverse;
	protected boolean topLeft;
	protected boolean topRight;
	protected boolean bottomLeft;
	protected boolean bottomRight;
	protected boolean bottomLeftThrough;
	protected boolean bottomRightThrough;

	// stretching collision
	protected boolean stretchDone;
	protected int stretchSpeed;

	// animation
	protected Animation animation;
	protected int currentAction;
	protected int previousAction;
	protected boolean facingRight;

	// movement
	protected boolean left;
	protected boolean right;
	protected boolean up;
	protected boolean down;
	protected boolean jumping;
	protected boolean falling;

	// movement attributes
	protected double moveSpeed;
	protected double maxSpeed;
	protected double stopSpeed;
	protected double fallSpeed;
	protected double maxFallSpeed;
	protected double jumpStart;
	protected double stopJumpSpeed;

	protected int modify_rectangle_x = 0;
	protected int modify_rectangle_y = 0;

	// constructor
	public MapObject(TileMap tm) {
		tileMap = tm;
		tileSize = tm.getTileSize();
	}
	public void setRectModifier(int x, int y) {
		modify_rectangle_x = x;
		modify_rectangle_y = y;
	}

	public boolean intersects(MapObject o) {
		Rectangle r1 = getRectangle();
		Rectangle r2 = o.getRectangle();
		return r1.intersects(r2);
	}

	public Rectangle getRectangle() {
		if(facingRight && right) {
			return new Rectangle((int)(xtemp + xmap) - (15 + modify_rectangle_x), (int)(ytemp + ymap) - (10 + modify_rectangle_y), cwidth, cheight);
		}

		if(!facingRight && left) {
			return new Rectangle((int)(xinverse + xtemp + xmap) - (15 + modify_rectangle_x), (int)(ytemp + ymap) - (15 + modify_rectangle_x), cwidth, cheight);
		}

		return new Rectangle((int)(xtemp + xmap) - (15 + modify_rectangle_x), (int)(ytemp + ymap) - (10 + modify_rectangle_y), cwidth, cheight);
	}

	public void calculateCorners(double x, double y) {

		int leftTile = (int)(x - cwidth / 2) / tileSize;
		int rightTile = (int)(x + cwidth / 2 - 1) / tileSize;
		int topTile = (int)(y - cheight / 2) / tileSize;
		int bottomTile = (int)(y + cheight / 2 - 1) / tileSize;

		int tl = tileMap.getType(topTile, leftTile);
		int tr = tileMap.getType(topTile, rightTile);
		int bl = tileMap.getType(bottomTile, leftTile);
		int br = tileMap.getType(bottomTile, rightTile);

		topLeft = tl == Tile.BLOCKED;
		topRight = tr == Tile.BLOCKED;
		bottomLeft = bl == Tile.BLOCKED;
		bottomRight = br == Tile.BLOCKED;

	}

	public void calculateBottom(double x, double y) {
		int leftTile = (int)(x - cwidth / 2) / tileSize;
		int rightTile = (int)(x + cwidth / 2 - 1) / tileSize;
		int bottomTile = (int)(y + cheight / 2) / tileSize;

		int bl = tileMap.getType(bottomTile, leftTile);
		int br = tileMap.getType(bottomTile, rightTile);

		bottomLeftThrough = bl == Tile.THROUGH;
		bottomRightThrough = br == Tile.THROUGH;
	}

	public void checkTileMapCollision() {

		currCol = (int)x / tileSize;
		currRow = (int)y / tileSize;

		xdest = x + dx;
		ydest = y + dy;

		xtemp = x;
		ytemp = y;

		calculateCorners(x, ydest);
		calculateBottom(x, ydest);

		if(dy < 0) {
			if(topLeft || topRight) {
				dy = 0;
				ytemp = currRow * tileSize + cheight / 2;
			}
			else {
				ytemp += dy;
			}
		}
		if(dy > 0) {
			if(bottomLeft || bottomRight || bottomLeftThrough || bottomRightThrough) {
				dy = 0;
				falling = false;
				ytemp = (currRow + 1) * tileSize - cheight / 2;
			}
			else {
				ytemp += dy;
			}
		}

		calculateCorners(xdest, y);
		calculateBottom(xdest, y);

		if(dx < 0) {
			if(topLeft || bottomLeft) {
				dx = 0;
				xtemp = currCol * tileSize + cwidth / 2;
			}
			else {
				xtemp += dx;
			}
		}
		if(dx > 0) {
			if(topRight || bottomRight) {
				dx = 0;
				xtemp = (currCol + 1) * tileSize - cwidth / 2;
			}
			else {
				xtemp += dx;
			}
		}

		if(!falling) {
			calculateCorners(x, ydest + 1);
			calculateBottom(x, ydest + 1);
			if(!bottomLeft && !bottomRight && !bottomLeftThrough && !bottomRightThrough) {
				falling = true;
			}
		}

	}

	public void stretchCollision () {
		if(facingRight && right) {
			if (cwidth < maxcwidth) cwidth += stretchSpeed;
			else if (cwidth >= maxcwidth) {
				cwidth = mincwidth;
				stretchDone = true;
			}
		}
		else if(!facingRight && left) {
			if (cwidth < maxcwidth) {
				cwidth += stretchSpeed;
				xinverse -= stretchSpeed;
			}
			else if (cwidth >= maxcwidth) {
				cwidth = mincwidth;
				xinverse = 0;
				stretchDone = true;
			}
		}
	}

	public int getx() { return (int)x; }
	public int gety() { return (int)y; }
	public int getWidth() { return width; }
	public int getHeight() { return height; }
	public int getCWidth() { return cwidth; }
	public int getCHeight() { return cheight; }

	public void setPosition(double x, double y) {
		this.x = x;
		this.y = y;
	}
	public void setVector(double dx, double dy) {
		this.dx = dx;
		this.dy = dy;
	}

	public void setMapPosition() {
		xmap = tileMap.getx();
		ymap = tileMap.gety();
	}

	public void setLeft(boolean b) { left = b; }
	public void setRight(boolean b) { right = b; }
	public void setUp(boolean b) { up = b; }
	public void setDown(boolean b) { down = b; }
	public void setJumping(boolean b) { jumping = b; }

	public boolean notOnScreen() {
		return x + xmap + width < 0 ||
				x + xmap - width > GamePanel.WIDTH ||
				y + ymap + height < 0 ||
				y + ymap - height > GamePanel.HEIGHT;
	}

	public void draw(java.awt.Graphics2D g) {
		// TODO: remove buat production
		if (hitboxFlag) {
			Rectangle rectangle = getRectangle();
			g.drawRect((int)rectangle.getX(), (int)rectangle.getY(), (int)rectangle.getWidth(), (int)rectangle.getHeight());
		}
		if(facingRight) {
			g.drawImage(
					animation.getImage(),
					(int)(x + xmap - width / 2),
					(int)(y + ymap - height / 2),
					null
			);
		}
		else {
			g.drawImage(
					animation.getImage(),
					(int)(x + xmap - width / 2 + width),
					(int)(y + ymap - height / 2),
					-width,
					height,
					null
			);
		}
	}

}
















