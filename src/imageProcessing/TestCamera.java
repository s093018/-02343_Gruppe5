package imageProcessing;

import java.util.ArrayList;
import java.util.List;
import java.lang.Math;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.*;

public class TestCamera implements Camera
{
	private List<Goal> goals;
	private Map map;
	private List<Point> balls;
	private Robot robot;

	public void setRobotPosition(Point position){robot = new Robot(position, robot.heading);}
	public void setRobotHeading(double heading){robot = new Robot(robot.position, heading);}
	public void removeBalls(Point position, double range)
	{
		List<Point> newBalls = new ArrayList<Point>();
		for(Point p : balls)
		{
			double dx = p.x - position.x;
			double dy = p.y - position.y;
			if(dx*dx + dy*dy > range)
				newBalls.add(p); 
		}
		balls = newBalls;
	}


	public TestCamera()
	{
		double scale = 180.0 / 550.0;
		map = new Map(new char[640][480], scale);
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		Mat image = Highgui.imread("src/imgInOut/TESTMAP.PNG");
		for(int y = 0; y < 480; ++y) {
			for(int x = 0; x < 640; ++x) {
				map.obstacle[x][y] = ((image.get(y, x)[0] == 0.0 && image.get(y, x)[1] == 0.0 && image.get(y, x)[2] == 255.0)? '0' : '1');
			}
		}

		goals = new ArrayList<Goal>();
		goals.add(new Goal(new Point(45, 221, scale), 4.0, Math.PI));
		goals.add(new Goal(new Point(585, 221, scale), 10.0, Math.PI));
		robot = new Robot(new Point(170, 210, scale), 0.0);
		balls = new ArrayList<Point>();
		balls.add(new Point(54, 95, scale));
		balls.add(new Point(414, 50, scale));
		balls.add(new Point(278, 63, scale));
		balls.add(new Point(292, 63, scale));
		balls.add(new Point(296, 77, scale));
		balls.add(new Point(112, 270, scale));
		balls.add(new Point(194, 290, scale));
		balls.add(new Point(204, 276, scale));
		balls.add(new Point(376, 298, scale));
		balls.add(new Point(290, 294, scale));
		balls.add(new Point(198, 381, scale));
		balls.add(new Point(201, 402, scale));
		balls.add(new Point(250, 339, scale));
		balls.add(new Point(334, 396, scale));
		balls.add(new Point(418, 396, scale));
	}
	public void update(){}
	public void shutDown(){}
	public void updateRobot(Point expectedPosition, double searchRadius){}
	public void updateBalls(){}
	public Robot getRobot(){return robot;}
	public List<Point> getBalls(){return balls;}
	public List<Goal> getGoals(){return goals;}
	public Map getMap(){return map;}
}
