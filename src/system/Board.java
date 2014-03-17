package system;

/**
 * @author Christian W. Nielsen - s093018
 *
 */
public class Board {

	private int x, y;
	private PixelSquare[][] board;
	
	public Board() {
		
		board = new PixelSquare[x][y];
	}
	
	public PixelSquare getSquare(int x, int y) {
		
		return board[x][y];
	}
	
	public void setSquare(int x, int y, PixelSquare pixelSquare) {
		board[x][y] = pixelSquare;
	}
	
}
