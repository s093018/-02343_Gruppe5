package routing;

import java.util.ArrayList;

import routing.DriverInstructions;

/**
 * @author Christian W. Nielsen - s093018
 */
public class RobotOperations {

	//	 private final int N = 0, NE = 45, E = 90, SE = 135, S = 180, SW = 225, W = 270, NW = 315; 
	private robot.Control robotControl;
	private double robotHeading, pixelSize;
	private int correctionRate = 60; //TODO adjust this

	public RobotOperations(robot.Control robotControl, double robotHeading, double pixelSize) {
		this.robotControl = robotControl;
		this.robotHeading = robotHeading;
		this.pixelSize = pixelSize;
	}

	public ArrayList<DriverInstructions> sequence (ArrayList<Integer> path) { 
	
		ArrayList<DriverInstructions> robotInstructions = new ArrayList<DriverInstructions>();
		int count = 0;
		int updateCount = 0;
		for(int i = 1; i < path.size(); i++) {

			if (path.get(i).equals((path.get(i-1)))) {

				if (i == path.size()-1) {
					count++;

					DriverInstructions di = new DriverInstructions(path.get(i-1), count);
					robotInstructions.add(di); 

				}else {
					count++;
				}
			} else {
				DriverInstructions di = new DriverInstructions(path.get(i-1), count);
				robotInstructions.add(di); 
				count = 0;
			}

			if(updateCount == correctionRate) {
				DriverInstructions di1 = new DriverInstructions(path.get(i-1), count);
				robotInstructions.add(di1);
				DriverInstructions di = new DriverInstructions(path.get(i-1), 0);
				robotInstructions.add(di); 
				updateCount = 0;
				count = 0;
			}
			updateCount++;
		}
		
		return robotInstructions;
	}

	public void shutdown () {
		robotControl.shutdown();
	}

	//	public void celebration () {
	//		for(int i = 0; i < 10; i++) {
	//			boolean done = false;
	//
	//			while(!done){
	//				done = robotControl.celebrate();
	//			}
	//		}
	//	}
	
	public int radianToDegree1(double radian) {

		int result = (int)Math.toDegrees(radian);

		if (result < 0) {
			result = result + 360;
		}
		return result;
	}

	public int turnDegree(int algoHeading, int robotHeading) {
		int dif = robotHeading-algoHeading;
		if(-180 <= dif && dif <= 180)
			return dif;
		else if(dif < 180)
			return dif + 360;
		else if(dif > 180)
			return dif - 360; // return antal grader roboten skal dreje (negativ venstre / positiv hoejre) MAX 180grader
		return dif;
	}

	public void turnRight(int turn) {
		boolean done = false;
		while (!done) {
			done = robotControl.turnRight(turn);
		}
	}

	public void turnLeft(int turn) {
		boolean done = false;
		while (!done) {
			done = robotControl.turnLeft(Math.abs(turn));
		}
	}

	public void in() {
		robotControl.in();
	}

	public void forward(double length) {
		boolean done = false;
		while (!done) { 
			done = robotControl.forward(length); 
		}
	}

	public void stop() {
		robotControl.collectStop();
	}

	public void reverse(int lenght) {
		boolean done = false;
		while(!done) {
			done = robotControl.revers(lenght);
		}
	}
	
	public void out () {
		robotControl.out();
	}

	public void kick() {
		boolean done = false;
		while(!done) {
			done = robotControl.kick();
		}
		done = false;
		while(!done) {
			done = robotControl.kick();
		}
		done = false;
		while(!done) {
			done = robotControl.kick();
		}
	}
}

