package routing;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

/**
 * The Maze class is in essence a two-dimensional array of
 *   Fields. In addition, each Maze has a name, a height
 *   and width, and the x- and y- coordinates of the
 *   starting point and finishing point (the ball) are maintained.
 */
public class BFS {
	private Field[][]    grid;
	private Field 		start;
	private int         width;
	private int        height;
	private int        startX;
	private int        startY;

	/**
	 * These static arrays of Strings represent the mazes
	 *  available. The constructor for the Maze class
	 *  needs one of the names of these mazes as input,
	 *  it then initializes the array of Fields appropriately.
	 *
	 * The first string is the maze's name.
	 * The second string is the maze's height.
	 * The third string is the maze's width.
	 * The remaining strings are the contents of the maze:
	 *   a 'O' character represents a wall
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
	public BFS(char[][] board) {
		/*		
		height  = (new Integer(info[1])).intValue();
		width = (new Integer(info[2])).intValue();
		 */
		grid = new Field[board.length][board[0].length];
		height = board.length;
		width = board[0].length;
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


	/**
	 * The printMark method changes contents of the Field to '*'
	 *   here invoked Queue is shortest path.
	 */
	public void printMark(Field pathCell) {
		while (pathCell.getParent() != null) {
			// changes contents until it meets a null pointer.
			pathCell = pathCell.getParent();
			pathCell.star();
		}
	}

