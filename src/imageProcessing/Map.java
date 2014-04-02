package imageProcessing;

public class Map{
	public char[][] obstacle;//true = blocked, false = open
	public double pixelSize;//cm / pixel
	
	public Map(char[][] obstacle, double pixelSize) {
		this.obstacle = obstacle; 
		this.pixelSize = pixelSize;
	}
}
