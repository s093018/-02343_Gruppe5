package system;

/**
 * @author Christian W. Nielsen - s093018
 *
 */
public class PixelSquare {
	
	/**
	 * FREE = 0;
	 * BALL = 1, 
	 * ROBOT = 2, 
	 * OBSTACLE = 3, 
	 * GOAL = 4, 
	 */
	private int status; 
	
	public PixelSquare (int status) {
		this.status = status;
	}
	
	public int getStatus () {
		return status;
	}
	
	public void setSatus (int newStatus) {
		status = newStatus;
	}
}