	public List<String> printPath(Field pathCell) {
		LinkedList<String> result = new LinkedList<String>();
		Stack<Field> s = new Stack<Field>();
		
		while (pathCell.getParent() != null) {
			s.push(pathCell);
			// changes contents until it meets a null pointer.
			pathCell = pathCell.getParent();
		}

		int tmpX, tmpY;
		s.push(start);
		
		while(!s.empty()) {
			
			Field f = s.pop();
			tmpX = start.getY() - f.getY();
			tmpY = start.getX() - f.getX();
			
			if(tmpX == 1 && tmpY == 0) {
				result.add("N");
			} else if(tmpX == 0 && tmpY == -1) {
				result.add("E");
			} else if(tmpX == 0 && tmpY == 1) {
				result.add("W");
			} else if(tmpX == -1 && tmpY == 0) {
				result.add("S");
			} else if(tmpX == 1 && tmpY == -1) {
				result.add("NE");
			} else if(tmpX == 1 && tmpY == 1) {
				result.add("NW");
			} else if(tmpX == -1 && tmpY == -1) {
				result.add("SE");
			} else if(tmpX == -1 && tmpY == 1) {
				result.add("SW");
			}

			start = f;
		}
		return result;
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

	/**
	 * BFS routine 
	 * Using a queue, this solve() method is doing BFS.
	 * enqueues all children and dequeues them until it finds the end point.
	 */
	public List<String> findPath() {
		/*
		 *  Store North(N), South(S), East(E), West(W), 
		 *	NorthEast(NE), NorthWest(NW), SouthWest(SW) and SouthEast(SE)
		 */
		Field N, S, E, W, NW, NE, SW, SE;

		// Create new Queue to use for BFS.
		Queue<Field> Q = new LinkedList<Field>();

		// Create a new Field and mark it as the starting cell.

		// Set it as visited.
		start.setMark();

		//Add it to the queue.
		Q.add(start);

		while(!Q.isEmpty()) {
			Field CMC = Q.remove();

			if(CMC.getValue() == 'B') {               
				// Checks content of dequeued Cell.
				// If it is 'B', found finish position.
				printMark(CMC);
				
				// Print '*' mark at shortest path.
				return printPath(CMC);
			}

			// North child
			// Check boundaries.
			if ( !((CMC.getY() - 1) <= 0) ) {               
				// Get North Field
				N = ( grid[(CMC.getY()) - 1][CMC.getX()] );

				// If content is not wall and the North Field is not visited
				if ( N.getValue() != 'O' && !(N.isMarked()) )
				{  
					// Set it as visited.
					N.setMark();

					// Store parent information.
					CMC.setParent(N);

					// Add child to queue.
					Q.add(N);
				}
			}

			// South child
			// Boundary check
			if ( !((CMC.getY()) + 1 >= height) ) {

				// Get South Field
				S = (grid[(CMC.getY())+1][CMC.getX()]);             

				// If content is not wall and the South Field isn't visited.
				if ( S.getValue() != 'O' && !(S.isMarked()) ) {        

					// Set it as visited cell.
					S.setMark();

					// Store parent information.
					CMC.setParent(S);

					// Add child to queue.
					Q.add(S);
				}
			}

			// East child
			//Boundary check
			if ( !((CMC.getX() - 1 ) <= 0) ) {               
				// Get East Field
				E = (grid[CMC.getY()][(CMC.getX())-1]);

				// If content isn't wall and the East Field is not visited.
				if ( E.getValue() != 'O' && !(E.isMarked()) ) {     

					// Set it as visited cell.
					E.setMark();

					// Store parent's information.
					CMC.setParent(E);

					// Add child to queue.
					Q.add(E);
				}
			}

			// West child

			// Boundary check
			if ( !((CMC.getX()) + 1 >= width) ) {               

				// Get West Field
				W = (grid[CMC.getY()][(CMC.getX())+1]);

				// If content isn't wall and the West Field is not visited.
				if ( W.getValue() != 'O' && !(W.isMarked()) ) {        

					// Set it as visited cell.
					W.setMark();

					// Store parent's infomation.
					CMC.setParent(W);

					// Add child to queue.
					Q.add(W);
				}
			}

			// Northeast child
			//Boundary check
			if ( !((CMC.getX() + 1) >= width) && !((CMC.getY() - 1) <= 0) ) {
				// Get northeast Field
				NE = ( grid[(CMC.getY()) - 1][CMC.getX() + 1] );

				// If content isn't wall and the NE Field isnt visited.
				if ( NE.getValue() != 'O' && !(NE.isMarked()) ) {

					// Set it as visited cell.
					NE.setMark();

					// Keep parent's infomation.
					CMC.setParent(NE);

					// Add child to queue.
					Q.add(NE);
				}
			}

			// Northwest child.
			// Boundary check
			if ( !((CMC.getX() - 1) <= 0) && !((CMC.getY() - 1) <= 0) ) {
				// Get northwest Field
				NW = ( grid[(CMC.getY()) - 1][CMC.getX() - 1] );

				// If content is not wall and the cell is not visited,
				if ( NW.getValue() != 'O' && !(NW.isMarked()) ) {

					// Set it as visited cell.
					NW.setMark();

					// Keep parent's infomation.
					CMC.setParent(NW);

					// Add child to queue.
					Q.add(NW);
				}
			}

			// Southeast child
			// Boundary check
			if ( !((CMC.getX() + 1) >= width) && !((CMC.getY() + 1) >= width) ) {               
				// Get southeast Field
				SE = ( grid[(CMC.getY()) + 1][CMC.getX() + 1] );

				// If content is not wall and the cell is not visited,
				if ( SE.getValue() != 'O' && !(SE.isMarked()) ) {

					// Set it as visited cell.
					SE.setMark();

					// Keep parent's infomation.
					CMC.setParent(SE);

					// Add child to queue.
					Q.add(SE);
				}
			}

			// Southwest child
			// Boundary check
			if ( !((CMC.getX() - 1) <= 0) && !((CMC.getY() + 1) >= width) ) {               
				// Get Southwest Field
				SW = (grid[(CMC.getY()) + 1][CMC.getX() - 1]);

				// If content is not wall and the cell is not visited,
				if ( SW.getValue() != 'O' && !(SW.isMarked()) ) {

					// Set it as visited cell.
					SW.setMark();

					// Keep parent's infomation.
					CMC.setParent(SW);

					// Add child to queue.
					Q.add(SW);
				}
			}
		}

		// If we get here, we're screwed!
		System.out.println("Maze not solvable!\n");
		return null;
	}
}
