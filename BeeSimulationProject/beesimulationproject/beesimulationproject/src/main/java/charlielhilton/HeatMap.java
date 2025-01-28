package charlielhilton;
import javax.swing.JPanel;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;

public class HeatMap extends JPanel
{
    private double[][] data;
    private int[][] dataColorIndices;

    // these four variables are used to print the axis labels
    private double xMin;
    private double xMax;
    private double yMin;
    private double yMax;

    private String title;
    private String xAxis;
    private String yAxis;

    private boolean drawTitle = false;
    private boolean drawXTitle = false;
    private boolean drawYTitle = false;
    private boolean drawLegend = false;
    private boolean drawXTicks = true;
    private boolean drawYTicks = true;

    private Color[] colors;
    private Color bg = Color.white;
    private Color fg = Color.black;

    private BufferedImage bufferedImage;
    private Graphics2D bufferedGraphics;
    public HeatMap(double[][] data, boolean useGraphicsYAxis, Color[] colors)
    {
        super();

        updateGradient(colors);
        updateData(data, useGraphicsYAxis);

        this.setPreferredSize(new Dimension(data.length, data[0].length));
        this.setDoubleBuffered(true);

        this.bg = new Color(0,0,0,0);
        this.fg = new Color(0,0,0,0);
        this.setOpaque(false);
        

        drawData();
    }

    public void setCoordinateBounds(double xMin, double xMax, double yMin, double yMax)
    {
        this.xMin = xMin;
        this.xMax = xMax;
        this.yMin = yMin;
        this.yMax = yMax;
        
        repaint();
    }

    public void setXCoordinateBounds(double xMin, double xMax)
    {
        this.xMin = xMin;
        this.xMax = xMax;
        
        repaint();
    }
    
    public void setXMinCoordinateBounds(double xMin)
    {
        this.xMin = xMin;
        
        repaint();
    }
    public void setXMaxCoordinateBounds(double xMax)
    {
        this.xMax = xMax;
        
        repaint();
    }

    public void setYCoordinateBounds(double yMin, double yMax)
    {
        this.yMin = yMin;
        this.yMax = yMax;
        
        repaint();
    }
    public void setYMinCoordinateBounds(double yMin)
    {
        this.yMin = yMin;
        
        repaint();
    }
    public void setYMaxCoordinateBounds(double yMax)
    {
        this.yMax = yMax;
        
        repaint();
    }
    public void setTitle(String title)
    {
        this.title = title;
        
        repaint();
    }
    public void setDrawTitle(boolean drawTitle)
    {
        this.drawTitle = drawTitle;
        
        repaint();
    }

    public void setXAxisTitle(String xAxisTitle)
    {
        this.xAxis = xAxisTitle;
        
        repaint();
    }
    public void setDrawXAxisTitle(boolean drawXAxisTitle)
    {
        this.drawXTitle = drawXAxisTitle;
        
        repaint();
    }

    public void setYAxisTitle(String yAxisTitle)
    {
        this.yAxis = yAxisTitle;
        
        repaint();
    }

    public void setDrawYAxisTitle(boolean drawYAxisTitle)
    {
        this.drawYTitle = drawYAxisTitle;
        
        repaint();
    }

    public void setDrawLegend(boolean drawLegend)
    {
        this.drawLegend = drawLegend;
        
        repaint();
    }

    public void setDrawXTicks(boolean drawXTicks)
    {
        this.drawXTicks = drawXTicks;
        
        repaint();
    }

    public void setDrawYTicks(boolean drawYTicks)
    {
        this.drawYTicks = drawYTicks;
        
        repaint();
    }

    public void setColorForeground(Color fg)
    {
        this.fg = fg;

        repaint();
    }

    public void setColorBackground(Color bg)
    {
        this.bg = bg;

        repaint();
    }
    public void updateGradient(Color[] colors)
    {
        this.colors = (Color[]) colors.clone();

        if (data != null)
        {
            updateDataColors();

            drawData();

            repaint();
        }
    }

