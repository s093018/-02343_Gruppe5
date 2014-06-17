package routing;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

/**
 * @author Christoffer - s103148
 */

/**
 * The Maze class is in essence a two-dimensional array of
 *   Fields. In addition, each Maze has a name, a height
 *   and width, and the x- and y- coordinates of the
 *   starting point and finishing point (the ball) are maintained.
 */
public class BFS2 {
	private Field[][]      grid;
	private Field 		  start;
	private Field	 robotFront;
	private char 	    endChar;
	private char      robotChar = 'R';
	private char 	   obstacle = 'O';
	private char   fakeObstacle = 'F';
	private boolean closeToWall = false;
	private char robotFrontChar = 'X';

	/**
	 * The BFS constructor takes a two-dimensional Array of Chars as input.
	 * The remaining strings are the contents of the maze:
	 *   a 'O' character represents an obstacle.
	 *   a capital R represents the starting point
	 *   a capital B represents the finishing point (the ball)
	 *
	 * These are static data members, meaning that they "belong to"
	 *   the maze class, not every constructed maze object.
	 *   After all, any particular object only needs to know
	 *   about the single maze it's representing.
	 */

	/** 
	 * This constructor takes in the name of one of the 
	 *  above statically defined String arrays and creates 
	 *  the maze represented in the specified array.
	 */

	public BFS2(Field[][] board, char end) {
		this.grid = board;
		this.endChar = end;

		for (int i = 0 ; i < board.length; i++) {
			for (int j = 0 ; j < board[i].length; j++) {
				if (board[i][j].getValue() == robotChar) {
					this.start = grid[i][j];
				}
				if(board[i][j].getValue() == robotFrontChar) {
					this.robotFront = grid[i][j];
				}
			}
		}
	}

