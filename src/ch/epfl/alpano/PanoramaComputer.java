package ch.epfl.alpano;

import java.util.Objects;
import java.util.function.DoubleUnaryOperator;
import ch.epfl.alpano.dem.*;
import ch.epfl.alpano.Panorama.Builder;

/**
 * 
 * @author Ghassen Karray (257478)
 */

public final class PanoramaComputer {
	private final ContinuousElevationModel dem;

	private static final int SMALL_SPACE = 4, LARGE_SPACE = 64;
	private static final double CONST = (1 - 0.13) / (2 * Distance.EARTH_RADIUS);

	/**
	 * Construit un calculateur de panorama obtenant les données du MNT continu
	 * passé en argument
	 * 
	 * @param dem
	 * @throws NullPointerException
	 *             s'il est nul.
	 */

	public PanoramaComputer(ContinuousElevationModel dem) {
		this.dem = Objects.requireNonNull(dem);
	}

	/**
	 * 
	 * @param parameters
	 * @return Retourne le panorama spécifié par les paramètres.
	 */

	public Panorama computePanorama(PanoramaParameters parameters) {
		Builder builder = new Builder(parameters);

		for (int i = 0; i <= parameters.width() - 1; i++) {
			double azimuth = parameters.azimuthForX(i);
			ElevationProfile profile = new ElevationProfile(dem, parameters.observerPosition(), azimuth,
					parameters.maxDistance());
			double x = 0;

			for (int j = parameters.height() - 1; j >= 0; j--) {
				double altitudeY = parameters.altitudeForY(j);

				DoubleUnaryOperator delta = rayToGroundDistance(profile, parameters.observerElevation(),
						Math.tan(altitudeY));
				x = Math2.firstIntervalContainingRoot(delta, x, parameters.maxDistance(), LARGE_SPACE);

				if (x != Double.POSITIVE_INFINITY) {
					x = Math2.improveRoot(delta, x, x + LARGE_SPACE, SMALL_SPACE);
					GeoPoint point = new GeoPoint(profile.positionAt(x).longitude(), profile.positionAt(x).latitude());

					builder.setDistanceAt(i, j, (float) (x / Math.cos(altitudeY)));
					builder.setElevationAt(i, j, (float) dem.elevationAt(point));
					builder.setLatitudeAt(i, j, (float) (point.latitude()));
					builder.setLongitudeAt(i, j, (float) (point.longitude()));
					builder.setSlopeAt(i, j, (float) dem.slopeAt(point));
				} else
					break;
			}
		}
		return builder.build();
	}

	/**
	 * 
	 * @param profile
	 * @param ray0
	 * @param raySlope
	 * @return Retourne la fonction donnant la distance entre un rayon
	 *         d'altitude initiale ray0 et de pente de raySlope, et le profil
	 *         altimétrique profile.
	 */

	public static DoubleUnaryOperator rayToGroundDistance(ElevationProfile profile, double ray0, double raySlope) {
		DoubleUnaryOperator delta = x -> ray0 + x * raySlope - profile.elevationAt(x) + (Math2.sq(x) * CONST);

		return delta;
	}

}
