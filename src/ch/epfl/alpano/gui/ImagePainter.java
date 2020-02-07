package ch.epfl.alpano.gui;

import javafx.scene.paint.*;

/**
 * 
 * @author Ghassen Karray (257478)
 */

public interface ImagePainter {

	/**
	 * 
	 * @param x
	 * @param y
	 * @return La couleur au point de coordonnée (x,y).
	 */

	Color colorAt(int x, int y);

	/**
	 * 
	 * @param hue
	 * @param saturation
	 * @param brightness
	 * @param opacity
	 * @return Le peintre d'image correspondant aux 4 peintres de cannaux.
	 */

	static ImagePainter hsb(ChannelPainter hue, ChannelPainter saturation, ChannelPainter brightness,
			ChannelPainter opacity) {
		ImagePainter painter = (x, y) -> Color.hsb(hue.valueAt(x, y), saturation.valueAt(x, y),
				brightness.valueAt(x, y), opacity.valueAt(x, y));

		return painter;
	}

	/**
	 * 
	 * @param grayHue
	 * @param opacity
	 * @return Le peintre d'image correspondant aux 2 peintres de cannaux.
	 */

	static ImagePainter gray(ChannelPainter grayHue, ChannelPainter opacity) {
		ImagePainter painter = (x, y) -> Color.gray(grayHue.valueAt(x, y), opacity.valueAt(x, y));

		return painter;
	}
}
