package charlielhilton;
import java.awt.Color;

public class Gradient
{
    public final static Color[] GRADIENT_BLUE_TO_RED = createGradient(Color.BLUE, Color.RED, 500, 0.64);

    public final static Color[] GRADIENT_HOT = createMultiGradient(new Color[]{Color.black, new Color(87, 0, 0), Color.red, Color.orange, Color.yellow, Color.white}, 500);

    public final static Color[] GRADIENT_HEAT = createMultiGradient(new Color[]{Color.black, new Color(105, 0, 0), new Color(192, 23, 0), new Color(255, 150, 38), Color.white}, 500);

    public final static Color[] GRADIENT_ROY = createMultiGradient(new Color[]{Color.red, Color.orange, Color.yellow}, 500);

    public static Color[] createGradient(final Color one, final Color two, final int numSteps, final double opacity)
    {
        int r1 = one.getRed();
        int g1 = one.getGreen();
        int b1 = one.getBlue();
        int a1 = one.getAlpha();

        int r2 = two.getRed();
        int g2 = two.getGreen();
        int b2 = two.getBlue();
        int a2 = two.getAlpha();

        int newR = 0;
        int newG = 0;
        int newB = 0;
        int newA = 0;

        Color[] gradient = new Color[numSteps];
        double iNorm;
        for (int i = 0; i < numSteps; i++)
        {
            iNorm = i / (double)numSteps; //a normalized [0:1] variable
            newR = (int) (r1 + iNorm * (r2 - r1));
            newG = (int) (g1 + iNorm * (g2 - g1));
            newB = (int) (b1 + iNorm * (b2 - b1));
            newA = (int) (70);
            gradient[i] = new Color(newR, newG, newB, newA);
        }
        return gradient;
    }

    public static Color[] createMultiGradient(Color[] colors, int numSteps)
    {
        int numSections = colors.length - 1;
        int gradientIndex = 0; //points to the next open spot in the final gradient
        Color[] gradient = new Color[numSteps];
        Color[] temp;

        if (numSections <= 0)
        {
            throw new IllegalArgumentException("You must pass in at least 2 colors in the array!");
        }

        for (int section = 0; section < numSections; section++)
        {
            //we divide the gradient into (n - 1) sections, and do a regular gradient for each
            temp = createGradient(colors[section], colors[section+1], numSteps / numSections, 0.64);
            for (int i = 0; i < temp.length; i++)
            {
                //copy the sub-gradient into the overall gradient
                gradient[gradientIndex++] = temp[i];
            }
        }

        if (gradientIndex < numSteps)
        {
            for (/* nothing to initialize */; gradientIndex < numSteps; gradientIndex++)
            {
                gradient[gradientIndex] = colors[colors.length - 1];
            }
        }

        return gradient;
    }
}
