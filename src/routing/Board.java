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

		grid = new Field[map.length][map[0].length]; //TODO grid's anden dimension tom?

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
	
	public boolean ballSurroundedByObstacles(Point ball){
		if(grid[ball.pixel_x-1][ball.pixel_y].getValue() == 'O' &&
				grid[ball.pixel_x+1][ball.pixel_y].getValue() == 'O' &&
				grid[ball.pixel_x][ball.pixel_y+1].getValue() == 'O' &&
				grid[ball.pixel_x][ball.pixel_y-1].getValue() == 'O' &&
				grid[ball.pixel_x-1][ball.pixel_y-1].getValue() == 'O' &&
				grid[ball.pixel_x-1][ball.pixel_y+1].getValue() == 'O' &&
				grid[ball.pixel_x+1][ball.pixel_y-1].getValue() == 'O' &&
				grid[ball.pixel_x+1][ball.pixel_y+1].getValue() == 'O')
			return true;
		return false;
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
									//	ball.mirrorAndSetPathDirection();
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
	/** Search in all directions (SE, S etc.) for obstacle - find closest obstacle and return direction
	 */
	public String directionToObstacle(Point point){
		int shortest_dist = 0, temp_dist = 0, temp_pixel_x = point.pixel_x, temp_pixel_y = point.pixel_y;
		String path_direction = "Failed to identify direction";
		boolean done = false;

		// N
		while(!done){
			temp_pixel_x--;
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
				while(!done){
					temp_pixel_y--;
					temp_pixel_x++;
					temp_dist = temp_dist + 2;
					try{
						if(grid[temp_pixel_x][temp_pixel_y].getValue() == 'O'){
							if(temp_dist < shortest_dist || shortest_dist == 0){
								shortest_dist = temp_dist;
								path_direction = "SW";
							}
							done = true;
						}
					}catch(IndexOutOfBoundsException e) { break; }
				}

		//Reset bookkeeping  values
				done = false;
				temp_dist = 0; temp_pixel_x = point.pixel_x; temp_pixel_y = point.pixel_y;

		// E
		while(!done){
			temp_pixel_y++;
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
				while(!done){
					temp_pixel_y++;
					temp_pixel_x++;
					temp_dist = temp_dist + 2;
					try{
						if(grid[temp_pixel_x][temp_pixel_y].getValue() == 'O'){
							if(temp_dist < shortest_dist || shortest_dist == 0){
								shortest_dist = temp_dist;
								path_direction = "NW";
							}
							done = true;
						}
					}catch(IndexOutOfBoundsException e) { break; }
				}

		//Reset bookkeeping  values
				done = false;
				temp_dist = 0; temp_pixel_x = point.pixel_x; temp_pixel_y = point.pixel_y;

		// S
		while(!done){
			temp_pixel_x++;
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
				while(!done){
					temp_pixel_y++;
					temp_pixel_x--;
					temp_dist = temp_dist + 2;
					try{
						if(grid[temp_pixel_x][temp_pixel_y].getValue() == 'O'){
							if(temp_dist < shortest_dist || shortest_dist == 0){
								shortest_dist = temp_dist;
								path_direction = "NE";
							}
							done = true;
						}
					}catch(IndexOutOfBoundsException e) { break; }
				}

		//Reset bookkeeping  values
				done = false;
				temp_dist = 0; temp_pixel_x = point.pixel_x; temp_pixel_y = point.pixel_y;


		// W
		while(!done){
			temp_pixel_y--;
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
				done = false;
				temp_dist = 0; temp_pixel_x = point.pixel_x; temp_pixel_y = point.pixel_y;

		// NW
				while(!done){
					temp_pixel_y--;
					temp_pixel_x++;
					temp_dist = temp_dist + 2;
					try{
						if(grid[temp_pixel_x][temp_pixel_y].getValue() == 'O'){
							if(temp_dist < shortest_dist || shortest_dist == 0){
								shortest_dist = temp_dist;
								path_direction = "SE";
							}
							done = true;
						}
					}catch(IndexOutOfBoundsException e) { break; }
				}		
		if(path_direction.equals("Failed to identify direction"))
			System.out.println("Failed to identify path direction for " + point.pixel_x + ", " + point.pixel_y);

		return path_direction;
	}

	public void buildObstacleAroundBall(Point ball, int pixelLength){

		List<Field> buildObstacles = new ArrayList<Field>();
		try{buildObstacles.add(grid[ball.pixel_x - pixelLength][ball.pixel_y - pixelLength]);}catch(IndexOutOfBoundsException e){}
		try{buildObstacles.add(grid[ball.pixel_x + pixelLength][ball.pixel_y - pixelLength]);}catch(IndexOutOfBoundsException e){}
		try{buildObstacles.add(grid[ball.pixel_x + pixelLength][ball.pixel_y + pixelLength]);}catch(IndexOutOfBoundsException e){}
		try{buildObstacles.add(grid[ball.pixel_x - pixelLength][ball.pixel_y + pixelLength]);}catch(IndexOutOfBoundsException e){}

		for(int i = 0; i < pixelLength * 2; i++){
			checkAndBuild(buildObstacles, 'F', false);
			incrBuildObstacles(buildObstacles);
		}

		// Outcomment this if using moveBalls() but not if using moveBallsPastFakeObstacles()
			buildPath(ball, pixelLength, ' ');
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
			directions.add(degreeToDirection(rotateDegree((radianToDegreePositive(goals.get(i).heading)))));
			fieldListOld.add(new Field(goals.get(i).center.pixel_x, goals.get(i).center.pixel_y, newValueAtOldLocation));
			int x_new = goals.get(i).center.pixel_x, y_new = goals.get(i).center.pixel_y;

			if(directions.get(i).contains("N")){
				y_new = goals.get(i).center.pixel_y - pixelDistance;
			}
			else if(directions.get(i).contains("S")){
				y_new = goals.get(i).center.pixel_y + pixelDistance;
			}
			else if(directions.get(i).contains("E")){
				x_new = goals.get(i).center.pixel_x + pixelDistance;
			}
			else if(directions.get(i).contains("W")){
				x_new = goals.get(i).center.pixel_x - pixelDistance;
			}
			else if(directions.get(i).equals("NW")){
				x_new = goals.get(i).center.pixel_x - pixelDistance;
				y_new = goals.get(i).center.pixel_y - pixelDistance;
			}
			else if(directions.get(i).equals("NE")){
				x_new = goals.get(i).center.pixel_x + pixelDistance;
				y_new = goals.get(i).center.pixel_y - pixelDistance;
			}
			else if(directions.get(i).equals("SW")){
				x_new = goals.get(i).center.pixel_x - pixelDistance;
				y_new = goals.get(i).center.pixel_y + pixelDistance;
			}
			else if(directions.get(i).equals("SE")){
				x_new = goals.get(i).center.pixel_x + pixelDistance;
				y_new = goals.get(i).center.pixel_y + pixelDistance;
			}

			fieldListNew.add(grid[x_new][y_new]);
		}

		int retryCounter = 0;
		do{
			if(!checkAndBuild(fieldListNew, 'G', true)){
				for(int i = 0; i < fieldListNew.size(); i++){
					Point newGoalPoint = new Point(fieldListNew.get(i).getX(), fieldListNew.get(i).getY(), 0);
					newGoalPoint.setPathDirection(directions.get(i));
					
					if(newGoalPoint.pathDirection.contains("W"))
						newGoalPoint.setPathDirection("N");
					else if(newGoalPoint.pathDirection.contains("E"))
						newGoalPoint.setPathDirection("S");
					else if(newGoalPoint.pathDirection.contains("S"))
						newGoalPoint.setPathDirection("W");
					else if(newGoalPoint.pathDirection.contains("N"))
						newGoalPoint.setPathDirection("E");
					buildObstacleAroundBall(newGoalPoint,	pixelLength);
				}
				for(Field fieldOld : fieldListOld)
					setField(fieldOld.getX(), fieldOld.getY(), fieldOld);
				return fieldListNew;
			}
			// Retry at x+1, y - May require optimizing
			try{
				for(int i = 0; i < fieldListNew.size(); i++){
					Field fieldNewRetry = grid[fieldListNew.get(i).getX() + 1][fieldListNew.get(i).getY()];
					fieldListNew.set(i, fieldNewRetry);
				}
				retryCounter++;
			}catch(IndexOutOfBoundsException e) {break;}
		}while(retryCounter < 50);

		System.out.println("The goals were not moved.");
		return fieldListNew;
	}

	/**
	 * Assumes the balls already have been assigned pathDirections (by ballsCloseToObstacle()). 
	 */
	public List<Field> moveBalls(List<Point> balls, int pixelDistance, char newValueAtOldLocation, int pixelLength){
		ArrayList<Field> fieldListOld = new ArrayList<Field>();
		ArrayList<Field> fieldListNew = new ArrayList<Field>();

		for(Point ball: balls){
			fieldListOld.add(new Field(ball.pixel_x, ball.pixel_y, newValueAtOldLocation));
			int x_new = ball.pixel_x, y_new = ball.pixel_y;

			if(ball.pathDirection.equals("N")){
				y_new = ball.pixel_y - pixelDistance; //Towards N
			}
			if(ball.pathDirection.equals("S")){
				y_new = ball.pixel_y + pixelDistance; //Towards S
			}
			if(ball.pathDirection.equals("E")){
				x_new = ball.pixel_x + pixelDistance; //Towards E
			}
			if(ball.pathDirection.equals("W")){
				x_new = ball.pixel_x - pixelDistance; //Towards W
			}
			if(ball.pathDirection.equals("NW")){
				x_new = ball.pixel_x - pixelDistance; //Towards NW
				y_new = ball.pixel_y - pixelDistance;
			}
			if(ball.pathDirection.equals("NE")){
				x_new = ball.pixel_x + pixelDistance; //Towards NE
				y_new = ball.pixel_y - pixelDistance;
			}
			if(ball.pathDirection.equals("SW")){
				x_new = ball.pixel_x - pixelDistance; //Towards SW
				y_new = ball.pixel_y + pixelDistance;
			}
			if(ball.pathDirection.equals("SE")){
				x_new = ball.pixel_x + pixelDistance; //Towards SE
				y_new = ball.pixel_y + pixelDistance;
			}

			try{fieldListNew.add(grid[x_new][y_new]);}catch(IndexOutOfBoundsException e) {
				System.out.println("Failed to move ball at: " + ball.pixel_x + ", " + ball.pixel_y);

			}
		}

		for(int i = 0; i < fieldListNew.size(); i++){
			int retryCounter = 0;
			while(retryCounter < 50){
				if(!checkAndBuildSingle(fieldListNew.get(i), 'B', true)){
					Point newBall = new Point(fieldListNew.get(i).getX(), fieldListNew.get(i).getY(), 0);
					newBall.setPathDirection(balls.get(i).pathDirection);
					buildObstacleAroundBall(newBall, pixelLength);
					buildPath(newBall,	pixelLength, ' ');
					for(Field fieldOld : fieldListOld)
						setField(fieldOld.getX(), fieldOld.getY(), fieldOld);
					return fieldListNew;
				}
				// Retry at x+1, y - May require optimizing
				try{
					for(int j = 0; j < fieldListNew.size(); j++){
						Field fieldNewRetry = grid[fieldListNew.get(j).getX() + 1][fieldListNew.get(j).getY()];
						fieldListNew.set(j, fieldNewRetry);
					}
					retryCounter++;
				}catch(IndexOutOfBoundsException e) {break;}
			}
		}

		System.out.println("The balls were not moved.");
		return fieldListNew;
	}

	/**
	 * Assumes the balls already have been assigned pathDirections (by ballsCloseToObstacle()). 
	 * @return TODO
	 */
	public ArrayList<Point> moveBallsPastFakeWalls(List<Point> balls, char newValueAtOldLocation, int pixelLength){
		ArrayList<Field> fieldListOld = new ArrayList<Field>();
		ArrayList<Point> newBalls = new ArrayList<Point>();
		for(Point ball: balls){
			fieldListOld.add(new Field(ball.pixel_x, ball.pixel_y, newValueAtOldLocation));
			if(ball.pathDirection.equals("N")){
				try{
					for(int i = 1; grid[ball.pixel_x-i][ball.pixel_y].getValue() == 'F'; i++){
						if(grid[ball.pixel_x - i - 1][ball.pixel_y].getValue() != 'F'){
							Point newBall = new Point(ball.pixel_x - i - 1,ball.pixel_y, 0);
							newBall.setPathDirection(ball.pathDirection);
							newBalls.add(newBall);
						}
					}
				}catch(IndexOutOfBoundsException e) {}
			}
			if(ball.pathDirection.equals("S")){
				try{
					for(int i = 1; grid[ball.pixel_x + i][ball.pixel_y].getValue() == 'F'; i++){
						if(grid[ball.pixel_x + i +1][ball.pixel_y].getValue() != 'F'){
							Point newBall = new Point(ball.pixel_x + i +1, ball.pixel_y, 0);
							newBall.setPathDirection(ball.pathDirection);
							newBalls.add(newBall);
						}
					}
				}catch(IndexOutOfBoundsException e) {}
			}
			if(ball.pathDirection.equals("E")){
				try{
					for(int i = 1; grid[ball.pixel_x][ball.pixel_y + i].getValue() == 'F'; i++){
						if(grid[ball.pixel_x][ball.pixel_y + i +1].getValue() != 'F'){
							Point newBall = new Point(ball.pixel_x, ball.pixel_y + i +1, 0);
							newBall.setPathDirection(ball.pathDirection);
							newBalls.add(newBall);
						}
					}
				}catch(IndexOutOfBoundsException e) {}
			}
			if(ball.pathDirection.equals("W")){
				try{
					for(int i = 1; grid[ball.pixel_x][ball.pixel_y - i].getValue() == 'F'; i++){
						if(grid[ball.pixel_x][ball.pixel_y - i -1].getValue() != 'F'){
							Point newBall = new Point(ball.pixel_x, ball.pixel_y - i -1, 0);
							newBall.setPathDirection(ball.pathDirection);
							newBalls.add(newBall);
						}
					}
				}catch(IndexOutOfBoundsException e) {}
			}
			if(ball.pathDirection.equals("NW")){
				try{
					for(int i = 1; grid[ball.pixel_x - i][ball.pixel_y - i].getValue() == 'F'; i++){
						if(grid[ball.pixel_x - i -1][ball.pixel_y - i -1].getValue() != 'F'){
							Point newBall = new Point(ball.pixel_x - i -1, ball.pixel_y - i -1, 0);
							newBall.setPathDirection(ball.pathDirection);
							newBalls.add(newBall);
						}
					}
				}catch(IndexOutOfBoundsException e) {}
			}
			if(ball.pathDirection.equals("NE")){
				try{
					for(int i = 1; grid[ball.pixel_x - i][ball.pixel_y + i].getValue() == 'F'; i++){
						if(grid[ball.pixel_x - i -1][ball.pixel_y + i +1].getValue() != 'F'){
							Point newBall = new Point(ball.pixel_x - i -1, ball.pixel_y + i +1, 0);
							newBall.setPathDirection(ball.pathDirection);
							newBalls.add(newBall);
						}
					}
				}catch(IndexOutOfBoundsException e) {}
			}
			if(ball.pathDirection.equals("SW")){
				try{
					for(int i = 1; grid[ball.pixel_x + i][ball.pixel_y - i].getValue() == 'F'; i++){
						if(grid[ball.pixel_x + i +1][ball.pixel_y - i -1].getValue() != 'F'){
							Point newBall = new Point(ball.pixel_x + i +1, ball.pixel_y - i -1, 0);
							newBall.setPathDirection(ball.pathDirection);
							newBalls.add(newBall);
						}
					}
				}catch(IndexOutOfBoundsException e) {}
			}
			if(ball.pathDirection.equals("SE")){
				try{
					for(int i = 1; grid[ball.pixel_x + i][ball.pixel_y + i].getValue() == 'F'; i++){
						if(grid[ball.pixel_x + i +1][ball.pixel_y + i +1].getValue() != 'F'){
							Point newBall = new Point(ball.pixel_x + i +1, ball.pixel_y + i +1, 0);
							newBall.setPathDirection(ball.pathDirection);
							newBalls.add(newBall);
						}
					}
				}catch(IndexOutOfBoundsException e) {}
			}

		}
		for(Point newBall : newBalls){
			grid[newBall.pixel_x][newBall.pixel_y].setValue('B');
			buildObstacleAroundBall(newBall, pixelLength);
		}
		for(Field fieldOld : fieldListOld)
			grid[fieldOld.getX()][fieldOld.getY()].setValue(newValueAtOldLocation);

		if(fieldListOld.size() != newBalls.size())
			System.out.println(newBalls.size() + " balls were moved, out of " +fieldListOld.size());
	
		return newBalls;
	}


	/**
	 * Create path from a point and outwards in the entrance directions
	 */
	public void buildPath(Point center, int pixelLength, char value){
		List<Field> path = new ArrayList<Field>();
		for(int i = 1; i <= pixelLength; i++){ //Could be optimized to: while <pixelLength and next char == F
			if(center.pathDirection.contains("N")){
				try{path.add(grid[center.pixel_x -i][center.pixel_y]);}catch(IndexOutOfBoundsException e) {}
			}
			if(center.pathDirection.contains("S")){
				try{path.add(grid[center.pixel_x +i][center.pixel_y]);}catch(IndexOutOfBoundsException e) {}
			}
			if(center.pathDirection.contains("E")){
				try{path.add(grid[center.pixel_x][center.pixel_y +i]);}catch(IndexOutOfBoundsException e) {}
			}
			if(center.pathDirection.contains("W")){
				try{path.add(grid[center.pixel_x][center.pixel_y -i]);}catch(IndexOutOfBoundsException e) {}
			}
			if(center.pathDirection.equals("NW")){
				try{path.add(grid[center.pixel_x - i][center.pixel_y - i]);}catch(IndexOutOfBoundsException e) {}
			}
			if(center.pathDirection.equals("NE")){
				try{path.add(grid[center.pixel_x - i][center.pixel_y + i]);}catch(IndexOutOfBoundsException e) {}
			}
			if(center.pathDirection.equals("SW")){
				try{path.add(grid[center.pixel_x + i][center.pixel_y - i]);}catch(IndexOutOfBoundsException e) {}
			}
			if(center.pathDirection.equals("SE")){
				try{path.add(grid[center.pixel_x + i][center.pixel_y + i]);}catch(IndexOutOfBoundsException e) {}
			}
		}
		for(Field field: path)
			checkAndBuildSingle(field, value, true);
		//checkAndBuild(path, value, true);
	}

	/**
	 * Returns true if a ball or the robot currently is where trying to
	 * build an obstacle, which means there exists an entrance to the ball
	 * through the walls in progress of being built.
	 */
	private boolean checkAndBuild(List<Field> buildObstacles, char value, boolean replaceObstacles){
		boolean obstructed = false;
		for(Field buildObstacle: buildObstacles){
			try{
				if(buildObstacle.getValue()!='R' && buildObstacle.getValue()!='X' && buildObstacle.getValue()!='Y'
						&& buildObstacle.getValue()!='B' && buildObstacle.getValue()!='G'){
					if(!replaceObstacles && buildObstacle.getValue()=='O')
						continue;
					buildObstacle.setValue(value);
					setField(buildObstacle.getX(), buildObstacle.getY(), buildObstacle);
				}
				else
					obstructed = true;
			}catch(IndexOutOfBoundsException e) {}
		}
		return obstructed;
	}

	private boolean checkAndBuildSingle(Field buildObstacle, char value, boolean replaceObstacles){
		boolean obstructed = false;
		try{
			if(buildObstacle.getValue()!='R' && buildObstacle.getValue()!='X' && buildObstacle.getValue()!='Y'
					&& buildObstacle.getValue()!='B' && buildObstacle.getValue()!='G'){
				if(!replaceObstacles && buildObstacle.getValue()=='O')
					obstructed = true;
				buildObstacle.setValue(value);
				setField(buildObstacle.getX(), buildObstacle.getY(), buildObstacle);
			}
			else
				obstructed = true;
		}catch(IndexOutOfBoundsException e) {}
		return obstructed;
	}

	private void incrBuildObstacles(List<Field> buildObstacles){
		buildObstacles.set(0, grid[buildObstacles.get(0).getX() + 1][buildObstacles.get(0).getY()]);
		buildObstacles.set(1, grid[buildObstacles.get(1).getX()][buildObstacles.get(1).getY() + 1]);
		buildObstacles.set(2, grid[buildObstacles.get(2).getX() - 1][buildObstacles.get(2).getY()]);
		buildObstacles.set(3, grid[buildObstacles.get(3).getX()][buildObstacles.get(3).getY() - 1]);
	}

	public void fakeWallsBuild(int pixelRadius) {		
		ArrayList<Field> fakeObstacles = new ArrayList<Field>();
		int halfRadius = Math.round((float)(pixelRadius/2)+9);

		for (int i = 0 ; i < grid.length ; i++) {
			for (int j = 0 ; j < grid[i].length ; j++) {
				if (grid[i][j].getValue() == 'O') {
					try{fakeObstacles.add(grid[i+pixelRadius][j]); }catch(IndexOutOfBoundsException e) {}
					try{fakeObstacles.add(grid[i-pixelRadius][j]); }catch(IndexOutOfBoundsException e) {}
					try{fakeObstacles.add(grid[i][j+pixelRadius]); }catch(IndexOutOfBoundsException e) {}
					try{fakeObstacles.add(grid[i][j-pixelRadius]); }catch(IndexOutOfBoundsException e) {}
					
					// Diagonal
					try{fakeObstacles.add(grid[i-halfRadius][j-halfRadius]); }catch(IndexOutOfBoundsException e) {}
					try{fakeObstacles.add(grid[i-halfRadius][j+halfRadius]); }catch(IndexOutOfBoundsException e) {}
					try{fakeObstacles.add(grid[i+halfRadius][j-halfRadius]); }catch(IndexOutOfBoundsException e) {}
					try{fakeObstacles.add(grid[i+halfRadius][j+halfRadius]); }catch(IndexOutOfBoundsException e) {}
				}
			}
		}

		checkAndBuild(fakeObstacles, 'F', false);
	}

	public void fillInBalls(List<Point> balls) {
		for(Point point: balls) {
			if(grid[point.pixel_x][point.pixel_y].getValue() != 'O')
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
	
	public void fillInSmallestGoal(List<Goal> goals) {
		Point tempPoint = goals.get(0).center;
		double tempWidth = goals.get(0).width;
		
		for(Goal goal: goals)
			if(goal.width <= tempWidth)
				tempPoint = goal.center;
		
		grid[tempPoint.pixel_x][tempPoint.pixel_y].setValue('G');
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

	public void clearBoard(){
		for (int i = 0 ; i < grid.length ; i++)
			for (int j = 0 ; j < grid[i].length ; j++)
				grid[i][j].setValue(' ');
	}

	public void clearBalls(List<Point> balls) {
		for(Point ball : balls)
			grid[ball.pixel_x][ball.pixel_y].setValue(' ');
	}

	public void clearRobot(Point robotPos) {
		grid[robotPos.pixel_x][robotPos.pixel_y].setValue(' ');
	}

	public int radianToDegreePositive(double radian) {
		return (int) Math.toDegrees(radian);
	}

	public int rotateDegree(int degree){
		return degree - 90 + 360;
	}

	// Modified code from: http://stackoverflow.com/questions/2131195/cardinal-direction-algorithm-in-java
	/**
	 * degree should be positive, if not may throw ArrayIndexOutOfBoundsException.
	 */
	public String degreeToDirection(int degree){
		String directions[] = {"N", "E", "S", "W"};
		return directions[(((degree + 45) % 360) / 90)];

		//		String directions[] = {"N", "NW", "W", "SW", "S", "SE", "E", "NE"};
		//		return directions[(((degree + 22) % 360) / 45)];
	}	
}
