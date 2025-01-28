package charlielhilton;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import javax.swing.JComponent;

public class Bee extends JComponent implements Runnable
{
    public enum BeeState
    {
        foraging, hauling, collecting, following, dead
    };
    public enum TempLocation
    {
        veryHot, hot, natural, cold, veryCold
    };

    FloatPoint fpLocation;
    double dRealX;
    double dRealY;
    private BeeState state;
    private TempLocation tempLoc;
    public Graphics2D g2;

    public int iVector;
    public boolean bAlive;
    public static int iPopulationNum;
    private Thread thr;
    public int iHealth;
    public int iSpeed;

    private int iDelay = 30;
    private Field fParent;
    String sName;
    public Source thisBeeSource;
    public SourceTwo thisBeeSource2;
    public Trail beeTrail;
    public Trail2 beeTrail2;
    public FloatPoint fpThisBeeTarget;
    public int iHotSourceBias;
    public int iColdSourceBias;


    public Bee(Field field, FloatPoint fp)
    {
        //Assigning values to bee
        fParent = field;
        state = BeeState.foraging;
        tempLoc = TempLocation.natural;
        iHealth = 5000;
        iSpeed = 3;
        fpLocation = fp;
        iVector = -1; //Not moving yet
        bAlive = true;
        thr=new Thread(this);
        thr.setName("Bee Number: " + ++iPopulationNum);
        thr.start();
        sName = "Bee #" + iPopulationNum;
    }   

