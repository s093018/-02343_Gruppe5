package routing;
import java.util.LinkedList;
import java.util.Queue;

/**
 * The MazeCell class implements a single cell of 
 *   a maze. It includes private data fields that
 *   tell: 
 *     whether a cell is marked (already visited by the BFS)
 *     what a cell's contents are
 *     what a cell's parent is (for the BFS)
 *     what the cell's position is in the maze
 */
class MazeCell 
{
	private boolean  marked;
	private char   contents;
	private MazeCell parent;
	private int        x, y;

	/**
	 * The constructor for MazeCells. The cells are by default
	 *   unmarked and without a parent cell. To construct a new cell,
	 *   its position in the maze (x,y) and its contents '+', 'R', or 'B'
	 *   must be specified.
	 */
	public MazeCell(int x, int y, char c) 
	{
		marked = false;
		contents = c;
		parent =  null;
		this.x = x; 
		this.y = y;
	}

	/**
	 * A public nonstatic accessor method that returns the contents
	 *   of the MazeCell that invokes it.
	 */
	public char getContents() 
	{
		return this.contents;
	}

	/**
	 * A public nonstatic method that changes marked value 
	 *   of the MazeCell to ture that invokes it.
	 */
	public void setMark()
	{
		marked = true;
	}

	/**
	 * A public nonstatic accessor method that returns the marked value
	 *   of the MazeCell that invokes it.
	 */
	public boolean isMarked() 
	{
		return this.marked;
	}

	/**
	 * A public nonstatic accessor method that returns the parent
	 *   of the MazeCell that invokes it.
	 */
	public MazeCell getParent() 
	{
		return this.parent;
	}

	/**
	 * A public nonstatic method that changes parent value 
	 *   of the MazeCell to parent that invokes it.
	 */
	public void setParent(MazeCell child) 
	{
		child.parent = this;
	}

	/**
	 * A public nonstatic accessor method that returns the x
	 *   of the MazeCell that invokes it.
	 */
	public int getX() 
	{
		return this.x;
	}  

	/**
	 * A public nonstatic accessor method that returns the y
	 *   of the MazeCell that invokes it.
	 */
	public int getY() 
	{
		return this.y;
	}

	/**
	 * A public nonstatic method that changes contents value 
	 *   of the MazeCell to '*' that invokes it.
	 */
	public void star() 
	{
		contents = '*';
	}   
}

/**
 * The Maze class is in essence a two-dimensional array of
 *   MazeCells. In addition, each Maze has a name, a height
 *   and width, and the x- and y- coordinates of the
 *   starting point and finishing point (the spam) are maintained.
 */
class Maze
{
	private String       name;
	private MazeCell[][] grid;
	private int         width;
	private int        height;
	private int        startX;
	private int        startY;

	/**
	 * These static arrays of Strings represent the mazes
	 *  available. The constructor for the Maze class
	 *  needs one of the names of these mazes as input,
	 *  it then initializes the array of MazeCells appropriately.
	 *
	 * The first string is the maze's name.
	 * The second string is the maze's height.
	 * The third string is the maze's width.
	 * The remaining strings are the contents of the maze:
	 *   a '+' character represents a wall
	 *   a capital R represents the starting point
	 *   a capital B represents the finishing point (the ball)
	 *
	 * These are static data members, meaning that they "belong to"
	 *   the maze class, not every constructed maze object.
	 *   After all, any particular object only needs to know
	 *   about the single maze it's representing.
	 */
	static String[] simpleMaze = 
		{
		"simpleMaze", "10", "10",
		"++++++++++",
		"+R       +",
		"+        +",
		"+   +    +",
		"+  +++   +",
		"+   +    +",
		"+        +",
		"+    B   +",
		"+        +",
		"++++++++++"
		};

	static String [] testMaze =
		{
		"testMaze", "7", "8",
		"++++++++",
		"+  R   +",
		"+ +++  +",
		"+      +",
		"+ +++  +",
		"+  + B +",
		"++++++++"
		};

	static String[] difficultMaze = 
		{
		"difficultMaze", "15", "15",
		"+++++++++++++++",
		"+R            +",
		"+ + +++++++ + +",
		"+ + +     + + +",
		"+ + + +++ + + +",
		"+ + + +B+ + + +",
		"+ +   + + + + +",
		"+ + + + + + + +",
		"+ + + + + + + +",
		"+ + +   + + + +",
		"+ + +++++ + + +",
		"+ +         + +",
		"+ +++++++++++ +",
		"+             +",
		"+++++++++++++++",
		};


	/** 
	 * This constructor takes in the name of one of the 
	 *  above statically defined String arrays and creates 
	 *  the maze represented in the specified array.
	 */
	public Maze(String[] info) 
	{
		name   = info[0];
		height  = (new Integer(info[1])).intValue();
		width = (new Integer(info[2])).intValue();
		grid   = new MazeCell[height][width];

		char c;

		for (int i = 0 ; i < height ; ++i) {
			for (int j = 0 ; j < width ; ++j) {
				
				c = info[3 + i].charAt(j);
				grid[i][j] = new MazeCell(j, i, c);

				if (c == 'R') {
					startX = j; 
					startY = i;
				}
			}
		}
	}


