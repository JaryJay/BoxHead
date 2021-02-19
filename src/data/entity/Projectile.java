package data.entity;

import java.util.ArrayList;
import java.util.List;

import data.coordinates.Vector2f;
import data.map.Tile;
import data.map.TileMap;
import processing.core.PApplet;

public class Projectile extends GameEntity {

	private int damage;
	private boolean pierce;

	private List<GameEntity> hitEnemies = new ArrayList<>();

	public Projectile(EntityType type, PApplet p, Vector2f position, float radius, int r, int g, int b, float speed, int damage, int lifetime, boolean pierce) {
		super(type, p, position, radius, r, g, b, speed, lifetime);
		this.damage = damage;
		this.pierce = pierce;
	}
	
	@Override
	public void update(TileMap t, List<GameEntity> entities) {
		super.update(t, entities);
		currentHealth--;
	}

	@Override
	protected void updateMapCollision(TileMap t) {
		int tileSize = Tile.TILE_SIZE;
		Tile[][] tiles = t.getTiles();
		int tileCoordinateX = (int) (position.x) / tileSize;
		int tileCoordinateY = (int) (position.y) / tileSize;
		for (int i = Math.max(0, tileCoordinateY - 1); i <= Math.min(tileCoordinateY + 1, tiles.length - 1); i++) {
			for (int j = Math.max(0, tileCoordinateX - 1); j <= Math.min(tileCoordinateX + 1, tiles[i].length - 1); j++) {
				if (tiles[i][j].isObstacle()) {
					// TODO: delete this projectile
				}
			}
		}
	}

	@Override
	protected void updateEntityCollision(List<GameEntity> entities) {
		for (GameEntity entity : entities) {
			if (entity != this && !hitEnemies.contains(entity)) {
				Vector2f thisToEntity = entity.position.copy().subtract(position);
//				System.out.println(entity + " " + radius + " " + entity.radius + " " + thisToEntity.length());
				float radiusSum = radius + entity.radius;
				if (thisToEntity.lengthSquared() < radiusSum * radiusSum) {
					entity.setCurrentHealth(entity.getCurrentHealth() - damage);
					hitEnemies.add(entity);
					System.out.println("aw4");
					if (!pierce) {
						setMarkedForRemoval(true);
					}
				}
			}
		}
	}

}
