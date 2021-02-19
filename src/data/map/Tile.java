package data.map;

import processing.core.PApplet;

public abstract class Tile {

	public static final int TILE_SIZE = 40;

	private boolean obstacle;
	protected int posX;
	protected int posY;

	public Tile(boolean obstacle, int posX, int posY) {
		super();
		this.obstacle = obstacle;
		this.posX = posX;
		this.posY = posY;
	}

	public abstract void displayLayer1(PApplet p);

	public abstract void displayLayer2(PApplet p);

	public boolean isObstacle() {
		return obstacle;
	}

	public void setObstacle(boolean obstacle) {
		this.obstacle = obstacle;
	}

	public int getPosX() {
		return posX;
	}

	public int getPosY() {
		return posY;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + " at (" + posX + ", " + posY + ")";
	}

}
