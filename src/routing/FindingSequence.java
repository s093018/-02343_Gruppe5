package routing;

import java.util.ArrayList;

public class FindingSequence {

	//	 private final int N = 0, NE = 45, E = 90, SE = 135, S = 180, SW = 225, W = 270, NW = 315; 
	private robot.Control robotControl;
	private double robotHeading, pixelSize;

	public FindingSequence(robot.Control robotControl, double robotHeading, double pixelSize) {
		this.robotControl = robotControl;
		this.robotHeading = robotHeading;
		this.pixelSize = pixelSize;
	}

	public ArrayList<DriverInstructions> sequence (ArrayList<Integer> path) { 

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
		for(DriverInstructions d : robotInstructions) {
		}
		return robotInstructions;
	}

	public void drive (ArrayList<DriverInstructions> instructions, boolean closeToWall) {
		int temp = 0;
		for(DriverInstructions di : instructions) {
			System.out.println("Instruktion "+temp+": heading="+di.getHeading()+", length="+di.getLength());
			temp++;
		}

		for(int i = 0; i < instructions.size(); i++) {	
			boolean done = false;

			/* turn robot heading or drive forward */
			if (i == 0) {

				int turn = turnDegree(instructions.get(i).getHeading(), radianToDegree1(robotHeading)); 
				System.out.println("turn 1: "+turn);
				if(turn < 0) {
					while (!done) {
						done = robotControl.turnRight(Math.abs(turn));
					}

				} else if(turn > 0) {
					while (!done) {
						done = robotControl.turnLeft(turn);
					}
				}

				/* open arms */
				if(i == instructions.size()-2) {
					done = false;
					while(!done) {
						done = robotControl.open();
					}

				}

				done = false;
				while (!done) { 
					done = robotControl.forward((int) (instructions.get(i).getLength()*pixelSize)); 
				}

			} else {
				int turn = turnDegree(instructions.get(i).getHeading(), instructions.get(i-1).getHeading());
				System.out.println("turn"+(i+1)+": "+turn);
				if(turn < 0) {
					while (!done) {
						done = robotControl.turnRight(Math.abs(turn));
					}
				} else if(turn > 0) {
					while (!done) {
						done = robotControl.turnLeft(turn);
					}
				}

				/* open arms */
				if(i == instructions.size()-1) {
					done = false;
					while(!done) {
						done = robotControl.open();
					}

				}

				done = false;
				while (!done) { 
					done = robotControl.forward((int) (instructions.get(i).getLength()*pixelSize)); 
				}
			}
		}
		/* close arms */
		boolean done = false;
		if(robotControl.getIsOpen()) { 

			while(!done) {
				done = robotControl.close();
			}

		}

		/*close to wall */
		done = false;
		if (closeToWall) {
			while(!done) {
				done = robotControl.revers(3);
			}
		}
	}

	public void goalDrive (ArrayList<DriverInstructions> instructions) {

		for(int i = 0; i < instructions.size(); i++) {	
			boolean done = false;

			/* turn robot heading or drive forward */
			if (i == 0) {
				int turn = turnDegree(instructions.get(i).getHeading(), radianToDegree1(robotHeading)); 
				if(turn < 0) {
					while (!done) {
						done = robotControl.turnLeft(Math.abs(turn));
					}

				} else if(turn > 0) {
					while (!done) {
						done = robotControl.turnRight(turn);
					}
				}

				/* open arms */
				done = false;
				if(i == instructions.size()-1) {
					while(!done) {
						done = robotControl.open();
					}

				}

				done = false;
				while (!done) { 
					done = robotControl.forward((int) (instructions.get(i).getLength()*pixelSize)); 
				}

			} else {
				int turn = turnDegree(instructions.get(i).getHeading(), instructions.get(i-1).getHeading());
				if(turn < 0) {
					while (!done) {
						done = robotControl.turnLeft(Math.abs(turn));
					}
				} else if(turn > 0) {
					while (!done) {
						done = robotControl.turnRight(turn);
					}
				}

				done = false;
				/* open arms */
				if(i == instructions.size()-1) {
					while(!done) {
						done = robotControl.open();
					}
				}
				
				done = false;
				while (!done) { 
					done = robotControl.forward((int) (instructions.get(i).getLength()*pixelSize)); 
				}
			}
		}
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
		done = false;
		if(robotControl.getIsOpen()) {
			while(!done) {
				done = robotControl.close();
			}

		}
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
	/**
	 * 
	 * @param double radian
	 * @return int result
	 * 
	 * finds degree in 45degree-interval
	 */
	public int radianToDegree(double radian) {
		double degree = (radian*180)/Math.PI;
		int result = (int)(degree + 22.5)/45;
		result *=45;
		return result;
	}
	
	public int radianToDegree1(double radian) {
		
		int result = (int)Math.toDegrees(radian);
		
		return result;
	}

	public int turnDegree(int algoHeading, int robotHeading) {
		int dif = algoHeading-robotHeading;
		if(-180<=dif && dif<=180)
			return dif;
		else if(dif<-180)
			return dif + 360;
		else if(dif>180)
			return dif - 360; // return antal grader roboten skal dreje (negativ venstre / positiv hoejre) MAX 180grader
		return dif;
	}

}

