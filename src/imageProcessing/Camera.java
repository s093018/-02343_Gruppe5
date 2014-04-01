package imageProcessing;

import java.util.List;


//
//	All angles in radians, distances in centimeters
//


public interface Camera
{
	public void update();
	public Point robotPosition();
	public double robotHeading();
	public List<Point> balls();
	public List<Goal> goals();
	public Map map();
}
