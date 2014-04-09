package routing;

public class DriverInstructions {

	private String heading;
	private int length;
	
	public DriverInstructions (String heading, int length) {
		this.heading = heading;
		this.length = length;
	}

	public String getHeading() {
		return heading;
	}

	public void setHeading(String heading) {
		this.heading = heading;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}
	
	
}
