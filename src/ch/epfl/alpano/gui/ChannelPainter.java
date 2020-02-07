package ch.epfl.alpano.gui;

import java.util.function.DoubleUnaryOperator;
import ch.epfl.alpano.Math2;
import ch.epfl.alpano.Panorama;

/**
 * 
 * @author Ghassen Karray (257478)
 */

public interface ChannelPainter {

	/**
	 * 
	 * @param x
	 * @param y
	 * @return Retourne la valeur du peintre de canal au point de coordonnée
	 *         (x,y).
	 */

	float valueAt(int x, int y);

	/**
	 * 
	 * @param p
	 * @return Retourne un peintre de canal dont la valeur pour un point est la
	 *         différence de distance entre le plus lointain des voisins et le
	 *         point en question.
	 */

	static ChannelPainter maxDistanceToNeighbors(Panorama p) {
		ChannelPainter painter = (x, y) -> Math.max(p.distanceAt(x + 1, y, 0),
				Math.max(p.distanceAt(x, y + 1, 0), Math.max(p.distanceAt(x - 1, y, 0), p.distanceAt(x, y - 1, 0))))
				- p.distanceAt(x, y);

		return painter;
	}

	/**
	 * 
	 * @param v
	 * @return Le peintre de canal resultant de l'addition de la valeur produite
	 *         par un canal avec une constante v.
	 */

	default ChannelPainter add(float v) {
		ChannelPainter painter = (x, y) -> valueAt(x, y) + v;

		return painter;
	}

	/**
	 * 
	 * @param v
	 * @return Le peintre de canal resultant de la soustraction de la valeur
	 *         produite par un canal avec une constante v.
	 */

	default ChannelPainter sub(float v) {
		ChannelPainter painter = (x, y) -> valueAt(x, y) - v;

		return painter;
	}

	/**
	 * 
	 * @param v
	 * @return Le peintre de canal resultant de la multiplication de la valeur
	 *         produite par un canal avec une constante v.
	 */

	default ChannelPainter mul(float v) {
		ChannelPainter painter = (x, y) -> valueAt(x, y) * v;

		return painter;
	}

	/**
	 * 
	 * @param v
	 * @return Le peintre de canal resultant de la division de la valeur
	 *         produite par un canal avec une constante v.
	 */

	default ChannelPainter div(float v) {
		ChannelPainter painter = (x, y) -> valueAt(x, y) / v;

		return painter;
	}

	/**
	 * 
	 * @param v
	 * @return Le peintre de canal resultant de l'application d'une operation
	 *         unaire sur la valeur produite par un canal avec une constante v.
	 */

	default ChannelPainter map(DoubleUnaryOperator f) {
		ChannelPainter painter = (x, y) -> (float) (f.applyAsDouble(valueAt(x, y)));

		return painter;
	}

	/**
	 * 
	 * @param v
	 * @return Le peintre de canal resultant de l'application d'une fonction
	 *         invert sur la valeur produite par un canal avec une constante v.
	 */

	default ChannelPainter inverted() {
		ChannelPainter painter = (x, y) -> 1 - valueAt(x, y);

		return painter;
	}

	/**
	 * 
	 * @param v
	 * @return Le peintre de canal resultant de l'application d'une fonction
	 *         clamp sur la valeur produite par un canal avec une constante v.
	 */

	default ChannelPainter clamped() {
		ChannelPainter painter = (x, y) -> Math.max(0, Math.min(valueAt(x, y), 1));

		return painter;
	}

	/**
	 * 
	 * @param v
	 * @return Le peintre de canal resultant de l'application d'une fonction
	 *         cycle sur la valeur produite par un canal avec une constante v.
	 */

	default ChannelPainter cycling() {
		ChannelPainter painter = (x, y) -> (float) (Math2.floorMod(valueAt(x, y), 1));

		return painter;
	}
}
