package ch.epfl.alpano.summit;

import java.util.Objects;

import ch.epfl.alpano.*;

/**
 * 
 * @author Ghassen Karray (257478)
 */

public final class Summit {
	private final String name;
	private final int elevation;
	private final GeoPoint position;

	/**
	 * Construit un sommet dont le nom, la position et l'altitude sont ceux
	 * donnés.
	 * 
	 * @param name
	 * @param position
	 * @param elevation
	 */

	public Summit(String name, GeoPoint position, int elevation) {
		this.name = Objects.requireNonNull(name);
		this.position = Objects.requireNonNull(position);
		this.elevation = elevation;
	}

	/**
	 * 
	 * @return Retourne le nom du sommet.
	 */

	public String name() {
		return new String(name);
	}

	/**
	 * 
	 * @return Retourne l'altitude du sommet.
	 */

	public int elevation() {
		return elevation;
	}

	/**
	 * 
	 * @return Retourne la pisition du sommet.
	 */

	public GeoPoint position() {
		return position;
	}

	@Override
	public String toString() {
		return name + " " + position.toString() + " " + elevation;
	}
}
