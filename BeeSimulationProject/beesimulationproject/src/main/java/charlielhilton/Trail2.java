package charlielhilton;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Ellipse2D;
import java.util.Date;
import java.util.Vector;

public class Trail2
{
    public Vector<Point> path;
    Graphics2D g2;
	SourceTwo source;
	Field field;
	long created;
	long maxTime = 10000; // 10 second lifetime on trails
	
	public Trail2(Field fld, SourceTwo f) {
		this.field = fld;
		path = new Vector<Point>();
		this.source = f;
		f.trail2 = this;
		Date now = new Date();
		created = now.getTime();
	}
	
	public void addStep(FloatPoint location) {
		Point p = new Point(location.getIntX(), location.getIntY());
		path.add(p);
		Long now = new Date().getTime();
		created = now;
	}

	public void paint(Graphics g) {
		g2 = (Graphics2D)g;
		g2.setColor(Color.white);
		for (int i=0; i<path.size(); i++){
			g2.draw(new Ellipse2D.Double(path.get(i).x, path.get(i).y, 1,1));
		}
	}

	public synchronized void decay(FloatPoint location) {
		Point p = new Point(location.getIntX(), location.getIntY());
		path.remove(p);
		//System.out.println("Trail decayed");
		Long now = new Date().getTime();
		if (now - created > maxTime) {
			path.removeAllElements();
		}
	}   
}

