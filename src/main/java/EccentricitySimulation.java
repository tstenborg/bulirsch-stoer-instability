import java.io.InputStream;
import tech.tablesaw.api.ColumnType;
import tech.tablesaw.api.DoubleColumn;
import tech.tablesaw.api.IntColumn;
import tech.tablesaw.api.Table;
import tech.tablesaw.io.csv.CsvReadOptions;
import tech.tablesaw.plotly.Plot;
import tech.tablesaw.plotly.components.Axis;
import tech.tablesaw.plotly.components.Figure;
import tech.tablesaw.plotly.components.Layout;
import tech.tablesaw.plotly.components.Line;
import tech.tablesaw.plotly.traces.ScatterTrace;
import tech.tablesaw.plotly.traces.Trace;

public final class EccentricitySimulation {

  /**
   * The file path, including file extension, of a file that holds the data the
   * program will plot. Should be a CSV file.
   * Class: String.
   */
  private static final String DATA_FILE = "orsa-output.csv";

  /**
   * An RGB hex triplet specifying a black colour for use in plots.
   * Class: String.
   * {@value}
   */
  private static final String HEX_BLACK = "#000000";

  /**
   * An RGB hex triplet specifying a blue colour for use in plots.
   * Class: String.
   * {@value}
   */
  private static final String HEX_BLUE = "#51B6D5";

  /**
   * An RGB hex triplet specifying a dark grey colour for use in plots.
   * Class: String.
   * {@value}
   */
  private static final String HEX_DARK_GREY = "#6A7A89";

  /**
   * A line style (black, dashdot) for use in plots.
   * Class: Line.
   */
  private static final Line BLACK_DASH_DOT =
      Line.builder().color(HEX_BLACK).dash(Line.Dash.DASH_DOT).build();

  /**
   * A line style (black, dotted) for use in plots.
   * Class: Line.
   */
  private static final Line BLACK_DOT =
      Line.builder().color(HEX_BLACK).dash(Line.Dash.DOT).build();

  /**
   * A line style (blue, solid) for use in plots.
   * Class: Line.
   */
  private static final Line BLUE_SOLID =
      Line.builder().color(HEX_BLUE).build();

  /**
   * A line style (dark grey, solid) for use in plots.
   * Class: Line.
   */
  private static final Line DARK_GREY_SOLID =
      Line.builder().color(HEX_DARK_GREY).build();

  /**
   * The number of traces (the data and visual properties of a data series)
   * that appear on each plot. Defined as named constant instead of using
   * magic numbers for array manipulation.
   * Data type: int.
   * {@value}
   */
  public static final int TRACES_PER_PLOT = 4;

  // Configure the class as a non-instantiable utility class.
  private EccentricitySimulation() {
    throw new AssertionError("Utility classes should not be instantiated");
  }

  public static void main(final String[] args) throws Exception {
    // Read packaged CSV data.
    InputStream inputStream =
        EccentricitySimulation.class.getResourceAsStream(DATA_FILE);
    if (inputStream == null) {
      throw new IllegalArgumentException(
          "Data file " + DATA_FILE + " not found in src/main/resources/.");
    }

    ColumnType[] types = {
      ColumnType.INTEGER,
      ColumnType.DOUBLE,
      ColumnType.DOUBLE,
      ColumnType.DOUBLE,
      ColumnType.DOUBLE,
      ColumnType.DOUBLE,
      ColumnType.DOUBLE
    };
    Table table =
        Table.read()
            .usingOptions(
                CsvReadOptions.builder(inputStream).columnTypes(types));

    IntColumn x = table.intColumn("time");
    DoubleColumn yJupiterCalc = table.doubleColumn("e Jup calc");
    DoubleColumn yJupiterRK = table.doubleColumn("e Jup Runge-Kutta");
    DoubleColumn yJupiterBS = table.doubleColumn("e Jup Bulirsch-Stoer");
    DoubleColumn ySaturnCalc = table.doubleColumn("e Sat calc");
    DoubleColumn ySaturnRK = table.doubleColumn("e Sat Runge-Kutta");
    DoubleColumn ySaturnBS = table.doubleColumn("e Sat Bulirsch-Stoer");

    // Configure plot traces.
    ScatterTrace traceJupiterExp =
        ScatterTrace.builder(x, yJupiterCalc)
            .mode(ScatterTrace.Mode.LINE)
            .name("Jupiter, expected")
            .line(BLACK_DASH_DOT)
            .build();

    ScatterTrace traceJupiterSimRK =
        ScatterTrace.builder(x, yJupiterRK)
            .mode(ScatterTrace.Mode.LINE)
            .name("Jupiter, simulated")
            .line(DARK_GREY_SOLID)
            .build();

    ScatterTrace traceJupiterSimBS =
        ScatterTrace.builder(x, yJupiterBS)
            .mode(ScatterTrace.Mode.LINE)
            .name("Jupiter, simulated")
            .line(DARK_GREY_SOLID)
            .build();

    ScatterTrace traceSaturnExp =
        ScatterTrace.builder(x, ySaturnCalc)
            .mode(ScatterTrace.Mode.LINE)
            .name("Saturn, expected")
            .line(BLACK_DOT)
            .build();

    ScatterTrace traceSaturnSimRK =
        ScatterTrace.builder(x, ySaturnRK)
            .mode(ScatterTrace.Mode.LINE)
            .name("Saturn, simulated")
            .line(BLUE_SOLID)
            .build();

    ScatterTrace traceSaturnSimBS =
        ScatterTrace.builder(x, ySaturnBS)
            .mode(ScatterTrace.Mode.LINE)
            .name("Saturn, simulated")
            .line(BLUE_SOLID)
            .build();

    // Prepare plot Strings.
    String plotTitle =
        new java.lang.String("Eccentricity Simulation Over 200,000 Years");
    String xAxis = new String("Years");
    String yAxis = new String("Eccentricity");

    // Prepare plot Traces.
    Trace[] traceEccentricities = new Trace[TRACES_PER_PLOT];
    traceEccentricities[0] = traceJupiterExp;
    traceEccentricities[1] = traceSaturnExp;

    // Plot results of Runge-Kutta simulation.
    Layout layoutRK =
        Layout.builder()
            .title(plotTitle + " (Runge-Kutta)")
            .xAxis(Axis.builder().title(xAxis).build())
            .yAxis(Axis.builder().title(yAxis).build())
            .build();
    traceEccentricities[TRACES_PER_PLOT - 2] = traceJupiterSimRK;
    traceEccentricities[TRACES_PER_PLOT - 1] = traceSaturnSimRK;
    Plot.show(new Figure(layoutRK, traceEccentricities));

    // Plot results of Bulirsch-Stoer simulation.
    Layout layoutBS =
        Layout.builder()
            .title(plotTitle + " (Bulirsch-Stoer)")
            .xAxis(Axis.builder().title(xAxis).build())
            .yAxis(Axis.builder().title(yAxis).build())
            .build();
    traceEccentricities[TRACES_PER_PLOT - 2] = traceJupiterSimBS;
    traceEccentricities[TRACES_PER_PLOT - 1] = traceSaturnSimBS;
    Plot.show(new Figure(layoutBS, traceEccentricities));
  }
}
