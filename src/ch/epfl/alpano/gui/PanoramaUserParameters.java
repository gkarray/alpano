package ch.epfl.alpano.gui;

import java.util.EnumMap;
import java.util.Map;

import ch.epfl.alpano.Azimuth;
import ch.epfl.alpano.GeoPoint;
import ch.epfl.alpano.PanoramaParameters;

/**
 * 
 * @author Ghassen Karray (257478)
 */

public final class PanoramaUserParameters {

	private final Map<UserParameter, Integer> parameters;

	private static final double DEGREES_PRECISION = 10000;
	private static final int KM_TO_M = 1000;

	/**
	 * Construit un objet PanoramaUserParameters a partir d'une table
	 * associative associant a chaque UserParameter une valeur Integer.
	 * 
	 * @param map
	 */

	public PanoramaUserParameters(Map<UserParameter, Integer> map) {
		parameters = new EnumMap<>(UserParameter.class);

		for (Map.Entry<UserParameter, Integer> entry : map.entrySet()) {
			if (entry.getKey() != UserParameter.HEIGHT)
				parameters.put(entry.getKey(), entry.getKey().sanitize(entry.getValue()));
			else {
				if (entry.getValue() <= heightLimit(parameters))
					parameters.put(entry.getKey(), entry.getKey().sanitize(entry.getValue()));
				else
					parameters.put(entry.getKey(), entry.getKey().sanitize((int) Math.floor(heightLimit(parameters))));
			}
		}
	}

	/**
	 * Construit un objet PanoramaUserParameters a partir des valeurs des
	 * parametres directement.
	 * 
	 * @param longi
	 * @param latit
	 * @param elev
	 * @param azi
	 * @param horiz
	 * @param maxdis
	 * @param width
	 * @param height
	 * @param sse
	 */

	public PanoramaUserParameters(int longi, int latit, int elev, int azi, int horiz, int maxdis, int width, int height,
			int sse) {
		this(method(longi, latit, elev, azi, horiz, maxdis, width, height, sse));
	}

	/**
	 * 
	 * @param param
	 * @return La valeur du paramètre passé en argument.
	 */

	public int get(UserParameter param) {
		return parameters.get(param);
	}

	/**
	 * 
	 * @return La valeur du parametre longitude.
	 */

	public int observerLongitude() {
		return parameters.get(UserParameter.OBSERVER_LONGITUDE);
	}

	/**
	 * 
	 * @return La valeur du parametre latitude.
	 */

	public int observerLatitude() {
		return parameters.get(UserParameter.OBSERVER_LATITUDE);
	}

	/**
	 * 
	 * @return La valeur du parametre altitude.
	 */

	public int observerElevation() {
		return parameters.get(UserParameter.OBSERVER_ELEVATION);
	}

	/**
	 * 
	 * @return La valeur du parametre azimuth central.
	 */

	public int centerAzimuth() {
		return parameters.get(UserParameter.CENTER_AZIMUTH);
	}

	/**
	 * 
	 * @return La valeur du parametre angle de vue horizontal.
	 */

	public int horizontalFieldOfView() {
		return parameters.get(UserParameter.HORIZONTAL_FIELD_OF_VIEW);
	}

	/**
	 * 
	 * @return La valeur du parametre visibilité.
	 */

	public int maxDistance() {
		return parameters.get(UserParameter.MAX_DISTANCE);
	}

	/**
	 * 
	 * @return La valeur du parametre largeur.
	 */

	public int width() {
		return parameters.get(UserParameter.WIDTH);
	}

	/**
	 * 
	 * @return La valeur du parametre hauteur.
	 */

	public int height() {
		return parameters.get(UserParameter.HEIGHT);
	}

	/**
	 * 
	 * @return La valeur du parametre degres de surechantillonnage.
	 */

	public int superSamplingExponent() {
		return parameters.get(UserParameter.SUPER_SAMPLING_EXPONENT);
	}

