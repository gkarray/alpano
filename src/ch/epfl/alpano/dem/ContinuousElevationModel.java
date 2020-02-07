package ch.epfl.alpano.dem;

import java.util.Objects;
import ch.epfl.alpano.*;

/**
 * 
 * @author Ghassen Karray (257478)
 */

public final class ContinuousElevationModel {
	private final DiscreteElevationModel dem;
	private static double d = Distance.toMeters(1 / DiscreteElevationModel.SAMPLES_PER_RADIAN);

	/**
	 * Construit un MNT continu basé sur le MNT discret passé en argument.
	 * 
	 * @param dem
	 * @throws NullPointerException
	 *             si celui-ci est nul.
	 */

	public ContinuousElevationModel(DiscreteElevationModel dem) {
		this.dem = Objects.requireNonNull(dem);
	}

	/**
	 * 
	 * @param p
	 * @return Retourne l'altitude au point donné, en mètres.
	 */

	public double elevationAt(GeoPoint p) {
		double doubleX = DiscreteElevationModel.sampleIndex(p.longitude());
		double doubleY = DiscreteElevationModel.sampleIndex(p.latitude());
		int x = (int) Math.floor(doubleX);
		int y = (int) Math.floor(doubleY);

		return Math2.bilerp(altitude(x, y), altitude(x + 1, y), altitude(x, y + 1), altitude(x + 1, y + 1), doubleX - x,
				doubleY - y);
	}

	/**
	 * 
	 * @param p
	 * @return Retourne la pente du terrain au point donné, en radians.
	 */

	public double slopeAt(GeoPoint p) {
		double doubleX = DiscreteElevationModel.sampleIndex(p.longitude());
		double doubleY = DiscreteElevationModel.sampleIndex(p.latitude());
		int x = (int) Math.floor(doubleX);
		int y = (int) Math.floor(doubleY);

		return Math2.bilerp(pente(x, y), pente(x + 1, y), pente(x, y + 1), pente(x + 1, y + 1), doubleX - x,
				doubleY - y);
	}

	private double pente(int x, int y) {
		if (dem.extent().contains(x, y)) {
			double zA = altitude(x, y) - altitude(x + 1, y);
			double zB = altitude(x, y) - altitude(x, y + 1);

			return Math.acos(d / Math.sqrt(Math2.sq(zA) + Math2.sq(zB) + Math2.sq(d)));
		} else
			return 0;
	}

	private double altitude(int x, int y) {
		if (dem.extent().contains(x, y)) {
			return dem.elevationSample(x, y);
		} else {
			return 0;
		}
	}

}
