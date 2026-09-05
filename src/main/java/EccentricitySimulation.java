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
  private static final String DATA_FILE = "orsa-output.csv";
  private static final String HEX_BLACK = "#000000";
  private static final String HEX_BLUE = "#51b6d5";
  private static final String HEX_DARK_GREY = "#6a7a89";

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
          "Data file " + DATA_FILE + " not found in src/main/resources/."
        );
    }

    ColumnType[] types = {
      ColumnType.INTEGER, ColumnType.DOUBLE, ColumnType.DOUBLE,
      ColumnType.DOUBLE, ColumnType.DOUBLE, ColumnType.DOUBLE,
      ColumnType.DOUBLE
    };
    Table table =
        Table.read().usingOptions(
            CsvReadOptions.builder(inputStream).columnTypes(types)
    );

    IntColumn x = table.intColumn("time");
    DoubleColumn yJupCalc = table.doubleColumn("e Jup calc");
    DoubleColumn yJupRK = table.doubleColumn("e Jup Runge-Kutta");
    DoubleColumn yJupBS = table.doubleColumn("e Jup Bulirsch-Stoer");
    DoubleColumn ySatCalc = table.doubleColumn("e Sat calc");
    DoubleColumn ySatRK = table.doubleColumn("e Sat Runge-Kutta");
    DoubleColumn ySatBS = table.doubleColumn("e Sat Bulirsch-Stoer");

    // Configure plot traces.
    ScatterTrace traceJupExp = ScatterTrace.builder(x, yJupCalc)
        .mode(ScatterTrace.Mode.LINE)
        .name("Jupiter, expected")
        .line(Line.builder().color(HEX_BLACK).dash(Line.Dash.DASH_DOT).build())
        .build();

    ScatterTrace traceJupSimRK = ScatterTrace.builder(x, yJupRK)
        .mode(ScatterTrace.Mode.LINE)
        .name("Jupiter, simulated")
        .line(Line.builder().color(HEX_DARK_GREY).build())
        .build();

    ScatterTrace traceJupSimBS = ScatterTrace.builder(x, yJupBS)
        .mode(ScatterTrace.Mode.LINE)
        .name("Jupiter, simulated")
        .line(Line.builder().color(HEX_DARK_GREY).build())
        .build();

    ScatterTrace traceSatExp = ScatterTrace.builder(x, ySatCalc)
        .mode(ScatterTrace.Mode.LINE)
        .name("Saturn, expected")
        .line(Line.builder().color(HEX_BLACK).dash(Line.Dash.DOT).build())
        .build();

    ScatterTrace traceSatSimRK = ScatterTrace.builder(x, ySatRK)
        .mode(ScatterTrace.Mode.LINE)
        .name("Saturn, simulated")
        .line(Line.builder().color(HEX_BLUE).build())
        .build();

    ScatterTrace traceSatSimBS = ScatterTrace.builder(x, ySatBS)
        .mode(ScatterTrace.Mode.LINE)
        .name("Saturn, simulated")
        .line(Line.builder().color(HEX_BLUE).build())
        .build();

    // Prepare for plotting.
    String plotTitle =
        new java.lang.String("Eccentricity Simulation Over 200,000 Years");
    String xAxis = new String("Years");
    String yAxis = new String("Eccentricity");

    // Plot results of Runge-Kutta simulation.
    Layout layoutRK =
        Layout.builder()
            .title(plotTitle + " (Runge-Kutta)")
            .xAxis(Axis.builder().title(xAxis).build())
            .yAxis(Axis.builder().title(yAxis).build())
            .build();
    Plot.show(new Figure(layoutRK,
        new Trace[] {traceJupExp, traceSatExp, traceJupSimRK, traceSatSimRK}));

    // Plot results of Bulirsch-Stoer simulation.
    Layout layoutBS = Layout.builder()
        .title(plotTitle + " (Bulirsch-Stoer)")
        .xAxis(Axis.builder().title(xAxis).build())
        .yAxis(Axis.builder().title(yAxis).build())
        .build();
    Plot.show(new Figure(layoutBS,
        new Trace[] {traceJupExp, traceSatExp, traceJupSimBS, traceSatSimBS}));
  }
}
