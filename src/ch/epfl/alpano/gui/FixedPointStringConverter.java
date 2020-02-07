package ch.epfl.alpano.gui;

import java.math.BigDecimal;
import java.math.RoundingMode;

import javafx.util.StringConverter;

/**
 * 
 * @author Ghassen Karray (257478)
 */

public final class FixedPointStringConverter extends StringConverter<Integer> {

	private int scale;

	/**
	 * Construit un convertisseur de chaine FixedPointStringConverter. La
	 * conversion d'une chaîne en entier se fait en interprétant celle-ci comme
	 * un nombre réel, en l'arrondissant à un nombre de décimales fixes et
	 * spécifié au moment de la construction du convertisseur, puis en
	 * «supprimant la virgule» afin d'obtenir un entier. La conversion d'un
	 * entier en chaîne suit le chemin inverse.
	 * 
	 * @param scale
	 */

	public FixedPointStringConverter(int scale) {
		this.scale = scale;
	}

	@Override
	public Integer fromString(String string) {
		double d = Double.parseDouble(string);
		BigDecimal decimal = new BigDecimal(d);
		decimal = decimal.movePointRight(scale);
		decimal = decimal.setScale(scale, BigDecimal.ROUND_HALF_UP);
		decimal = decimal.setScale(0, RoundingMode.HALF_UP);

		return decimal.intValueExact();
	}

	@Override
	public String toString(Integer object) {
		BigDecimal decimal = new BigDecimal(object);
		decimal = decimal.movePointLeft(scale);

		return decimal.toPlainString();
	}

}
