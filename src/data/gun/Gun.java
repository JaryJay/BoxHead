package data.gun;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import data.coordinates.Vector2f;
import data.entity.GameEntity;
import data.entity.Projectile;
import data.map.TileMap;
import processing.core.PApplet;

public class Gun {

	private int maxAmmo;
	private int ammo;
	private int damage;
	private int shootSpeed;
	private int cooldown;
	private float range;
	private float spread;
	private boolean pierce;
	private boolean hitscan;
	private Float projectileRadius = 5f;
	private Float projectileSpeed = 5f;
	private Integer projectileLifetime = 100;
	private int dirX = 1;
	private int dirY = 0;

	public Gun(int maxAmmo, int damage, int shootSpeed, float range, float spread, boolean pierce, boolean hitscan) {
		super();
		this.maxAmmo = maxAmmo;
		this.ammo = maxAmmo;
		this.damage = damage;
		this.shootSpeed = shootSpeed;
		this.cooldown = 0;
		this.range = range;
		this.spread = spread;
		this.pierce = pierce;
		this.hitscan = hitscan;
	}

	public void reload() {
		ammo = maxAmmo;
		cooldown = 10;
	}

	public void shoot(Vector2f position, float radius, List<GameEntity> entities, TileMap tileMap, PApplet p) {
		if (cooldown == 0 && ammo > 0) {
			if (hitscan) {
				if (pierce) {
				} else {
					Map<Float, GameEntity> distanceToEntity = getDistanceToEntityMap(position, radius, entities);
					if (distanceToEntity.isEmpty()) {
						displayBullet(position, radius, range, p);
					} else {
						shootBullet(position, radius, distanceToEntity, tileMap, p);
					}
				}
			} else {
				shootProjectileBullet(position, radius, entities, p);
			}
			ammo--;
			cooldown = shootSpeed;
		}
	}

	private void shootProjectileBullet(Vector2f position, float radius, List<GameEntity> entities, PApplet p) {
		float vx = dirX;
		float vy = dirY;
		if (Math.abs(vx) + Math.abs(vy) == 2) {
			vx *= GameEntity.SQRT_TWO_OVER_TWO;
			vy *= GameEntity.SQRT_TWO_OVER_TWO;
		}
		Vector2f g = position.copy().add(Vector2f.fromLengthAngle(radius + projectileRadius, (float) (Math.atan2(vy, vx) + Math.PI / 4)));
		Projectile projectile = new Projectile(null, p, g, projectileRadius, 0, 0, 0, projectileSpeed, 10, projectileLifetime, false);
		projectile.setDirection(dirX, dirY);
		entities.add(projectile);
	}

	private void shootBullet(Vector2f position, float radius, Map<Float, GameEntity> distanceToEntity, TileMap tileMap, PApplet p) {
		float distance = Float.MAX_VALUE;
		for (Float f : distanceToEntity.keySet()) {
			if (f < 0) {
				continue;
			}
			distance = Math.min(distance, f);
		}
		displayBullet(position, radius, distance + 20, p);

		GameEntity e = distanceToEntity.get(distance);
//			System.out.println(e);
		if (e != null) {
			float vx = dirX;
			float vy = dirY;
			if (Math.abs(vx) + Math.abs(vy) == 2) {
				vx *= GameEntity.SQRT_TWO_OVER_TWO;
				vy *= GameEntity.SQRT_TWO_OVER_TWO;
			}
			e.setCurrentHealth(e.getCurrentHealth() - damage);
			e.getPosition().add(new Vector2f(vx, vy).scale(damage));
		} else {
			displayBullet(position, radius, range, p);
		}
	}

	private Map<Float, GameEntity> getDistanceToEntityMap(Vector2f position, float radius, List<GameEntity> entities) {
		Map<Float, GameEntity> result = new HashMap<>();
		for (GameEntity e : entities) {
			Float distance = getDistance(position, radius, e);
			if (position.equals(e.getPosition()) || distance == null) {
				continue;
			}
			result.put(distance, e);
		}
		return result;
	}

	private Float getDistance(Vector2f position, float radius, GameEntity e) {
		float vx = dirX;
		float vy = dirY;
		if (Math.abs(vx) + Math.abs(vy) == 2) {
			vx *= GameEntity.SQRT_TWO_OVER_TWO;
			vy *= GameEntity.SQRT_TWO_OVER_TWO;
		}
		Vector2f g = position.copy().add(Vector2f.fromLengthAngle(radius, (float) (Math.atan2(vy, vx) + Math.PI / 4)));
		float cx = e.getPosition().x;
		float cy = e.getPosition().y;
		float r = e.getRadius();
		float b = 2 * (g.x * vx + g.y * vy - cx * vx - cy * vy);
		float c = g.x * g.x + g.y * g.y + cx * cx + cy * cy - 2 * g.x * cx - 2 * g.y * cy - r * r;
		float discriminant = b * b - 4 * c;
		if (discriminant >= 0) {
			return (float) ((-b - Math.sqrt(discriminant)) / 2);
		}
		return null;
	}

	private void displayBullet(Vector2f position, float radius, float distance, PApplet p) {
		p.pushMatrix();
		p.translate(position.x, position.y);
		p.rotate((float) Math.atan2(dirY, dirX));
		p.stroke(255);
//		System.out.println(dirX + " " + dirY);
		p.line(radius * GameEntity.SQRT_TWO_OVER_TWO + 10, radius * GameEntity.SQRT_TWO_OVER_TWO + 2, distance, radius * GameEntity.SQRT_TWO_OVER_TWO + 2);
		p.popMatrix();
	}

	public void display(Vector2f position, float radius, PApplet p) {
		p.pushMatrix();
		p.translate(position.x, position.y);
		p.rotate((float) Math.atan2(dirY, dirX));
		p.fill(50, 55, 56);
		p.stroke(0);
		p.rect(radius * GameEntity.SQRT_TWO_OVER_TWO - 10, radius * GameEntity.SQRT_TWO_OVER_TWO, 20, 5);
		p.popMatrix();
	}

	public int getCooldown() {
		return cooldown;
	}

	public void setCooldown(int cooldown) {
		this.cooldown = cooldown;
	}

	public void decreaseCooldown() {
		cooldown--;
	}

	public int getMaxAmmo() {
		return maxAmmo;
	}

	public int getAmmo() {
		return ammo;
	}

	public int getDamage() {
		return damage;
	}

	public boolean isHitscan() {
		return hitscan;
	}

	public int getDirX() {
		return dirX;
	}

	public void setDirX(int dirX) {
		this.dirX = dirX;
	}

	public int getDirY() {
		return dirY;
	}

	public void setDirY(int dirY) {
		this.dirY = dirY;
	}

}
