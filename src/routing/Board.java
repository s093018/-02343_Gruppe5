package routing;

/**
 * @author Christian W. Nielsen - s093018
 *
 */
public class Board {

	private PixelSquare[][] board;

	public Board(int x, int y) {
		
		board = new PixelSquare[x][y];
	}
	
	public PixelSquare getSquare(int x, int y) {
		
		return board[x][y];
	}
	
	public void setSquare(int x, int y, PixelSquare pixelSquare) {
		board[x][y] = pixelSquare;
	}
	
	public PixelSquare[][] getBoard() {
		return board;
	}

	public void setBoard(PixelSquare[][] board) {
		this.board = board;
	}
	
}
