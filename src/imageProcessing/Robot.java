package imageProcessing;

public class Robot
{
	public final double heading;
	public final Point position;
	public final double pixelLength;
	public final double pixelWidth;
	
	public Robot(double heading, Point position, double pixelLength,
			double pixelWidth) {
		this.heading = heading;
		this.position = position;
		this.pixelLength = pixelLength;
		this.pixelWidth = pixelWidth;
	}
}
