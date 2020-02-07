package ch.epfl.alpano.dem;

import java.io.*;
import java.nio.*;
import java.nio.channels.FileChannel.MapMode;
import ch.epfl.alpano.*;

/**
 * 
 * @author Ghassen Karray (257478)
 */

public final class HgtDiscreteElevationModel implements DiscreteElevationModel {
	private ShortBuffer buffer;
	private final String hgtName;
	private Interval2D extent;

	private static final int LENGTH = 25934402;

	/**
	 * Construit un MNT discret dont les échantillons proviennent du fichier HGT
	 * passé en argument.
	 * 
	 * @param file
	 * @throws IllegalArgumentException
	 *             si le nom du fichier est invalide ou si sa longueur n'est pas
	 *             celle attendue.
	 */

	public HgtDiscreteElevationModel(File file) {
		Preconditions.checkArgument(fileIsGood(file));

		hgtName = file.getName();
		try (FileInputStream s = new FileInputStream(file)) {
			ShortBuffer b = s.getChannel().map(MapMode.READ_ONLY, 0, file.length()).asShortBuffer();
			buffer = b;
		} catch (IOException e) {
			throw new IllegalArgumentException();
		}

		int lat1 = Integer.parseInt(hgtName.substring(1, 3));
		int lon1 = Integer.parseInt(hgtName.substring(4, 7));
		int lat2 = lat1 + 1;
		int lon2 = lon1 + 1;

		if (hgtName.charAt(0) == 'S') {
			lat1 = lat1 * -1;
			lat2 = lat2 * -1;
		}

		if (hgtName.charAt(3) == 'W') {
			lon1 = lon1 * -1;
			lon2 = lon2 * -1;
		}

		lat1 = lat1 * DiscreteElevationModel.SAMPLES_PER_DEGREE;
		lon1 = lon1 * DiscreteElevationModel.SAMPLES_PER_DEGREE;
		lat2 = lat2 * DiscreteElevationModel.SAMPLES_PER_DEGREE;
		lon2 = lon2 * DiscreteElevationModel.SAMPLES_PER_DEGREE;

		extent = new Interval2D(new Interval1D(lon1, lon2), new Interval1D(lat1, lat2));
	}

	private static boolean fileIsGood(File f) {
		String s = f.getName();

		if (s.length() != 11)
			return false;
		if (s.charAt(0) != 'S' && s.charAt(0) != 'N')
			return false;
		if (s.charAt(3) != 'E' && s.charAt(3) != 'W')
			return false;
		if (!(s.substring(7).equals(".hgt")))
			return false;
		if (f.length() != LENGTH)
			return false;
		try {
			if (Integer.parseInt(s.substring(4, 7)) < 0 || Integer.parseInt(s.substring(4, 7)) > 180)
				return false;
			if (Integer.parseInt(s.substring(1, 3)) < 0 || Integer.parseInt(s.substring(1, 3)) > 90)
				return false;
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException();
		}

		return true;
	}

	@Override
	public void close() throws Exception {
		buffer = null;
	}

	@Override
	public Interval2D extent() {
		return extent;
	}

	@Override
	public double elevationSample(int x, int y) {
		Preconditions.checkArgument(this.extent().contains(x, y));

		int newX = x - extent().iX().includedFrom();
		int newY = y - extent().iY().includedTo();
		int i = newX - (DiscreteElevationModel.SAMPLES_PER_DEGREE + 1) * newY;

		return buffer.get(i);
	}

}
