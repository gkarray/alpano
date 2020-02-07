package ch.epfl.alpano.dem;

import ch.epfl.alpano.*;

/**
 * 
 * @author Ghassen Karray (257478)
 */

public interface DiscreteElevationModel extends AutoCloseable {
	/**
	 * 
	 * Contient le nombre d'échantillons par degré d'un MNT discret.
	 */

	static int SAMPLES_PER_DEGREE = 3600;

	/**
	 * 
	 * Contient le nombre d'échantillons par radians d'un MNT discret.
	 */

	static double SAMPLES_PER_RADIAN = SAMPLES_PER_DEGREE * 180 / Math.PI;

	/**
	 * 
	 * @param angle
	 * @return Retourne l'index correspondant à l'angle donné.
	 */

	static double sampleIndex(double angle) {
		return angle * SAMPLES_PER_RADIAN;
	}

	/**
	 * 
	 * @return Retourne l'étendue du MNT.
	 */

	Interval2D extent();

	/**
	 * 
	 * @param x
	 * @param y
	 * @throws IllegalArgumentException
	 *             si l'index ne fait pas partie de l'étendue du MNT.
	 * @return Retourne l'échantillon d'altitude à l'index donné, en mètres.
	 */

	double elevationSample(int x, int y);

	/**
	 * 
	 * @param that
	 * @throws IllegalArgumentException
	 *             si leurs étendues ne sont pas unionables.
	 * @return Retourne un MNT discret représentant l'union du récepteur et de
	 *         l'argument that.
	 */

	default DiscreteElevationModel union(DiscreteElevationModel that) {
		Preconditions.checkArgument(this.extent().isUnionableWith(that.extent()));

		return new CompositeDiscreteElevationModel(this, that);
	}

}
