package data.entity;

import data.coordinates.Vector2f;
import processing.core.PApplet;

public class Zombie extends AbstractPathfindingEntity {

	public Zombie(PApplet p, Vector2f position) {
		super(EntityType.HOSTILE, new EntityType[] { EntityType.FRIENDLY }, p, position, 20, 140, 163, 139, 1, 100);
	}

}
