package data.entity;

import java.util.List;

import data.coordinates.Vector2f;
import data.map.Tile;
import data.map.TileMap;
import processing.core.PApplet;

public abstract class GameEntity {

	public final static float SQRT_TWO_OVER_TWO = 0.70710678118f;

	protected EntityType type;

	public EntityType getType() {
		return type;
	}

	public void setType(EntityType type) {
		this.type = type;
	}

	protected Vector2f position;
	protected float radius;
	protected int r;
	protected int g;
	protected int b;
	protected float speed;
	protected int dirX;
	protected int dirY;
	protected int currentHealth;
	protected int maxHealth;

	private boolean markedForRemoval;

	protected PApplet p;

	public GameEntity(EntityType type, PApplet p, Vector2f position, float radius, int r, int g, int b, float speed, int maxHealth) {
		this.type = type;
		this.p = p;
		this.position = position;
		this.radius = radius;
		this.r = r;
		this.g = g;
		this.b = b;
		this.speed = speed;
		this.dirX = 0;
		this.dirY = 0;
		this.currentHealth = maxHealth;
		this.maxHealth = maxHealth;

		markedForRemoval = false;
	}

	public void display() {
		p.fill(r, g, b);
		p.stroke(0);
		p.ellipse(position.x, position.y, 2 * radius, 2 * radius);
	}

	public void update(TileMap t, List<GameEntity> entities) {
		updatePosition();
		checkForRemoval();
		updateMapCollision(t);
		updateEntityCollision(entities);
	}

	protected void updatePosition() {
		if (Math.abs(dirX) + Math.abs(dirY) == 2) {
			position.x += dirX * speed * SQRT_TWO_OVER_TWO;
			position.y += dirY * speed * SQRT_TWO_OVER_TWO;
		} else {
			position.x += dirX * speed;
			position.y += dirY * speed;
		}
	}

	protected void checkForRemoval() {
		if (currentHealth <= 0) {
			markedForRemoval = true;
		}
	}

	protected void updateMapCollision(TileMap t) {
		int tileSize = Tile.TILE_SIZE;
		Tile[][] tiles = t.getTiles();
		int tileCoordinateX = (int) (position.x) / tileSize;
		int tileCoordinateY = (int) (position.y) / tileSize;
		for (int i = Math.max(0, tileCoordinateY - 1); i <= Math.min(tileCoordinateY + 1, tiles.length - 1); i++) {
			for (int j = Math.max(0, tileCoordinateX - 1); j <= Math.min(tileCoordinateX + 1, tiles[i].length - 1); j++) {
				if (!tiles[i][j].isObstacle()) {
					continue;
				}
				Vector2f closestPoint = new Vector2f(Math.max((j + 0.1f) * tileSize, Math.min((j + 0.9f) * tileSize, position.x)), Math.max((i + 0.1f) * tileSize, Math.min((i + 0.9f) * tileSize, position.y)));
				Vector2f closestPointToCenterOfEntity = position.copy().subtract(closestPoint);
				if (closestPointToCenterOfEntity.lengthSquared() < radius * radius) {
					position.set(closestPoint.add(closestPointToCenterOfEntity.normalize().scale(radius)));
				}
			}
		}
	}

	protected void updateEntityCollision(List<GameEntity> entities) {
		for (GameEntity entity : entities) {
			if (entity != this && !(entity instanceof Projectile)) {
				Vector2f thisToEntity = entity.position.copy().subtract(position);
				float radiusSum = radius + entity.radius;
				if (thisToEntity.lengthSquared() < radiusSum * radiusSum) {
					float length = thisToEntity.length();
					float fraction = radius * radius / (radius * radius + entity.radius * entity.radius);
					float overlapLength = radiusSum - length;
					Vector2f pushVector = thisToEntity.copy().invert().normalize().scale(overlapLength * (1 - fraction));
					Vector2f entityPushVector = thisToEntity.copy().normalize().scale(overlapLength * fraction);
					position.add(pushVector);
					entity.position.add(entityPushVector);
				}
			}
		}
	}

	public void setDirection(int x, int y) {
		dirX = x;
		dirY = y;
	}

	public void north() {
		dirY--;
	};

	public void east() {
		dirX++;
	};

	public void south() {
		dirY++;
	};

	public void west() {
		dirX--;
	};

	public Vector2f getPosition() {
		return position;
	}

	public void setPosition(Vector2f position) {
		this.position = position;
	}

	public float getRadius() {
		return radius;
	}

	public void setRadius(float radius) {
		this.radius = radius;
	}

	public int getCurrentHealth() {
		return currentHealth;
	}

	public void setCurrentHealth(int currentHealth) {
		this.currentHealth = currentHealth;
	}

	public int getMaxHealth() {
		return maxHealth;
	}

	public void setMaxHealth(int maxHealth) {
		this.maxHealth = maxHealth;
	}

	public boolean isMarkedForRemoval() {
		return markedForRemoval;
	}

	public void setMarkedForRemoval(boolean markedForRemoval) {
		this.markedForRemoval = markedForRemoval;
	}

}
