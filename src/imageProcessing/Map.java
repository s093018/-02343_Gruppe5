package imageProcessing;

public class Map
{
	public final char[][] obstacle;//1 = blocked, 0 = open
	public final double pixelSize;//cm / pixel
	
	public Map(char[][] obstacle, double pixelSize)
	{
		this.obstacle = obstacle; 
		this.pixelSize = pixelSize;
	}
}