    public void run()
    {
        while (bAlive)
        {
            if(iHealth<=0)
                {
                    //Once health depletes to 0 bee dies
                    fParent.beeDied(sName);
                    state = BeeState.dead;
                    bAlive = false; //Stops switch     
                }
            switch(state)
            {
                
                case dead:
                        bAlive = false;
                        
                break;
                case foraging:
                if(iHealth<=0)
                {
                    fParent.beeDied(sName);    
                    state = BeeState.dead;   
                }
                //  Basically just moves around randomly till it finds a source
                fpLocation = moveFrom(fpLocation);
                    if(senseSource())
                    {
                        state=BeeState.collecting;
                    }
                    Source sourceHint = fParent.senseTrail(fpLocation);
                    if(sourceHint != null) //If the agent senses a trail on the ground follow it
                    {
                        state = BeeState.following;
                        fpThisBeeTarget = sourceHint.fpLocation;
                        beeTrail = sourceHint.trail;
                    }
                    SourceTwo source2Hint = fParent.senseTrail2(fpLocation);
                    if(source2Hint != null) //If the agent senses a trail on the ground follow it
                    {
                        state = BeeState.following;
                        fpThisBeeTarget = source2Hint.fpLocation;
                        beeTrail2 = source2Hint.trail2;
                    }
                    break;
                case hauling:
                if(iHealth<=0)
                {
                    fParent.beeDied(sName);
                    state = BeeState.dead;     
                }
                    //Moves back towards the hive after grabbing from source :)
                    if(fpLocation.distance(fParent.hive.fpLocation)<5)
                    {
                        int stored = fParent.hive.amountStored();
                        if(stored>0 && iHealth<4500)
                        {
                            iHealth = iHealth+500;//Replenishes 500 health points if agent has lost any
                            fParent.hive.eatFood();
                        }
                        fParent.hive.deliverFood(); //Deposits food to hive store
                        state = BeeState.foraging;
                    }
                    else if(fpLocation.distance(fParent.source.fpLocation)<fpLocation.distance(fParent.source2.fpLocation))
                    {
                        thisBeeSource.trail.addStep(fpLocation);
                        fpLocation = moveToTrig(fParent.hive.fpLocation);
                    }
                    else
                    {
                        thisBeeSource2.trail2.addStep(fpLocation);
                        fpLocation = moveToTrig(fParent.hive.fpLocation);
                    }
                    break;
                case following:
                if(iHealth<=0)
                {
                    fParent.beeDied(sName);
                    state = BeeState.dead;   
                }  
                    if(fpLocation.distance(fpThisBeeTarget)<5) //Returns to foraging if source is not applicable
                    {
                        state = BeeState.foraging;
                        fpThisBeeTarget=null;
                    }
                    else
                    {
                        fpLocation = moveToTrig(fpThisBeeTarget);//Follow trail on floor :)
                        if(beeTrail!=null)
                        {
                            beeTrail.decay(fpLocation); //Decays trail after being used
                        }

                        if(beeTrail2!=null)
                        {
                            beeTrail2.decay(fpLocation);
                        }
                    }
                    break;
                case collecting:
                    thisBeeSource = fParent.sourceNear(fpLocation);
                    thisBeeSource2 = fParent.source2Near(fpLocation);
                    if(iHealth<=0)
                    {
                        fParent.beeDied(sName);
                        state = BeeState.dead;
                    }
                        if(thisBeeSource != null ) //As long as source has food remaining take stock 
                        {
                                thisBeeSource.takeBite(fParent);
                                state = BeeState.hauling;
                        }
                        if(thisBeeSource2 != null)
                        {
                                thisBeeSource2.takeBite(fParent);
                                state = BeeState.hauling;
                            
                        }
                        break;
                
                
                    }
            try
                {
                    Thread.currentThread().sleep(iDelay);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }

            switch(tempLoc)
            {
                case veryHot:
                    if(fpLocation.getY()< 120)
                    {
                        iHealth = iHealth - 20;
                        iSpeed= 1;                    
                    }
                    else if(fpLocation.getY()>120)
                    {
                        tempLoc = TempLocation.hot;
                    }
                    break;
                case hot:
                    if(fpLocation.getY()> 120 && fpLocation.getY()<240)
                    {
                        iHealth = iHealth - 15;
                        iSpeed=4; 
                    }
                    else if(fpLocation.getY()<120)
                    {
                        tempLoc = TempLocation.veryHot;
                    }
                    else if(fpLocation.getY()> 240 && fpLocation.getY()<360)
                    {
                        tempLoc = TempLocation.natural;
                    }
                    break;
                case natural:
                    if(fpLocation.getY()> 240 && fpLocation.getY()<360)
                    {
                        iSpeed=3;
                    }
                    else if(fpLocation.getY()> 120 && fpLocation.getY()<240)
                    {
                        tempLoc = TempLocation.hot;
                    }
                    else if(fpLocation.getY()> 360 && fpLocation.getY()<480)
                    {
                        tempLoc = TempLocation.cold;
                    }
                    break;
                case cold:
                    if(fpLocation.getY()> 360 && fpLocation.getY()<480)
                    {
                        iHealth = iHealth - 5;
                        iSpeed=3;
                    }
                    else if(fpLocation.getY()> 240 && fpLocation.getY()<360)
                    {
                        tempLoc = TempLocation.natural;
                    }
                    else if(fpLocation.getY()>480)
                    {
                        tempLoc = TempLocation.veryCold;
                    }
                    break;
                case veryCold:
                    if(fpLocation.getY()> 480)
                    {
                        iHealth = iHealth - 10;
                        iSpeed=2;
                    }
                    else if(fpLocation.getY()> 360 && fpLocation.getY()<480)
                    {
                        tempLoc = TempLocation.cold;
                    }
                try
                {
                    Thread.currentThread().sleep(iDelay);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
    protected FloatPoint moveTo(FloatPoint fp)
    {
        //Maths makes me sad so im cheating how they move to their goal point becuase i like to make problems for myself
        double ax = fpLocation.getX();
		double ay = fpLocation.getY();
		double bx = fp.getX();
		double by = fp.getY();
		double dx = bx-ax;
		double dy = by-ay;
		double sx;
		double sy;
		double scaleFactor;
		double nx;
		double ny;

        scaleFactor = Math.max(Math.abs(dx), Math.abs(dy));
		
		sx = dx/scaleFactor;
		sy = dy/scaleFactor;

		nx = ((ax+sx));
		ny = ((ay+sy));
		
		return new FloatPoint(nx,ny);
    }

    protected FloatPoint moveToTrig(FloatPoint fpLocation2)
    {
        double ax = fpLocation.getX();
		double ay = fpLocation.getY();
		double bx = fpLocation2.getX();
		double by = fpLocation2.getY();
		double dx = bx-ax;
		double dy = by-ay;
		double course = 0;
		double tangent; 
		double sx;
		double sy;
		if (dx >=0){
			if (dy >=0){
				//System.out.println("target is in 2nd quadrant");
				tangent = Math.atan(Math.abs(dy/dx));
				course = Math.PI/2 + tangent;
				sy = Math.sin(course-Math.PI/2);
				sx = Math.sin(Math.PI-course);
			} else {
				//System.out.println("target is in 1st quadrant");
				tangent = Math.atan(Math.abs(dy/dx));
				course = Math.PI/2 - tangent;
				sx = Math.sin(course);
				sy = -Math.sin(Math.PI/2-course);
			}
		} else {
			if (dy>=0) {
				//System.out.println("target is in 3rd quadrant");
				tangent = Math.atan(Math.abs(dy/dx));
				course = (3*Math.PI)/2 - tangent;
				sx = Math.sin(Math.PI-course);
				sy = Math.sin((3*Math.PI)/2-course);
			} else {
				//System.out.println("target is in 4th quadrant");
				tangent = Math.atan(Math.abs(dy/dx));
				course = (3*Math.PI)/2 + tangent;
				sy = -Math.sin(course - (3*Math.PI)/2);
				sx = -Math.sin(2*Math.PI-course);
			}
		}
		return new FloatPoint((ax + sx), (ay + sy));
    }
    private boolean senseSource()
    {
        if(fParent.isSourceNear(fpLocation) || fParent.isSource2Near(fpLocation))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public void paint(Graphics g)
    //Oh my lord do i hate paint classes i stil dont understand them nightmarenightmarenightmarenightmare
    {
        Color c;
		switch (state) {
		case collecting:
			c = Color.yellow;
			break;
		case hauling:
			c = Color.pink;
			break;
		case following:
			c = Color.gray;
			break;
        case dead:
            c = Color.red;
            break;
		default:
			c = Color.black;
		}
		g2 = (Graphics2D) g;
		Shape n = new Ellipse2D.Float((int) fpLocation.getX()-1,
				(int) fpLocation.getY()-1, 2, 2);
		g2.setPaint(c);
		g2.fill(n);
		g2.draw(n);
    }

    protected FloatPoint moveFrom(FloatPoint location2) {
		if (iVector == -1) {
			iVector = (int) (Math.random() * 8.0) + 1; // determine initial
														// vector
		} else {
			int turn = (int) (Math.random() * 3.0) - 1; // determine change of
														// course (-1 to 1)
			iVector = iVector + turn;
			if (iVector > 7) {
				iVector = iVector % 8;
			}
			if (iVector < 0) {
				iVector = iVector + 8;
			}
		}
		// apply vector to get new location
		double x = fpLocation.getX();
		double y = fpLocation.getY();
		switch (iVector) {
		case 0:
			y = y - iSpeed;
			break;
		case 1:
			x = x + iSpeed;
			y = y - iSpeed;
			break;
		case 2:
			x = x + iSpeed;
			break;
		case 3:
			x = x + iSpeed;
			y = y + iSpeed;
			break;
		case 4:
			y = y + iSpeed;
			break;
		case 5:
			x = x - iSpeed;
			y = y + iSpeed;
			break;
		case 6:
			x = x - iSpeed;
			break;
		case 7:
			x = x - iSpeed;
			y = y - iSpeed;
			break;
		}
		if (x>=fParent.getWidth()) x=fParent.getWidth()-1;
		if (x<0) x=0;
		if (y>=fParent.getHeight()) y=fParent.getHeight()-1;
		if (y<0) y=0;
		fpLocation = new FloatPoint(x, y);
		return fpLocation;
	}
    

}