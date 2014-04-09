package imageProcessing;

public class Point
{
	public final double x, y;//Spatial position
	public final int pixel_x, pixel_y;//Position on the image
		
	public Point(int pixel_x, int pixel_y, double pixelSize)
	{
		this.x = pixel_x*pixelSize;
		this.y = pixel_x*pixelSize;
		this.pixel_x = pixel_x;
		this.pixel_y = pixel_y;
	}

	public Point(double x, double y, int pixel_x, int pixel_y)
	{
		this.x = x; 
		this.y = y;
		this.pixel_x = pixel_x;
		this.pixel_y = pixel_y;
	}
}
