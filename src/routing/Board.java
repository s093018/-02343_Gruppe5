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
			for (int j = 0 ; j < map[0].length ; ++j) {
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
									ball.setPathDirection(directionToObstacle(ball));
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

	private String directionToObstacle(Point point){
		// Search in all directions (SE, S etc.) for obstacle - find closest obstacle and return direction
		int shortest_dist = 0, temp_dist = 0, temp_pixel_x = point.pixel_x, temp_pixel_y = point.pixel_y;
		String path_direction = "Failed to identify direction";
		boolean done = false;

		// N
		while(!done){
			temp_pixel_y--;
			temp_dist++;
			try{
				if(grid[temp_pixel_x][temp_pixel_y].getValue() == 'O'){
					if(temp_dist < shortest_dist || shortest_dist == 0){
						shortest_dist = temp_dist;
						path_direction = "S";
					}
					done = true;
				}
			}catch(IndexOutOfBoundsException e) { break; }
		}

		//Reset bookkeeping  values
		done = false;
		temp_dist = 0; temp_pixel_x = point.pixel_x; temp_pixel_y = point.pixel_y;

		// NE
//		while(!done){
//			temp_pixel_y--;
//			temp_pixel_x++;
//			temp_dist++;
//			try{
//				if(grid[temp_pixel_x][temp_pixel_y].getValue() == 'O'){
//					if(temp_dist < shortest_dist || shortest_dist == 0){
//						shortest_dist = temp_dist;
//						path_direction = "SW";
//					}
//					done = true;
//				}
//			}catch(IndexOutOfBoundsException e) { break; }
//		}

		//Reset bookkeeping  values
//		done = false;
//		temp_dist = 0; temp_pixel_x = point.pixel_x; temp_pixel_y = point.pixel_y;

		// E
		while(!done){
			temp_pixel_x++;
			temp_dist++;
			try{
				if(grid[temp_pixel_x][temp_pixel_y].getValue() == 'O'){
					if(temp_dist < shortest_dist || shortest_dist == 0){
						shortest_dist = temp_dist;
						path_direction = "W";
					}
					done = true;
				}
			}catch(IndexOutOfBoundsException e) { break; }
		}

		//Reset bookkeeping  values
		done = false;
		temp_dist = 0; temp_pixel_x = point.pixel_x; temp_pixel_y = point.pixel_y;

		// SE
//		while(!done){
//			temp_pixel_y++;
//			temp_pixel_x++;
//			temp_dist++;
//			try{
//				if(grid[temp_pixel_x][temp_pixel_y].getValue() == 'O'){
//					if(temp_dist < shortest_dist || shortest_dist == 0){
//						shortest_dist = temp_dist;
//						path_direction = "NW";
//					}
//					done = true;
//				}
//			}catch(IndexOutOfBoundsException e) { break; }
//		}
//
		//Reset bookkeeping  values
//		done = false;
//		temp_dist = 0; temp_pixel_x = point.pixel_x; temp_pixel_y = point.pixel_y;

		// S
		while(!done){
			temp_pixel_y++;
			temp_dist++;
			try{
				if(grid[temp_pixel_x][temp_pixel_y].getValue() == 'O'){
					if(temp_dist < shortest_dist || shortest_dist == 0){
						shortest_dist = temp_dist;
						path_direction = "N";
					}
					done = true;
				}
			}catch(IndexOutOfBoundsException e) { break; }
		}

		//Reset bookkeeping  values
		done = false;
		temp_dist = 0; temp_pixel_x = point.pixel_x; temp_pixel_y = point.pixel_y;

		// SW
//		while(!done){
//			temp_pixel_y++;
//			temp_pixel_x--;
//			temp_dist++;
//			try{
//				if(grid[temp_pixel_x][temp_pixel_y].getValue() == 'O'){
//					if(temp_dist < shortest_dist || shortest_dist == 0){
//						shortest_dist = temp_dist;
//						path_direction = "NE";
//					}
//					done = true;
//				}
//			}catch(IndexOutOfBoundsException e) { break; }
//		}
//
		//Reset bookkeeping  values
//		done = false;
//		temp_dist = 0; temp_pixel_x = point.pixel_x; temp_pixel_y = point.pixel_y;


		// W
		while(!done){
			temp_pixel_x--;
			temp_dist++;
			try{
				if(grid[temp_pixel_x][temp_pixel_y].getValue() == 'O'){
					if(temp_dist < shortest_dist || shortest_dist == 0){
						shortest_dist = temp_dist;
						path_direction = "E";
					}
					done = true;
				}
			}catch(IndexOutOfBoundsException e) { break; }
		}

		//Reset bookkeeping  values
//		done = false;
//		temp_dist = 0; temp_pixel_x = point.pixel_x; temp_pixel_y = point.pixel_y;

		// NW
//		while(!done){
//			temp_pixel_y--;
//			temp_pixel_x++;
//			temp_dist++;
//			try{
//				if(grid[temp_pixel_x][temp_pixel_y].getValue() == 'O'){
//					if(temp_dist < shortest_dist || shortest_dist == 0){
//						shortest_dist = temp_dist;
//						path_direction = "SE";
//					}
//					done = true;
//				}
//			}catch(IndexOutOfBoundsException e) { break; }
//		}		

		return path_direction;
	}

	public void buildObstacleAroundBall(Point ball, int pixelRadius){

		List<Field> buildObstacles = new ArrayList<Field>();
		buildObstacles.add(grid[ball.pixel_x - pixelRadius][ball.pixel_y - pixelRadius]);
		buildObstacles.add(grid[ball.pixel_x + pixelRadius][ball.pixel_y - pixelRadius]);
		buildObstacles.add(grid[ball.pixel_x + pixelRadius][ball.pixel_y + pixelRadius]);
		buildObstacles.add(grid[ball.pixel_x - pixelRadius][ball.pixel_y + pixelRadius]);

		for(int i = 0; i < pixelRadius * 2; i++){
			checkAndBuild(buildObstacles, 'F', true);
			incrBuildObstacles(buildObstacles);
		}

		buildPath(ball, pixelRadius, ' ');
	}

	/**
	 * If new location was obstructed and the point wasn't moved, retries at another nearby field.
	 * Also builds path using pixelLength.
	 */
	public List<Field> moveGoals(List<Goal> goals, int pixelDistance, char newValueAtOldLocation, int pixelLength){
		ArrayList<Field> fieldListOld = new ArrayList<Field>();
		ArrayList<Field> fieldListNew = new ArrayList<Field>();
		ArrayList<String> directions = new ArrayList<String>();

		for(int i = 0; i < goals.size(); i++){
			directions.add(degreeToDirection((radianToDegreePositive(goals.get(i).heading))));
			fieldListOld.add(new Field(goals.get(i).center.pixel_x, goals.get(i).center.pixel_y, newValueAtOldLocation));
			int x_new = goals.get(i).center.pixel_x, y_new = goals.get(i).center.pixel_y;

			if(directions.get(i).equals("N")){
				y_new = goals.get(i).center.pixel_y - pixelDistance;
			}
			if(directions.get(i).equals("S")){
				y_new = goals.get(i).center.pixel_y + pixelDistance;
			}
			if(directions.get(i).equals("E")){
				x_new = goals.get(i).center.pixel_x + pixelDistance;
			}
			if(directions.get(i).equals("W")){
				x_new = goals.get(i).center.pixel_x - pixelDistance;
			}
			if(directions.get(i).equals("NW")){
				x_new = goals.get(i).center.pixel_x - pixelDistance;
				y_new = goals.get(i).center.pixel_y - pixelDistance;
			}
			if(directions.get(i).equals("NE")){
				x_new = goals.get(i).center.pixel_x + pixelDistance;
				y_new = goals.get(i).center.pixel_y - pixelDistance;
			}
			if(directions.get(i).equals("SW")){
				x_new = goals.get(i).center.pixel_x - pixelDistance;
				y_new = goals.get(i).center.pixel_y + pixelDistance;
			}
			if(directions.get(i).equals("SE")){
				x_new = goals.get(i).center.pixel_x + pixelDistance;
				y_new = goals.get(i).center.pixel_y + pixelDistance;
			}

			fieldListNew.add(new Field(x_new, y_new, 'G'));
		}

		int retryCounter = 0;
		do{
			if(!checkAndBuild(fieldListNew, 'G', true)){
				ArrayList<String> directionArg = new ArrayList<String>();
				for(int i = 0; i < fieldListNew.size(); i++){
					directionArg.clear();
					directionArg.add(directions.get(i));

					buildPath(new Point(fieldListNew.get(i).getX(), fieldListNew.get(i).getY(), 0),
							pixelLength, ' ');
				}
				for(Field fieldOld : fieldListOld)
					setField(fieldOld.getX(), fieldOld.getY(), fieldOld);
				return fieldListNew;
			}
			// May require optimizing
			for(int i = 0; i < fieldListNew.size(); i++){
				Field fieldNewRetry = new Field(fieldListNew.get(i).getX() + 1, fieldListNew.get(i).getY(), fieldListNew.get(i).getValue());
				fieldListNew.set(i, fieldNewRetry);
			}
			retryCounter++;
		}while(retryCounter < 50);

		System.out.println("The goals were not moved.");
		return fieldListNew;
	}


	/**
	 * Create path from a point and outwards in the entrance directions
	 */
	public void buildPath(Point center, int pixelLength, char value){
		List<Field> path = new ArrayList<Field>();
		for(int l = 1; l <= pixelLength; l++){
			if(center.pathDirection.equals("N")){
				path.add(grid[center.pixel_x][center.pixel_y - l]);
			}
			if(center.pathDirection.equals("S")){
				path.add(grid[center.pixel_x][center.pixel_y + l]);
			}
			if(center.pathDirection.equals("E")){
				path.add(grid[center.pixel_x + l][center.pixel_y]);
			}
			if(center.pathDirection.equals("W")){
				path.add(grid[center.pixel_x - l][center.pixel_y]);
			}
			if(center.pathDirection.equals("NW")){
				path.add(grid[center.pixel_x - l][center.pixel_y - l]);
			}
			if(center.pathDirection.equals("NE")){
				path.add(grid[center.pixel_x + l][center.pixel_y - l]);
			}
			if(center.pathDirection.equals("SW")){
				path.add(grid[center.pixel_x - l][center.pixel_y + l]);
			}
			if(center.pathDirection.equals("SE")){
				path.add(grid[center.pixel_x + l][center.pixel_y + l]);
			}
		}
		checkAndBuild(path, value, true);
	}

	/**
	 * Returns true if a ball or the robot currently is where trying to
	 * build an obstacle, which means there exists an entrance to the ball
	 * through the walls in progress of being built.
	 * @param replaceObstacles TODO
	 */
	private boolean checkAndBuild(List<Field> buildObstacles, char value, boolean replaceObstacles){
		boolean ret = false;
		for(Field buildObstacle: buildObstacles){
			try{
				if(buildObstacle.getValue()!='R' || buildObstacle.getValue()!='X' || buildObstacle.getValue()!='Y'
						|| buildObstacle.getValue()!='B' || buildObstacle.getValue()!='G'){
					if(!replaceObstacles && buildObstacle.getValue()!='O')
						break;
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

	public void fakeWallsBuild(int pixelRadius) {		
		ArrayList<Field> fakeObstacles = new ArrayList<Field>();

		//i = 640
		//j = 480
		for (int i = 0 ; i < grid.length ; i++) {
			for (int j = 0 ; j < grid[i].length ; j++) {
				if (grid[i][j].getValue() == 'O') {
					try{
							fakeObstacles.add(grid[i+pixelRadius][j]);
							fakeObstacles.add(grid[i-pixelRadius][j]);
							fakeObstacles.add(grid[i][j+pixelRadius]);
							fakeObstacles.add(grid[i][j-pixelRadius]);
						// tilfj evt [i-1][j-1], [i-1][j+1], [j-1][i+1], og [j+1][i+1]
					}catch(IndexOutOfBoundsException e) {}


					//					System.out.println("********FAKEWALL********");
					//					System.out.println("1: "+(i+pixelRadius)+" < "+grid.length);
					//					System.out.println("2 "+(i-pixelRadius)+" >= "+0);
					//					System.out.println("3: "+(j+pixelRadius)+" < "+grid[i].length);
					//					System.out.println("4: "+(j-pixelRadius)+" >= "+0);
					//					System.out.println("********FAKEWALL********");
					//					System.out.println();

					//					
					//					if(i+pixelRadius < grid.length && i-pixelRadius >= 0 && j+pixelRadius < grid[i].length && j-pixelRadius >= 0) {
					//					if(grid[i+pixelRadius][j].getValue() != 'G' && grid[i+pixelRadius][j].getValue() != 'B' && grid[i+pixelRadius][j].getValue() != 'O'&& grid[i+pixelRadius][j].getValue() != 'R') {
					//						grid[i+pixelRadius][j].setValue('F');
					//						}
					//						if(grid[i-pixelRadius][j].getValue() != 'G' && grid[i-pixelRadius][j].getValue() != 'B' && grid[i-pixelRadius][j].getValue() != 'O' && grid[i+pixelRadius][j].getValue() != 'R') {
					//							grid[i-pixelRadius][j].setValue('F');
					//						}
					//						if(grid[i][j+pixelRadius].getValue() != 'G' && grid[i][j+pixelRadius].getValue() != 'B' && grid[i][j+pixelRadius].getValue() != 'O' && grid[i+pixelRadius][j].getValue() != 'R') {
					//							grid[i][j+pixelRadius].setValue('F');
					//						}
					//						if(grid[i][j-pixelRadius].getValue() != 'G' && grid[i][j-pixelRadius].getValue() != 'B' && grid[i][j-pixelRadius].getValue() != 'O' && grid[i+pixelRadius][j].getValue() != 'R') {
					//							grid[i][j-pixelRadius].setValue('F');
					//						}
					// tilfj evt [i-1][j-1], [i-1][j+1], [j-1][i+1], og [j+1][i+1]
				}
			}
		}
		
		checkAndBuild(fakeObstacles, 'F', false);
	}
	//	}

	public void fillInBalls(List<Point> balls) {
		for(Point point: balls) {
			//	point = point.convert();
			grid[point.pixel_x][point.pixel_y].setValue('B');
		}
	}

	public void fillInRobotPosition(Point robotPosition) {
		//	robotPosition = robotPosition.convert();
		grid[robotPosition.pixel_x][robotPosition.pixel_y].setValue('R');	
	}

	public void fillInGoals(List<Goal> goals) {
		for(Goal goal: goals){
			Point tempPoint = goal.center;	
			grid[tempPoint.pixel_x][tempPoint.pixel_y].setValue('G');
		}
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

	public void setGrid(Field[][] newGrid) {
		grid = newGrid;
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

	public int radianToDegreePositive(double radian) {
		double degree = (radian*180)/Math.PI;
		int result = (int)(degree + 22.5)/45;
		result *=45;
		while (result < 0) 
			result = result + 360;
		return result;
	}

	public void clearBalls(List<Point> balls) {
		for(Point ball : balls) {
			grid[ball.pixel_x][ball.pixel_y].setValue(' ');
		}
	}

	public void clearRobot(Point robotPos) {
		grid[robotPos.pixel_x][robotPos.pixel_y].setValue(' ');
	}


	// Modified code from: http://stackoverflow.com/questions/2131195/cardinal-direction-algorithm-in-java
	/**
	 * degree should be positive, if not may throw ArrayIndexOutOfBoundsException.
	 */
	public String degreeToDirection(int degree){
		String directions[] = {"N", "NE", "E", "SE", "S", "SW", "W", "NW"};
		return directions[(((degree + 22) % 360) / 45)];
	}	
}
