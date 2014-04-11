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

	public Board(char[][] map) {
		
		grid = new Field[map.length][map[0].length];
		
		for (int i = 0 ; i < map.length ; ++i) {
			for (int j = 0 ; j < map[i].length ; ++j) {
				
				grid[i][j] = new Field(j, i, map[i][j]);

				if (map[i][j] == 'R') {
					startX = j; 
					startY = i;
					start = new Field(startX, startY, 'R');
				}
			}
		}
	}


	/**
	 * The toString method allows a user to print
	 *   a BFS Object with the System.out.println
	 *   command
	 */
	public String toString()
	{
		StringBuilder printer = new StringBuilder();
		for (int i = 0 ; i < grid.length ; ++i) {
			for (int j = 0 ; j < grid[i].length ; ++j) {
				printer.append(grid[i][j].getValue() + " ");
			}
			printer.append("\n");
		}
		printer.append("\n");
		return printer.toString();
	}
	
	public String toString2(char[][]map)
	{
		StringBuilder printer = new StringBuilder();
		for (int i = 0 ; i < map.length ; ++i) {
			for (int j = 0 ; j < map[i].length ; ++j) {
				printer.append(map[i][j] + " ");
			}
			printer.append("\n");
		}
		printer.append("\n");
		return printer.toString();
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
