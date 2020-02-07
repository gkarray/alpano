package ch.epfl.alpano;

import java.util.*;

/**
 * 
 * @author Ghassen Karray (257478)
 */

public final class GeoPoint {
	private final double longitude;
	private final double latitude;

	/**
	 * Construit le point de coordonnées données (en radians)
	 * 
	 * @param longi
	 *            : longitude
	 * @param latit
	 *            : latitude
	 * @throws IllegalArgumentException
	 *             si la longitude n'appartient pas à [-PI;PI] ou si la latitude
	 *             n'appartient pas à [-PI/2;PI/2].
	 */

	public GeoPoint(double longi, double latit) {
		Preconditions.checkArgument(
				(longi >= -Math.PI && longi <= Math.PI) && (latit >= -Math.PI / 2 && latit <= Math.PI / 2));

		longitude = longi;
		latitude = latit;
	}

	/**
	 * 
	 * @return Retourne la longitude du point, en radians.
	 */

	public double longitude() {
		return longitude;
	}

	/**
	 * 
	 * @return Retourne la latitude du point, en radians.
	 */

	public double latitude() {
		return latitude;
	}

	/**
	 * 
	 * @param that
	 * @return Retourne la distance en mètres séparant le récepteur (this) de
	 *         l'argument (that).
	 */

	public double distanceTo(GeoPoint that) {
		double alpha, a;

		a = Math2.haversin(latitude - that.latitude())
				+ Math.cos(latitude) * Math.cos(that.latitude()) * Math2.haversin(longitude - that.longitude());
		alpha = 2 * Math.asin(Math.sqrt(a));

		return Distance.toMeters(alpha);
	}

	/**
	 * 
	 * @param that
	 * @return Retourne l'azimut de l'argument (that) par rapport au récepteur
	 *         (this).
	 */

	public double azimuthTo(GeoPoint that) {
		double beta, a, b;

		a = Math.sin(longitude - that.longitude()) * Math.cos(that.latitude());
		b = Math.cos(latitude) * Math.sin(that.latitude())
				- Math.sin(latitude) * Math.cos(that.latitude()) * Math.cos(longitude - that.longitude());
		beta = Math.atan2(a, b);

		return Azimuth.fromMath(Azimuth.canonicalize(beta));
	}

	@Override
	public String toString() {
		double a = Math.toDegrees(longitude);
		double b = Math.toDegrees(latitude);

		Locale l = null;
		String s = String.format(l, "(%.4f,%.4f)", a, b);

		return s;
	}

}
