import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class Main {

	public double getExpectedEndAngle(double height )
	{
		DirectionOfBottle b = new DirectionOfBottle();
		
		VideoCap v = new VideoCap(1);
				
		Mat [] matsFiltered = v.getFilteredFrames();
		
		double [][] directions = new double[2][3];
		
		directions[0] = b.getDirection(matsFiltered[0]);
		directions[1] = b.getDirection(matsFiltered[0]);

		// 1 cm is 30 pixels at the distance I videoed
		// 1 m = 3000 pixels
		
		double yVelocity = ((directions[1][2] - directions[0][2]) / 3000) * 30;
		double angularVelocity = (directions[1][0] - directions[0][0]) * 30;
		
		double angleInitial = directions[0][0];
		double heightInitial = directions[0][2] / 3000;
		
		model m = new model(angularVelocity, heightInitial, yVelocity);
		return angleInitial - m.endAngle();
		
	}
	
	public static void main (String [] args)
	{
		for (int i = 1; i <= 10; i ++)
		{
			run(i);
		}
	}
	
	public static void run (int fileint)
	{
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
		double adjustment = 0;
		
		DirectionOfBottle b = new DirectionOfBottle();
		
		VideoCap v = new VideoCap(fileint);
		
		Mat [] matsFiltered = v.getFilteredFrames();
		
		double [][] directions = new double[matsFiltered.length][3];
		
		double [] directionsOut = new double[directions.length];
		
		for (int i = 0; i < matsFiltered.length; i ++)
		{			
			directions[i] = b.getDirection(matsFiltered[i]);
			
			try {
				if (Math.abs(directions[i][0] - directions[i-1][0]) > Math.PI/2)
				{
					adjustment -= Math.PI;
				}
			}
			catch(Exception e)
			{
				
			}
			
			directionsOut[i] = directions[i][0] + adjustment;
			//System.out.println(directions[i][0] + adjustment);
			//System.out.println((1720 - directions[i][2])/3000);
			
			Mat lines = b.getLines(matsFiltered[i]);

			Mat out = new Mat();
			
			Imgproc.cvtColor(matsFiltered[i], out, Imgproc.COLOR_GRAY2RGB);			
						
			Mat toDesktopLines = ImageIN.getImgXYPoints(out, lines);

			toDesktopLines = ImageIN.drawLine(toDesktopLines,directions[i]);

			Imgcodecs.imwrite("/Users/Drew/Desktop/flips" + "/lines" + i +".jpg", toDesktopLines);
			Imgcodecs.imwrite("/Users/Drew/Desktop/flips" + "/filtered" + i +".jpg", matsFiltered[i]);
			
		}
		System.out.println("\n\n\nvideo: " + fileint);

		for (int i = 0; i < directionsOut.length; i ++)
		{
			System.out.println(directionsOut[i]);
		}
		
		System.out.println("-----");
		
		for (int i = 0; i < directions.length; i ++)
		{
			System.out.println((1720 - directions[i][2])/3000);
		}
		
		double yVelocity = ((-directions[1][2] + directions[0][2]) / 3000) * 30;
		double angularVelocity = (Math.abs(directionsOut[0] - directionsOut[1])) * 30;
		
		double angleInitial = directionsOut[0];
		double heightInitial = ((1720 - directions[0][2])/3000);
		
		model m = new model(angularVelocity, heightInitial, yVelocity);
		
		double angleTraveled = m.endAngle();
		
		double guess = angleInitial - angleTraveled;
		
		System.out.println("Guess: " + guess);
	}
	
}
