package control;

import imageProcessing.*;

import java.io.*;
import java.util.*;

import routing.*;

public class ControllerBoard {

	private RealCamera realCamera;
	private Board board;
	private BFS bfs;

	private Field frontField, backField;
	private ArrayList<Integer> path;
	private ArrayList<Point> closeBalls;
	private List<Point> allBalls;
	private List<Goal> goals;
	private char [][] map;
	private boolean endGame = false;
	private int ballCount = 0;
	private final int MAX_NO_BALLS =1;
	private int pixelRadius, pixelDistance, pixelLength;

	File f;
	int ballsOnTrack;
	int iterations = 0;

	/**
	 * @author Christian W. Nielsen - s093018
	 * @author Julian Villadsen - s123641
	 */
	public ControllerBoard () {
		try {
			this.realCamera = new RealCamera();
		} catch (Exception e) {	e.printStackTrace(); }

	}

	public void run() {

			try {
				System.out.println("Started!");
				map = realCamera.getMap().obstacle;

				while(!endGame) {
					board = new Board(map);
//					for(int i = 1; i < (realCamera.getRobot().robotLength/2)+33; i++) {
//						board.buildObstacleAroundBall(realCamera.getObstacleCenter(), i);
//					}
					
					closeBalls = new ArrayList<Point>();
					allBalls = realCamera.getBalls();
					// Front and back positions for testing purposes
					//				frontField = new Field(realCamera.frontPoint.pixel_x, realCamera.frontPoint.pixel_y, 'X');
					//				board.setField(realCamera.backPoint.pixel_x, realCamera.backPoint.pixel_y, frontField);
					//				backField = new Field(realCamera.backPoint.pixel_x, realCamera.backPoint.pixel_y, 'Y');
					//				board.setField(realCamera.backPoint.pixel_x, realCamera.backPoint.pixel_y, backField);


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
					pixelRadius = (int)(realCamera.getRobot().robotLength/2)+6;
					//				pixelDistance = (int)realCamera.getRobot().robotLength/2 + 6;
					pixelDistance = pixelRadius;
					pixelLength = 2;


					//Solve problem with balls outside playground.
//					ArrayList<Point> ballsToBeRemoved = new ArrayList<Point>();
//					for (Point ball : allBalls) {
//						if(board.ballSurroundedByObstacles(ball)){
//							board.setField(ball.pixel_x, ball.pixel_y, new Field(ball.pixel_x,ball.pixel_y , 'O'));
//							ballsToBeRemoved.add(ball);
//						}
//					}
//					allBalls.removeAll(ballsToBeRemoved);
//
					closeBalls.addAll(board.ballsCloseToObstacle(allBalls, pixelRadius));
//					board.clearBalls(allBalls);
//
//					allBalls.removeAll(closeBalls);

					board.fakeWallsBuild(pixelRadius);
					board.moveGoals(realCamera.getGoals(), pixelDistance, 'F', pixelLength);

//					if(allBalls != null && allBalls.size() > 0) {
//						board.fillInBalls(allBalls);
//					} else if(closeBalls != null && closeBalls.size() > 0) {
//						board.fillInBalls(closeBalls);
						closeBalls = board.moveBallsPastFakeWalls(closeBalls, ' ', pixelLength);
//					}


					if(ballCount < MAX_NO_BALLS) {

						bfs = new BFS(board.getGrid(), 'G');  
						path = bfs.findPath(closeBalls);

						// No path found!
						if(path == null && allBalls.size() > 0) {
							Point robotPosition = realCamera.getRobot().position;
							robotPosition.setPathDirection(board.directionToObstacle(robotPosition));
							board.buildPath(robotPosition, pixelDistance, ' ');
							bfs = new BFS(board.getGrid(), 'B');
							path = bfs.findPath(closeBalls);
						}

						// To be deleted - only used for testing!
						String filepath = "/Users/Christian/Desktop/PathDirections/outputPath"+iterations+".txt";
						f = new File(filepath);
						FileWriter fw = null;
						try  {
							fw =  new  FileWriter(f);
							fw.write(board.toString());
						}  catch  (IOException e) {
							e.printStackTrace();
	
							System.out.println("Emergency shutdown");
							try {
								fw.close();
							} catch (IOException e1) {
								e1.printStackTrace();

								System.out.println("Emergency shutdown");
							} 
						}
						System.out.println("Board file called: "+filepath+" created.");
						//
					
					if(path == null) {
						endGame = true;
					}
				}

				board.clearBoard();

				iterations++;

				System.out.println("NYT BILLEDE NY RUTE");
				realCamera.update();
				if(ballCount == 1) {
					endGame = true;
				}
				ballCount++;
			}
			System.out.println("shutdown done");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Emergency shutdown");
		} 		
	}
}



