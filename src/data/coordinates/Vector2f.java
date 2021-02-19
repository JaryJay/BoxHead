package data.coordinates;

public class Vector2f {

	public float x;
	public float y;

	public Vector2f(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public static Vector2f fromXY(float x, float y) {
		return new Vector2f(x, y);
	}

	public static Vector2f fromLengthAngle(float length, float angle) {
		return new Vector2f((float) (length * Math.cos(angle)), (float) (length * Math.sin(angle)));
	}

	public float lengthSquared() {
		return x * x + y * y;
	}

	public float length() {
		return (float) Math.sqrt(x * x + y * y);
	}

	public Vector2f add(Vector2f vector) {
		x += vector.x;
		y += vector.y;
		return this;
	}

	public Vector2f subtract(Vector2f vector) {
		x -= vector.x;
		y -= vector.y;
		return this;
	}

	public Vector2f scale(float factor) {
		x *= factor;
		y *= factor;
		return this;
	}

	/**
	 * Modifies the vector so that its length is 1.
	 * 
	 * @return the modified vector
	 */
	public Vector2f normalize() {
		return scale(1 / length());
	}

	public Vector2f copy() {
		return new Vector2f(x, y);
	}

	public Vector2f set(Vector2f vector) {
		x = vector.x;
		y = vector.y;
		return this;
	}

	public Vector2f invert() {
		x = -x;
		y = -y;
		return this;
	}

	public float dot(Vector2f vector) {
		return vector.x * x + vector.y * y;
	}

	public Vector2f projectOnto(Vector2f vector) {
		return set(vector.copy().scale(dot(vector) / vector.lengthSquared()));
	}

	public Vector2f reflect(Vector2f vector) {
		return set(projectOnto(vector).scale(2).subtract(this));
	}

	@Override
	public String toString() {
		return "(" + x + ", " + y + ")";
	}

	@Override
	public boolean equals(Object obj) {
		return x == ((Vector2f) obj).x && y == ((Vector2f) obj).y;
	}

}
