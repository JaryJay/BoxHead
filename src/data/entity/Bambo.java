package data.entity;

import java.util.List;

import data.coordinates.Vector2f;
import data.gun.Gun;
import data.gun.UZI;
import data.map.TileMap;
import data.notification.Notification;
import main.BoxHeadSketch;
import processing.core.PApplet;

public class Bambo extends GameEntity {

	private Gun gun;

	public Bambo(PApplet p, Vector2f position) {
		super(EntityType.FRIENDLY, p, new Vector2f(50, 50), 20, 50, 52, 61, 2, 100);
		gun = new UZI(50, 20, 5, 400, 0, false, true);
		gun.setDirX(1);
		gun.setDirY(0);
	}

	@Override
	public void update(TileMap t, List<GameEntity> entities) {
		super.update(t, entities);
		if (!(dirX == 0 && dirY == 0)) {
			gun.setDirX(dirX);
			gun.setDirY(dirY);
		}
		if (gun.getCooldown() > 0) {
			gun.decreaseCooldown();
		}
	}

	@Override
	public void display() {
		gun.display(position, radius, p);
		super.display();
	}

	public Gun getGun() {
		return gun;
	}

	public void setGun(Gun gun) {
		this.gun = gun;
	}

	public void shoot(List<GameEntity> entities, TileMap tileMap, PApplet p) {
		gun.shoot(position, radius, entities, tileMap, p);
		if (gun.getAmmo() == 0 && gun.getCooldown() == 0) {
			((BoxHeadSketch) p).getNotificationGui().addNotification(new Notification("Out of ammo!", 230, 40, 50));
			gun.setCooldown(30);
		}
	}

}
