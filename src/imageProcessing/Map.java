package imageProcessing;

public class Map
{
	public final char[][] obstacle;//true = blocked, false = open
	public final double pixelSize;//cm / pixel
	
	public Map(char[][] obstacle, double pixelSize)
	{
		this.obstacle = obstacle; 
		this.pixelSize = pixelSize;
	}
}
