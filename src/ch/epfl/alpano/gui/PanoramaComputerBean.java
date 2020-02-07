package ch.epfl.alpano.gui;

import java.util.List;

import ch.epfl.alpano.Panorama;
import ch.epfl.alpano.PanoramaComputer;
import ch.epfl.alpano.dem.ContinuousElevationModel;
import ch.epfl.alpano.summit.Summit;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.image.Image;

/**
 * 
 * @author Ghassen Karray (257478)
 */

public class PanoramaComputerBean {
	private ObjectProperty<PanoramaUserParameters> parametersProperty;
	private ObjectProperty<Panorama> panoramaProperty;
	private ObjectProperty<Image> imageProperty;
	private ObjectProperty<ObservableList<Node>> labelsProperty;
	private List<Summit> listSummit;
	private ContinuousElevationModel CEM;

	/**
	 * Construit un bean calculateur de panorama.
	 * 
	 * @param CEM
	 * @param listSummit
	 */

	public PanoramaComputerBean(ContinuousElevationModel CEM, List<Summit> listSummit) {
		parametersProperty = new SimpleObjectProperty<>();
		panoramaProperty = new SimpleObjectProperty<>();
		imageProperty = new SimpleObjectProperty<>();
		labelsProperty = new SimpleObjectProperty<>(FXCollections.observableArrayList());

		this.listSummit = listSummit;
		this.CEM = CEM;

		parametersProperty.addListener((prop, oldV, newV) -> syncronizeParameters());
	}

	private void syncronizeParameters() {
		Panorama p = (new PanoramaComputer(CEM)).computePanorama(parametersProperty.getValue().panoramaParameters());

		if (parametersProperty.getValue().superSamplingExponent() != 0) {
			Panorama q = (new PanoramaComputer(CEM))
					.computePanorama(parametersProperty.getValue().panoramaDisplayParameters());

			panoramaProperty.setValue(q);
		} else
			panoramaProperty.setValue(p);

		ChannelPainter hue = (x, y) -> {
			ChannelPainter temp = (s, t) -> p.distanceAt(s, t) / 100000;
			return temp.cycling().valueAt(x, y) * 360;
		};
		ChannelPainter saturation = (x, y) -> {
			ChannelPainter temp = (s, t) -> p.distanceAt(s, t) / 200000;
			return temp.clamped().inverted().valueAt(x, y);
		};
		ChannelPainter brightness = (x, y) -> {
			ChannelPainter temp = (s, t) -> 2 * p.slopeAt(s, t) / (float) Math.PI;
			return (float) 0.3 + ((float) 0.7 * temp.inverted().valueAt(x, y));
		};
		ChannelPainter opacity = (x, y) -> p.distanceAt(x, y) == Float.POSITIVE_INFINITY ? 0 : 1;
		ImagePainter lm = ImagePainter.hsb(hue, saturation, brightness, opacity);
		Image i = PanoramaRenderer.renderPanorama(p, lm);

		imageProperty.setValue(i);

		ObservableList<Node> list = FXCollections.observableArrayList(
				(new Labelizer(CEM, listSummit)).labels(parametersProperty.getValue().panoramaDisplayParameters()));
		list = FXCollections.unmodifiableObservableList(list);

		labelsProperty.getValue().setAll(list);
	}

	/**
	 * 
	 * @return Retourne la proprieté parametres.
	 */

	public ObjectProperty<PanoramaUserParameters> parametersProperty() {
		return parametersProperty;
	}

	/**
	 * 
	 * @return Retourne la valeur de la proprieté parametres.
	 */

	public PanoramaUserParameters getParameters() {
		return parametersProperty().getValue();
	}

	/**
	 * Met la valeur de la propriété parametres a newParameters.
	 * 
	 * @param newParameters
	 */

	public void setParameters(PanoramaUserParameters newParameters) {
		if (parametersProperty.getValue() == null) {
			parametersProperty = new SimpleObjectProperty<>(newParameters);

			parametersProperty.addListener((prop, oldV, newV) -> syncronizeParameters());
		} else
			parametersProperty.setValue(newParameters);
	}

	/**
	 * 
	 * @return Retourne la proprieté panorama.
	 */

	public ReadOnlyProperty<Panorama> panoramaProperty() {
		return panoramaProperty;
	}

	/**
	 * 
	 * @return Retourne la valeur de la proprieté parametres.
	 */

	public Panorama getPanorama() {
		return panoramaProperty().getValue();
	}

	/**
	 * 
	 * @return Retourne la proprieté image.
	 */

	public ReadOnlyProperty<Image> imageProperty() {
		return imageProperty;
	}

	/**
	 * 
	 * @return Retourne la valeur de la proprieté parametres.
	 */

	public Image getImage() {
		return imageProperty().getValue();
	}

	/**
	 * 
	 * @return Retourne la proprieté labels.
	 */

	public ReadOnlyProperty<ObservableList<Node>> labelsProperty() {
		return labelsProperty;
	}

	/**
	 * 
	 * @return Retourne la valeur de la proprieté parametres.
	 */

	public ObservableList<Node> getLabels() {
		return labelsProperty().getValue();
	}
}
