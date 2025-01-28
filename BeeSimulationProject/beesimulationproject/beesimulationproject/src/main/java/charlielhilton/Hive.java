package charlielhilton;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import javax.swing.JComponent;

public class Hive extends JComponent
{
    public FloatPoint fpLocation;
    Graphics2D g2;
    public int iAmountStored =0;

    public Hive(Field f)
    {
        int x = (int)(f.getWidth()/2);
		int y = (int)(f.getHeight()/2);
        fpLocation = new FloatPoint(x,y);
        //JLabel population = new JLabel("Population #"+ iBeeSize);
    }

    public int amountStored()
    {
        return iAmountStored;
    }

    public void deliverFood()
    {
        iAmountStored++;
    }

    public void eatFood()
    {
        iAmountStored--;
    }
    public void paint(Graphics g)
    {
        g2 = (Graphics2D)g;
        Shape n = new Ellipse2D.Float((int)fpLocation.getX()-3, (int)fpLocation.getY()-3, 6, 6);
		g2.setPaint(Color.red);
		g2.fill(n);
		g2.draw(n);
    }
}
