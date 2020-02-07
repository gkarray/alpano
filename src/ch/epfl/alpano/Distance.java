package ch.epfl.alpano;

/**
 * 
 * @author Ghassen Karray (257478)
 */

public interface Distance {

	/**
	 * 
	 * EARTH_RADIUS : Rayon de la terre.
	 */

	public static final double EARTH_RADIUS = 6371000;

	/**
	 * 
	 * @param distanceInMeters
	 *            : distance à la surface de la terre (en mètres).
	 * @return Convertit une distance à la surface de la Terre exprimée en
	 *         mètres en l'angle correspondant, en radians.
	 */

	public static double toRadians(double distanceInMeters) {
		return distanceInMeters / EARTH_RADIUS;
	}

	/**
	 * 
	 * @param distanceInRadians
	 *            : distance à la surface de la terre (en radians).
	 * @return Convertit un angle en radians en la distance correspondanté la
	 *         surface de la Terre, en mètres.
	 */

	public static double toMeters(double distanceInRadians) {
		return distanceInRadians * EARTH_RADIUS;
	}
}
