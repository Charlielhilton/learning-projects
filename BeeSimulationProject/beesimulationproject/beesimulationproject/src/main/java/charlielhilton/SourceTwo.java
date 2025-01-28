package charlielhilton;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.beans.*;
import javax.swing.JOptionPane;

import javax.swing.JComponent;
import javax.swing.JProgressBar;

public class SourceTwo extends JComponent
{
    public FloatPoint fpLocation;
    public String sName;
    public int iIndex;
    public int iAmount;
    public int iNumber;
    Trail2 trail2;
    Graphics2D g2;
    JProgressBar sourceHealthBar;

    public SourceTwo(Field f)
    {
        iAmount = (int)(50);
        fpLocation = new FloatPoint(
            (int)(f.getWidth()/4),
            (int)(f.getHeight()/2)-200);
        sName = "Source #" + ++iNumber;
        iIndex = iNumber;

        sourceHealthBar = new JProgressBar(0, iAmount);
        sourceHealthBar.setValue(0);
        sourceHealthBar.setStringPainted(true);
    }

    public void takeBite(Field field)
    {
        iAmount = iAmount - 1;
        if(iAmount <= 0)
        {
            System.out.println("Source 2 depleted");
            JOptionPane.showMessageDialog(field, "Source Two ran out of food");
            int iBeeSize=0;
            field.populationSliderChange(iBeeSize);
            field.endSimulation();
        }
    }

    public int findFoodStore()
    {
        return iAmount;
    }

    public void storeChange(int newStore)
    {
        iAmount = newStore;
        repaint();
    }

    public void paint(Graphics g)
    {
        g2 = (Graphics2D)g;
        Shape n = new Ellipse2D.Float((int)fpLocation.getX()-iAmount/2,(int)fpLocation.getY()-iAmount/2,iAmount, iAmount);
        g2.setPaint(Color.blue);
        if(iAmount == 0)
        {
            g2.setPaint(Color.RED);
        }
        g2.fill(n);
        g2.draw(n);
    }

    public void propertyChange(PropertyChangeEvent pce)
    {
        if ("iAmount" == pce.getPropertyName())
        {
            int progress = (Integer) pce.getNewValue();
            sourceHealthBar.setValue(progress);
        } 
    }
}
