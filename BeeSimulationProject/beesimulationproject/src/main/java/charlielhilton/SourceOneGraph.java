package charlielhilton;

import java.awt.Color;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import javax.swing.JPanel;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A demonstration application showing a time series chart where you can dynamically add
 * (random) data by clicking on a button.
 *
 */
public class SourceOneGraph extends JPanel 
{
    public SourceOneGraph(JPanel pInfo, Source source, SourceTwo sourceTwo, Hive hive)
    {
        TimeSeries sourceOneSeries = new TimeSeries("Source One", Millisecond.class);
        TimeSeries sourceTwoSeries = new TimeSeries("Source Two", Millisecond.class);
        TimeSeries hiveStore = new TimeSeries ("Hive food store", Millisecond.class);
        final TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(sourceOneSeries);
        dataset.addSeries(sourceTwoSeries);
        dataset.addSeries(hiveStore);
        final JFreeChart chart = createChart(dataset);
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setBackground(Color.decode("#F7f7c9"));
        chartPanel.setPreferredSize(new java.awt.Dimension(195, 270));

        pInfo.add(chartPanel);

        Timer timer = new Timer();
        timer.schedule(new TimerTask(){
        @Override
        public void run()
        {
            final double newestDataSourceOne = source.findFoodStore();
            sourceOneSeries.add(new Millisecond(), newestDataSourceOne);

            final double newestDataSourceTwo = sourceTwo.findFoodStore();
            //final Millisecond now = new Millisecond();
            sourceTwoSeries.add(new Millisecond(), newestDataSourceTwo);

            final double newestDataHiveStore = hive.amountStored();
            //final Millisecond now = new Millisecond();
            hiveStore.add(new Millisecond(), newestDataHiveStore);
        }
        }, 0, 1000);
    }

    /**
     * Creates a sample chart.
     * 
     * @param dataset  the dataset.
     * 
     * @return A sample chart.
     */
    private JFreeChart createChart(final XYDataset dataset) {
        final JFreeChart result = ChartFactory.createTimeSeriesChart(
            "Food Stores", 
            "Time", 
            "Food Stored",
            dataset, 
            true, 
            true, 
            false
        );
        final XYPlot plot = result.getXYPlot();
        ValueAxis axis = plot.getDomainAxis();
        axis.setAutoRange(true);
        axis.setFixedAutoRange(60000.0);  // 60 seconds
        axis = plot.getRangeAxis();
        axis.setRange(0.0, 100.0); 
        return result;
    }

}
