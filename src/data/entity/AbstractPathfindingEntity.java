package data.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import data.coordinates.Vector2f;
import data.map.Tile;
import data.map.TileMap;
import processing.core.PApplet;

public abstract class AbstractPathfindingEntity extends GameEntity {

	private EntityType[] followTypes;

	public AbstractPathfindingEntity(EntityType type, EntityType[] followTypes, PApplet p, Vector2f position, float radius, int r, int g, int b, float speed, int maxHealth) {
		super(type, p, position, radius, r, g, b, speed, maxHealth);
		this.followTypes = followTypes;
	}

	@Override
	public void update(TileMap t, List<GameEntity> entities) {
		updatePathfinding(t);
		super.update(t, entities);
	}

	private void updatePathfinding(TileMap t) {
		Map<Tile, Tile> cameFrom = new HashMap<>();
		Tile start = t.getTiles()[(int) (position.y / Tile.TILE_SIZE)][(int) (position.x / Tile.TILE_SIZE)];
		Tile goal = findGoal(t, cameFrom);
		Tile current = goal;
		System.out.println(goal);
		while (cameFrom.get(current) != start && current != start) {
			current = cameFrom.get(current);
		}
//		System.out.println("Current is at (" + current.getPosX() + ", " + current.getPosY() + ")");
		dirX = current.getPosX() - start.getPosX();
		dirY = current.getPosY() - start.getPosY();
	}

	private Tile findGoal(TileMap tileMap, Map<Tile, Tile> cameFrom) {
		int tilePosX = (int) (position.x / Tile.TILE_SIZE);
		int tilePosY = (int) (position.y / Tile.TILE_SIZE);
		EntityGroup[][] entityGroups = tileMap.getEntityGroups();
		Tile start = tileMap.getTiles()[tilePosY][tilePosX];

		List<Tile> frontier = new ArrayList<>();
		frontier.add(start);
		cameFrom.put(start, null);

		while (!frontier.isEmpty()) {
			List<Tile> newFrontier = new ArrayList<>();
			for (Tile t : frontier) {
				// End condition
				if (containsFollowableEntities(entityGroups, t)) {
					return t;
				}

				// Add neighbours
				for (Tile next : tileMap.getNeighbours(t)) {
					if (cameFrom.containsKey(next)) {
						continue;
					}
					cameFrom.put(next, t);
					newFrontier.add(next);
				}
			}
			frontier = newFrontier;
		}
		return start;
	}

	private boolean containsFollowableEntities(EntityGroup[][] entityGroups, Tile t) {
		EntityGroup entityGroup = entityGroups[t.getPosY()][t.getPosX()];
		if (entityGroup == null) {
			return false;
		}
		List<GameEntity> entities = entityGroup.getEntities();
		for (GameEntity entity : entities) {
			for (EntityType type : followTypes) {
				if (entity.getType() == type) {
					return true;
				}
			}
		}
		return false;
	}

}
