package ch.epfl.alpano.gui;

/**
 * 
 * @author Ghassen Karray (257478)
 */

public enum UserParameter {
	OBSERVER_LONGITUDE(60000, 120000), OBSERVER_LATITUDE(450000, 480000), OBSERVER_ELEVATION(300,
			10000), CENTER_AZIMUTH(0, 359), HORIZONTAL_FIELD_OF_VIEW(1,
					360), MAX_DISTANCE(10, 600), WIDTH(30, 16000), HEIGHT(10, 4000), SUPER_SAMPLING_EXPONENT(0, 2);

	private int min;
	private int max;

	private UserParameter(int min, int max) {
		this.min = min;
		this.max = max;
	}

	/**
	 * 
	 * @param val
	 * @return Retourne la valeur valide la plus proche de val.
	 */

	public int sanitize(int val) {
		if (val < this.min)
			return min;
		else if (val > this.max)
			return max;
		else
			return val;
	}
}
