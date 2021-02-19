package data.notification;

import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;

public class NotificationGui {

	private static final int DEFAULT_LIFETIME = 150;
	private final int MAX_NOTIFICATIONS = 10;

	private List<Notification> notifications;
	private List<Integer> lifetimes;

	public NotificationGui() {
		notifications = new ArrayList<>();
		lifetimes = new ArrayList<>();
	}

	public void update() { 
		if (notifications.size() > MAX_NOTIFICATIONS) {
			notifications.remove(notifications.size() - 1);
			lifetimes.remove(lifetimes.size() - 1);
		}
		List<Notification> newNotifications = new ArrayList<>();
		List<Integer> newLifetimes = new ArrayList<>();
		for (int i = 0; i < lifetimes.size(); i++) {
			int lifetime = lifetimes.get(i);
			if (lifetime == 1) {
				continue;
			}
			lifetimes.set(i, lifetime - 1);
			newNotifications.add(notifications.get(i));
			newLifetimes.add(lifetimes.get(i));
		}
		notifications = newNotifications;
		lifetimes = newLifetimes;
	}

	public void display(PApplet p) {
		for (int i = 0; i < notifications.size(); i++) {
			Notification n = notifications.get(i);
			p.fill(n.r, n.g, n.b);
			p.text(n.message, 400, 410 - 30 * i);
		}
	}

	public void addNotification(Notification notification) {
		notifications.add(0, notification);
		lifetimes.add(0, DEFAULT_LIFETIME);
	}

	public void addNotification(Notification notification, int lifetime) {
		notifications.add(0, notification);
		lifetimes.add(0, lifetime);
	}

}
