package routing;

/**
 * @author Christian W. Nielsen - s093018
 *
 */
public class PixelSquare {
	
	/**
	 * FREE = 0;
	 * BALL = 1, 
	 * ROBOT = 2,
	 * ROBOT FRONT = 3
	 * ROBOT BACK = 4 
	 * OBSTACLE = 5, 
	 * GOAL = 6, 
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
