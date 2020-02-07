package ch.epfl.alpano;

/**
 * 
 * @author Ghassen Karray (257478)
 */

public interface Azimuth {

	/**
	 * @param azimuth
	 *            : angle (azimut)
	 * @return Vrai ssi son argument est un azimut "canonique", c-à-d compris
	 *         dans l'intervalle [0;2PI[.
	 */

	public static boolean isCanonical(double azimuth) {
		if (azimuth >= 0 && azimuth < Math2.PI2)
			return true;

		return false;
	}

	/**
	 * 
	 * @param azimuth
	 *            : angle (azimut)
	 * @return L'azimut canonique équivalent à celui passé en argument, c-à-d
	 *         compris dans l'intervalle [0;2PI[.
	 */

	public static double canonicalize(double azimuth) {
		return azimuth - Math2.PI2 * Math.floor(azimuth / Math2.PI2);
	}

	/**
	 * 
	 * @param azimuth
	 *            : angle (azimut)
	 * @throws IllegalArgumentException
	 *             si son argument n'est pas un azimut canonique.
	 * @return L'azimut sous forme mathematique (antihoraire).
	 */

	public static double toMath(double azimuth) {
		Preconditions.checkArgument(isCanonical(azimuth));

		if (azimuth != 0)
			return Math2.PI2 - azimuth;
		else
			return azimuth;
	}

	/**
	 * 
	 * @param azimuth
	 *            : angle (azimut)
	 * @throws IllegalArgumentException
	 *             si son argument n'est pas un azimut canonique.
	 * @return L'angle mathematique sous forme d'azimut (horaire).
	 */

	public static double fromMath(double azimuth) {
		Preconditions.checkArgument(isCanonical(azimuth));

		if (azimuth != 0)
			return Math2.PI2 - azimuth;
		else
			return azimuth;
	}

	/**
	 * 
	 * @param azimuth
	 *            : angle (azimut)
	 * @param n
	 * @param e
	 * @param s
	 * @param w
	 * @throws IllegalArgumentException
	 *             si l'azimut donné n'est pas canonique.
	 * @return Une chaine correspondant à l'octant dans lequel se trouve
	 *         l'azimut donné.
	 */

	public static String toOctantString(double azimuth, String n, String e, String s, String w) {
		if (isCanonical(azimuth)) {
			if (azimuth > (Math2.PI2 / 16) && azimuth <= 3 * (Math2.PI2 / 16))
				return n + e;

			if (azimuth > 3 * (Math2.PI2 / 16) && azimuth <= 5 * (Math2.PI2 / 16))
				return e;

			if (azimuth > 5 * (Math2.PI2 / 16) && azimuth <= 7 * (Math2.PI2 / 16))
				return s + e;
			if (azimuth > 7 * (Math2.PI2 / 16) && azimuth <= 9 * (Math2.PI2 / 16))
				return s;

			if (azimuth > 9 * (Math2.PI2 / 16) && azimuth <= 11 * (Math2.PI2 / 16))
				return s + w;

			if (azimuth > 11 * (Math2.PI2 / 16) && azimuth <= 13 * (Math2.PI2 / 16))
				return w;

			if (azimuth > 13 * (Math2.PI2 / 16) && azimuth <= 15 * (Math2.PI2 / 16))
				return n + w;

			if (azimuth > 15 * (Math2.PI2 / 16) || azimuth <= (Math2.PI2 / 16))
				return n;
			else
				throw new IllegalArgumentException();
		} else {
			throw new IllegalArgumentException();
		}
	}

}
