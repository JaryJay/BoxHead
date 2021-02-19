package data.map;

import processing.core.PApplet;

public class WallTile extends Tile {

	public WallTile(int posX, int posY) {
		super(true, posX, posY);
	}

	@Override
	public void displayLayer1(PApplet p) {
		p.fill(158, 158, 158);
		p.stroke(149, 149, 149);
		p.rect(posX * TILE_SIZE, posY * TILE_SIZE, TILE_SIZE, TILE_SIZE);
	}

	@Override
	public void displayLayer2(PApplet p) {
		p.fill(219, 219, 219);
		p.stroke(191, 191, 191);
		p.rect(posX * TILE_SIZE, posY * TILE_SIZE - TILE_SIZE * 0.3f, TILE_SIZE, TILE_SIZE);
	}

}
