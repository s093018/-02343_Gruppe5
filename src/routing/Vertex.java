package routing;

/**
 * 
 * @author Christoffer
 *
 * The Vertex object contains a coordinate (row, col), together with a boolean value.
 * The boolean value is true if the field is a treasure.
 */

class Vertex {
	
	public boolean treasure;
	public char val;
	public int row, col;

	public Vertex(char c, int row, int col, char val) {
		this.row = row;
		this.col = col;
		this.treasure = (c == '$')? true : false;
		this.val = val;
	}
}