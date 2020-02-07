package ch.epfl.alpano.gui;

/**
 * 
 * @author Ghassen Karray (257478)
 */

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.Locale;
import ch.epfl.alpano.Azimuth;
import ch.epfl.alpano.GeoPoint;
import ch.epfl.alpano.dem.ContinuousElevationModel;
import ch.epfl.alpano.dem.DiscreteElevationModel;
import ch.epfl.alpano.dem.HgtDiscreteElevationModel;
import ch.epfl.alpano.summit.GazetteerParser;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanExpression;
import javafx.beans.property.ObjectProperty;
import javafx.collections.FXCollections;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;

public final class Alpano extends Application {
	public File N45E006 = new File("N45E006.hgt");
	public File N45E007 = new File("N45E007.hgt");
	public File N45E008 = new File("N45E008.hgt");
	public File N45E009 = new File("N45E009.hgt");
	public File N46E006 = new File("N46E006.hgt");
	public File N46E007 = new File("N46E007.hgt");
	public File N46E008 = new File("N46E008.hgt");
	public File N46E009 = new File("N46E009.hgt");
	public File summits = new File("alps.txt");
	public PanoramaParametersBean parameters = new PanoramaParametersBean(PredefinedPanoramas.ALPES_DU_JURA);
	public PanoramaComputerBean computer;

	private void chargeComputer() throws IOException {
		DiscreteElevationModel dem1 = new HgtDiscreteElevationModel(N45E006);
		DiscreteElevationModel dem2 = new HgtDiscreteElevationModel(N45E007);
		DiscreteElevationModel dem3 = new HgtDiscreteElevationModel(N45E008);
		DiscreteElevationModel dem4 = new HgtDiscreteElevationModel(N45E009);
		DiscreteElevationModel dem5 = new HgtDiscreteElevationModel(N46E006);
		DiscreteElevationModel dem6 = new HgtDiscreteElevationModel(N46E007);
		DiscreteElevationModel dem7 = new HgtDiscreteElevationModel(N46E008);
		DiscreteElevationModel dem8 = new HgtDiscreteElevationModel(N46E009);

		DiscreteElevationModel DEM = (dem1.union(dem2).union(dem3).union(dem4))
				.union(dem5.union(dem6).union(dem7).union(dem8));
		ContinuousElevationModel CEM = new ContinuousElevationModel(DEM);

		computer = new PanoramaComputerBean(CEM, GazetteerParser.readSummitsFrom(summits));
	}

	private ImageView createPanoView() {
		ImageView view = new ImageView(computer.getImage());

		view.fitWidthProperty().bind(parameters.widthProperty());
		view.imageProperty().bind(computer.imageProperty());
		view.setSmooth(true);
		view.setPreserveRatio(true);

		view.setOnMouseClicked(e -> {
			GeoPoint point = new GeoPoint(computer.getPanorama().longitudeAt((int) (e.getX()), (int) (e.getY())),
					computer.getPanorama().latitudeAt((int) (e.getX()), (int) (e.getY())));
			Locale l = null;
			String qy = String.format(l, "mlat=%.2f&mlon=%.2f", Math.toDegrees(point.latitude()),
					Math.toDegrees(point.longitude()));
			String fg = String.format(l, "map=15/%.2f/%.2f", Math.toDegrees(point.latitude()),
					Math.toDegrees(point.longitude()));
			try {
				URI osmURI = new URI("http", "www.openstreetmap.org", "/", qy, fg);
				java.awt.Desktop.getDesktop().browse(osmURI);
			} catch (Exception m) {
			}
		});

		return view;
	}

