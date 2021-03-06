package imageProcessing;

public class Point {
	public final double x, y;//Spatial position
	public final int pixel_x, pixel_y;//Position on the image
	public String pathDirection = null; 
		
	public void setPathDirection(String pathDirection) {
		this.pathDirection = pathDirection;
	}

	public Point(int pixel_x, int pixel_y, double pixelSize) {
		this.x = pixel_x*pixelSize;
		this.y = pixel_y*pixelSize;
		this.pixel_x = pixel_x;
		this.pixel_y = pixel_y;
	}

	public Point(double x, double y, int pixel_x, int pixel_y) {
		this.x = x; 
		this.y = y;
		this.pixel_x = pixel_x;
		this.pixel_y = pixel_y;
	}
	public Point mirrorCoordinates() {
		Point newPoint = new Point(x, 480-1-y, pixel_x, 480-1-pixel_y);
		newPoint.setPathDirection(pathDirection);
		return newPoint;
	}
	public void mirrorAndSetPathDirection() {
		if(pathDirection.contains("N"))
			setPathDirection(pathDirection.replace('N', 'S'));
		else if(pathDirection.contains("S"))
			setPathDirection(pathDirection.replace('S', 'N'));
	}
}