    private void updateDataColors()
    {
        double largest = Double.MIN_VALUE;
        double smallest = Double.MAX_VALUE;
        for (int x = 0; x < data.length; x++)
        {
            for (int y = 0; y < data[0].length; y++)
            {
                largest = Math.max(data[x][y], largest);
                smallest = Math.min(data[x][y], smallest);
            }
        }
        double range = largest - smallest;

        // dataColorIndices is the same size as the data array
        // It stores an int index into the color array
        dataColorIndices = new int[data.length][data[0].length];    

        //assign a Color to each data point
        for (int x = 0; x < data.length; x++)
        {
            for (int y = 0; y < data[0].length; y++)
            {
                double norm = (data[x][y] - smallest) / range; // 0 < norm < 1
                int colorIndex = (int) Math.floor(norm * (colors.length - 1));
                dataColorIndices[x][y] = colorIndex;
            }
        }
    }
    public static double[][] generateData()
    {
        double[][] data = new double[5][5];
        for (int x = 0; x < 5; x++)
        {
            for (int y = 0; y < 5; y++)
            {
                data[x][y] = y;
            }
        }

        return data;
    }    
    public void updateData(double[][] data, boolean useGraphicsYAxis)
    {
        this.data = new double[data.length][data[0].length];
        for (int ix = 0; ix < data.length; ix++)
        {
            for (int iy = 0; iy < data[0].length; iy++)
            {
                // we use the graphics Y-axis internally
                if (useGraphicsYAxis)
                {
                    this.data[ix][iy] = data[ix][iy];
                }
                else
                {
                    this.data[ix][iy] = data[ix][data[0].length - iy - 1];
                }
            }
        }

        updateDataColors();
        
        drawData();

        repaint();
    }
        private void drawData()
    {
        bufferedImage = new BufferedImage(data.length,data[0].length, BufferedImage.TYPE_INT_ARGB);
        bufferedGraphics = bufferedImage.createGraphics();
        
        for (int x = 0; x < data.length; x++)
        {
            for (int y = 0; y < data[0].length; y++)
            {
                bufferedGraphics.setColor(colors[dataColorIndices[x][y]]);
                bufferedGraphics.fillRect(x, y, 1, 1);
            }
        }
    }
        public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        
        int width = this.getWidth();
        int height = this.getHeight();
        
        this.setOpaque(false);

        // clear the panel
        g2d.setColor(bg);
        g2d.fillRect(0, 0, width, height);

        // draw the heat map
        if (bufferedImage == null)
        {
            drawData();
        }
                g2d.drawImage(bufferedImage,
                      0, 0,
                      width,
                      height,
                      0, 0,
                      bufferedImage.getWidth(), bufferedImage.getHeight(),
                      null);

        // border
        g2d.setColor(fg);
        g2d.drawRect(0, 0, width-10, height-10);
        
        // title
        if (drawTitle && title != null)
        {
            g2d.drawString(title, (width / 2) - 4 * title.length(), 20);
        }

        // axis ticks - ticks start even with the bottom left coner, end very close to end of line (might not be right on)
        int numXTicks = (width - 60) / 50;
        int numYTicks = (height - 60) / 50;

        String label = "";
        DecimalFormat df = new DecimalFormat("##.##");

        // Y-Axis ticks
        if (drawYTicks)
        {
            int yDist = (int) ((height - 60) / (double) numYTicks); //distance between ticks
            for (int y = 0; y <= numYTicks; y++)
            {
                g2d.drawLine(26, height - 30 - y * yDist, 30, height - 30 - y * yDist);
                label = df.format(((y / (double) numYTicks) * (yMax - yMin)) + yMin);
                int labelY = height - 30 - y * yDist - 4 * label.length();
                //to get the text to fit nicely, we need to rotate the graphics
                g2d.rotate(Math.PI / 2);
                g2d.drawString(label, labelY, -14);
                g2d.rotate( -Math.PI / 2);
            }
        }

        // Y-Axis title
        if (drawYTitle && yAxis != null)
        {
            //to get the text to fit nicely, we need to rotate the graphics
            g2d.rotate(Math.PI / 2);
            g2d.drawString(yAxis, (height / 2) - 4 * yAxis.length(), -3);
            g2d.rotate( -Math.PI / 2);
        }


        // X-Axis ticks
        if (drawXTicks)
        {
            int xDist = (int) ((width - 60) / (double) numXTicks); //distance between ticks
            for (int x = 0; x <= numXTicks; x++)
            {
                g2d.drawLine(30 + x * xDist, height - 30, 30 + x * xDist, height - 26);
                label = df.format(((x / (double) numXTicks) * (xMax - xMin)) + xMin);
                int labelX = (31 + x * xDist) - 4 * label.length();
                g2d.drawString(label, labelX, height - 14);
            }
        }

        // X-Axis title
        if (drawXTitle && xAxis != null)
        {
            g2d.drawString(xAxis, (width / 2) - 4 * xAxis.length(), height - 3);
        }

        // Legend
        if (drawLegend)
        {
            g2d.drawRect(width - 20, 30, 10, height - 60);
            for (int y = 0; y < height - 61; y++)
            {
                int yStart = height - 31 - (int) Math.ceil(y * ((height - 60) / (colors.length * 1.0)));
                yStart = height - 31 - y;
                g2d.setColor(colors[(int) ((y / (double) (height - 60)) * (colors.length * 1.0))]);
                g2d.fillRect(width - 19, yStart, 9, 1);
                System.out.println(colors.length);
            }
        }
    }
}