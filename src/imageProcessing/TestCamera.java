package imageProcessing;

import java.util.ArrayList;
import java.util.List;
import java.lang.Math;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.*;

public class TestCamera implements Camera
{
	private List<Goal> _goals;
	private Map _map;
	private Point _robotPosition;
	private double _robotHeading;
	private List<Point> _balls;

	void setRobotPosition(Point position){_robotPosition = position;}
	void setRobotHeading(double heading){_robotHeading = heading;}
	void removeBalls(Point position, double range)
	{
		List<Point> newBalls = new ArrayList<Point>();
		for(Point p : _balls)
		{
			double dx = p.x - position.x;
			double dy = p.y - position.y;
			if(dx*dx + dy*dy > range)
				newBalls.add(p); 
		}
		_balls = newBalls;
	}

	
	public TestCamera()
	{
		double _scale = 180.0 / 550.0;
		_map = new Map(new char[640][480], _scale);
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		Mat image = Highgui.imread("TESTDATA.PNG");
		for(int y = 0; y < 480; ++y)
			for(int x = 0; x < 640; ++x)
				_map.obstacle[x][y] = (image.get(y, x)[2] == 255.0 ? '\0' : '\1');
		_goals = new ArrayList<Goal>();
		_goals.add(new Goal(new Point(45, 221, _scale), 4.0, Math.PI));
		_goals.add(new Goal(new Point(585, 221, _scale), 10.0, Math.PI));
		_robotPosition = new Point(170, 210, _scale);
		_robotHeading = 0.0;
		_balls = new ArrayList<Point>();
		_balls.add(new Point(54, 95, _scale));
		_balls.add(new Point(414, 50, _scale));
		_balls.add(new Point(278, 63, _scale));
		_balls.add(new Point(292, 63, _scale));
		_balls.add(new Point(296, 77, _scale));
		_balls.add(new Point(112, 270, _scale));
		_balls.add(new Point(194, 290, _scale));
		_balls.add(new Point(204, 276, _scale));
		_balls.add(new Point(376, 298, _scale));
		_balls.add(new Point(290, 294, _scale));
		_balls.add(new Point(198, 381, _scale));
		_balls.add(new Point(201, 402, _scale));
		_balls.add(new Point(250, 339, _scale));
		_balls.add(new Point(334, 396, _scale));
		_balls.add(new Point(418, 396, _scale));
	}
	public void update(){}
	public void updateRobot(Point expectedPosition, double searchRadius){}
	public void updateBalls(){}
	public Point robotPosition(){return _robotPosition;}
	public double robotHeading(){return _robotHeading;}
	public List<Point> balls(){return _balls;}
	public List<Goal> goals(){return _goals;}
	public Map map(){return _map;}
}
