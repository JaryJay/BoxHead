package data.entity;

import java.util.ArrayList;
import java.util.List;

public class EntityGroup {

	private List<GameEntity> entities;

	public EntityGroup() {
		entities = new ArrayList<>();
	}

	public List<GameEntity> getEntities() {
		return entities;
	}

	public void add(GameEntity entity) {
		entities.add(entity);
	}

	public void remove(GameEntity entity) {
		entities.remove(entity);
	}

}
