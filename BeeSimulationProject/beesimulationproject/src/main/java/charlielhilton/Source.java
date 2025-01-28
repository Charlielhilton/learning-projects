package charlielhilton;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import javax.swing.JOptionPane;
import javax.swing.JComponent;

public class Source extends JComponent
{
    public FloatPoint fpLocation;
    public String sName;
    public int iIndex;
    public int iAmount;
    public int iNumber;

    Trail trail;
    Graphics2D g2;

    

    public Source(Field f)
    {
        iAmount = (int)(50);
        fpLocation = new FloatPoint(
            (int)(f.getWidth()/4*3),
            (int)(f.getHeight()-100));
        sName = "Source #" + ++iNumber;
        iIndex = iNumber;
    }

    public void takeBite(Field field)
    {
        iAmount--;
        if(iAmount <= 0)
        {
            System.out.println("Source is depleted");
            JOptionPane.showMessageDialog(field, "Source One ran out of food");
            int iBeeSize=0;
            field.populationSliderChange(iBeeSize);
            field.endSimulation();
            //g2.dispose();
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
        g2.setPaint(Color.MAGENTA);
        
        if(iAmount == 0)
        {
            g2.setPaint(Color.RED);
            //g2.setPaint(Color.RED);
        }
        else{
            g2.fill(n);
            g2.draw(n);
        }
        

    }
}
