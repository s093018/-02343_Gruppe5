package routing;

/**
 * @author Christoffer - s103148
 */

/**
 * The Field class implements a single cell of 
 *   a map. It includes private data fields that
 *   tell: 
 *     whether a cell is marked (already visited by the BFS)
 *     what a cell's contents are
 *     what a cell's parent is (for the BFS)
 *     what the cell's position is in the maze
 */
class Field {
	
	private boolean  marked;
	private char      value;
	private Field    parent;
	private int        x, y;

	/**
	 * The constructor for Fields. The cells are by default
	 *   unmarked and without a parent cell. To construct a new cell,
	 *   its position in the maze (x,y) and its value 'O', 'R', or 'B'
	 *   must be specified.
	 */
	public Field(int x, int y, char c) {
		marked = false;
		parent =  null;
		this.value = c;
		this.x = x; 
		this.y = y;
	}

	/**
	 * A public method that returns the value of the Field that invokes it.
	 */
	public char getValue() {
		return this.value;
	}
	
	/**
	 * A public method that changes the value of the Field that invokes it.
	 */
	public void setValue(char val) {
		this.value = val;
	}
	
	/**
	 * A public method that changes marked value of the Field that invokes it.
	 */
	public void setMark() {
		marked = true;
	}

	/**
	 * A public method that returns the marked value of the Field that invokes it.
	 */
	public boolean isMarked() {
		return this.marked;
	}

	/**
	 * A public method that returns the parent of the Field that invokes it.
	 */
	public Field getParent() {
		return this.parent;
	}

	/**
	 * A public method that changes parent value of the Field to parent that invokes it.
	 */
	public void setParent(Field child) {
		child.parent = this;
	}

	/**
	 * A public method that returns the x of the Field that invokes it.
	 */
	public int getX() {
		return this.x;
	}  

	/**
	 * A public method that returns the y-coordinate of the Field that invokes it.
	 */
	public int getY() {
		return this.y;
	}

	/**
	 * A public method that changes the value of the Field to '*'.
	 */
	public void star() {
		value = '*';
	}   
}
