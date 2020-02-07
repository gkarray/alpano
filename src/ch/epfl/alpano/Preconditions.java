package ch.epfl.alpano;

/**
 * 
 * @author Ghassen Karray (257478)
 */

public interface Preconditions {

	/**
	 * 
	 * @param b
	 * @throws IllegalArgumentException,
	 *             sans message attaché, si son argument est faux.
	 */

	public static void checkArgument(boolean b) {
		if (!b)
			throw new IllegalArgumentException();
	}

	/**
	 * 
	 * @param b
	 * @param message
	 * @throws IllegalArgumentException
	 *             avec le message donné attaché si son argument est faux.
	 */

	public static void checkArgument(boolean b, String message) {
		if (!b)
			throw new IllegalArgumentException(message);
	}
}
