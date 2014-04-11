package routing;

public class DriverInstructions {

	private int heading;
	private int length;
	
	public DriverInstructions (int heading, int length) {
		this.heading = heading;
		this.length = length;
	}

	public int getHeading() {
		return heading;
	}

	public void setHeading(int heading) {
		this.heading = heading;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}
	
	
}
