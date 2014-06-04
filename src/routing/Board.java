package routing;

import java.util.ArrayList;
import java.util.List;

import imageProcessing.Goal;
import imageProcessing.Point;

/**
 * @author Julian Villadsen - s123641
 *
 */
public class Board {

	private Field[][]    grid;
	private Field 		start;
	private int        startX;
	private int        startY;

	public Board(char[][] map) {

		grid = new Field[map.length][map[0].length];

		for (int i = 0 ; i < map.length ; ++i) {
			for (int j = 0 ; j < map[i].length ; ++j) {
				grid[i][j] = new Field(i, j, map[i][j]);
			}
		}
	}

	/**
	 * The toString method allows a user to print
	 *   a BFS Object with the System.out.println
	 *   command
	 */
	public String toString() {
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

	public List<Point> ballsCloseToObstacle(List<Point> balls, int pixelRadius){
		List<Point> closeBalls = new ArrayList<Point>();
		int x,y;
		for(Point ball: balls){
			x=ball.pixel_x-pixelRadius;
			y=ball.pixel_y-pixelRadius;
			squareLoop:
				for(int i=0; i<pixelRadius*2 +1; i++){
					try{
						Field checkObstacle = grid[x][y];
						for(int j=0; j<pixelRadius*2 +1; j++){
							try{
								checkObstacle = grid[x][y];
								if(checkObstacle.getValue()=='O'){
									closeBalls.add(ball);
									break squareLoop;
								}
							}catch(IndexOutOfBoundsException e) {}
							x++;
						}
					}catch(IndexOutOfBoundsException e) {}
						y++;
						x=ball.pixel_x-pixelRadius;
					}
				}
			return closeBalls;
		}

	public void buildObstacleAroundBall(Point ball, List<String> entranceDirections, int pixelRadius){
		List<Field> buildObstacles = new ArrayList<Field>();
		buildObstacles.add(grid[ball.pixel_x - pixelRadius][ball.pixel_y - pixelRadius]);
		buildObstacles.add(grid[ball.pixel_x + pixelRadius][ball.pixel_y - pixelRadius]);
		buildObstacles.add(grid[ball.pixel_x + pixelRadius][ball.pixel_y + pixelRadius]);
		buildObstacles.add(grid[ball.pixel_x - pixelRadius][ball.pixel_y + pixelRadius]);
		boolean entrance = false;
		
		for(int i = 0; i < pixelRadius * 2; i++){
			if(checkAndBuild(buildObstacles, 'O'))
					entrance = true;
			incrBuildObstacles(buildObstacles);
		}
		
		// Create path from ball and outwards in the entrance directions
		if(!entrance){ //TODO reconsider this "if"; are multiple entrances problematic?
			List<Field> entrances = new ArrayList<Field>();
			for(int r = 1; r <= pixelRadius; r++){
				if(entranceDirections.contains("N")){
					entrances.add(grid[ball.pixel_x][ball.pixel_y - r]);
				}
				if(entranceDirections.contains("S")){
					entrances.add(grid[ball.pixel_x][ball.pixel_y + r]);
				}
				if(entranceDirections.contains("E")){
					entrances.add(grid[ball.pixel_x + r][ball.pixel_y]);
				}
				if(entranceDirections.contains("W")){
					entrances.add(grid[ball.pixel_x - r][ball.pixel_y]);
				}
				if(entranceDirections.contains("NW")){
					entrances.add(grid[ball.pixel_x - r][ball.pixel_y - r]);
				}
				if(entranceDirections.contains("NE")){
					entrances.add(grid[ball.pixel_x + r][ball.pixel_y - r]);
				}
				if(entranceDirections.contains("SW")){
					entrances.add(grid[ball.pixel_x - r][ball.pixel_y + r]);
				}
				if(entranceDirections.contains("SE")){
					entrances.add(grid[ball.pixel_x + r][ball.pixel_y + r]);
				}
			}
			checkAndBuild(entrances, ' ');
		}
	}
	
/**
 * Returns true if a ball or the robot currently is where trying to
 * build an obstacle, which means there exists an entrance to the ball
 * through the walls in progress of being built.
 */
	private boolean checkAndBuild(List<Field> buildObstacles, char value){
		boolean ret = false;
		for(Field buildObstacle: buildObstacles){
			try{
				if(buildObstacle.getValue()!='R'
						|| buildObstacle.getValue()!='B'){
					buildObstacle.setValue(value);
					setField(buildObstacle.getX(), buildObstacle.getY(), buildObstacle);
				}
				else
					ret = true;
			}catch(IndexOutOfBoundsException e) {}
		}
		return ret;
	}

	private void incrBuildObstacles(List<Field> buildObstacles){
	buildObstacles.set(0, grid[buildObstacles.get(0).getX() + 1][buildObstacles.get(0).getY()]);
		buildObstacles.set(1, grid[buildObstacles.get(1).getX()][buildObstacles.get(1).getY() + 1]);
		buildObstacles.set(2, grid[buildObstacles.get(2).getX() - 1][buildObstacles.get(2).getY()]);
		buildObstacles.set(3, grid[buildObstacles.get(3).getX()][buildObstacles.get(3).getY() - 1]);
	}
	
	public void fakeWallsBuild(double robotWidth) {
		int pixelRadius = (int)robotWidth;
		for (int i = 0 ; i < grid.length ; ++i) {
			for (int j = 0 ; j < grid[i].length ; ++j) {
				if (grid[i][j].getValue() == 'O') {
					if(i+pixelRadius < grid.length && i-pixelRadius >= 0 && j+pixelRadius < grid[i].length && j-pixelRadius >= 0) {
						if(grid[i+pixelRadius][j].getValue() != 'G' && grid[i+pixelRadius][j].getValue() != 'B'&& grid[i+pixelRadius][j].getValue() != 'O') {
							grid[i+pixelRadius][j].setValue('F');
						}
						if(grid[i-pixelRadius][j].getValue() != 'G' && grid[i-pixelRadius][j].getValue() != 'B'&& grid[i-pixelRadius][j].getValue() != 'O') {
							grid[i][j+pixelRadius].setValue('F');
						}
						if(grid[i][j+pixelRadius].getValue() != 'G' && grid[i][j+pixelRadius].getValue() != 'B' && grid[i][j+pixelRadius].getValue() != 'O') {
							grid[i-pixelRadius][j].setValue('F');
						}
						if(grid[i][j-pixelRadius].getValue() != 'G' && grid[i][j-pixelRadius].getValue() != 'B' && grid[i][j-pixelRadius].getValue() != 'O') {
							grid[i][j-pixelRadius].setValue('F');
						}
					}
				}
			}
		}
	}

	public void fillInBalls(List<Point> balls) {
		for(Point point: balls)
			grid[point.pixel_x][point.pixel_y].setValue('B');
	}

	public void fillInRobotPosition(Point robotPosition) {
		grid[robotPosition.pixel_x][robotPosition.pixel_y].setValue('R');	
	}

	public void fillInGoals(List<Goal> goals) {
		for(Goal goal: goals)
			grid[goal.center.pixel_x][goal.center.pixel_y].setValue('G');
	}

	public void clearBalls(List<Point> balls) {
		for(Point point: balls)
			grid[point.pixel_x][point.pixel_y].setValue(' ');
	}

	public void clearRobotPosition(Point robotPosition) {
		grid[robotPosition.pixel_x][robotPosition.pixel_y].setValue(' ');
	}

	public void clearGoals(List<Goal> goals) {
		for(Goal goal: goals)
			grid[goal.center.pixel_x][goal.center.pixel_y].setValue(' ');
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
