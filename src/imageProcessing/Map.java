package imageProcessing;

public class Map {
	public final char[][] obstacle;//1 = blocked, 0 = open
	public final double pixelSize;//cm / pixel
	
	public Map(char[][] obstacle, double pixelSize) {
		this.obstacle = obstacle; 
		this.pixelSize = pixelSize;
	}
	
	public String toString() {
		StringBuilder printer = new StringBuilder();
		for (int i = 0 ; i < obstacle.length ; ++i) {
			for (int j = 0 ; j < obstacle[i].length ; ++j) {
				printer.append(obstacle[i][j]+ " ");
			}
			printer.append("\n");
		}
		printer.append("\n");
		return printer.toString();
	}
}
