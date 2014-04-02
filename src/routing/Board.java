package routing;

import java.util.List;
import imageProcessing.Point;

/**
 * @author Julian Villadsen - s123641
 *
 */
public class Board {

	private char[][] board;

	//  this constructor is superfluous because of setBoard()
	//	public Board(char[][] map) {
	//		board = map;
	//	}

	public void fillInBalls(List<Point> balls){
		for(Point point: balls)
			board[point.pixel_x][point.pixel_y] = 'B';
	}
	
	public void fillInRobotPosition(Point robotPosition){
		board[robotPosition.pixel_x][robotPosition.pixel_y] = 'R';	
	}
	
	public void clearBalls(List<Point> balls){
		for(Point point: balls)
			board[point.pixel_x][point.pixel_y] = '\u0000';  //the default value of char
	}
	
	public void clearRobotPosition(Point robotPosition){
		board[robotPosition.pixel_x][robotPosition.pixel_y] = '\u0000'; //the default value of char
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
