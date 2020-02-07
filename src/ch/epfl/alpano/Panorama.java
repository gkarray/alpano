package ch.epfl.alpano;

import java.util.Arrays;

/**
 * 
 * @author Ghassen Karray (257478)
 */

public final class Panorama {
	private final PanoramaParameters parameters;
	private final float[] distance;
	private final float[] longitude;
	private final float[] latitude;
	private final float[] elevation;
	private final float[] slope;

	private Panorama(PanoramaParameters parameters, float[] distance, float[] longitude, float[] latitude,
			float[] elevation, float[] slope) {
		this.parameters = parameters;
		this.distance = distance;
		this.longitude = longitude;
		this.latitude = latitude;
		this.elevation = elevation;
		this.slope = slope;
	}

	/**
	 * 
	 * @return Retourne les parametres du panorama.
	 */

	public PanoramaParameters parameters() {
		return parameters;
	}

	/**
	 * 
	 * @param x
	 * @param y
	 * @throws IndexOutOfBoundsException
	 *             si les coordonnées du point passées sont hors des bornes du
	 *             panorama.
	 * @return Retourne la valeur de la distance a l'index donné.
	 */

	public float distanceAt(int x, int y) {
		checkOutOfBounds(x, y);

		return distance[parameters.linearSampleIndex(x, y)];
	}

	/**
	 * 
	 * @param x
	 * @param y
	 * @param d
	 * @throws IndexOutOfBoundsException
	 *             si les coordonnées du point passées sont hors des bornes du
	 *             panorama.
	 * @return Retourne la distance pour le point de coordonnées données, ou la
	 *         valeur par défaut d si les coordonnées sont hors des bornes du
	 *         panorama.
	 */

	public float distanceAt(int x, int y, float d) {
		if (parameters.isValidSampleIndex(x, y))
			return distance[parameters.linearSampleIndex(x, y)];
		else
			return d;
	}

	/**
	 * 
	 * @param x
	 * @param y
	 * @throws IndexOutOfBoundsException
	 *             si les coordonnées du point passées sont hors des bornes du
	 *             panorama.
	 * @return Retourne la valeur de la longitude a l'index donné.
	 */

	public float longitudeAt(int x, int y) {
		checkOutOfBounds(x, y);

		return longitude[parameters.linearSampleIndex(x, y)];
	}

	/**
	 * 
	 * @param x
	 * @param y
	 * @throws IndexOutOfBoundsException
	 *             si les coordonnées du point passées sont hors des bornes du
	 *             panorama.
	 * @return Retourne la valeur de la latitude a l'index donné.
	 */

	public float latitudeAt(int x, int y) {
		checkOutOfBounds(x, y);

		return latitude[parameters.linearSampleIndex(x, y)];
	}

	/**
	 * 
	 * @param x
	 * @param y
	 * @throws IndexOutOfBoundsException
	 *             si les coordonnées du point passées sont hors des bornes du
	 *             panorama.
	 * @return Retourne la valeur de l'altitude a l'index donné.
	 */

	public float elevationAt(int x, int y) {
		checkOutOfBounds(x, y);

		return elevation[parameters.linearSampleIndex(x, y)];
	}

	/**
	 * 
	 * @param x
	 * @param y
	 * @throws IndexOutOfBoundsException
	 *             si les coordonnées du point passées sont hors des bornes du
	 *             panorama.
	 * @return Retourne la valeur de la pente a l'index donné.
	 */

	public float slopeAt(int x, int y) {
		checkOutOfBounds(x, y);

		return slope[parameters.linearSampleIndex(x, y)];
	}

	private void checkOutOfBounds(int x, int y) {
		if (!parameters.isValidSampleIndex(x, y))
			throw new IndexOutOfBoundsException();
	}

	public final static class Builder {
		private final PanoramaParameters parameters;
		private float[] distance;
		private float[] longitude;
		private float[] latitude;
		private float[] elevation;
		private float[] slope;
		private boolean state = false;

		/**
		 * Construit un bâtisseur de panorama dont les paramètres sont ceux
		 * donnés.
		 * 
		 * @param parameters
		 * @throws NullPointerException
		 *             s'ils sont nuls.
		 */

