package imageProcessing;

public class Map{
	public boolean[][] obstacle;//true = blocked, false = open
	public double pixelSize;//cm / pixel
	
	public Map(boolean[][] obstacle, double pixelSize) {
		this.obstacle = obstacle; 
		this.pixelSize = pixelSize;
	}
}