	/**
	 * 
	 * @return Les parametres du panorama tel qu'il sera calculé.
	 */

	public PanoramaParameters panoramaParameters() {

		GeoPoint position = new GeoPoint(
				Math.toRadians(parameters.get(UserParameter.OBSERVER_LONGITUDE) / DEGREES_PRECISION),
				Math.toRadians(parameters.get(UserParameter.OBSERVER_LATITUDE) / DEGREES_PRECISION));
		int elevation = parameters.get(UserParameter.OBSERVER_ELEVATION);
		double azimuth = Azimuth.canonicalize(Math.toRadians(parameters.get(UserParameter.CENTER_AZIMUTH)));
		double field = Math.toRadians(parameters.get(UserParameter.HORIZONTAL_FIELD_OF_VIEW));
		int distance = parameters.get(UserParameter.MAX_DISTANCE) * KM_TO_M;
		int width = parameters.get(UserParameter.WIDTH)
				* (int) Math.pow(2, parameters.get(UserParameter.SUPER_SAMPLING_EXPONENT));
		int height = parameters.get(UserParameter.HEIGHT)
				* (int) Math.pow(2, parameters.get(UserParameter.SUPER_SAMPLING_EXPONENT));

		return new PanoramaParameters(position, elevation, azimuth, field, distance, width, height);
	}

	/**
	 * 
	 * @return Les parametres du panorama tel qu'il sera affiché.
	 */

	public PanoramaParameters panoramaDisplayParameters() {
		GeoPoint position = new GeoPoint(
				Math.toRadians(parameters.get(UserParameter.OBSERVER_LONGITUDE) / DEGREES_PRECISION),
				Math.toRadians(parameters.get(UserParameter.OBSERVER_LATITUDE) / DEGREES_PRECISION));
		int elevation = parameters.get(UserParameter.OBSERVER_ELEVATION);
		double azimuth = Azimuth.canonicalize(Math.toRadians(parameters.get(UserParameter.CENTER_AZIMUTH)));
		double field = Math.toRadians(parameters.get(UserParameter.HORIZONTAL_FIELD_OF_VIEW));
		int distance = parameters.get(UserParameter.MAX_DISTANCE) * KM_TO_M;
		int width = parameters.get(UserParameter.WIDTH);
		int height = parameters.get(UserParameter.HEIGHT);

		return new PanoramaParameters(position, elevation, azimuth, field, distance, width, height);
	}

	@Override
	public boolean equals(Object o) {
		if (o == null)
			return false;
		else {
			if (o instanceof PanoramaUserParameters) {
				return parameters.equals(((PanoramaUserParameters) o).parameters);
			} else
				return false;
		}
	}

	@Override
	public int hashCode() {
		return parameters.hashCode();
	}

	private static Map<UserParameter, Integer> method(int longi, int latit, int elev, int azi, int horiz, int maxdis,
			int width, int height, int sse) {
		Map<UserParameter, Integer> temp = new EnumMap<>(UserParameter.class);
		temp.put(UserParameter.OBSERVER_LONGITUDE, longi);
		temp.put(UserParameter.OBSERVER_LATITUDE, latit);
		temp.put(UserParameter.OBSERVER_ELEVATION, elev);
		temp.put(UserParameter.CENTER_AZIMUTH, azi);
		temp.put(UserParameter.HORIZONTAL_FIELD_OF_VIEW, horiz);
		temp.put(UserParameter.MAX_DISTANCE, maxdis);
		temp.put(UserParameter.WIDTH, width);
		temp.put(UserParameter.HEIGHT, height);
		temp.put(UserParameter.SUPER_SAMPLING_EXPONENT, sse);

		return temp;
	}

	private double heightLimit(Map<UserParameter, Integer> map) {
		return 170 * (map.get(UserParameter.WIDTH) - 1) / (map.get(UserParameter.HORIZONTAL_FIELD_OF_VIEW)) + 1;
	}

}
