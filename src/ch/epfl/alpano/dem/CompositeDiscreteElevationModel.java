package ch.epfl.alpano.dem;

import java.util.*;
import ch.epfl.alpano.*;

/**
 * 
 * @author Ghassen Karray (257478)
 */

final class CompositeDiscreteElevationModel implements DiscreteElevationModel {
	private final DiscreteElevationModel dem1;
	private final DiscreteElevationModel dem2;
	private final Interval2D extent;

	/**
	 * Construit un MNT discret représentant l'union des MNT donnés.
	 * 
	 * @param dem1
	 * @param dem2
	 * @throws NullPointerException
	 *             si l'un de ces MNT est nul.
	 */

	CompositeDiscreteElevationModel(DiscreteElevationModel dem1, DiscreteElevationModel dem2) {
		this.dem1 = Objects.requireNonNull(dem1);
		this.dem2 = Objects.requireNonNull(dem2);
		extent = dem1.extent().union(dem2.extent());
	}

	@Override
	public void close() throws Exception {
		dem1.close();
		dem2.close();
	}

	@Override
	public Interval2D extent() {
		return extent;
	}

	@Override
	public double elevationSample(int x, int y) {
		Preconditions.checkArgument(this.extent().contains(x, y));

		if (dem1.extent().contains(x, y))
			return dem1.elevationSample(x, y);
		else
			return dem2.elevationSample(x, y);
	}

}
