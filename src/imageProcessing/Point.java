package imageProcessing;

	public class Point{
		public double x, y;//Spatial position
		public int pixel_x, pixel_y;//Position on the image
		public Point(int x, int y, double scale){this.x = x; this.y = y; this.pixel_x = (int)(x/scale); this.pixel_y = (int)(y/scale);}
		public Point(double x, double y, int pixel_x, int pixel_y){this.x = x; this.y = y;}
	}
