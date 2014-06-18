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

	private Field frontField, backField;
	private char [][] map;

	int iterations = 0; //Skal slettes 
	private File f; // Skal slettes

	private ArrayList<DriverInstructions> di;
	private ArrayList<Integer> path;
	private List<Point> closeBalls;
	private RobotOperations ro;
	private Control robotControl;
	private RealCamera realCamera;
	private Board board;
	private BFS2 bfs;

	public ControllerFinal () {
		try {
			this.realCamera = new RealCamera();
			MAX_NO_BALLS = realCamera.getBalls().size();

			robotControl = new robot.Control();
		} catch (Exception e) {	e.printStackTrace(); }

	}

	public void run() {

		try {
			System.out.println("Started!");
			map = realCamera.getMap().obstacle;

			while(!endGame) {
				board = new Board(map);

				frontField = new Field(realCamera.frontPoint.pixel_x, realCamera.frontPoint.pixel_y, 'X');
				board.setField(realCamera.backPoint.pixel_x, realCamera.backPoint.pixel_y, frontField);
				backField = new Field(realCamera.backPoint.pixel_x, realCamera.backPoint.pixel_y, 'Y');
				board.setField(realCamera.backPoint.pixel_x, realCamera.backPoint.pixel_y, backField);


				// Add balls, robot and goals to the board.
				board.fillInBalls(realCamera.getBalls());
				board.fillInRobotPosition(realCamera.getRobot().position);
				board.fillInGoals(realCamera.getGoals());

				// Skal slettes - bruges kun til test.
				int temp = 0;
				for (Point p : realCamera.getBalls()) {
					System.out.println("Bold "+temp+": ("+p.pixel_x+","+p.pixel_y+")" );
					temp++;
				}
				//


				//				board.moveGoals(realCamera.getGoals(), 10, 'F', 10);
				closeBalls = board.ballsCloseToObstacle(realCamera.getBalls(), 10);
				//				board.fakeWallsBuild(realCamera.getRobot().robotWidth);

				if(ballsInRobot < MAX_NO_BALLS_BEFORE_SCORING) {

					Iterator<Point> it = closeBalls.iterator();
					while(it.hasNext()) {
						Point p = it.next();
						board.buildObstacleAroundBall(p, 10);
						System.out.println("Closeball found at [" + p.pixel_x + "," + p.pixel_y + "]");
					}

					bfs = new BFS2(board.getGrid(), 'B');  
					//					path = bfs.findPath(closeBalls);
					path = bfs.findPath();


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

					if(path != null) {
						di = ro.sequence(path);
						int temp1 = 0;
						for (DriverInstructions i : di) {
							System.out.println("instructions "+temp1+": Heading:"+i.getHeading()+", length: "+i.getLength());
							temp1++;
						}
						int turn = ro.turnDegree(di.get(0).getHeading(),ro.radianToDegree1(realCamera.getRobot().heading));
						if (turn < 0) {
							ro.turnLeft(turn);
						} else if(turn > 0) {
							ro.turnRight(turn);
						}

						if(di.size() == 1) {
							ro.open();
						}

						ro.forward(di.get(0).getLength()*realCamera.getMap().pixelSize);

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

								if (i == di.size()-1) { // hvornaar aabner laagerne sig
									ro.open();
								}
								ro.forward(di.get(i).getLength()*realCamera.getMap().pixelSize);
							}
						}
						ro.close();

						if(bfs.getCloseToWall()) {
							ro.reverse();
						}

						if(MAX_NO_BALLS > realCamera.getBalls().size()) {
							ballsInRobot++;
						}
						System.out.println("Number of balls currently in the robot = " + ballsInRobot);

					} 
				} else if(ballsInRobot >= MAX_NO_BALLS_BEFORE_SCORING || realCamera.getBalls().size() == 0) {

					bfs = new BFS2(board.getGrid(), 'G');
					path = bfs.findPath(/*closeBalls*/);
					ro = new RobotOperations(robotControl, realCamera.getRobot().heading, realCamera.getMap().pixelSize);
					di = ro.sequence(path);
					int temp1 = 0;
					for (DriverInstructions i : di) {
						System.out.println("instructions "+temp1+": Heading:"+i.getHeading()+", length: "+i.getLength());
						temp1++;
					}

					int turn = ro.turnDegree(di.get(0).getHeading(),ro.radianToDegree1(realCamera.getRobot().heading));
					if (turn < 0) {
						ro.turnLeft(turn);
					} else if(turn > 0) {
						ro.turnRight(turn);
					}

					if(di.size() == 1) {
						ro.open();
					}

					ro.forward(di.get(0).getLength()*realCamera.getMap().pixelSize);

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

							if (i == di.size()-1) { // hvornaar aabner laagerne sig
								ro.open();
							}
							ro.forward(di.get(i).getLength()*realCamera.getMap().pixelSize);
						}
					}

					ro.kick();

					ro.close();

					ballsInRobot = 0;
				}


				board.getField(frontField.getX(), frontField.getY()).setValue(' ');
				board.getField(backField.getX(), backField.getY()).setValue(' ');
				board.clearRobot(realCamera.getRobot().position);
				board.clearBalls(realCamera.getBalls());


				iterations++;
				System.out.println("NYT BILLEDE NY RUTE");
				realCamera.update();
				MAX_NO_BALLS = realCamera.getBalls().size();
				if(realCamera.getBalls().size() == 0 && ballsInRobot == 0) {
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
}



