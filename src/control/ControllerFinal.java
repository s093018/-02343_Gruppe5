package control;

import imageProcessing.*;

import java.io.*;
import java.util.*;

import robot.Control;
import routing.*;

public class ControllerFinal {

	private boolean endGame = false;
	private static int MAX_NO_BALLS;
	private final int MAX_NO_BALLS_BEFORE_SCORING = 5;
	int ballsInRobot = 0;

	//	private Field frontField, backField;
	private char [][] map;

	int iterations = 0; //Skal slettes 
	private File f; // Skal slettes

	private ArrayList<DriverInstructions> di;
	private ArrayList<Integer> path;
	private ArrayList<Point> closeBalls = new ArrayList<Point>();
	private RobotOperations ro;
	private Control robotControl;
	private RealCamera realCamera;
	private Board board;
	private BFS bfs;
	private int pixelRadius, pixelDistance, pixelLength;

	public ControllerFinal () {
		try {
			this.realCamera = new RealCamera();
			MAX_NO_BALLS = realCamera.getBalls().size();
			robotControl = new robot.Control();
		} catch (Exception e) {	
			e.printStackTrace(); 
		}

	}

	public void run() {

		try {
			System.out.println("Started!");
			map = realCamera.getMap().obstacle;

			while(!endGame) {
				board = new Board(map);

				// Bruges ikke lige pt - skal det slettes?
				//				frontField = new Field(realCamera.frontPoint.pixel_x, realCamera.frontPoint.pixel_y, 'X');
				//				board.setField(realCamera.backPoint.pixel_x, realCamera.backPoint.pixel_y, frontField);
				//				backField = new Field(realCamera.backPoint.pixel_x, realCamera.backPoint.pixel_y, 'Y');
				//				board.setField(realCamera.backPoint.pixel_x, realCamera.backPoint.pixel_y, backField);


				// Add balls, robot and goals to the board.
				board.fillInBalls(realCamera.getBalls());
				board.fillInRobotPosition(realCamera.getRobot().position);
				board.fillInGoals(realCamera.getGoals());

				pixelRadius = (int)realCamera.getRobot().robotWidth/4;
				pixelDistance = (int)realCamera.getRobot().robotLength/2 + 6;
				pixelLength = pixelRadius;

				closeBalls.addAll(board.ballsCloseToObstacle(realCamera.getBalls(), pixelRadius));
				board.fakeWallsBuild((int)realCamera.getRobot().robotWidth/2);

				board.moveGoals(realCamera.getGoals(), pixelDistance, 'F', pixelLength);
				board.moveBalls(closeBalls, pixelDistance, ' ', pixelLength, pixelRadius);
				for(int i = 0; i < realCamera.getGoals().size(); i++){

					Point goalPoints = realCamera.getGoals().get(i).center;
					board.buildPath(goalPoints, pixelLength, ' ');
				}

				if(ballsInRobot < MAX_NO_BALLS_BEFORE_SCORING) {

					bfs = new BFS(board.getGrid(), 'B');  
					path = bfs.findPath(closeBalls);

					// To be deleted - only used for testing!
					String filepath = "/Users/Christian/Desktop/PathDirections/outputPath"+iterations+".txt";
					f = new File(filepath);
					FileWriter fw = null;
					try  {
						fw =  new  FileWriter(f);
						fw.write(board.toString());
					}  catch  (IOException e) {
						e.printStackTrace();
						ro.shutdown();
						System.out.println("Emergency shutdown");
						try {
							fw.close();
						} catch (IOException e1) {
							e1.printStackTrace();
							ro.shutdown();
							System.out.println("Emergency shutdown");
						} 
					}
					//

					ro = new RobotOperations(robotControl, realCamera.getRobot().heading, realCamera.getMap().pixelSize);

					System.out.println("robotHeading: "+ro.radianToDegree1(realCamera.getRobot().heading));

					// No path found!
					if(path != null) { 
						di = ro.sequence(path);
						int temp1 = 0;

						// Only used for testing
						for (DriverInstructions i : di) {
							System.out.println("instructions "+temp1+": Heading:"+i.getHeading()+", length: "+i.getLength());
							temp1++;
						}
						//

						ro.in();

						int turn = ro.turnDegree(di.get(0).getHeading(), ro.radianToDegree1(realCamera.getRobot().heading));
						if (turn < 0) {
							ro.turnLeft(turn);
						} else if(turn > 0) {
							ro.turnRight(turn);
						}


						ro.forward(di.get(0).getLength()*realCamera.getMap().pixelSize);

						for(int i = 1; i < di.size(); i++) {

							// If the length of the instruction is 0, it means that we must recalibrate the heading
							// by taking a new picture and comparing the two headings.
							if(di.get(i).getLength() == 0) {
								// Only used for testing
								System.out.println("robotHeading before update: " + ro.radianToDegree1(realCamera.getRobot().heading)); //
								System.out.println("NYT BILLEDE KALIBRERING"); //
								realCamera.update();
								System.out.println("RobotHeading after Update: " + ro.radianToDegree1(realCamera.getRobot().heading)); //

								turn = ro.turnDegree(di.get(i).getHeading(),ro.radianToDegree1(realCamera.getRobot().heading));

								if (turn < 0) {
									ro.turnLeft(turn);
								} else if(turn > 0) {
									ro.turnRight(turn);
								}

							} else {

								turn = ro.turnDegree(di.get(i).getHeading(), di.get(i-1).getHeading());
								if (turn < 0) {
									ro.turnLeft(turn);
								} else if(turn > 0) {
									ro.turnRight(turn);
								}
								ro.forward(di.get(i).getLength() * realCamera.getMap().pixelSize);
							}
						}

						if(bfs.getCloseToWall()) {
							ro.reverse();
						}

						if(MAX_NO_BALLS > realCamera.getBalls().size()) {
							ballsInRobot = MAX_NO_BALLS - realCamera.getBalls().size();
						}
						System.out.println("Number of balls currently in the robot = " + ballsInRobot);

					} 
				} else if(ballsInRobot >= MAX_NO_BALLS_BEFORE_SCORING || realCamera.getBalls().size() == 0) {
					// The robot has collected the maximum number of balls it can contain, or there are not any balls left 
					// on the field -> Drive to goal and deliver the balls.
					bfs = new BFS(board.getGrid(), 'G');
					path = bfs.findPath(closeBalls);
					ro = new RobotOperations(robotControl, realCamera.getRobot().heading, realCamera.getMap().pixelSize);
					this.userShutdown();
					di = ro.sequence(path);

					int temp1 = 0; //
					// Only used for testing.
					for (DriverInstructions i : di) { //
						System.out.println("instructions "+temp1+": Heading:"+i.getHeading()+", length: "+i.getLength()); //
						temp1++; //
					} //

					ro.in();

					int turn = ro.turnDegree(di.get(0).getHeading(), ro.radianToDegree1(realCamera.getRobot().heading));
					if (turn < 0) {
						ro.turnLeft(turn);
					} else if(turn > 0) {
						ro.turnRight(turn);
					}

					ro.forward(di.get(0).getLength() * realCamera.getMap().pixelSize);

					for(int i = 1; i < di.size(); i++) {

						if(di.get(i).getLength() == 0) {
							System.out.println("robotHeading before update: "+ro.radianToDegree1(realCamera.getRobot().heading));
							System.out.println("NYT BILLEDE KALIBRERING");
							realCamera.update();
							System.out.println("RobotHeading after Update: "+ro.radianToDegree1(realCamera.getRobot().heading));

							turn = ro.turnDegree(di.get(i).getHeading(),ro.radianToDegree1(realCamera.getRobot().heading));

							if (turn < 0) {
								ro.turnLeft(turn);
							} else if(turn > 0) {
								ro.turnRight(turn);
							}

						} else {

							turn = ro.turnDegree(di.get(i).getHeading(), di.get(i-1).getHeading());
							if (turn < 0) {
								ro.turnLeft(turn);
							} else if(turn > 0) {
								ro.turnRight(turn);
							}

							ro.forward(di.get(i).getLength() * realCamera.getMap().pixelSize);
						}
					}
					ro.out();
					ro.kick();
					ro.stop();

					ballsInRobot = 0;
				}

				// Bruges ikke lige pt - skal det slettes?
				//				board.getField(frontField.getX(), frontField.getY()).setValue(' ');
				//				board.getField(backField.getX(), backField.getY()).setValue(' ');

				//				board.clearRobot(realCamera.getRobot().position);
				//				board.clearBalls(realCamera.getBalls());


				iterations++; // fjernes
				System.out.println("NYT BILLEDE NY RUTE");
				realCamera.update();
				MAX_NO_BALLS = realCamera.getBalls().size();
				if(MAX_NO_BALLS == 0 && ballsInRobot == 0) {
					endGame = true;
				}
			}

			ro.shutdown();
			System.out.println("shutdown done");
		} catch (Exception e) {
			e.printStackTrace();
			ro.shutdown();
			System.out.println("Emergency shutdown");
		} 		
	}
	/** Listen for input and shutdown if input is detected.
	 * DOES NOT WORK
	 * @author Julian
	 */
	public void userShutdown(){
					Thread userShutdown = new Thread(){
						public void run(){
							try {
								BufferedReader stdIn = new BufferedReader(
										new InputStreamReader(System.in, "UTF-8"));
								while (stdIn.readLine() != null) {
									System.out.println("-------------------User input detected, shutdown imminent.");
									ro.shutdown();
								}
							} catch (Exception e) {
								e.printStackTrace();
								ro.shutdown();
							}			
						}
					};
					userShutdown.run();
	}
}



