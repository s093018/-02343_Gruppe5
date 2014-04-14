package routing;

import imageProcessing.Camera;

import java.util.ArrayList;

import robot.*;

public class FindingSequence {

	private Controller ctrl;
	private final int N = 0, NE = 45, E = 90, SE = 135, S = 180, SW = 225, W = 270, NW = 315;
	private robot.Control robot;
	private Camera camera;

	public FindingSequence(Controller ctrl) {
		this.ctrl = ctrl;
		robot = ctrl.getrobot();
		camera = ctrl.getCamera();
	}

	public ArrayList<DriverInstructions> sequence (ArrayList<Integer> path) { // path er det arraylist der kommer ud fra BFS algoritmen

		ArrayList<DriverInstructions> robotInstructions = new ArrayList<DriverInstructions>();

		int count = 1;
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
				count = 1;
			}
		}

		return robotInstructions;
	}

	public void drive (ArrayList<DriverInstructions> instructions) {

		for(int i = 0; i < instructions.size(); i++) {			

			if (i == 0) {
				int turn = turnDegree(instructions.get(i).getHeading(), 90); //radianToDegree(camera.getRobot().heading));
				if(turn < 0) {
					robot.turnLeft(Math.abs(turn));
				} else if(turn > 0) {
					robot.turnRight(turn);
				} else {
					robot.forward(instructions.get(i).getLength()); //camera.getMap().pixelSize);
				}
			} else {
				int turn = turnDegree(instructions.get(i).getHeading(), instructions.get(i-1).getHeading());
				if(turn < 0) {
					robot.turnLeft(Math.abs(turn));
				} else if(turn > 0) {
					robot.turnRight(turn);
				}
				robot.forward(instructions.get(i).getLength()); //camera.getMap().pixelSize);
			}
		}		
	}

	public int radianToDegree(double radian) {
		double degree = (radian*180)/Math.PI;
		int result = (int)(degree + 22.5)/45;
		result *=45;
		return result;
	}

	public int turnDegree(int algoHeading, int robotHeading) {
		int dif = algoHeading-robotHeading;
		if(-180<=dif && dif<=180)
			return dif;
		else if(dif<-180)
			return dif + 360;
		else if(dif>180)
			return dif - 360; // return antal grader roboten skal dreje (negativ venstre / positiv højre) MAX 180grader
		return dif;
	}

}

