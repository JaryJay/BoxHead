package main;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import data.coordinates.Vector2f;
import data.entity.Bambo;
import data.entity.EntityType;
import data.entity.GameEntity;
import data.entity.Zombie;
import data.map.TileMap;
import data.notification.Notification;
import data.notification.NotificationGui;
import processing.core.PApplet;

public class BoxHeadSketch extends PApplet {

	private String windowTitle;
	private Bambo bambo;
	private List<GameEntity> entities = new ArrayList<>();
	private NotificationGui notificationGui = new NotificationGui();
//	private transient List<AbstractPersistentArtifact> artifacts = new ArrayList<>();

	private TileMap tileMap = new TileMap();

	private boolean wPressed;
	private boolean aPressed;
	private boolean sPressed;
	private boolean dPressed;

	private float timeUntilNextZombie = 50;
	private float currentTimeUntilNextZombie = timeUntilNextZombie * 5;

	private int score = 0;
	private int multiplier = 1;
	private float timeUntilMultiplierDecreases = 400;
	private float currentTmeUntilMultiplierDecreases = timeUntilMultiplierDecreases;

	private boolean lost = false;

	public BoxHeadSketch() {
		bambo = new Bambo(this, new Vector2f(210, 110));
		entities.add(new Zombie(this, new Vector2f(250, 50)));
		entities.add(new Zombie(this, new Vector2f(325, 225)));
		entities.add(new Zombie(this, new Vector2f(325, 325)));
		entities.add(new Zombie(this, new Vector2f(425, 203)));
		entities.add(bambo);
		tileMap.bindEntities(entities);
	}

	public void startWindow() {
		String[] processingArgs = { this.getClass().getName() };
		PApplet.runSketch(processingArgs, this);
	}

	@Override
	public void settings() {
		size(800, 450, P2D);
	}

	@Override
	public void setup() {
		surface.setTitle(windowTitle);
		textAlign(CENTER, CENTER);
		textSize(24);
	}

	@Override
	public void draw() {
		if (!lost) {
			update();
		}
		display();
	}

	private void update() {
		updateEntities();
		updateZombieSpawning();
		tileMap.bindEntities(entities);
		notificationGui.update();
	}

	private void updateEntities() {
		List<GameEntity> newEntities = new ArrayList<>();
		for (int i = 0; i < entities.size(); i++) {
			GameEntity e = entities.get(i);
			if (e.isMarkedForRemoval()) {
				if (e.getType() == EntityType.HOSTILE) {
					timeUntilNextZombie *= 0.99f;
					score += multiplier * 10;
					multiplier++;
					timeUntilMultiplierDecreases = 800 / multiplier;
					currentTmeUntilMultiplierDecreases = timeUntilMultiplierDecreases;
				}
				continue;
			}
			newEntities.add(e);
			e.update(tileMap, entities);
		}
		entities = newEntities;
		if (bambo.getCurrentHealth() <= 0) {
			lost = true;
		}
	}

	private void updateZombieSpawning() {
		if (multiplier > 1) {
			if (currentTmeUntilMultiplierDecreases <= 0) {
				multiplier--;
				timeUntilMultiplierDecreases = 800 / multiplier;
				currentTmeUntilMultiplierDecreases = timeUntilMultiplierDecreases;
			} else {
				currentTmeUntilMultiplierDecreases--;
			}
		}
		if (currentTimeUntilNextZombie <= 0) {
			entities.add(new Zombie(this, new Vector2f(50, 50)));
			currentTimeUntilNextZombie = timeUntilNextZombie;
		} else {
			currentTimeUntilNextZombie--;
		}
	}

	private void display() {
		background(141, 142, 145);
		tileMap.displayLayer1(this);
		if (mousePressed) {
			bambo.shoot(entities, tileMap, this);
		}
		for (GameEntity e : entities) {
			e.display();
		}
		tileMap.displayLayer2(this);
		displayHealthBar();
		notificationGui.display(this);
//		List<AbstractPersistentArtifact> newArtifacts = new ArrayList<>();
//		for (AbstractPersistentArtifact a : artifacts) {
//			a.display(this);
//			a.decrementCountdown();
//			if (a.getCountdown() != 0) {
//				newArtifacts.add(a);
//			}
//		}
//		artifacts = newArtifacts;
		fill(255);
		textSize(24);
		text("Score: " + score + "     x" + multiplier, 400, 30);
		if (lost) {
			textSize(48);
			fill(0);
			text("YOU LOSE", 400, 225);
		}
	}

	private void displayHealthBar() {
		int currentHealth = bambo.getCurrentHealth();
		int maxHealth = bambo.getMaxHealth();
		stroke(0);
		fill(255, 255, 255, 0);
		rect(bambo.getPosition().x - 25, bambo.getPosition().y - bambo.getRadius() - 17, 50, 12);
		if (currentHealth <= 50) {
			fill(200, 4 * currentHealth, 20);
		} else {
			fill(4 * (maxHealth - currentHealth), 200, 20);
		}
		rect(bambo.getPosition().x - 25, bambo.getPosition().y - bambo.getRadius() - 17, 50 * currentHealth / maxHealth, 12);
	}

	@Override
	public void keyPressed() {
		if (key == 'w' || keyCode == KeyEvent.VK_UP) {
			if (!wPressed) {
				wPressed = true;
			}
			bambo.north();
		} else if (key == 'a' || keyCode == KeyEvent.VK_LEFT) {
			if (!aPressed) {
				aPressed = true;
			}
			bambo.west();
		}
		if (key == 's' || keyCode == KeyEvent.VK_DOWN) {
			if (!sPressed) {
				sPressed = true;
			}
			bambo.south();
		}
		if (key == 'd' || keyCode == KeyEvent.VK_RIGHT) {
			if (!dPressed) {
				dPressed = true;
			}
			bambo.east();
		}
		if (key == 'r') {
			bambo.getGun().reload();
		}
		if (key == ' ') {
			notificationGui.addNotification(new Notification("Pressed Space!", 50, 50, 50));
		}
		if (keyCode == KeyEvent.VK_SHIFT) {
			notificationGui.addNotification(new Notification("WOAAHHHH!!!", 255, 0, 0));
		}
		if (keyCode == KeyEvent.VK_CONTROL) {
			bambo.setCurrentHealth(bambo.getCurrentHealth() - 1);
		}
	}

	@Override
	public void keyReleased() {
		if ((key == 'w' || keyCode == KeyEvent.VK_UP) && wPressed) {
			wPressed = false;
			bambo.south();
		}
		if ((key == 'a' || keyCode == KeyEvent.VK_LEFT) && aPressed) {
			aPressed = false;
			bambo.east();
		}
		if ((key == 's' || keyCode == KeyEvent.VK_DOWN) && sPressed) {
			sPressed = false;
			bambo.north();
		}
		if ((key == 'd' || keyCode == KeyEvent.VK_RIGHT) && dPressed) {
			dPressed = false;
			bambo.west();
		}
	}

	public NotificationGui getNotificationGui() {
		return notificationGui;
	}

	public Bambo getBambo() {
		return bambo;
	}

	public String getWindowTitle() {
		return windowTitle;
	}

	public void setWindowTitle(String windowTitle) {
		this.windowTitle = windowTitle;
	}

}
