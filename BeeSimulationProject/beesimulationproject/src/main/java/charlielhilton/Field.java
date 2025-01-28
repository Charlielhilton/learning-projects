package charlielhilton;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import java.util.Timer;
import java.util.TimerTask;

public class Field extends JComponent
{
    Graphics2D g2;
    public int iSourceSize = 2;
    public int iBeeSize = 400;
    public Hive hive;
    public Source source;
    public SourceTwo source2;
    public Bee[] bee = new Bee[iBeeSize];
    public Trail[] trail = new Trail[iSourceSize];
    public Trail2[] trail2 = new Trail2[iSourceSize];

    HeatMap hmPanel;

    JLabel lblSource = new JLabel("Source One");
    JProgressBar sourceHealthBar;

    public Field()
    {
        this.setSize(800,600);
        hive = new Hive(this);
        source = new Source(this);
        source2 = new SourceTwo(this);
        trail[0] = new Trail(this, source);
        trail2[0] = new Trail2(this, source2);


        lblSource.setText("Source One");
        lblSource.setLocation((int)(this.getWidth()/4*3),(int)(this.getHeight()-150));
        lblSource.setVisible(true);
        lblSource.setOpaque(true);
        this.add(lblSource);

        boolean bUseGraphicsYAxis = true;
        double[][] data = HeatMap.generateData();
        hmPanel = new HeatMap(data, bUseGraphicsYAxis, Gradient.GRADIENT_BLUE_TO_RED);
        this.add(hmPanel);

        this.setOpaque(false);

        for(int i=0; i<400;i++)
        {
            bee[i] = new Bee(this, hive.fpLocation);
        }
        //iBeeSize = 100;
    }

    public void paint(Graphics g)
    {
        g2 = (Graphics2D)g;
        Shape r = new Rectangle(800,600);
        g2.setPaint(Color.white);
        g2.fill(r);
        g2.draw(r);

        for(int i=0; i<iSourceSize;i++)
        {
            trail[0].paint(g2);
            trail2[0].paint(g2);
            source.paint(g2);
            source2.paint(g2);
        }

        for(int i=0; i<iBeeSize;i++)
        {
            bee[i].paint(g2);
        }

        hive.paint(g2);
        super.paint(g2);
        super.setOpaque(false);
    }

    public boolean isSourceNear(FloatPoint fpLocation)
    {
        int x = (int)fpLocation.getX();
        int y = (int)fpLocation.getY();

        for(int i=0; i<iSourceSize;i++)
        {
            if(source.iAmount > 0||source2.iAmount > 0)
            {
                if(distance(fpLocation, source.fpLocation, source.iAmount))
                {
                    //System.out.println(source.iAmount + " Source one amount");
                    //System.out.println(source.iAmount + " Source two amount");
                    return true;
                }
            }
            else if(source.iAmount<0 || source2.iAmount<0)
            {
                if(source.iAmount<0)
                {
                    System.out.println("Source 1 depleted first");
                    
                }
                else if(source2.iAmount<0)
                {
                    System.out.println("Source 2 depleted first");
                    
                }
            }
        }
        return false;
    }

    public boolean isSource2Near(FloatPoint fpLocation)
    {
        int x = (int)fpLocation.getX();
        int y = (int)fpLocation.getY();

        for(int i=0; i<1;i++)
        {
            if(source2.iAmount > 0||source2.iAmount > 0)
            {
                if(distance(fpLocation, source2.fpLocation, source2.iAmount))
                {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean distance(FloatPoint p, FloatPoint location, int amount)
    {
        if(p.distance(location)-amount/2 < 5)
        {
            return true;
        }
        else{
            return false;
        }
    }

    public Source sourceNear(FloatPoint location)
    {
        int x = (int)location.getX();
        int y = (int)location.getY();

        for(int i=0; i<iSourceSize;i++)
        {
            if(distance(location, source.fpLocation, source.iAmount))
            {
                return source;
            }
        }
        return null;
    }

    public SourceTwo source2Near(FloatPoint location)
    {
        int x = (int)location.getX();
        int y = (int)location.getY();

        for(int i=0; i<1;i++)
        {
            if(distance(location, source2.fpLocation, source2.iAmount))
            {
                return source2;
            }
        }
        return null;
    }
    
    public synchronized Source senseTrail(FloatPoint location)
    {
		for(int i=0; i<1; i++)
        {
			Trail t = trail[i];
			for (int j=0; j<t.path.size(); j++)
            {
				// is the bee near any point on this trail? 
				if (location.distance(t.path.get(j))<1.0)
                {
					t.decay(location);
					return t.source;
				}
			}
		}
		return null;
	}
    public synchronized SourceTwo senseTrail2(FloatPoint location)
    {
		for(int i=0; i<1; i++)
        {
			Trail2 t2 = trail2[i];
			for (int j=0; j<t2.path.size(); j++)
            {
				// is the bee near any point on this trail? 
				if (location.distance(t2.path.get(j))<1.0)
                {
					t2.decay(location);
					return t2.source;
				}
			}
		}
		return null;
	}

    public void beeDied(String sName)
    {
        for(int i=0; i>iBeeSize;i++)
        {
            if(bee[i].sName.equals(sName))
            {
                this.remove(bee[i]);
            }
        }
        repaint();
        iBeeSize--;
        //boolean bReturn = false;
        //return bReturn;
    }

    public void removeBeeFromObject(String sName)
    {
        iBeeSize = iBeeSize-1;
        Bee bee[] = new Bee[iBeeSize];
        repaint();
    }

    public int getPopulation()
    {
        return iBeeSize;
    }

    public void populationSliderChange(int newPopulation)
    {
        iBeeSize = newPopulation;
        Bee bee[] = new Bee[newPopulation];
        repaint();
    }

    public void endSimulation()
    {
        populationSliderChange(0);
        source.storeChange(0);
        source2.storeChange(0);
        Timer timer = new Timer();
        timer.schedule(new TimerTask(){
        @Override
        public void run()
        {
            System.exit(0);
        }
        }, 0, 100);

    }
}
