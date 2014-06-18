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
public class BFSTest {
	private Field[][]              grid;
	private Field 		          start;
	private char 	            endChar;
	private char        robotChar = 'R';
	private char 	     obstacle = 'O';
	private char     fakeObstacle = 'F';
	private boolean closeToWall = false;
	private int 		    robotLength;
	private int 			 robotWidth;

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

	public BFSTest(Field[][] board, char end, int robotLength, int robotWidth) {
		this.grid = board;
		this.endChar = end;
		this.robotLength = robotLength;
		this.robotWidth = robotWidth;

		for (int i = 0 ; i < board.length; i++) {
			for (int j = 0 ; j < board[i].length; j++) {
				if (board[i][j].getValue() == robotChar) {
					this.start = grid[i][j];
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
		boolean moveable = false;
		Field N, S, E, W, current;
		Field NW_corner, NE_corner, SW_corner, SE_corner, NE_middle, SE_middle;
		// Create new Queue to use for BFS. 
		Queue<Field> bfsQueue = new LinkedList<Field>();

		System.out.println("Starting at: [" + start.getX() + "," + start.getY() + "]");

		// Create a new Field and mark it as the starting cell + set it as visited.
		start.visit();
		//Add it to the queue.
		bfsQueue.add(start);
		while(!bfsQueue.isEmpty()) {

			current = bfsQueue.remove();

			if(current.getValue() == endChar) {
				return printPath(current);
			}
			if(current.getY() + robotLength/2 < grid[0].length && grid[current.getX()][current.getY() + robotLength/2].getValue() == endChar) {
				current.setParent(grid[current.getX()][current.getY() + robotLength/2]);
				return printPath(grid[current.getX()][current.getY() + robotLength/2]);
			} else if(current.getY() - robotLength/2 >= 0 && grid[current.getX()][current.getY() - robotLength/2].getValue() == endChar) {
				current.setParent(grid[current.getX()][current.getY() - robotLength/2]);
				return printPath(grid[current.getX()][current.getY() - robotLength/2]);
			} else if(current.getX() + robotLength/2 < grid.length && grid[current.getX() + robotLength/2][current.getY()].getValue() == endChar) {
				current.setParent(grid[current.getX() + robotLength/2][current.getY()]);
				return printPath(grid[current.getX() + robotLength/2][current.getY()]);
			} else if((current.getX() - robotLength/2) >= 0 && grid[current.getX() - robotLength/2][current.getY()].getValue() == endChar) {
				current.setParent(grid[current.getX() - robotLength/2][current.getY()]);
				return printPath(grid[current.getX() - robotLength/2][current.getY()]);
			}
			//grid[W.getX() - robotLength/2][W.getY()];
			
			
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



			if(current.getY() + 1 < grid[0].length) {               
				// Get North Field
				N = grid[current.getX()][current.getY() + 1];
				// Check if the value of the Field is not an obstacle and that the Field has not been visited.

				if (N.getValue() != obstacle && N.getValue() != fakeObstacle && !grid[N.getX()][N.getY()].isVisited()) {  
					if(N.getX() - robotWidth/2 >= 0 && N.getY() - robotLength/2 >= 0 && N.getX() + robotWidth/2 < grid.length && N.getY() + robotLength/2 < grid[0].length) {
						NW_corner = grid[N.getX() - robotWidth/2][N.getY() + robotLength/2];
						NE_corner = grid[N.getX() + robotWidth/2][N.getY() + robotLength/2];
						SW_corner = grid[N.getX() - robotWidth/2][N.getY() - robotLength/2];
						SE_corner = grid[N.getX() + robotWidth/2][N.getY() - robotLength/2];
						System.out.println("N");
						for(int i = NW_corner.getX(); i <= NE_corner.getX(); i++) {
							for(int j = SW_corner.getY(); j <= NE_corner.getY(); j++) {
								if(grid[i][j].getValue() != obstacle && grid[i][j].getValue() != fakeObstacle && grid[i][j].getValue() != endChar){
									moveable = true;
								} else {
									moveable = false;
									break;
								}
							}
						}
						if(moveable) {
							// Set it as visited.
							grid[N.getX()][N.getY()].visit();
							// Store parent information.
							current.setParent(N);
							// Add the found Field to queue.
							bfsQueue.add(N);
						}
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
				if (S.getValue() != obstacle && S.getValue() != fakeObstacle && !grid[S.getX()][S.getY()].isVisited()) {  

					if(S.getX() - robotWidth/2 >= 0 && S.getY() - robotLength/2 >= 0 && S.getX() + robotWidth/2 < grid.length 
							&& S.getY() + robotLength/2 < grid[0].length) {

						NW_corner = grid[S.getX() - robotWidth/2][S.getY() + robotLength/2];
						NE_corner = grid[S.getX() + robotWidth/2][S.getY() + robotLength/2];
						SW_corner = grid[S.getX() - robotWidth/2][S.getY() - robotLength/2];
						SE_corner = grid[S.getX() + robotWidth/2][S.getY() - robotLength/2];
						for(int i = NW_corner.getX(); i <= NE_corner.getX(); i++) {
							for(int j = SW_corner.getY(); j <= NE_corner.getY(); j++) {
								if(grid[i][j].getValue() != obstacle && grid[i][j].getValue() != fakeObstacle && grid[i][j].getValue() != endChar) {
									moveable = true;
								} else {
									moveable = false;
									break;
								}
							}
						}
						if(moveable) {
							grid[S.getX()][S.getY()].visit();
							current.setParent(S);
							bfsQueue.add(S);
						}
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
				System.out.println("h");
				if (E.getValue() != obstacle && E.getValue() != fakeObstacle && !grid[E.getX()][E.getY()].isVisited()) {    
					System.out.println("Hej");
					if((E.getX() - robotLength/2) >= 0 && E.getY() - (robotWidth/2) >= 0 && E.getX() + (robotLength/2) < grid.length && 
							E.getY() + (robotWidth/2) < grid[0].length) {

						NW_corner = grid[E.getX() - robotLength/2][E.getY() + robotWidth/2];
						NE_corner = grid[E.getX() + robotLength/2][E.getY() + robotWidth/2];
						SW_corner = grid[E.getX() - robotLength/2][E.getY() - robotWidth/2];
						SE_corner = grid[E.getX() + robotLength/2][E.getY() - robotWidth/2];

						for(int i = NW_corner.getX(); i <= NE_corner.getX(); i++) {
							for(int j = SW_corner.getY(); j <= NE_corner.getY(); j++) {
								if(grid[i][j].getValue() != obstacle && grid[i][j].getValue() != fakeObstacle && grid[i][j].getValue() != endChar) {
									moveable = true;
								} else {
									moveable = false;
									break;
								}
							}
						}
						if(moveable) {
							E.visit();
							current.setParent(E);
							bfsQueue.add(E);
						}
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
				if (W.getValue() != obstacle && W.getValue() != fakeObstacle && !grid[W.getX()][W.getY()].isVisited()) {
					if(W.getX() - robotLength/2 >= 0 && W.getY() - robotWidth/2 >= 0 && W.getX() + robotLength/2 < grid.length && W.getY() + robotWidth/2 < grid[0].length) {

						NW_corner = grid[W.getX() - robotLength/2][W.getY() + robotWidth/2];
						NE_corner = grid[W.getX() + robotLength/2][W.getY() + robotWidth/2];
						SW_corner = grid[W.getX() - robotLength/2][W.getY() - robotWidth/2];
						SE_corner = grid[W.getX() + robotLength/2][W.getY() - robotWidth/2];
						NE_middle = grid[W.getX() - robotLength/2][W.getY()];
						for(int i = NW_corner.getX(); i <= NE_corner.getX(); i++) {
							for(int j = SW_corner.getY(); j <= NE_corner.getY(); j++) {
								if(grid[i][j].getValue() != obstacle && grid[i][j].getValue() != fakeObstacle && grid[i][j].getValue() != endChar) {
									moveable = true;
								} else {
									moveable = false;
									break;
								}
							}
						}
						if(moveable) {
							grid[W.getX()][W.getY()].visit();
							current.setParent(W);
							bfsQueue.add(W);
						}					
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
			pathCell = pathCell.getParent();
			if(pathCell.getValue() != 'R') {
				pathCell.star();
			}
		}

		path.push(start);
		while(!path.empty()) {
			Field current_field = path.pop();

			tmpX = start.getX() - current_field.getX();
			tmpY = start.getY() - current_field.getY();

			if(tmpX == 0 && tmpY == 1) {
				result.add(90); //
			} else if(tmpX < 0 && tmpY == 0) {
				result.add(0); //
			} else if(tmpX == 0 && tmpY < 0) {
				result.add(270); //
			} else if(tmpX > 1 && tmpY == 0) {
				result.add(180); //
			} else if(tmpX < 0 && tmpY > 0) {
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
