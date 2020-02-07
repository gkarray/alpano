package ch.epfl.alpano;

import java.util.Locale;
import java.util.Objects;

/**
 * 
 * @author Ghassen Karray (257478)
 */

public final class Interval2D {
	private final Interval1D interval1;
	private final Interval1D interval2;

	/**
	 * Construit le produit cartésien des intervalles iX et iY.
	 * 
	 * @param int1
	 * @param int2
	 * @throws NullPointerException
	 *             si l'un ou l'autre de ces intervalles est null.
	 */

	public Interval2D(Interval1D int1, Interval1D int2) {
		interval1 = Objects.requireNonNull(int1);
		interval2 = Objects.requireNonNull(int2);
	}

	/**
	 * 
	 * @return Retourne le premier intervalle du produit cartésien
	 */

	public Interval1D iX() {
		return interval1;
	}

	/**
	 * 
	 * @return Retourne le second intervalle du produit cartésien.
	 */

	public Interval1D iY() {
		return interval2;
	}

	/**
	 * 
	 * @param x
	 * @param y
	 * @return Retourne vrai ssi l'intervalle contient la paire (x, y).
	 */

	public boolean contains(int x, int y) {
		return iX().contains(x) && iY().contains(y);
	}

	/**
	 * 
	 * @return Retourne la taille de l'intervalle, c-à-d le nombre d'éléments
	 *         qu'il contient.
	 */

	public int size() {
		return iX().size() * iY().size();
	}

	/**
	 * 
	 * @param that
	 * @return Retourne la taille de l'intersection du récepteur this avec
	 *         l'argument that.
	 */

	public int sizeOfIntersectionWith(Interval2D that) {
		return (iX().sizeOfIntersectionWith(that.iX())) * (iY().sizeOfIntersectionWith(that.iY()));
	}

	/**
	 * 
	 * @param that
	 * @return Retourne l'union englobante du récepteur this et de l'argument
	 *         that.
	 */

	public Interval2D boundingUnion(Interval2D that) {
		return new Interval2D(iX().boundingUnion(that.iX()), iY().boundingUnion(that.iY()));
	}

	/**
	 * 
	 * @param that
	 * @return Retourne vrai ssi le récepteur this et l'argument that sont
	 *         unionables.
	 */

	public boolean isUnionableWith(Interval2D that) {
		return boundingUnion(that).size() == size() + that.size() - sizeOfIntersectionWith(that);
	}

	/**
	 * 
	 * @param that
	 * @throws IllegalArgumentException
	 *             s'ils ne sont pas unionables.
	 * @return Retourne l'union du récepteur et de that.
	 */

	public Interval2D union(Interval2D that) {
		Preconditions.checkArgument(isUnionableWith(that));

		return boundingUnion(that);
	}

	@Override
	public boolean equals(Object that0) {
		if (!(that0 instanceof Interval2D))
			return false;
		else {
			return iX().equals(((Interval2D) that0).iX()) && iY().equals(((Interval2D) that0).iY());
		}
	}

	@Override
	public int hashCode() {
		return Objects.hash(interval1, interval2);
	}

	@Override
	public String toString() {
		return iX().toString() + "x" + iY().toString();
	}
}
