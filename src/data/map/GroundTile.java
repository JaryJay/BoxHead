package data.map;

import processing.core.PApplet;

public class GroundTile extends Tile {

	public GroundTile(int posX, int posY) {
		super(false, posX, posY);
	}

	@Override
	public void displayLayer1(PApplet p) {
		p.fill(184, 184, 184);
		p.stroke(170, 170, 170);
		p.rect(posX * TILE_SIZE, posY * TILE_SIZE, TILE_SIZE, TILE_SIZE);
	}

	@Override
	public void displayLayer2(PApplet p) {
	}

}