		public Builder(PanoramaParameters parameters) {
			this.parameters = parameters;
			int size = parameters.height() * parameters.width();
			longitude = new float[size];
			latitude = new float[size];
			elevation = new float[size];
			slope = new float[size];
			distance = new float[size];
			Arrays.fill(distance, Float.POSITIVE_INFINITY);
		}

		/**
		 * 
		 * @param x
		 * @param y
		 * @param dist
		 * @throws IllegalArgumentException
		 *             si la methode build() a deja été appelée sur ce
		 *             batisseur.
		 * @throws IndexOutOfBoundsException
		 *             si l'index x,y n'est pas valide.
		 * @return Le batisseur lui même apres avoir changé la valeur de la
		 *         distance a l'index x,y.
		 */

		public Builder setDistanceAt(int x, int y, float dist) {
			if (!parameters.isValidSampleIndex(x, y))
				throw new IndexOutOfBoundsException();
			if (state)
				throw new IllegalStateException();

			distance[parameters.linearSampleIndex(x, y)] = dist;

			return this;
		}

		/**
		 * 
		 * @param x
		 * @param y
		 * @param longi
		 * @throws IllegalArgumentException
		 *             si la methode build() a deja été appelée sur ce
		 *             batisseur.
		 * @throws IndexOutOfBoundsException
		 *             si l'index x,y n'est pas valide.
		 * @return Le batisseur lui même apres avoir changé la valeur de la
		 *         longitude a l'index x,y.
		 */

		public Builder setLongitudeAt(int x, int y, float longi) {
			if (!parameters.isValidSampleIndex(x, y))
				throw new IndexOutOfBoundsException();
			if (state)
				throw new IllegalStateException();

			longitude[parameters.linearSampleIndex(x, y)] = longi;

			return this;
		}

		/**
		 * 
		 * @param x
		 * @param y
		 * @param latit
		 * @throws IllegalArgumentException
		 *             si la methode build() a deja été appelée sur ce
		 *             batisseur.
		 * @throws IndexOutOfBoundsException
		 *             si l'index x,y n'est pas valide.
		 * @return Le batisseur lui même apres avoir changé la valeur de la
		 *         latitude a l'index x,y.
		 */

		public Builder setLatitudeAt(int x, int y, float latit) {
			if (!parameters.isValidSampleIndex(x, y))
				throw new IndexOutOfBoundsException();
			if (state)
				throw new IllegalStateException();

			latitude[parameters.linearSampleIndex(x, y)] = latit;

			return this;
		}

		/**
		 * 
		 * @param x
		 * @param y
		 * @param elev
		 * @throws IllegalArgumentException
		 *             si la methode build() a deja été appelée sur ce
		 *             batisseur.
		 * @throws IndexOutOfBoundsException
		 *             si l'index x,y n'est pas valide.
		 * @return Le batisseur lui même apres avoir changé la valeur de
		 *         l'altitude a l'index x,y.
		 */

		public Builder setElevationAt(int x, int y, float elev) {
			if (!parameters.isValidSampleIndex(x, y))
				throw new IndexOutOfBoundsException();
			if (state)
				throw new IllegalStateException();

			elevation[parameters.linearSampleIndex(x, y)] = elev;

			return this;
		}

		/**
		 * 
		 * @param x
		 * @param y
		 * @param slo
		 * @throws IllegalArgumentException
		 *             si la methode build() a deja été appelée sur ce
		 *             batisseur.
		 * @throws IndexOutOfBoundsException
		 *             si l'index x,y n'est pas valide.
		 * @return Le batisseur lui même apres avoir changé la valeur de la
		 *         pente a l'index x,y.
		 */

		public Builder setSlopeAt(int x, int y, float slo) {
			if (!parameters.isValidSampleIndex(x, y))
				throw new IndexOutOfBoundsException();
			if (state)
				throw new IllegalStateException();

			slope[parameters.linearSampleIndex(x, y)] = slo;

			return this;
		}

		/**
		 * 
		 * @throws IllegalStateException
		 *             si elle a déjà été appelée une fois.
		 * @return Le panorama construit.
		 */

		public Panorama build() {
			if (state)
				throw new IllegalStateException();
			else {
				state = true;
				
				Panorama p = new Panorama(parameters, distance, longitude, latitude, elevation, slope) ;
				
				distance = null ;
				longitude = null ;
				latitude = null ;
				elevation = null ;
				slope = null ;
				
				return p;
			}
		}
	}
}
