
public class Prediction {

	private double [] angles;
	private double [] angularVelocities;
	private double [] heights;
	
	public Prediction(double [] a, double [] av, double [] h)
	{
		angles = a;
		angularVelocities = av;
		heights = h;
	}
	
	public double timeToHitGround(double height, double Vi)
	{
		return (2 * height) / ( Vi + Math.sqrt(Math.pow(Vi, 2) + 2 * 9.81 * height) );
	}
	
	
}
