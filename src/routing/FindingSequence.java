package routing;

import java.util.ArrayList;
import robot.*;

public class FindingSequence {
	
	private Controller ctrl;
	private final int N = 0, NE = 45, E = 90, SE = 135, S = 180, SW = 225, W = 270, NW = 315;
	
	public FindingSequence(Controller ctrl) {
		this.ctrl = ctrl;
	}
	
	public ArrayList<DriverInstructions> sequence (ArrayList<Integer> path) { // path er det arraylist der kommer ud fra BFS algoritmen

		ArrayList<DriverInstructions> robotInstructions = new ArrayList<DriverInstructions>();

		int count = 1;
		for(int i = 1; i < path.size(); i++) {
			if (path.get(i) == (path.get(i-1))) {

				if (i == path.size()-1) {
					count++;
					
					DriverInstructions di = new DriverInstructions(path.get(i-1), count);
					robotInstructions.add(di); // her skal der gemmes noget robot instruktion med heading og count

				}else {
					count++;
				}
			} else {
				DriverInstructions di = new DriverInstructions(path.get(i-1), count);
				robotInstructions.add(di); // her skal der gemmes noget robot instruktion med heading og count
				count = 1;
			}
		}

		return robotInstructions;
	}
	
	public void drive (ArrayList<DriverInstructions> instructions) {
		
		for(DriverInstructions i : instructions) {			
			
			/*switch (radianToDegree(ctrl.getCamera().getRobot().heading)) {
				
				case N:
					
					break;
				case NE:
					
					break;
				case E:
					
					break;
				case SE:
					
					break;
				case S:
					
					break;
				case SW:
					
					break;
				case W:
					
					break;
				case NW:
					
					break;
			}*/
		}		
	}
	
	public int radianToDegree(double radian) {
		double degree = (radian*180)/Math.PI;
		int result = (int)(degree + 22.5)/45;
		result *=45;
		return result;
	}
	
	public int turnDegree(int algoHeading, int robotHeading) {
	
		return 0; // return antal grader roboten skal dreje (negativ venstre / positiv højre) MAX 180grader
	}

}

