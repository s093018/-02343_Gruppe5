package imageProcessing;

public class Goal {
	public final Point center;
	public final double width;
	public final double heading;//Direction to eject balls in

	public Goal(Point center, double width, double heading)	{
		System.out.println("goal: " + center.x + ", " + center.y + ", " + heading);
		this.center = center; 
		this.width = width; 
		this.heading = heading;
	}
}
