package ch.epfl.alpano.summit;

import java.io.*;
import java.util.*;
import ch.epfl.alpano.*;

/**
 * 
 * @author Ghassen Karray (257478)
 */

public class GazetteerParser {
	private GazetteerParser() {
	}

	/**
	 * 
	 * @param file
	 * @return Retourne un tableau dynamique non modifiable contenant les
	 *         sommets lus depuis le fichier file.
	 * @throws IOException
	 *             en cas d'erreur d'entrée/sortie ou si une ligne du fichier
	 *             n'obéit pas au format.
	 */

	public static List<Summit> readSummitsFrom(File file) throws IOException {
		ArrayList<Summit> list = new ArrayList<Summit>();

		try (BufferedReader b = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
			String s;
			s = b.readLine();

			while (s != null) {
				s = b.readLine();
				if (s != null)
					list.add(newSummit(s));
			}
		} catch (StringIndexOutOfBoundsException | NumberFormatException e) {
			throw new IOException();
		}

		return Collections.unmodifiableList(list);
	}

	private static Summit newSummit(String s) throws IOException {
		String longitude = s.substring(0, 9);
		String latitude = s.substring(10, 18);
		String name = s.substring(36);
		String elevation = s.substring(20, 24);

		return new Summit(name, new GeoPoint(decoup(longitude), decoup(latitude)), Integer.parseInt(elevation.trim()));
	}

	private static double decoup(String s) {
		String[] hms = s.trim().split(":");

		return Math.toRadians(
				Integer.parseInt(hms[0]) + Integer.parseInt(hms[1]) / 60.0 + Integer.parseInt(hms[2]) / 3600.0);
	}
}
