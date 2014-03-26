package routing;

/**
 * @author Christian W. Nielsen - s093018
 *
 */
public class PixelSquare {
	
	/**
	 * FREE = '';
	 * BALL = "B" 
	 * ROBOT = "R",
	 * ROBOT FRONT = "F"
	 * ROBOT BACK = "A" 
	 * OBSTACLE = "O", 
	 * GOAL = "G", 
	 */
	private char status; 
	
	public PixelSquare (char status) {
		this.status = status;
	}
	
	public char getStatus () {
		return status;
	}
	
	public void setSatus (char newStatus) {
		status = newStatus;
	}
}
