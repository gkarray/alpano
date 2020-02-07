package ch.epfl.alpano.dem;

import ch.epfl.alpano.*;

/**
 * 
 * @author Ghassen Karray (257478)
 */

public final class ElevationProfile {
	private final double[] longitudes;
	private final double[] latitudes;
	private final ContinuousElevationModel dem;
	private final double length;
	
	private static final int SPACE = 4096;

	/**
	 * Construit un profil altimétrique basé sur le MNT donné et dont le tracé
	 * débute au point origin, suit le grand cercle dans la direction donnée par
	 * azimuth, et a une longueur de length mètres.
	 * 
	 * @param dem
	 * @param origin
	 * @param azimuth
	 * @param length
	 * 
	 * @throws IllegalArgumentException
	 *             si l'azimuth n'est pas canonique, ou si la longueur n'est pas
	 *             strictement positive.
	 * @throws NullPointerException
	 *             si l'un des deux autres arguments est null.
	 */

	public ElevationProfile(ContinuousElevationModel dem, GeoPoint origin, double azimuth, double length) {
		Preconditions.checkArgument(Azimuth.isCanonical(azimuth) && length > 0);

		if (dem == null || origin == null)
			throw new NullPointerException();

		int n = 0;
		double k = Distance.toRadians(4096);
		double x = 0;
		longitudes = new double[(int) Math.floor(Distance.toRadians(length) / k) + 2];
		latitudes = new double[(int) Math.floor(Distance.toRadians(length) / k) + 2];

		while (x <= Distance.toRadians(length)) {
			longitudes[n] = calculLongitude(origin, azimuth, x);
			latitudes[n] = calculLatitude(origin, azimuth, x);
			n++;
			x = x + k;
		}

		longitudes[n] = calculLongitude(origin, azimuth, x);
		latitudes[n] = calculLatitude(origin, azimuth, x);

		this.length = length;
		this.dem = dem;
	}

	private static double calculLongitude(GeoPoint o, double azimuth, double dist) {
		double angle = Azimuth.toMath(azimuth);
		double a = Math.asin((Math.sin(angle) * Math.sin(dist)) / Math.cos(calculLatitude(o, azimuth, dist)));
		double b = o.longitude() - a + Math.PI;
		double c = Math2.floorMod(b, Math2.PI2) - Math.PI;

		return c;
	}

	private static double calculLatitude(GeoPoint o, double azimuth, double dist) {
		return Math.asin((Math.sin(o.latitude()) * Math.cos(dist))
				+ (Math.cos(o.latitude()) * Math.sin(dist) * Math.cos(Azimuth.toMath(azimuth))));
	}

	/**
	 * 
	 * @param x
	 * @throws IllegalArgumentException
	 *             si cette position n'est pas dans les bornes du profil.
	 * @return Retourne les coordonnées du point à la position donnée du profil.
	 */

	public GeoPoint positionAt(double x) {
		Preconditions.checkArgument(x <= length && x >= 0);

		int i = (int) Math.floor(x / SPACE);
		double longi = Math2.lerp(longitudes[i], longitudes[i + 1], x / SPACE - i);
		double latit = Math2.lerp(latitudes[i], latitudes[i + 1], x / SPACE - i);

		return new GeoPoint(longi, latit);
	}

	/**
	 * 
	 * @param x
	 * @throws IllegalArgumentException
	 *             si cette position n'est pas dans les bornes du profil.
	 * @return Retourne l'altitude du terrain à la position donnée du profil.
	 */

	public double elevationAt(double x) {
		Preconditions.checkArgument(x <= length && x >= 0);

		return dem.elevationAt(positionAt(x));
	}

	/**
	 * 
	 * @param x
	 * @throws IllegalArgumentException
	 *             si cette position n'est pas dans les bornes du profil.
	 * @return Retourne la pente du terrain à la position donnée du profil.
	 */

	public double slopeAt(double x) {
		Preconditions.checkArgument(x <= length && x >= 0);

		return dem.slopeAt(positionAt(x));
	}
}
