package ch.epfl.alpano;

import java.util.function.DoubleUnaryOperator;

/**
 * 
 * @author Ghassen Karray (257478)
 */

public interface Math2 {

	/**
	 * 
	 * Champ statique et final de valeur 2*PI.
	 */

	public final static double PI2 = 2 * Math.PI;

	/**
	 * 
	 * @param x
	 * @return Retourne x élevé au carré.
	 */

	public static double sq(double x) {
		return x * x;
	}

	/**
	 * 
	 * @param x
	 * @param y
	 * @return Retourne le reste de la division entière par défaut de x par y.
	 */

	public static double floorMod(double x, double y) {
		return x - y * (Math.floor(x / y));
	}

	/**
	 * 
	 * @param x
	 * @return Retourne le demi sinus verse de x.
	 */

	public static double haversin(double x) {
		return sq(Math.sin(x / 2));
	}

	/**
	 * 
	 * @param a1
	 * @param a2
	 * @return Retourne la différence angulaire de a1 et a2.
	 */

	public static double angularDistance(double a1, double a2) {
		return floorMod(a2 - a1 + PI2 / 2, PI2) - PI2 / 2;
	}

	/**
	 * 
	 * @param y0
	 * @param y1
	 * @param x
	 * @return Retourne la valeur de f(x) obtenue par interpolation linéaire.
	 */
	public static double lerp(double y0, double y1, double x) {
		return y0 + x * (y1 - y0);
	}

	/**
	 * @param z00
	 * @param z10
	 * @param z01
	 * @param z11
	 * @param x
	 * @param y
	 * @return retourne la valeur de f(x,y) obtenue par interpolation
	 *         bilinéaire.
	 */

	public static double bilerp(double z00, double z10, double z01, double z11, double x, double y) {
		double l1 = lerp(z00, z10, x);
		double l2 = lerp(z01, z11, x);

		return lerp(l1, l2, y);
	}

	/**
	 * @param f
	 * @param minX
	 * @param maxX
	 * @param dX
	 * @return Retourne la borne inférieure du premier intervalle de taille dX
	 *         contenant un zéro de la fonction f et compris entre minX et maxX.
	 */
	public static double firstIntervalContainingRoot(DoubleUnaryOperator f, double minX, double maxX, double dX) {
		Preconditions.checkArgument(minX <= maxX && 0 < dX);

		double m1 = minX;
		double m2 = minX + dX;

		while (m2 < maxX) {
			if (f.applyAsDouble(m1) * f.applyAsDouble(m2) <= 0 && f.applyAsDouble(m1) + dX <= maxX)
				return m1;
			m1 += dX;
			m2 += dX;
		}

		if (f.applyAsDouble(m1) * f.applyAsDouble(maxX) <= 0 && f.applyAsDouble(m1) + dX <= maxX)
			return m1;

		return Double.POSITIVE_INFINITY;
	}

	/**
	 * 
	 * @param f
	 * @param x1
	 * @param x2
	 * @param epsilon
	 * @throws IllegalArgumentException
	 *             si f(x1) et f(x2) sont de même signe.
	 * @return Retourne la borne inférieure d'un intervalle compris entre x1 et
	 *         x2, de taille inférieure à epsilon et contenant un zéro de f.
	 */

	public static double improveRoot(DoubleUnaryOperator f, double x1, double x2, double epsilon) {
		double borneinf = x1;
		double bornesup = x2;
		Preconditions.checkArgument(f.applyAsDouble(x1) * f.applyAsDouble(x2) <= 0);

		while (bornesup - borneinf > epsilon) {
			double xm = (borneinf + bornesup) / 2;

			if (f.applyAsDouble(xm) * f.applyAsDouble(borneinf) <= 0)
				bornesup = xm;
			else
				borneinf = xm;
		}

		return borneinf;
	}
}
