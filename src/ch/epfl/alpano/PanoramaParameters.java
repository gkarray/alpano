package ch.epfl.alpano;

import java.util.Objects;

/**
 * 
 * @author Ghassen Karray (257478)
 */

public final class PanoramaParameters {
	private final GeoPoint observerPosition;
	private final int observerElevation;
	private final double centerAzimuth;
	private final double horizontalFieldOfView;
	private final int maxDistance;
	private final int width;
	private final int height;
	private final double verticalFieldOfView;
	private final double centerX;
	private final double centerY;

	/**
	 * Construit un objet contenant les paramètres passés en argument.
	 * 
	 * @param observerPosition
	 * @param observerElevation
	 * @param centerAzimuth
	 * @param horizontalFieldOfView
	 * @param maxDistance
	 * @param width
	 * @param height
	 * @throws NullPointerException
	 *             si la position de l'observateur est nulle.
	 * @throws IllegalArgumentException
	 *             si l'azimut central n'est pas canonique, si l'angle de vue
	 *             horizontal n'est pas compris entre 0 (exclu) et 2PI (inclu)
	 *             ou si la largeur, hauteur et distance maximales ne sont pas
	 *             strictement positives.
	 */

	public PanoramaParameters(GeoPoint observerPosition, int observerElevation, double centerAzimuth,
			double horizontalFieldOfView, int maxDistance, int width, int height) {
		Preconditions.checkArgument(Azimuth.isCanonical(centerAzimuth));
		Preconditions.checkArgument(horizontalFieldOfView > 0 && horizontalFieldOfView <= Math2.PI2);
		Preconditions.checkArgument(maxDistance > 0 && width > 0 && height > 0);

		this.observerPosition = Objects.requireNonNull(observerPosition);
		this.observerElevation = observerElevation;
		this.centerAzimuth = centerAzimuth;
		this.horizontalFieldOfView = horizontalFieldOfView;
		this.maxDistance = maxDistance;
		this.width = width;
		this.height = height;
		verticalFieldOfView = horizontalFieldOfView * (height - 1) / (width - 1);
		centerX = (width - 1) / 2d;
		centerY = (height - 1) / 2d;
	}

	/**
	 * 
	 * @return Retourne la position de l'observateur.
	 */

	public GeoPoint observerPosition() {
		return observerPosition;
	}

	/**
	 * 
	 * @return Retourne l'altitude de l'observateur.
	 */

	public int observerElevation() {
		return observerElevation;
	}

	/**
	 * 
	 * @return Retourne l'azimuth central.
	 */

	public double centerAzimuth() {
		return centerAzimuth;
	}

	/**
	 * 
	 * @return Retourne l'angle de vue horizontal.
	 */

	public double horizontalFieldOfView() {
		return horizontalFieldOfView;
	}

	/**
	 * 
	 * @return Retourne la distance maximale de visibilité.
	 */

	public int maxDistance() {
		return maxDistance;
	}

	/**
	 * 
	 * @return Retourne la largeur du panorama.
	 */

	public int width() {
		return width;
	}

	/**
	 * 
	 * @return Retourne la hauteur du panorama.
	 */

	public int height() {
		return height;
	}

	/**
	 * 
	 * @return Retourne l'angle de vue vertical.
	 */

	public double verticalFieldOfView() {
		return verticalFieldOfView;
	}

	/**
	 * 
	 * @param x
	 * @throws IllegalArgumentException
	 *             si celui-ci est inférieur à zéro, ou supérieur à la largeur
	 *             moins un.
	 * @return Retourne l'azimut correspondant à l'index de pixel horizontal x.
	 */

	public double azimuthForX(double x) {
		Preconditions.checkArgument(x >= 0 && x <= width - 1);

		return Azimuth.canonicalize(centerAzimuth + Math2.angularDistance(centerX * horizontalFieldOfView / (width - 1), x * horizontalFieldOfView / (width - 1)));
	}

	/**
	 * 
	 * @param a
	 * @throws IllegalArgumentException
	 *             si cet azimut n'appartient pas à la zone visible.
	 * @return Retourne l'index de pixel horizontal correspondant à l'azimut
	 *         donné.
	 */

	public double xForAzimuth(double a) {
		Preconditions.checkArgument(
				a >= centerAzimuth - horizontalFieldOfView / 2d && a <= centerAzimuth + horizontalFieldOfView / 2d);

		return centerX + ((a - centerAzimuth) * (width - 1) / horizontalFieldOfView);
	}

	/**
	 * 
	 * @param y
	 * @throws IllegalArgumentException
	 *             si celui-ci est inférieur à zéro, ou supérieur à la hauteur
	 *             moins un.
	 * @return Retourne l'élévation (nommée, malheureusement, altitude en
	 *         anglais) correspondant à l'index de pixel vertical y.
	 */

	public double altitudeForY(double y) {
		Preconditions.checkArgument(y >= 0 && y <= height - 1);

		return Math2.angularDistance(y * horizontalFieldOfView / (width - 1), centerY * horizontalFieldOfView / (width - 1));
	}

	/**
	 * 
	 * @param a
	 * @throws IllegalArgumentException
	 *             si celle-ci n'appartient pas à la zone visible.
	 * @return Retourne l'index de pixel vertical correspondant à l'élévation
	 *         donnée.
	 */

	public double yForAltitude(double a) {
		Preconditions.checkArgument(a >= altitudeForY(height - 1) && a <= altitudeForY(0));

		return centerY - a / (horizontalFieldOfView / (width - 1));
	}

	/**
	 * 
	 * @param x
	 * @param y
	 * @return Retourne vrai si et seulement si l'index passé est un index de
	 *         pixel valide.
	 */

	boolean isValidSampleIndex(int x, int y) {
		if (x < 0 || y < 0 || x > (width - 1) || y > (height - 1))
			return false;
		else
			return true;
	}

	/**
	 * 
	 * @param x
	 * @param y
	 * @return Retourne l'index linéaire du pixel d'index donné.
	 */

	int linearSampleIndex(int x, int y) {
		assert (isValidSampleIndex(x, y));
		return y * width + x;
	}
}
