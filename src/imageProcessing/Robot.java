package imageProcessing;

public class Robot
{
	public final double heading;
	public final Point position;
	public final double robotLength;
	public final double robotWidth;
	
	public Robot(Point position, double heading, double pixelLength, double pixelWidth) {
		System.out.println("Robot at: (" + position.x + ", " + position.y + "), heading: " + heading);
		this.heading = heading;
		this.position = position;
		this.robotLength = pixelLength;
		this.robotWidth = pixelWidth;
	}
}