	/**
	 * BFS routine 
	 * Using a queue, this findPath(closeBalls) method is doing BFS.
	 * enqueues all children and dequeues them until it finds the end point.
	 */
	public ArrayList<Integer> findPath() {
		/*
		 *  Crete fields for North(N), South(S), East(E), West(W), 
		 *	Northeast(NE), Northwest(NW), Southwest(SW) and Southeast(SE)
		 */
		Field N, S, E, W, NW, NE, SW, SE, current;
		// Create new Queue to use for BFS. 
		Queue<Field> bfsQueue = new LinkedList<Field>();


		boolean moveable = false;
		int dist = 0;
		int distX = start.getX() - robotFront.getX();
		int distY = start.getY() - robotFront.getY();
		if(distX < 1 && distY < 1) {
			distX = Math.abs(distX);
			distY = Math.abs(distY);
			dist = (distX > distY) ? distX : distY;
		} else {
			dist = (distX > distY) ? distX : distY;
		}
		// Create a new Field and mark it as the starting cell + set it as visited.
		start.visit();
		//Add it to the queue.
		bfsQueue.add(start);

		while(!bfsQueue.isEmpty()) {

			current = bfsQueue.remove();

			if(current.getValue() == endChar) {      
				/*
			for(int i = 0; i < closeBalls.size(); i++) {
				Point ball = closeBalls.get(i);
				if(ball.pixel_x == current.getX() && ball.pixel_y == current.getY()) {
					System.out.println("The found ball is close to a wall.");
					setCloseToWall();
					break;
				}
			}

				/* Call printPath to get a list of directions to return. */
				return printPath(current);
			}

			if(current.getY() + 1 < grid[0].length) {
				N = grid[current.getX()][current.getY() + 1];
				if(N.getY() + dist < grid[0].length) { 
					for(int i = N.getY(); i < N.getY() + dist; i++) {
						if(grid[N.getX()][i].getValue() != obstacle && grid[N.getX()][i].getValue() != fakeObstacle && grid[N.getX()][i].getValue() != endChar && !grid[N.getX()][i].isVisited()) {
							moveable = true;
						} else {
							moveable = false;
							break;
						}
					}
					if(moveable && grid[N.getX()][N.getY() + dist].getValue() == endChar) {
						current.setParent(N);
						bfsQueue.add(N);
						return printPath(N);
					}
					// Get North Field
					// Check if the value of the Field is not an obstacle and that the Field has not been visited.
					// Set it as visited.
					if(!grid[N.getX()][N.getY()].isVisited()) {
						grid[N.getX()][N.getY()].visit();
						// Store parent information.
						current.setParent(N);
						// Add the found Field to queue.
						bfsQueue.add(N);
					}
				}
			}

			/*
			 *	South child
			 * 	Boundary check.
			 *  Get South Field
			 *  Check if the value of the Field is not an obstacle and that the Field has not been visited.
			 *  Set it as visited.
			 *  Store parent information.
			 *  Add the found Field to queue.
			 */

			if (current.getY() - 1 >= 0) {
				S = grid[current.getX()][current.getY() - 1];   
				if(S.getY() - dist >= 0) {
					for(int i = S.getY(); i > S.getY() - dist; i--) {
						if(grid[S.getX()][i].getValue() != obstacle && grid[S.getX()][i].getValue() != fakeObstacle && grid[S.getX()][i].getValue() != endChar && !grid[S.getX()][i].isVisited()) {
							moveable = true;
						} else {
							moveable = false;
							break;
						}
					}
					if(moveable && grid[S.getX()][S.getY() - dist].getValue() == endChar) {
						current.setParent(S);
						bfsQueue.add(S);
						return printPath(S);
					}
					if(!grid[S.getX()][S.getY()].isVisited()) {
						grid[S.getX()][S.getY()].visit();
						current.setParent(S);
						bfsQueue.add(S);
					}
				}
			}



			/*
			 *	East child
			 * 	Boundary check.
			 *  Get East Field
			 *  Check if the value of the Field is not an obstacle and that the Field has not been visited.
			 *  Set it as visited.
			 *	Store parent's information.
			 *  Add Field to queue.
			 */	

			if (current.getX() + 1 < grid.length) {               
				E = grid[current.getX() + 1][current.getY()];
				if(E.getX() + dist < grid.length) {
					for(int i = E.getX(); i < E.getX() + dist; i++) {
						if(grid[i][E.getY()].getValue() != obstacle && grid[i][E.getY()].getValue() != fakeObstacle && grid[i][E.getY()].getValue() != endChar && !grid[i][E.getY()].isVisited()) {
							moveable = true;
						} else {
							moveable = false;
							break;
						}
					}
					if(moveable && grid[E.getX() + dist][E.getY()].getValue() == endChar) {
						current.setParent(E);
						bfsQueue.add(E);
						return printPath(E);
					}
					if(!grid[E.getX()][E.getY()].isVisited()) {
						grid[E.getX()][E.getY()].visit();
						current.setParent(E);
						bfsQueue.add(E);
					}
				}
			}

			/*
			 *	West child
			 * 	1. Boundary check.
			 *  2. Get West Field
			 *  3. Check if the value of the Field is not an obstacle and that the Field has not been visited.
			 *  4. Set it as visited.
			 *  5. Store parent's information.
			 *  6. Add Field to queue.
			 */
			if (current.getX() - 1 >= 0) {               
				W = grid[current.getX() - 1][current.getY()];
				if(W.getX() - dist >= 0) {
					for(int i = W.getX(); i > W.getX() - dist; i--) {
						if(grid[i][W.getY()].getValue() !=  endChar && grid[i][W.getY()].getValue() != obstacle && grid[i][W.getY()].getValue() != fakeObstacle && !grid[i][W.getY()].isVisited()) {
							moveable = true;
						} else {
							moveable = false;
							break;
						}
					}
					if(moveable && grid[W.getX() - dist][W.getY()].getValue() == endChar) {
						current.setParent(W);
						bfsQueue.add(W);
						return printPath(W);
					}
					if(!grid[W.getX()][W.getY()].isVisited()) {
						grid[W.getX()][W.getY()].visit();
						current.setParent(W);
						bfsQueue.add(W);
					}
				}
			}
			/*
			 *	Northeast child
			 * 	Boundary check.
			 *  Get northeast Field
			 *  Check if the value of the Field is not an obstacle and that the Field has not been visited.
			 *  Set it as visited cell.
			 *  Keep parent information.
			 *  Add Field to queue.
			 */

			//			if (current.getX() + 1 < grid.length && current.getY() + 1 < grid[0].length) {
			//				NE = grid[current.getX() + 1][current.getY() + 1];
			//				if (NE.getValue() != obstacle && NE.getValue() != fakeObstacle && !grid[NE.getX()][NE.getY()].isVisited()) {
			//					grid[NE.getX()][NE.getY()].visit();
			//					current.setParent(NE);
			//					bfsQueue.add(NE);
			//				}
			//			}

			/*
			 *	Northwest child
			 * 	Boundary check.
			 *  Get northwest Field
			 *  Check if the value of the Field is not an obstacle and that the Field has not been visited.
			 *  Set it as visited.
			 *  Keep parents information.
			 *  Add Field to queue.
			 */

			//			if (current.getX() - 1 >= 0 && current.getY() + 1 < grid[0].length) {
			//				NW = grid[current.getX() - 1][current.getY() + 1];
			//				if (NW.getValue() != obstacle && NW.getValue() != fakeObstacle && !grid[NW.getX()][NW.getY()].isVisited()) {
			//					grid[NW.getX()][NW.getY()].visit();
			//					current.setParent(NW);
			//					bfsQueue.add(NW);
			//				}
			//			}

			/*
			 *	Southeast child
			 * 	Boundary check.
			 * 	Get southeast Field.
			 * 	Check if the value of the Field is not an obstacle and that the Field has not been visited.
			 * 	Set it as visited.
			 * 	Store parent's information.
			 * 	Add Field to queue.
			 */

			//			if (current.getX() + 1 < grid.length && current.getY() - 1 >= 0) {               
			//				SE = grid[current.getX() + 1][current.getY() - 1];
			//				if (SE.getValue() != obstacle && SE.getValue() != fakeObstacle && !grid[SE.getX()][SE.getY()].isVisited()) {
			//					grid[SE.getX()][SE.getY()].visit();
			//					current.setParent(SE);
			//					bfsQueue.add(SE);
			//				}
			//			}

			/*
			 *	Southwest child
			 * 	Boundary check.
			 * 	Get southwest Field.
			 * 	Check if the value of the Field is not an obstacle and that the Field ahs not been visited.
			 * 	Set it as visited.
			 * 	Keep parents information.
			 * 	Add Field to queue.
			 */

			//			if (current.getX() - 1 >= 0 && current.getY() - 1 >= 0) {
			//				SW = grid[current.getX() - 1][current.getY() - 1];
			//				if (SW.getValue() != obstacle && SW.getValue() != fakeObstacle && !grid[SW.getX()][SW.getY()].isVisited()) {
			//					grid[SW.getX()][SW.getY()].visit();
			//					current.setParent(SW);
			//					bfsQueue.add(SW);
			//				}
			//			}
		}

		// If we get here, we're screwed!
		System.out.println("Path not found!\n");
		return null;
	}

