import java.util.ArrayList;

public class model {

	public  double _heightBalls = .04; //4 cm
	public  double _centerOfMass;
	public  double _angularVelocity;
	public  double _velocity = 0;
	public  double _momentOfInertia;

	public  double massBall = .083; // 83 grams
	public  double massBottle = .033; // 33 grams
	public  double heightBottle = .22; // 22 cm
	public  double radiusOfBall = .02; // 2 cm
	public  double radiusInit = _heightBalls - radiusOfBall; //center of top ball

	public  double initialMomentOfInertia;

	public  double initialAngularVelocity;
	
	public  double spacialHeightInit;
	public  double spacialVelocityInit;

	public  double totalAngleRotated = 0;
	public  ArrayList<Double> totalAnglesRotated = new ArrayList<Double>();
	
	public model(double angularVelocity, double height, double velocity)
	{
		_angularVelocity = angularVelocity;
		initialAngularVelocity = _angularVelocity;
		spacialHeightInit = height;
		spacialVelocityInit = velocity;
	}
	
	public double endAngle()
	{
		_centerOfMass = get_centerOfMass();

		_momentOfInertia = get_momentOfInertia();

		initialMomentOfInertia = _momentOfInertia;
		
		for (int i = 0; i < 1000; i ++)
		{
			_velocity += get_velocity(.001);

			_heightBalls += get_Height(.001);

			if(_heightBalls >= heightBottle)
			{
				while (i < 1000)
				{
					totalAngleRotated += _angularVelocity * .001;
					totalAnglesRotated.add(new Double(totalAngleRotated));
					i ++;
				}
				break;
			}
			_centerOfMass = get_centerOfMass();

			_momentOfInertia = get_momentOfInertia();

			_angularVelocity = get_angularVelocity();	
			
			totalAngleRotated += _angularVelocity * .001;
			
			totalAnglesRotated.add(new Double(totalAngleRotated));
		}
		
		double timeToFall = timeToFall();
		
		timeToFall *= 1000;
		
		int timeToFallInt = (int) timeToFall;
		
		double totalAngleTraveled = totalAnglesRotated.get(timeToFallInt);
		
		return totalAngleTraveled;
	}

	public double timeToFall()
	{
		return (-spacialVelocityInit - Math.sqrt( Math.pow(spacialVelocityInit, 2) + 2*(9.81) * spacialHeightInit )) / -9.81;
	}

	public  double get_centerOfMass()
	{
		return (heightBottle / 2) * ( (massBottle + (2 * massBall * ( _heightBalls / heightBottle ) ) ) / ( massBottle + 2*massBall ) );
	}

	public  double get_velocity(double time)
	{
		return (Math.pow((_heightBalls - ((1/2) * radiusOfBall) - _centerOfMass) * _angularVelocity,2) / ( (_heightBalls - ( (1/2) * radiusOfBall) - _centerOfMass) ) ) * time;
	}
	
	public  double get_Height(double time)
	{
		return _velocity * time;
	}
	
	public  double get_momentOfInertiaBottle()
	{
		return (massBottle / 12) * (6 * Math.pow(radiusOfBall , 2) + Math.pow(heightBottle, 2))  + massBottle * Math.pow((heightBottle/2) - _centerOfMass,2);     
	}
	
	public  double get_momentOfInertiaBallLow()
	{
		return (2/3) * massBall * Math.pow(radiusOfBall , 2) + massBall * (Math.pow(radiusOfBall - _centerOfMass, 2));
	}
	
	public  double get_momentOfInertiaBallHigh()
	{
		return (2/3) * massBall * Math.pow(radiusOfBall , 2) + massBall * (Math.pow(_heightBalls - radiusOfBall - _centerOfMass, 2));
	}
	
	public  double get_momentOfInertia()
	{
		return get_momentOfInertiaBallHigh() + get_momentOfInertiaBallLow() + get_momentOfInertiaBottle();
	}
	
	public  double get_angularVelocity()
	{
		return (initialAngularVelocity * initialMomentOfInertia) / _momentOfInertia;
	}	
	
}