	/**
	 * The printMark method changes contents of the MazeCell to '*'
	 *   here invoked Queue is shortest path.
	 */
	public void printMark(MazeCell pathCell) 
	{
		while (pathCell.getParent() != null) {
			// changes contents until it meets null pointer.
			pathCell = pathCell.getParent();
			pathCell.star();
		}
	}   


	/**
	 * The toString method allows a user to print
	 *   a Maze Object with the System.out.println
	 *   command
	 */
	public String toString()
	{
		StringBuilder printMaze = new StringBuilder();
		printMaze.append("Maze ");
		printMaze.append(name);
		printMaze.append(" :  \n");
		for (int i = 0 ; i < height ; ++i) {
			for (int j = 0 ; j < width ; ++j) {
				printMaze.append(String.valueOf(grid[i][j].getContents()));
			}
			printMaze.append("\n");
		}
		printMaze.append("\n");
		return printMaze.toString();
	}

	/**
	 * BFS routine 
	 * Using a queue, this solve() method is doing BFS.
	 * enqueues all children and deques them until it finds the end point.
	 */
	void solve() 
	{
		// Store North(N), South(S), East(E), West(W), NorthEast(NE), NorthWest(NW), SouthWest(SW) and SouthEast(SE) of MazeCell.
		MazeCell N, S, E, W, NW, NE, SW, SE;

		// Create new Queue to use for BFS.
		Queue<MazeCell> Q = new LinkedList<MazeCell>();

		// Create a new MazeCell and mark it as the starting cell.
		MazeCell startCell = new MazeCell(startX, startY, 'R');
		
		// Set it as visited.
		startCell.setMark();
		
		//Add it to the queue.
		Q.add(startCell);

		while( !Q.isEmpty() ) {
			MazeCell CMC = Q.remove();

			if(CMC.getContents() == 'B')
			{               
				// Checks content of dequeued Cell.
				// If it is 'B', found finish position.
				printMark(CMC);
				// Print '*' mark at shortest path.
				return;
			}

			// North child
			
			// Check boundaries.
			if ( !((CMC.getY() - 1) <= 0) ) {               

				// Get North MazeCell
				N = ( grid[(CMC.getY()) - 1][CMC.getX()] );

				// If content is not wall and the North MazeCell is not visited
				if ( N.getContents() != '+' && !(N.isMarked()) )
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
				
				// Get South MazeCell
				S = (grid[(CMC.getY())+1][CMC.getX()]);             

				// If content is not wall and the South mazecell isn't visited.
				if ( S.getContents() != '+' && !(S.isMarked()) ) {        

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

				// Get East MazeCell
				E = (grid[CMC.getY()][(CMC.getX())-1]);

				// If content isn't wall and the East MazeCell is not visited.
				if ( E.getContents() != '+' && !(E.isMarked()) ) {     
					
					// Set it as visited cell.
					E.setMark();

					// Store parent's infomation.
					CMC.setParent(E);

					// Add child to queue.
					Q.add(E);
				}
			}

			// West child

			// Boundary check
			if ( !((CMC.getX()) + 1 >= width) ) {               

				
				// Get West MazeCell
				W = (grid[CMC.getY()][(CMC.getX())+1]);

				// If content isn't wall and the West MazeCell is not visited.
				if ( W.getContents() != '+' && !(W.isMarked()) ) {        
					
					// Set it as visited cell.
					W.setMark();

					// Store parent's infomation.
					CMC.setParent(W);

					// Add child to queue.
					Q.add(W);
				}
			}

			// Northeast child
			
			//Bounary check
			if ( !((CMC.getX() + 1) >= width) && !((CMC.getY() - 1) <= 0) ) {

				// Get northeast MazeCell
				NE = ( grid[(CMC.getY()) - 1][CMC.getX() + 1] );

				// If content isn't wall and the NE mazecell isnt visited.
				if ( NE.getContents() != '+' && !(NE.isMarked()) ) {

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

				// Get northwest MazeCell
				NW = ( grid[(CMC.getY()) - 1][CMC.getX() - 1] );

				// If content is not wall and the cell is not visited,
				if ( NW.getContents() != '+' && !(NW.isMarked()) ) {

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

				// Get southeast MazeCell
				SE = ( grid[(CMC.getY()) + 1][CMC.getX() + 1] );

				// If content is not wall and the cell is not visited,
				if ( SE.getContents() != '+' && !(SE.isMarked()) ) {

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
			if ( !((CMC.getX() - 1) <= 0) && !((CMC.getY() + 1) >= width) )
			{               

				// Get Southwest MazeCell
				SW = ( grid[(CMC.getY()) + 1][CMC.getX() - 1] );

				// If content is not wall and the cell is not visited,
				if ( SW.getContents() != '+' && !(SW.isMarked()) ) {

					// Set it as visited cell.
					SW.setMark();

					// Keep parent's infomation.
					CMC.setParent(SW);

					// Add child to queue.
					Q.add(SW);
				}
			}
		}
		
		// If we get here, the maze is not solvable.
		System.out.println("Maze not solvable!");
		System.out.println();

	}

	/**
	 * Main method for test cases:
	 */
	public static void main(String[] arg)
	{

		Maze M = new Maze(testMaze);  
		System.out.println(M);
		M.solve();
		System.out.println(M);

		M = new Maze(simpleMaze);
		System.out.println(M);
		M.solve();
		System.out.println(M);

		M = new Maze(difficultMaze);  
		System.out.println(M);
		M.solve();
		System.out.println(M);
	}
}