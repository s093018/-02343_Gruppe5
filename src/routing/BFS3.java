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
public class BFS3 {
	private Field[][]              grid;
	private Field 		          start;
	private Field	         robotFront;
	private char 	            endChar;
	private char        robotChar = 'R';
	private char 	     obstacle = 'O';
	private char     fakeObstacle = 'F';
	private char   robotFrontChar = 'X';
	private boolean closeToWall = false;

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

	public BFS3(Field[][] board, char end) {
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

	public int getDistance(int distX, int distY) {
		int dist;
		if(distX < 1 && distY < 1) {
			distX = Math.abs(distX);
			distY = Math.abs(distY);
			dist = (distX > distY) ? distX : distY;
		} else {
			dist = (distX > distY) ? distX : distY;
		}
		return dist;
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
		int dist = getDistance(start.getX() - robotFront.getX(), start.getY() - robotFront.getY());

		// Create a new Field and mark it as the starting cell + set it as visited.
		start.visit();
		//Add it to the queue.
		bfsQueue.add(start);

		while(!bfsQueue.isEmpty()) {

			current = bfsQueue.remove();
//			System.out.println("Current = [" + current.getX() + "," + current.getY() + "]");
			if(current.getY() + dist < grid[0].length && grid[current.getX()][current.getY() + dist].getValue() == endChar) {
//				System.out.println("Found ball N at [" + current.getX() + "," + (current.getY() + dist) + "]");
				current.setParent(grid[current.getX()][current.getY() + dist]);
				return printPath(grid[current.getX()][current.getY() + dist]);
			} 
			else if(current.getY() - dist >= 0 && grid[current.getX()][current.getY() - dist].getValue() == endChar) {
//				System.out.println("Found ball S at [" + current.getX() + "," + (current.getY() - dist) + "]");
				current.setParent(grid[current.getX()][current.getY() - dist]);
				return printPath(grid[current.getX()][current.getY() - dist]);
			} 
			else if(current.getX() + dist < grid.length && grid[current.getX() + dist][current.getY()].getValue() == endChar) {
//				System.out.println("Found ball E at [" + (current.getX() + dist) + "," + current.getY() + "]");
				current.setParent(grid[current.getX() + dist][current.getY()]);
				return printPath(grid[current.getX() + dist][current.getY()]);
			} 
			else if(current.getX() - dist >= 0 && grid[current.getX() - dist][current.getY()].getValue() == endChar) {
//				System.out.println("Found ball at W: [" + (current.getX() - dist) + "," + current.getY() + "]");
				current.setParent(grid[current.getX() - dist][current.getY()]);
				return printPath(grid[current.getX() - dist][current.getY()]);
			} else if(current.getX() + dist < grid.length && current.getY() + dist < grid[0].length && grid[current.getX() + dist][current.getY() + dist].getValue() == endChar) {
//				System.out.println("Found ball at NE: [ " + (current.getX() + dist) + "," + (current.getY() + dist) + "]");
				current.setParent(grid[current.getX() + dist][current.getY() + dist]);
				return printPath(grid[current.getX() + dist][current.getY() + dist]);
			} else if(current.getX() - dist >= 0 && current.getY() + dist < grid[0].length && grid[current.getX() - dist][current.getY() + dist].getValue() == endChar) {
//				System.out.println("Found ball at NW: [" + (current.getX() - dist) + "," + current.getY() + dist + "]");
				current.setParent(grid[current.getX() - dist][current.getY() + dist]);
				return printPath(grid[current.getX() - dist][current.getY() + dist]);
			} else if(current.getX() - dist >= 0 && current.getY() - dist >= 0 && grid[current.getX() - dist][current.getY() - dist].getValue() == endChar) {
//				System.out.println("Found ball at SW: [" + (current.getX() - dist) + "," + (current.getY() - dist) + "]");
				current.setParent(grid[current.getX() - dist][current.getY() - dist]);
				return printPath(grid[current.getX() - dist][current.getY() - dist]);
			} else if(current.getX() + dist < grid.length && current.getY() - dist >= 0 && grid[current.getX() + dist][current.getY() - dist].getValue() == endChar) {
//				System.out.println("Found ball at SE: [" + (current.getX() + dist) + "," + (current.getY() - dist) + "]");
				current.setParent(grid[current.getX() + dist][current.getY() - dist]);
				return printPath(grid[current.getX() + dist][current.getY() - dist]);
			}
			
			/*
			for(int i = 0; i < closeBalls.size(); i++) {
				Point ball = closeBalls.get(i);
				if(ball.pixel_x == current.getX() && ball.pixel_y == current.getY()) {
					System.out.println("The found ball is close to a wall.");
					setCloseToWall();
					break;
				}
			}
			 */

			// Get North Field
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
					if(moveable) {
						N.visit();
						current.setParent(N);
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
					if(moveable) {
						S.visit();
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
					if(moveable) {
						E.visit();
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
					if(moveable) {
						W.visit();
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
			if (current.getX() + 1 < grid.length && current.getY() + 1 < grid[0].length) {
				NE = grid[current.getX() + 1][current.getY() + 1];
				if(NE.getX() + dist < grid.length && NE.getY() + dist < grid[0].length) {
					int tmp = NE.getY();
					for(int i = NE.getX(); i <= NE.getX() + dist; i++) {
						if(grid[i][tmp].getValue() != endChar && grid[i][tmp].getValue() != obstacle && grid[i][tmp].getValue() != fakeObstacle && !grid[i][tmp].isVisited()) {
							moveable = true;
						} else {
							moveable = false;
							break;
						}
						tmp++;
					}
					if(moveable) {
						NE.visit();
						current.setParent(NE);
						bfsQueue.add(NE);
					}
				}
			}

			/*
			 *	Northwest child
			 * 	Boundary check.
			 *  Get northwest Field
			 *  Check if the value of the Field is not an obstacle and that the Field has not been visited.
			 *  Set it as visited.
			 *  Keep parents information.
			 *  Add Field to queue.
			 */

			if (current.getX() - 1 >= 0 && current.getY() + 1 < grid[0].length) {
				NW = grid[current.getX() - 1][current.getY() + 1];

				if(NW.getX() - dist >= 0 && NW.getY() + dist < grid[0].length) {
					int tmp = NW.getY();
					for(int i = NW.getX(); i > NW.getX() - dist; i--) {
						if(grid[i][tmp].getValue() != endChar && grid[i][tmp].getValue() != obstacle && grid[i][tmp].getValue() != fakeObstacle && !grid[i][tmp].isVisited()) {
							moveable = true;
						} else {
							moveable = false;
							break;
						}
						tmp++;
					}
					if(moveable) {
						NW.visit();
						current.setParent(NW);
						bfsQueue.add(NW);
					}
				}
			}

			/*
			 *	Southeast child
			 * 	Boundary check.
			 * 	Get southeast Field.
			 * 	Check if the value of the Field is not an obstacle and that the Field has not been visited.
			 * 	Set it as visited.
			 * 	Store parent's information.
			 * 	Add Field to queue.
			 */

			if (current.getX() + 1 < grid.length && current.getY() - 1 >= 0) {               
				SE = grid[current.getX() + 1][current.getY() - 1];
				if(SE.getX() + dist < grid.length && SE.getY() - dist >= 0) {
					int tmp = SE.getY();
					for(int i = SE.getX(); i < SE.getX() + dist; i++) {
						if(grid[i][tmp].getValue() != endChar && grid[i][tmp].getValue() != obstacle && grid[i][tmp].getValue() != fakeObstacle && !grid[i][tmp].isVisited()) {
							moveable = true;
						} else {
							moveable = false;
							break;
						}
						tmp--;
					}
					if(moveable) {
						SE.visit();
						current.setParent(SE);
						bfsQueue.add(SE);
					}
				}
			}

			/*
			 *	Southwest child
			 * 	Boundary check.
			 * 	Get southwest Field.
			 * 	Check if the value of the Field is not an obstacle and that the Field ahs not been visited.
			 * 	Set it as visited.
			 * 	Keep parents information.
			 * 	Add Field to queue.
			 */

			if (current.getX() - 1 >= 0 && current.getY() - 1 >= 0) {
				SW = grid[current.getX() - 1][current.getY() - 1];
				if(SW.getX() - dist >= 0 && SW.getY() - dist >= 0) {
					int tmp = SW.getY();
					for(int i = SW.getX(); i > SW.getX() - dist; i--) {
						if(grid[i][tmp].getValue() != endChar && grid[i][tmp].getValue() != obstacle && grid[i][tmp].getValue() != fakeObstacle && !grid[i][tmp].isVisited()) {
							moveable = true;
						} else {
							moveable = false;
							break;
						}
						tmp--;
					}
					if(moveable) {
						SW.visit();
						current.setParent(SW);
						bfsQueue.add(SW);
					}
				}
			}
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
			if(pathCell.getValue() != 'R' && pathCell.getValue() != 'B') {
				pathCell.star();
			}
			pathCell = pathCell.getParent();
		}
		while(!path.empty()) {
			Field current_field = path.pop();
			tmpX = start.getX() - current_field.getX();
			tmpY = start.getY() - current_field.getY();

			if(tmpX == 0 && tmpY > 0) {
				result.add(270); //
			} else if(tmpX < 0 && tmpY == 0) {
				result.add(0); //
			} else if(tmpX == 0 && tmpY < 0) {
				result.add(90); //
			} else if(tmpX > 0 && tmpY == 0) {
				result.add(180); //
			} else if(tmpX < 0 && tmpY > 0) {
				result.add(315); //
			} else if(tmpX > 0 && tmpY > 0) {
				result.add(225);
			} else if(tmpX > 0 && tmpY < 0) {
				result.add(135); //
			} else if(tmpX < 0 && tmpY < 0) {
				result.add(45);
			}

			start = current_field;
		}
		return result;
	}   

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
