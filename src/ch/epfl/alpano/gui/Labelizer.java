package ch.epfl.alpano.gui;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.DoubleUnaryOperator;
import ch.epfl.alpano.Math2;
import ch.epfl.alpano.PanoramaComputer;
import ch.epfl.alpano.PanoramaParameters;
import ch.epfl.alpano.dem.ContinuousElevationModel;
import ch.epfl.alpano.dem.ElevationProfile;
import ch.epfl.alpano.summit.Summit;
import javafx.scene.Node;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

/**
 * 
 * @author Ghassen Karray (257478)
 */

public final class Labelizer {
	private ContinuousElevationModel cDEM;
	private List<Summit> listSummit;

	private static final int MIN_LINE = 20, TOP_LIMIT = 170, TOLERANCE = 200, LARGE_SPACE = 64;

	/**
	 * Construit un etiqueteur de panorama.
	 * 
	 * @param cDEM
	 * @param listSummit
	 */

	public Labelizer(ContinuousElevationModel cDEM, List<Summit> listSummit) {
		this.cDEM = Objects.requireNonNull(cDEM);
		this.listSummit = Objects.requireNonNull(listSummit);
	}

	/**
	 * 
	 * @param parameters
	 * @return Retourne une liste de nœuds JavaFX représentant les étiquettes à
	 *         attacher au panorama correspondant au parametres mis en argument.
	 */

	public List<Node> labels(PanoramaParameters parameters) {
		ArrayList<Node> listNode = new ArrayList<Node>();
		BitSet horizontalPlaces = new BitSet(parameters.width());
		horizontalPlaces.set(MIN_LINE, parameters.width() - MIN_LINE);

		List<ExtendedSummit> visibleExtendedSummits = extendedSummits(listSummit, parameters);

		Collections.sort(visibleExtendedSummits, (y, x) -> {
			if (x.y == y.y)
				return Integer.compare(y.s.elevation(), x.s.elevation());
			else
				return Integer.compare(y.y, x.y);
		});

		final int firstY = visibleExtendedSummits.get(0).y;
		for (int i = 0; i < visibleExtendedSummits.size(); i++) {
			if (horizontalPlaces.nextClearBit(visibleExtendedSummits.get(i).x) > visibleExtendedSummits.get(i).x
					+ MIN_LINE - 1) {
				horizontalPlaces.clear(visibleExtendedSummits.get(i).x, visibleExtendedSummits.get(i).x + MIN_LINE);
				Text t = new Text(visibleExtendedSummits.get(i).s.name() + "("
						+ visibleExtendedSummits.get(i).s.elevation() + " m)");
				t.getTransforms().addAll(new Translate(visibleExtendedSummits.get(i).x, firstY - MIN_LINE - 2),
						new Rotate(-60, 0, 0));
				Line l = new Line(visibleExtendedSummits.get(i).x, firstY - MIN_LINE, visibleExtendedSummits.get(i).x,
						visibleExtendedSummits.get(i).y);
				listNode.add(t);
				listNode.add(l);
			}
		}

		return listNode;
	}

	private List<ExtendedSummit> extendedSummits(List<Summit> listSummit, PanoramaParameters parameters) {
		ArrayList<ExtendedSummit> extendedSummits = new ArrayList<ExtendedSummit>();

		for (int i = 0; i < listSummit.size(); i++) {
			if (isValid(listSummit.get(i), parameters)) {
				extendedSummits.add(new ExtendedSummit(listSummit.get(i), parameters));
			}
		}

		return extendedSummits;
	}

	private boolean isValid(Summit s, PanoramaParameters parameters) {
		if (parameters.observerPosition().distanceTo(s.position()) <= parameters.maxDistance()) {
			double dHorizontal = Math.abs(Math2.angularDistance(parameters.centerAzimuth(),
					parameters.observerPosition().azimuthTo(s.position())));

			if (dHorizontal <= parameters.horizontalFieldOfView() / 2d) {
				double alpha = Math.atan(-(PanoramaComputer.rayToGroundDistance(
						new ElevationProfile(cDEM, parameters.observerPosition(),
								parameters.observerPosition().azimuthTo(s.position()), parameters.maxDistance()),
						parameters.observerElevation(), 0))
								.applyAsDouble(parameters.observerPosition().distanceTo(s.position()))
						/ (parameters.observerPosition().distanceTo(s.position())));

				if (Math.abs(alpha) <= parameters.verticalFieldOfView() / 2d) {
					return isLabelizable(s, parameters);
				}
			}
		}

		return false;
	}

	private boolean isLabelizable(Summit s, PanoramaParameters parameters) {
		double a = PanoramaComputer
				.rayToGroundDistance(
						new ElevationProfile(cDEM, parameters.observerPosition(),
								parameters.observerPosition().azimuthTo(s.position()), parameters.maxDistance()),
						parameters.observerElevation(), 0)
				.applyAsDouble(parameters.observerPosition().distanceTo(s.position()));
		double b = parameters.observerPosition().distanceTo(s.position());
		double alpha = Math.atan(-a / b);
		int xSummit = (int) (Math.round(parameters.xForAzimuth(parameters.observerPosition().azimuthTo(s.position()))));
		int ySummit = (int) (Math.round(parameters.yForAltitude(alpha)));

		if (ySummit >= TOP_LIMIT) {
			ElevationProfile profile = new ElevationProfile(cDEM, parameters.observerPosition(),
					parameters.azimuthForX(xSummit), parameters.maxDistance());
			DoubleUnaryOperator delta = PanoramaComputer.rayToGroundDistance(profile, parameters.observerElevation(),
					Math.tan(parameters.altitudeForY(ySummit)));
			double x = Math2.firstIntervalContainingRoot(delta, 0, parameters.maxDistance(), LARGE_SPACE);

			if (x >= Math.cos(alpha) * parameters.observerPosition().distanceTo(s.position()) - TOLERANCE)
				return true;
		}

		return false;
	}

	private final class ExtendedSummit {
		private final int x, y;
		private final Summit s;

		private ExtendedSummit(Summit s, PanoramaParameters parameters) {
			this.s = s;
			double a = PanoramaComputer
					.rayToGroundDistance(
							new ElevationProfile(cDEM, parameters.observerPosition(),
									parameters.observerPosition().azimuthTo(s.position()), parameters.maxDistance()),
							parameters.observerElevation(), 0)
					.applyAsDouble(parameters.observerPosition().distanceTo(s.position()));
			double b = parameters.observerPosition().distanceTo(s.position());
			double alpha = Math.atan(-a / b);

			x = (int) (Math.round(parameters.xForAzimuth(parameters.observerPosition().azimuthTo(s.position()))));
			y = (int) (Math.round(parameters.yForAltitude(alpha)));
		}
	}
}
