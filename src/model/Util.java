package model;

public final class Util {
	public static Color opponent(Color c) {
		return c == Color.WHITE ? Color.BLACK : Color.WHITE;
	}
}
