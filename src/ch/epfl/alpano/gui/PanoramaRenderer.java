package ch.epfl.alpano.gui;

import ch.epfl.alpano.Panorama;
import javafx.scene.image.*;

/**
 * 
 * @author Ghassen Karray (257478)
 */

public interface PanoramaRenderer {

	/**
	 * 
	 * @param p
	 * @param painter
	 * @return L'image correspondant au panorama p et au peintre d'image
	 *         painter.
	 */

	static Image renderPanorama(Panorama p, ImagePainter painter) {
		WritableImage image = new WritableImage(p.parameters().width(), p.parameters().height());
		PixelWriter writer = image.getPixelWriter();

		for (int j = 0; j < p.parameters().height(); j++) {
			for (int i = 0; i < p.parameters().width(); i++)
				writer.setColor(i, j, painter.colorAt(i, j));
		}

		return image;
	}

}
