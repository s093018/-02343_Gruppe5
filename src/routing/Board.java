package routing;

import java.util.List;
import imageProcessing.Point;

/**
 * @author Julian Villadsen - s123641
 *
 */
public class Board {

	private Field[][] grid;
	private Field 		start;
	private int        startX;
	private int        startY;

	public Board(char[][] board) {
		
		grid = new Field[board.length][board[0].length];
		for (int i = 0 ; i < board.length ; ++i) {
			for (int j = 0 ; j < board[i].length ; ++j) {

				grid[i][j] = new Field(j, i, board[i][j]);

				if (board[i][j] == 'R') {
					startX = j; 
					startY = i;
					start = new Field(startX, startY, 'R');
				}
			}
		}
	}

	public void fillInBalls(List<Point> balls){
		for(Point point: balls)
			grid[point.pixel_x][point.pixel_y] = new Field(point.pixel_x, point.pixel_y, 'B');
	}

	public void fillInRobotPosition(Point robotPosition){
		grid[robotPosition.pixel_x][robotPosition.pixel_y] = 
				new Field(robotPosition.pixel_x, robotPosition.pixel_y, 'R');	
	}

	public void clearBalls(List<Point> balls){
		for(Point point: balls)
			grid[point.pixel_x][point.pixel_y].setValue('\u0000');  //the default value of char
	}

	public void clearRobotPosition(Point robotPosition) {
		grid[robotPosition.pixel_x][robotPosition.pixel_y].setValue('\u0000'); //the default value of char
	}

	public Field getField(int x, int y) {
		return grid[x][y];
	}

	public void setField(int x, int y, Field field) {
		grid[x][y] = field;
	}

	public Field[][] getGrid() {
		return grid;
	}

	public Field getStart() {
		return start;
	}

	public int getStartX() {
		return startX;
	}

	public int getStartY() {
		return startY;
	}
}
