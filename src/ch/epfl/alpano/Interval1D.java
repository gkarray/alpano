package ch.epfl.alpano;

import java.util.*;

/**
 * 
 * @author Ghassen Karray (257478)
 */

public final class Interval1D {
	private final int includedTo;
	private final int includedFrom;

	/**
	 * Construit un intervalle allant de includedFrom jusqu'a includedTo (bornes
	 * incluses).
	 * 
	 * @param includedFrom
	 * @param includedTo
	 */

	public Interval1D(int includedFrom1, int includedTo1) {
		Preconditions.checkArgument(includedFrom1 <= includedTo1);

		includedFrom = includedFrom1;
		includedTo = includedTo1;
	}

	/**
	 * 
	 * @return Retourne le plus petit élément de l'intervalle.
	 */

	public int includedFrom() {
		return includedFrom;
	}

	/**
	 * 
	 * @return Retourne le plus grand élément de l'intervalle.
	 */

	public int includedTo() {
		return includedTo;
	}

	/**
	 * 
	 * @param v
	 * @return Retourne vrai ssi v appartient à l'intervalle.
	 */

	public boolean contains(int v) {
		return (v >= includedFrom() && v <= includedTo());
	}

	/**
	 * 
	 * @return Retourne la taille de l'intervalle, c-à-d le nombre d'éléments
	 *         qu'il contient.
	 */

	public int size() {
		return includedTo() - includedFrom() + 1;
	}

	/**
	 * 
	 * @param that
	 * @return Retourne la taille de l'intersection du récepteur this et de
	 *         l'argument that.
	 */

	public int sizeOfIntersectionWith(Interval1D that) {
		int a = Math.min(includedTo(), that.includedTo());
		int b = Math.max(includedFrom(), that.includedFrom());
		int c = a - b + 1;

		if (c <= 0)
			return 0;

		return c;
	}

	/**
	 * 
	 * @param that
	 * @return Retourne l'union englobante du récepteur this et de l'argument
	 *         that.
	 */

	public Interval1D boundingUnion(Interval1D that) {
		return new Interval1D(Math.min(includedFrom(), that.includedFrom()), Math.max(includedTo(), that.includedTo()));
	}

	/**
	 * 
	 * @param that
	 * @return Retourne vrai ssi le récepteur this et l'argument that sont
	 *         unionables.
	 */

	public boolean isUnionableWith(Interval1D that) {
		return boundingUnion(that).size() == (that.size() + size() - sizeOfIntersectionWith(that));
	}

	/**
	 * 
	 * @param that
	 * @throws IllegalArgumentException
	 *             s'ils ne sont pas unionables.
	 * @return Retourne l'union du récepteur this et de l'argument that.
	 */

	public Interval1D union(Interval1D that) {
		Preconditions.checkArgument(isUnionableWith(that));

		return boundingUnion(that);
	}

	@Override
	public boolean equals(Object that0) {
		if (!(that0 instanceof Interval1D))
			return false;
		else {
			return includedFrom() == ((Interval1D) that0).includedFrom()
					&& includedTo() == ((Interval1D) that0).includedTo();
		}
	}

	@Override
	public int hashCode() {
		return Objects.hash(includedFrom(), includedTo());
	}

	@Override
	public String toString() {
		Locale l = null;
		String s = String.format(l, "[%d..%d]", includedFrom(), includedTo());

		return s;
	}

}