	private TextArea createArea(ImageView view) {
		TextArea area = new TextArea();

		view.setOnMouseMoved(e -> {
			StringBuilder b = new StringBuilder();
			GeoPoint point = new GeoPoint(computer.getPanorama().longitudeAt((int) (e.getX()), (int) (e.getY())),
					computer.getPanorama().latitudeAt((int) (e.getX()), (int) (e.getY())));
			Locale l = null;
			b.append(String.format(l, "Position : %.4f°", Math.toDegrees(point.latitude())));
			if (point.latitude() < 0)
				b.append('S');
			else
				b.append('N');
			b.append(String.format(l, " %.4f°", Math.toDegrees(point.longitude())));
			if (point.longitude() < 0)
				b.append('W');
			else
				b.append('E');
			String newLine = System.getProperty("line.separator");
			b.append(newLine)
					.append(String.format(l, "Distance : %.1f km",
							computer.getPanorama().distanceAt((int) (e.getX()), (int) (e.getY())) / 1000))
					.append(newLine);
			b.append(String.format(l, "Altitude : %d m",
					(int) (computer.getPanorama().elevationAt((int) (e.getX()), (int) (e.getY())))));
			b.append(newLine)
					.append(String.format(l, "Azimut : %.2f ° (%s) \t Elévation : %.2f °",
							Math.toDegrees(parameters.parametersProperty().getValue().panoramaDisplayParameters()
									.azimuthForX(e.getX())),
							Azimuth.toOctantString(parameters.parametersProperty().getValue()
									.panoramaDisplayParameters().azimuthForX(e.getX()), "N", "E", "S", "W"),
							Math.toDegrees(parameters.parametersProperty().getValue().panoramaDisplayParameters()
									.altitudeForY(e.getY()))));
			area.setText(b.toString());
		});

		area.setEditable(false);
		area.setPrefColumnCount(2);
		area.setPrefRowCount(3);

		return area;
	}

	private Pane createLabelsPane() {
		Pane labelsPane = new Pane();

		Bindings.bindContent(labelsPane.getChildren(), computer.getLabels());
		labelsPane.prefHeightProperty().bind(parameters.heightProperty());
		labelsPane.prefWidthProperty().bind(parameters.widthProperty());
		labelsPane.setMouseTransparent(true);

		return labelsPane;
	}

	private StackPane createUpdateNotice() {
		StackPane updateNotice = new StackPane();

		Text updateText = new Text("Les paramètres du panorama ont changé. Cliquez ici pour mettre le dessin à jour.");
		updateText.setFont(new Font(40));
		updateText.setTextAlignment(TextAlignment.CENTER);

		updateNotice.getChildren().add(updateText);
		BooleanExpression parametersNotEqual = parameters.parametersProperty()
				.isNotEqualTo(computer.parametersProperty());
		updateNotice.visibleProperty().bind(parametersNotEqual);
		updateNotice.setBackground(
				new Background(new BackgroundFill(new Color(1, 1, 1, 0.9), CornerRadii.EMPTY, Insets.EMPTY)));

		updateNotice.setOnMouseClicked(e -> computer.setParameters(parameters.parametersProperty().getValue()));

		return updateNotice;
	}

	private StackPane createPanoPane(ImageView view, Pane pane, StackPane notice) {
		StackPane panoGroup = new StackPane();

		panoGroup.getChildren().add(view);
		panoGroup.getChildren().add(pane);
		panoGroup.setAlignment(Pos.CENTER);
		ScrollPane panoScrollPane = new ScrollPane(panoGroup);
		StackPane panoPane = new StackPane();
		panoPane.getChildren().add(panoScrollPane);
		panoPane.getChildren().add(notice);

		return panoPane;
	}

