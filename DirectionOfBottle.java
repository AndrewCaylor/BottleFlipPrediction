import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JLabel;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class DirectionOfBottle {

	JFrame frame;
	JLabel outImg;
	
	public static final int x1 = 0;
	public static final int y1 = 1;
	public static final int x2 = 2;
	public static final int y2 = 3;
	
	public static final int theta = 0;
	public static final int xMid = 1;
	public static final int yMid = 2;
	
	public DirectionOfBottle()
	{
		
	}
	
	public double[] getDirection(Mat img)
	{	

		Mat linesXY = getLines(img);
		// Gets x1,y1,x2,y2 coords of lines
		if (linesXY != null)
		{
			double [] angles = getAngles(linesXY);
			
			double [][] midpoints = getMidpoints(linesXY);
			//outImg.setIcon(new ImageIcon(ImageIN.Mat2Buff(img)));
			
			double avgTheta = getAvgTheta(angles); 
			/* adjusts for a break if there is one (if greater than pi/2 subtracts pi)
			 * finds the average
			 * if average is negative it will add pi*/
			
			double [] avgMidpoint = getAvgMidpoint(midpoints);
			// takes the average of all midpoints of lines
			
			return new double [] {avgTheta, avgMidpoint[0], avgMidpoint[1]};
		}
		else
		{
			System.out.println("returned null");
			return null;
		}
	}
	
	public Mat getLines(Mat edgesImg)
	{
		Mat lines = new Mat();
        Imgproc.HoughLinesP(edgesImg, lines, 1, Math.PI/180, 100,50,100); // runs the actual detection
        
		return lines;
	}
	
	private double [] getAngles(Mat lines)
	{
		double [] out = new double [lines.height()];
		for (int i = 0; i < lines.height(); i++) 
        {
			out[i] = Math.atan2(lines.get(i, 0)[y1] - lines.get(i, 0)[y2], lines.get(i, 0)[x1] - lines.get(i, 0)[x2]);
        }
		return out;
	}
	
	public static double getAngle(Mat lines, int i)
	{
		return Math.atan2(lines.get(i, 0)[y1] - lines.get(i, 0)[y2], lines.get(i, 0)[x1] - lines.get(i, 0)[x2]); 
	}
	
	private double [][] getMidpoints(Mat lines)
	{
		double [][] out = new double [lines.height()][2];
		for (int i = 0; i < lines.height(); i++) 
        {
			out[i][0] = (lines.get(i, 0)[x1] + lines.get(i, 0)[x2])/2;
			out[i][1] = (lines.get(i, 0)[y1] + lines.get(i, 0)[y2])/2;
        }
		return out;
	}
	
	private double[] getAvgMidpoint(double [][] midpoints)
	{
		if (midpoints.length == 0)
		{
			return new double [] {0,0}; 
		}
		
		double meanX = 0;
		double meanY = 0;
		
		for (int i = 0; i < midpoints.length; i++) 
        {
			meanX += midpoints[i][0];
			meanY += midpoints[i][1];
        }
		meanX /= midpoints.length;
		meanY /= midpoints.length;
		
		ArrayList<Double> round2midX = new ArrayList<Double>();
		ArrayList<Double> round2midY = new ArrayList<Double>();
		
		for (int i = 0; i < midpoints.length; i ++)
		{
			if (Math.abs( meanX - midpoints[i][0]) < 100 & Math.abs( meanY - midpoints[i][1]) < 100)
			{
				round2midX.add(midpoints[i][0]);
				round2midY.add(midpoints[i][1]);
			}
		}
		
		double XFinalTotal = 0;
		double YFinalTotal = 0;
		
		for (int i = 0; i < round2midX.size(); i++) 
        {
			XFinalTotal += round2midX.get(i);
			YFinalTotal += round2midY.get(i);
        }
		
		return new double [] {XFinalTotal/round2midX.size(), YFinalTotal/round2midY.size()};
	}
	private double getAvgTheta(double [] values) // Adjusts for a break. 0 to pi. raw average theta from where the average would be
	{
		  for (int i = 0; i < values.length; i ++) 
	        {
	        		if (values[i]< 0)
	        		{
	        			values[i] += Math.PI;
	        		}
	        }
		
		double x = 0.0;
        double y = 0.0;
 
    
        double [] coss = new double[values.length];
        double [] sins = new double[values.length];

        
        for (int i = 0; i < values.length; i ++) 
        {        	
            x += Math.cos(values[i]);
            coss[i] = Math.cos(values[i]);
            y += Math.sin(values[i]);
            sins[i] = Math.sin(values[i]);
        }
        
        double meanX = x/values.length;
        double meanY = y/values.length;
		
		
        return Math.atan2(meanY, meanX);
	}
}