	public ArrayList<Integer> printPath(Field pathCell) {
		ArrayList<Integer> result = new ArrayList<Integer>();
		Stack<Field> path = new Stack<Field>();
		int tmpX, tmpY;

		while (pathCell.getParent() != null) {
			path.push(pathCell);
			// changes contents until it meets a null pointer.
			pathCell = pathCell.getParent();
			if(pathCell.getValue() != 'R' && pathCell.getValue() != 'X') {
				pathCell.star();
			}
		}

		path.push(start);
		while(!path.empty()) {
			Field current_field = path.pop();

			tmpX = start.getX() - current_field.getX();
			tmpY = start.getY() - current_field.getY();

			if(tmpX == 0 && tmpY == 1) {
				result.add(270); //
			} else if(tmpX == -1 && tmpY == 0) {
				result.add(0); //
			} else if(tmpX == 0 && tmpY == -1) {
				result.add(90); //
			} else if(tmpX == 1 && tmpY == 0) {
				result.add(180); //
			} else if(tmpX == -1 && tmpY == 1) {
				result.add(45); //
			} else if(tmpX == 1 && tmpY == 1) {
				result.add(135);
			} else if(tmpX == 1 && tmpY == -1) {
				result.add(225); //
			} else if(tmpX == -1 && tmpY == -1) {
				result.add(315);
			}

			start = current_field;
		}
		//		result = convertPath(result);
		return result;
	}   

	//	private ArrayList<Integer> convertPath(ArrayList<Integer> path) {
	//		ArrayList<Integer> convertedPath = new ArrayList<Integer>();
	//		int first = 0, last = 0;
	//
	//		for(int i = 1; i < path.size(); i++) {
	//			if(path.get(i-1) != path.get(i)) {
	//				first = i;
	//				last = path.size() - i;
	//				break;
	//			}
	//		}
	//		if(first > last) {
	//			for(int i = 0; i < last; i++) {
	//				convertedPath.add(path.get(path.size() - last));
	//			}
	//			for(int i = 0; i < first; i++) {
	//				convertedPath.add(path.get(0));
	//			}
	//		}
	//		return convertedPath;
	//	}

	public String toString() {
		StringBuilder printer = new StringBuilder();
		for (int i = 0; i < grid.length ; ++i) {
			for (int j = 0 ; j < grid[i].length ; ++j) {
				printer.append(grid[i][j].getValue() + " ");
			}
			printer.append("\n");
		}
		printer.append("\n");
		return printer.toString();
	}

	public boolean getCloseToWall() {
		return closeToWall;
	}

	public void setCloseToWall() {
		this.closeToWall = true;
	}
}