	private GridPane createParamsGrid(TextArea area) {
		GridPane paramsGrid = new GridPane();

		TextField longitudeField = createField(new TextFormatter<Integer>(new FixedPointStringConverter(4)),
				parameters.observerLongitudeProperty(), 7);
		TextField latitudeField = createField(new TextFormatter<Integer>(new FixedPointStringConverter(4)),
				parameters.observerLatitudeProperty(), 7);
		TextField elevationField = createField(new TextFormatter<Integer>(new IntegerStringConverter()),
				parameters.observerElevationProperty(), 4);
		TextField azimuthField = createField(new TextFormatter<Integer>(new IntegerStringConverter()),
				parameters.centerAzimuthProperty(), 3);
		TextField horizontalFieldOfViewField = createField(new TextFormatter<Integer>(new IntegerStringConverter()),
				parameters.horizontalFieldOfViewProperty(), 3);
		TextField maxDistanceField = createField(new TextFormatter<Integer>(new IntegerStringConverter()),
				parameters.maxDistanceProperty(), 3);
		TextField widthField = createField(new TextFormatter<Integer>(new IntegerStringConverter()),
				parameters.widthProperty(), 4);
		TextField heightField = createField(new TextFormatter<Integer>(new IntegerStringConverter()),
				parameters.heightProperty(), 4);
		ChoiceBox exponentBox = new ChoiceBox(FXCollections.observableArrayList(Arrays.asList(0, 1, 2)));
		exponentBox.setConverter(new LabeledListStringConverter("non", "x2", "x4"));
		Bindings.bindBidirectional(exponentBox.valueProperty(), parameters.superSamplingExponentProperty());

		Label longitudeLabel = new Label("Longitude (°) :");
		Label latitudeLabel = new Label("Latitude (°) :");
		Label elevationLabel = new Label("Altitude (m) :");
		Label azimuthLabel = new Label("Azimut (°) :");
		Label horizontalFieldOfViewLabel = new Label("Angle de vue (°) :");
		Label maxDistanceLabel = new Label("Visibilité (km) :");
		Label widthLabel = new Label("Largeur (px) :");
		Label heightLabel = new Label("Hauteur (px) :");
		Label exponentLabel = new Label("Suréchantillonnage :");

		paramsGrid.addRow(0, latitudeLabel, latitudeField, longitudeLabel, longitudeField, elevationLabel,
				elevationField);
		paramsGrid.addRow(1, azimuthLabel, azimuthField, horizontalFieldOfViewLabel, horizontalFieldOfViewField,
				maxDistanceLabel, maxDistanceField);
		paramsGrid.addRow(2, widthLabel, widthField, heightLabel, heightField, exponentLabel, exponentBox);

		GridPane.setHalignment(longitudeLabel, HPos.RIGHT);
		GridPane.setHalignment(latitudeLabel, HPos.RIGHT);
		GridPane.setHalignment(elevationLabel, HPos.RIGHT);
		GridPane.setHalignment(azimuthLabel, HPos.RIGHT);
		GridPane.setHalignment(horizontalFieldOfViewLabel, HPos.RIGHT);
		GridPane.setHalignment(maxDistanceLabel, HPos.RIGHT);
		GridPane.setHalignment(widthLabel, HPos.RIGHT);
		GridPane.setHalignment(heightLabel, HPos.RIGHT);
		GridPane.setHalignment(exponentLabel, HPos.RIGHT);

		paramsGrid.add(area, 7, 0, 40, 3);
		paramsGrid.setHgap(10);
		paramsGrid.setVgap(0);
		paramsGrid.setPadding(new Insets(7, 5, 5, 5));

		return paramsGrid;
	}

	private static TextField createField(TextFormatter<Integer> formatter, ObjectProperty<Integer> property,
			int columns) {
		TextField field = new TextField();

		Bindings.bindBidirectional(formatter.valueProperty(), property);
		field.setTextFormatter(formatter);
		field.setPrefColumnCount(columns);

		return field;
	}

	public static void main(String[] args) {
		Application.launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws IOException {
		chargeComputer();

		PanoramaParametersBean parameters0 = new PanoramaParametersBean(
				new PanoramaUserParameters(0, 0, 0, 0, 0, 0, 0, 0, 0));
		computer.setParameters(parameters0.parametersProperty().getValue());

		ImageView panoView = createPanoView();

		TextArea area = createArea(panoView);

		Pane labelsPane = createLabelsPane();

		StackPane updateNotice = createUpdateNotice();

		StackPane panoPane = createPanoPane(panoView, labelsPane, updateNotice);

		GridPane paramsGrid = createParamsGrid(area);

		BorderPane root = new BorderPane(panoPane, null, null, paramsGrid, null);
		Scene scene = new Scene(root);
		primaryStage.setTitle("Alpano");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
}