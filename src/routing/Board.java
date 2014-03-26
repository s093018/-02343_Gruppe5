package routing;

/**
 * @author Christian W. Nielsen - s093018
 *
 */
public class Board {

	private char[][] board;

	public Board() {
		
		board = imageProcessing.Camera.map();
	}
	
	public char getSquare() {
		
		return board;
	}
	
	public void setSquare(int x, int y, char character) {
		board[x][y] = character;
	}
	
	public char[][] getBoard() {
		return board;
	}

	public void setBoard(char[][] board) {
		this.board = board;
	}
	
}
