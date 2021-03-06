package control;

import imageProcessing.*;

import java.io.*;
import java.util.*;

import robot.Control;
import routing.*;

public class ControllerFinal {

	private boolean endGame = false;
	private static int MAX_NO_BALLS;
	private final int MAX_NO_BALLS_BEFORE_SCORING = 4;
	int ballsInRobot = 0;
	int numberOfBallLeft;
	private char [][] map;
	int iterations = 0; 
	int iterations1 = 0;
	private File f; 
	private ArrayList<DriverInstructions> di;
	private ArrayList<Integer> path;
	private ArrayList<Point> closeBalls;
	private List<Point> allBalls;
	private RobotOperations ro;
	private Control robotControl;
	private RealCamera realCamera;
	private Board board;
	private BFS bfs;
	private int pixelRadius, pixelDistance, pixelLength;
	private int ballsInWall = -1;

	
	/**
	 * @author Christian W. Nielsen - s093018
	 * @author Christoffer Passer Jensen - s103148
	 */
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
				for(int i = 1; i < (realCamera.getRobot().robotLength/2)+21; i++) {
					board.buildObstacleAroundBall(realCamera.getObstacleCenter(), i);
				}
				
				closeBalls = new ArrayList<Point>();
				allBalls = realCamera.getBalls();

				// Add balls, robot and goals to the board.
				board.fillInBalls(allBalls);
				board.fillInRobotPosition(realCamera.getRobot().position);
				board.fillInSmallestGoal(realCamera.getGoals());

				/* pixelRadius is for fake walls.
				 * pixelDistance is for moving goals (or balls, if using moveBalls())
				 * 		away from obstacles
				 * 		and should be equal to or less than pixelRadius.
				 * pixelLength is for fake obstacles around balls and
				 * 		making paths for balls or goals through (fake) obstacles
				 * 		and should be quite small.
				 * @author Julian Villadsen - s123641 */
				pixelRadius = (int)(realCamera.getRobot().robotLength/2);
				pixelDistance = pixelRadius;
				pixelLength = 9;

				//Solve problem with balls outside playground.
				ArrayList<Point> ballsToBeRemoved = new ArrayList<Point>();
				for (Point ball : allBalls) {
					if(board.ballSurroundedByObstacles(ball)){
						board.setField(ball.pixel_x, ball.pixel_y, new Field(ball.pixel_x,ball.pixel_y , 'O'));
						ballsToBeRemoved.add(ball);
					}
				}
				
				allBalls.removeAll(ballsToBeRemoved);
				closeBalls.addAll(board.ballsCloseToObstacle(allBalls, pixelRadius));
				board.clearBalls(allBalls);
				allBalls.removeAll(closeBalls);
				board.fakeWallsBuild(pixelRadius);
				board.moveGoals(realCamera.getGoals(), pixelDistance, 'F', pixelLength);

				if(allBalls != null && allBalls.size() > 0 && allBalls.size() != ballsInWall) {
					board.fillInBalls(allBalls);
				} else if(closeBalls != null && closeBalls.size() > 0) {
					board.fillInBalls(closeBalls);
					closeBalls = board.moveBallsPastFakeWalls(closeBalls, ' ', pixelLength);
				}
				ballsInWall = -1;
				
				if(ballsInRobot < MAX_NO_BALLS_BEFORE_SCORING) {
					bfs = new BFS(board.getGrid(), 'B');  
					path = bfs.findPath(closeBalls);			

					// No path found!
					if(path == null ) {
						Point robotPosition = realCamera.getRobot().position;
						robotPosition.setPathDirection(board.directionToObstacle(robotPosition));
						board.buildPath(robotPosition, pixelDistance, ' ');
						bfs = new BFS(board.getGrid(), 'B');
						path = bfs.findPath(closeBalls);
					}

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
					System.out.println("Board file called: "+filepath+" created.");
					
					ro = new RobotOperations(robotControl, realCamera.getRobot().heading, realCamera.getMap().pixelSize);

					System.out.println("robotHeading: "+ro.radianToDegree1(realCamera.getRobot().heading));

					// If path found -> Drive!
					if(path != null) { 
						di = ro.sequence(path);
						
						int temp1 = 0;
						for (DriverInstructions i : di) {
							System.out.println("instructions "+temp1+": Heading:"+i.getHeading()+", length: "+i.getLength());
							temp1++;
						}
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
								System.out.println("robotHeading before update: " + ro.radianToDegree1(realCamera.getRobot().heading)); 
								System.out.println("NYT BILLEDE KALIBRERING"); //
								realCamera.update();
								System.out.println("RobotHeading after Update: " + ro.radianToDegree1(realCamera.getRobot().heading)); 

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
							ro.reverse((int)(pixelRadius/2*realCamera.getMap().pixelSize));
						}
						if(MAX_NO_BALLS > realCamera.getBalls().size()) {
							ballsInRobot = MAX_NO_BALLS - realCamera.getBalls().size();
						}
						System.out.println("Number of balls currently in the robot = " + ballsInRobot);
					}
					else {
						ballsInWall = allBalls.size();	
						System.out.println("CloseBalls.size: "+closeBalls.size());
						if(closeBalls.size() == 0) {
							endGame = true;
						}
					}
				}  
				
				if(ballsInRobot >= MAX_NO_BALLS_BEFORE_SCORING || realCamera.getBalls().size() == 0 || endGame == true) {
					// The robot has collected the maximum number of balls it can contain, or there are not any balls left 
					// on the field -> Drive to goal and deliver the balls.
					bfs = new BFS(board.getGrid(), 'G');
					path = bfs.findPath(closeBalls);

					// No path found!
					if(path == null) {
						Point robotPosition = realCamera.getRobot().position;
						robotPosition.setPathDirection(board.directionToObstacle(robotPosition));
						board.buildPath(robotPosition, pixelDistance, ' ');
						bfs = new BFS(board.getGrid(), 'G');
						path = bfs.findPath(closeBalls);
					}
					
					String filepath = "/Users/Christian/Desktop/PathDirections/outputPathGoal"+iterations1+".txt";
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
					System.out.println("Board file called: "+filepath+" created.");

					if(path != null) {
						ro = new RobotOperations(robotControl, realCamera.getRobot().heading, realCamera.getMap().pixelSize);
						di = ro.sequence(path);
						
						int temp1 = 0; 
						for (DriverInstructions i : di) { 
							System.out.println("instructions "+temp1+": Heading:"+i.getHeading()+", length: "+i.getLength()); 
							temp1++; 
						} 

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
						ro.in();
						ro.reverse((int)(pixelRadius/2*realCamera.getMap().pixelSize));

						ballsInRobot = 0;
						MAX_NO_BALLS = realCamera.getBalls().size();
					}
				}

				board.clearBoard();

				iterations++;
				iterations1++;
				System.out.println("NYT BILLEDE NY RUTE");
				realCamera.update();
				numberOfBallLeft = realCamera.getBalls().size();
				if(numberOfBallLeft == 0 && ballsInRobot == 0) {
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



