package imageProcessing;

import java.util.List;


//
//	All angles in radians, distances in centimeters
//


public interface Camera
{
	public void update();
	public void updateRobot(Point expectedPosition, double searchRadius);
	public void updateBalls();
	public void shutDown();

	public Robot getRobot();
	public List<Point> getBalls();
	public List<Goal> getGoals();
	public Map getMap();
}
