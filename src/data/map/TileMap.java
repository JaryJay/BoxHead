package data.map;

import java.util.ArrayList;
import java.util.List;

import data.entity.EntityGroup;
import data.entity.GameEntity;
import processing.core.PApplet;

public class TileMap {

	private Tile[][] tiles;
	private EntityGroup[][] entityGroups;

	public TileMap() {
		int[][] tileNums = {

				{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },

				{ 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 },

				{ 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 },

				{ 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 },

				{ 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 0, 0, 0, 0, 1 },

				{ 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 1 },

				{ 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1 },

				{ 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 1, 0, 1 },

				{ 1, 0, 0, 0, 1, 1, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 1 },

				{ 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 1, 0, 1 },

				{ 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1 },

				{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 } };

		tiles = new Tile[tileNums.length][tileNums[0].length];
		entityGroups = new EntityGroup[tileNums.length][tileNums[0].length];

		for (int i = 0; i < tileNums.length; i++) {
			for (int j = 0; j < tileNums[i].length; j++) {
				entityGroups[i][j] = new EntityGroup();
				switch (tileNums[i][j]) {
					case 0:
						tiles[i][j] = new GroundTile(j, i);
						break;
					case 1:
						tiles[i][j] = new WallTile(j, i);
						break;
					default:
						break;
				}
			}
		}
	}

	public void bindEntities(List<GameEntity> entities) {
		entityGroups = new EntityGroup[entityGroups.length][entityGroups[0].length];
		for (GameEntity entity : entities) {
			int tilePosX = (int) (entity.getPosition().x / Tile.TILE_SIZE);
			int tilePosY = (int) (entity.getPosition().y / Tile.TILE_SIZE);
			if (entityGroups[tilePosY][tilePosX] == null) {
				EntityGroup group = new EntityGroup();
				group.add(entity);
				entityGroups[tilePosY][tilePosX] = group;
			} else {
				entityGroups[tilePosY][tilePosX].add(entity);
			}
		}
	}

	public void displayLayer1(PApplet p) {
		for (int i = 0; i < tiles.length; i++) {
			for (int j = 0; j < tiles[i].length; j++) {
				tiles[i][j].displayLayer1(p);
			}
		}
	}

	public void displayLayer2(PApplet p) {
		for (int i = 0; i < tiles.length; i++) {
			for (int j = 0; j < tiles[i].length; j++) {
				tiles[i][j].displayLayer2(p);
			}
		}
	}

	public Tile[][] getTiles() {
		return tiles;
	}

	public EntityGroup[][] getEntityGroups() {
		return entityGroups;
	}

	public List<Tile> getNeighbours(Tile tile) {
		List<Tile> neighbours = new ArrayList<>();
		int tileX = tile.getPosX();
		int tileY = tile.getPosY();
		if (isValidTile(tileX, tileY - 1) && !tiles[tileY - 1][tileX].isObstacle()) {
			neighbours.add(tiles[tileY - 1][tileX]);
		}
		if (isValidTile(tileX + 1, tileY) && !tiles[tileY][tileX + 1].isObstacle()) {
			neighbours.add(tiles[tileY][tileX + 1]);
		}
		if (isValidTile(tileX, tileY + 1) && !tiles[tileY + 1][tileX].isObstacle()) {
			neighbours.add(tiles[tileY + 1][tileX]);
		}
		if (isValidTile(tileX - 1, tileY) && !tiles[tileY][tileX - 1].isObstacle()) {
			neighbours.add(tiles[tileY][tileX - 1]);
		}
		return neighbours;
	}

	private boolean isValidTile(int x, int y) {
		return x >= 0 && y >= 0 && x < tiles[y].length && y < tiles.length;
	}

}
