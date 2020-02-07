package ch.epfl.alpano.gui;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.alpano.Preconditions;
import javafx.util.StringConverter;

/**
 * 
 * @author Ghassen Karray (257478)
 */

public final class LabeledListStringConverter extends StringConverter<Integer> {

	private List<String> list;

	/**
	 * Construit un convertisseur de chaine LabeledListStringConverter. La
	 * conversion se fait en fonction d'une liste de chaînes passées au
	 * constructeur sous la forme d'un nombre variable d'arguments : une chaîne
	 * égale à celle à la position n de cette liste est convertie en l'entier n,
	 * et vice versa.
	 * 
	 * @param args
	 */

	public LabeledListStringConverter(String... args) {
		list = new ArrayList<String>();
		for (String string : args) {
			list.add(string);
		}
	}

	@Override
	public Integer fromString(String string) {
		Preconditions.checkArgument(list.contains(string));

		return list.indexOf(string);
	}

	@Override
	public String toString(Integer object) {
		Preconditions.checkArgument(object >= 0 && object < list.size());

		if (object == null)
			return "";

		return list.get(object);
	}

}
