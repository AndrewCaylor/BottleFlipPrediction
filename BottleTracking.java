import org.opencv.core.Mat;

public class BottleTracking {

	DirectionOfBottle dir = new DirectionOfBottle();
	VideoCap vc = new VideoCap(1);
	
	private double [] angles;
	private double [] angularVelocities;
	private double [] heights;
		
	public BottleTracking()
	{

	}
	
	public void setAll()
	{
		// frames is black and white
		Mat [] frames = vc.getFilteredFrames();
		
		angles = new double[frames.length];
		heights = new double[frames.length];
		angularVelocities = new double[frames.length - 1];

		
		for (int i = 0; i < frames.length; i++)
		{
			double [] output = dir.getDirection(frames[i]);
			
			if (output != null)
			{
				angles[i] = output[DirectionOfBottle.theta];
				heights[i] = output[DirectionOfBottle.yMid];
			}
			else
			{
				angles[i] = Double.NaN;
				heights[i] = Double.NaN;
			}
		}
		
		for (int i = 0; i < angularVelocities.length; i ++)
		{
			angularVelocities[i] = angles[i + 1] - angles[i];
		}
	}
	
	
}
