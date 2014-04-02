package imageProcessing;

import java.util.List;

public class RealCamera implements Camera
{
	private List<Goal> goals;
	private Map map;
	private List<Point> balls;
	private Robot robot;

	public RealCamera()
	{
		//tag billede, find forhindringer
		update();
	}
	
	public void update()
	{
		//opdater balls, robot
	}

	//optimer evt. de her
	public void updateRobot(Point expectedPosition, double searchRadius){update();}
	public void updateBalls(){update();}

	public Robot getRobot(){return robot;}
	public List<Point> getBalls(){return balls;}
	public List<Goal> getGoals(){return goals;}
	public Map getMap(){return map;}
}
