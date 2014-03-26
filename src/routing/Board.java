package routing;

/**
 * @author Christian W. Nielsen - s093018
 *
 */
public class Board {

	private char[][] board;

	public Board(int x, int y) {
		
		board = new char[x][y];
	}
	
	public char getSquare(int x, int y) {
		
		return board[x][y];
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
