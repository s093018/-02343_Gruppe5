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


class Point
{
	public double x, y;//Spatial position
	public int pixel_x, pixel_y;//Position on the image
	public Point(int x, int y, double scale){this.x = x; this.y = y; this.pixel_x = (int)(x/scale); this.pixel_y = (int)(y/scale);}
	public Point(double x, double y, int pixel_x, int pixel_y){this.x = x; this.y = y;}
}


class Goal
{
	public Point center;
	public double width;
	public double heading;//Direction to eject balls in
	public Goal(Point center, double width, double heading){this.center = center; this.width = width; this.heading = heading;}
}


class Map
{
	public boolean[][] obstacle;//true = blocked, false = open
	public double pixelSize;//cm / pixel
	public Map(boolean[][] obstacle, double pixelSize){this.obstacle = obstacle; this.pixelSize = pixelSize;}
}



