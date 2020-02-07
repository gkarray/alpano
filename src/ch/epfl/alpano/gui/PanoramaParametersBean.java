package ch.epfl.alpano.gui;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * 
 * @author Ghassen Karray (257478)
 */

public class PanoramaParametersBean {
	private ObjectProperty<PanoramaUserParameters> parametersProperty;
	private ObjectProperty<Integer> observerLongitudeProperty;
	private ObjectProperty<Integer> observerLatitudeProperty;
	private ObjectProperty<Integer> observerElevationProperty;
	private ObjectProperty<Integer> centerAzimuthProperty;
	private ObjectProperty<Integer> horizontalFieldOfViewProperty;
	private ObjectProperty<Integer> maxDistanceProperty;
	private ObjectProperty<Integer> widthProperty;
	private ObjectProperty<Integer> heightProperty;
	private ObjectProperty<Integer> superSamplingExponentProperty;

	/**
	 * Construit un bean de parametres a partir des parametres utilisateur
	 * parameters.
	 * 
	 * @param parameters
	 */

	public PanoramaParametersBean(PanoramaUserParameters parameters) {
		parametersProperty = new SimpleObjectProperty<>(parameters);
		observerLongitudeProperty = new SimpleObjectProperty<>(parameters.observerLongitude());
		observerLatitudeProperty = new SimpleObjectProperty<>(parameters.observerLatitude());
		observerElevationProperty = new SimpleObjectProperty<>(parameters.observerElevation());
		centerAzimuthProperty = new SimpleObjectProperty<>(parameters.centerAzimuth());
		horizontalFieldOfViewProperty = new SimpleObjectProperty<>(parameters.horizontalFieldOfView());
		maxDistanceProperty = new SimpleObjectProperty<>(parameters.maxDistance());
		widthProperty = new SimpleObjectProperty<>(parameters.width());
		heightProperty = new SimpleObjectProperty<>(parameters.height());
		superSamplingExponentProperty = new SimpleObjectProperty<>(parameters.superSamplingExponent());

		observerLongitudeProperty.addListener((prop, oldV, newV) -> Platform.runLater(this::syncronizeParameters));
		observerLatitudeProperty.addListener((prop, oldV, newV) -> Platform.runLater(this::syncronizeParameters));
		observerElevationProperty.addListener((prop, oldV, newV) -> Platform.runLater(this::syncronizeParameters));
		centerAzimuthProperty.addListener((prop, oldV, newV) -> Platform.runLater(this::syncronizeParameters));
		horizontalFieldOfViewProperty.addListener((prop, oldV, newV) -> Platform.runLater(this::syncronizeParameters));
		maxDistanceProperty.addListener((prop, oldV, newV) -> Platform.runLater(this::syncronizeParameters));
		widthProperty.addListener((prop, oldV, newV) -> Platform.runLater(this::syncronizeParameters));
		heightProperty.addListener((prop, oldV, newV) -> Platform.runLater(this::syncronizeParameters));
		superSamplingExponentProperty.addListener((prop, oldV, newV) -> Platform.runLater(this::syncronizeParameters));
	}

	private void syncronizeParameters() {
		PanoramaUserParameters p = new PanoramaUserParameters(observerLongitudeProperty.get(),
				observerLatitudeProperty.get(), observerElevationProperty.get(), centerAzimuthProperty.get(),
				horizontalFieldOfViewProperty.get(), maxDistanceProperty.get(), widthProperty.get(),
				heightProperty.get(), superSamplingExponentProperty.get());

		parametersProperty.setValue(p);
		observerLongitudeProperty.set(p.observerLongitude());
		observerLatitudeProperty.set(p.observerLatitude());
		observerElevationProperty.set(p.observerElevation());
		centerAzimuthProperty.set(p.centerAzimuth());
		horizontalFieldOfViewProperty.set(p.horizontalFieldOfView());
		maxDistanceProperty.set(p.maxDistance());
		widthProperty.set(p.width());
		heightProperty.set(p.height());
		superSamplingExponentProperty.set(p.superSamplingExponent());
	}

	/**
	 * 
	 * @return Retourne la proprieté parametres.
	 */

	public ReadOnlyObjectProperty<PanoramaUserParameters> parametersProperty() {
		return parametersProperty;
	}

	/**
	 * 
	 * @return Retourne la proprieté longitude.
	 */

	public ObjectProperty<Integer> observerLongitudeProperty() {
		return observerLongitudeProperty;
	}

	/**
	 * 
	 * @return Retourne la proprieté latitude.
	 */

	public ObjectProperty<Integer> observerLatitudeProperty() {
		return observerLatitudeProperty;
	}

	/**
	 * 
	 * @return Retourne la proprieté altitude.
	 */

	public ObjectProperty<Integer> observerElevationProperty() {
		return observerElevationProperty;
	}

	/**
	 * 
	 * @return Retourne la proprieté azimuth central.
	 */

	public ObjectProperty<Integer> centerAzimuthProperty() {
		return centerAzimuthProperty;
	}

	/**
	 * 
	 * @return Retourne la proprieté angle de vue horizontal.
	 */

	public ObjectProperty<Integer> horizontalFieldOfViewProperty() {
		return horizontalFieldOfViewProperty;
	}

	/**
	 * 
	 * @return Retourne la proprieté visibilité.
	 */

	public ObjectProperty<Integer> maxDistanceProperty() {
		return maxDistanceProperty;
	}

	/**
	 * 
	 * @return Retourne la proprieté largeur.
	 */

	public ObjectProperty<Integer> widthProperty() {
		return widthProperty;
	}

	/**
	 * 
	 * @return Retourne la proprieté hauteur.
	 */

	public ObjectProperty<Integer> heightProperty() {
		return heightProperty;
	}

	/**
	 * 
	 * @return Retourne la proprieté degres de surechantillonnage.
	 */

	public ObjectProperty<Integer> superSamplingExponentProperty() {
		return superSamplingExponentProperty;
	}
}
