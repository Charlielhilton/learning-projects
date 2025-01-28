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

public class PopulationGraph extends JPanel
{

    public PopulationGraph(JPanel pInfo, Field field)
    {
        TimeSeries populationSeries = new TimeSeries("Population", Millisecond.class);
        final TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(populationSeries);
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
            final double newestDataSourceOne = field.getPopulation();
            populationSeries.add(new Millisecond(), newestDataSourceOne);
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
            "Population", 
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
        axis.setRange(0.0, 400.0); 
        return result;
    }

}
