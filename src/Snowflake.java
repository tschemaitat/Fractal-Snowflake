import java.awt.*;
import javax.swing.*;
import java.awt.geom.*;
import java.util.*;
import java.util.List;

//Made by Tyler Schemaitat 9/2/22
//Constructs fractal snowflake by creating an array of angles
//limited by resolution, if I could shade pixels instead of drawing lines it might look more realistic
public class Snowflake extends JPanel{
    private static int frameWidth = 1500;
    private static int frameHeight = 1000;
    private static int levels = 10;
    private static double imageSpaceFromRightFactor = 0.2;
    private static double imageSpaceFromTopFactor = 0.02;
    private static final double pi = 3.14159;

    public static void main(String[] args){

        JFrame frame = new JFrame();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new Snowflake());
        frame.setSize(frameWidth, frameHeight);
        frame.setLocation(10, 10);
        frame.setVisible(true);
    }

    protected void paintComponent(Graphics grf){
        super.paintComponent(grf);
        Graphics2D graph = (Graphics2D)grf;
        graph.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        //We gather all the angles changes that makes up the snowflake
        List<Double> snowflakeAngles = new ArrayList<>();
        buildSnowflakeAngles(levels, snowflakeAngles, true);

        int[] x_cord = new int[snowflakeAngles.size() + 2];
        int[] y_cord = new int[snowflakeAngles.size() + 2];
        //to keep it the same size, we divide the length of each line by 3 for each level
        double distance_between_points = 0.9 * getHeight() * Math.pow(0.33, levels);

        y_cord[0] =(int)( imageSpaceFromTopFactor * (double)getHeight());
        x_cord[0] =(int)( imageSpaceFromRightFactor * (double)getWidth());

        //this will be used for having accurate position when we calculate the coordinates
        double y_temp = y_cord[0];
        double x_temp = x_cord[0];
        double currentAngle = pi/2;

        //build the first lines without using an angle (angles only make sense when there's 2 lines)
        y_temp = y_temp + distance_between_points * Math.sin(currentAngle);
        x_temp = x_temp + distance_between_points * Math.cos(currentAngle);
        y_cord[1] = (int)y_temp;
        x_cord[1] = (int)x_temp;

        for(int i = 2; i < 2 + snowflakeAngles.size(); i++){
            //adjust the direction that we are facing in
            currentAngle += snowflakeAngles.get(i - 2);
            //increment our position by a vector
            y_temp = y_temp + distance_between_points * Math.sin(currentAngle);
            x_temp = x_temp + distance_between_points * Math.cos(currentAngle);
            //log the coordinate
            y_cord[i] = (int)y_temp;
            x_cord[i] = (int)x_temp;
        }
        //magical line
        graph.drawPolygon(x_cord, y_cord, x_cord.length);
    }

    //numLevels is how many recursive steps are to be done
    private void buildSnowflakeAngles(int numLevels, List<Double> angles, Boolean firstIteration){
        //these are the angles that build the mini triangle on a line
        double[] baseAngles = {pi / 3, -2 * pi / 3, pi / 3};
        //creates the initial triangle
        if(firstIteration){
            for(int i = 0; i < 3; i++){
                buildSnowflakeAngles(numLevels, angles, false);
                angles.add(-2 * pi / 3);
            }
            return;
        }
        if(numLevels == 0)
            return;

        //injects our 3 base angles in between 4 fractals, making triangles in between each line
        buildSnowflakeAngles(numLevels - 1, angles, false);
        for(int j = 0; j < 3; j++){
            angles.add(baseAngles[j]);
            buildSnowflakeAngles(numLevels - 1, angles, false);
        }
    }
}
