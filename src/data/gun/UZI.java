package data.gun;

import data.coordinates.Vector2f;
import data.entity.GameEntity;
import processing.core.PApplet;

public class UZI extends Gun {

	public UZI(int maxAmmo, int damage, int shootSpeed, float range, float spread, boolean pierce, boolean hitscan) {
		super(maxAmmo, damage, shootSpeed, range, spread, pierce, hitscan);
	}

	@Override
	public void displayGun(Vector2f position, float radius, PApplet p) {
		p.fill(255);
		p.stroke(0);
		p.rect(radius * GameEntity.SQRT_TWO_OVER_TWO - 10, radius * GameEntity.SQRT_TWO_OVER_TWO, 20, 5);
	}

}
