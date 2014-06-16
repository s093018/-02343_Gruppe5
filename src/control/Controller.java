package control;

import imageProcessing.*;

import java.io.*;
import java.util.*;

import robot.Control;
import routing.*;

public class Controller {

	private RobotOperations ro;
	private Control robotControl;
	private RealCamera realCamera;
	private Board board;
	private BFS bfs;

	private Field frontField, backField;
	private ArrayList<Integer> path;
	private ArrayList<DriverInstructions> di;
	private char [][] map;
	private boolean endGame = false;
	private int ballCount = 0;
	private final int MAX_NO_BALLS =1;
	private List<Point> closeBalls;
	File f;
	int ballsOnTrack;
	int iterations = 0;

	public Controller () {
		try {
			this.realCamera = new RealCamera();
			robotControl = new robot.Control();
		} catch (Exception e) {	e.printStackTrace(); }

	}

	public void run() {

		try {
			System.out.println("Started!");
			map = realCamera.getMap().obstacle;
			for(int i = 0; i < 480; ++i)
				System.out.print(" " + map[345][i]);
			System.out.println();
			while(!endGame) {
				realCamera.update();
				board = new Board(map);

				Point tempPoint = realCamera.frontPoint;
				frontField = new Field(tempPoint.pixel_x, tempPoint.pixel_y, 'X');
				board.setField(tempPoint.pixel_x, tempPoint.pixel_y, frontField);
				tempPoint = realCamera.backPoint;
				backField = new Field(tempPoint.pixel_x, tempPoint.pixel_y, 'Y');
				board.setField(tempPoint.pixel_x, tempPoint.pixel_y, backField);


				board.fillInBalls(realCamera.getBalls());
				int temp = 0;
				for (Point p : realCamera.getBalls()) {
					System.out.println("Bold "+temp+": ("+p.pixel_x+","+p.pixel_y+")" );
					temp++;
				}

				board.fillInRobotPosition(realCamera.getRobot().position);
				board.fillInGoals(realCamera.getGoals());

				//				board.moveGoals(realCamera.getGoals(), 10, 'F', 10);
				closeBalls = board.ballsCloseToObstacle(realCamera.getBalls(), 40);
				//			board.fakeWallsBuild(realCamera.getRobot().robotWidth);
				ballsOnTrack = realCamera.getBalls().size();

				if(ballCount <= MAX_NO_BALLS) {

					Iterator<Point> it = closeBalls.iterator();
					while(it.hasNext()) {
						Point p = it.next();
						board.buildObstacleAroundBall(p, 10);
						System.out.println("Closeball found at [" + p.pixel_x + "," + p.pixel_y + "]");
					}

					bfs = new BFS(board.getGrid(), 'B');  
					path = bfs.findPath(closeBalls);


					String filepath = "/Users/Christian/Desktop/outputPath"+iterations+".txt";

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

					ro = new RobotOperations(robotControl, realCamera.getRobot().heading, realCamera.getMap().pixelSize);

					System.out.println("robotHeading: "+ro.radianToDegree1(realCamera.getRobot().heading));

					if(path != null) {
						di = ro.sequence(path);
						int temp1 = 0;
						for (DriverInstructions i : di) {
							System.out.println("instructions "+temp1+": Heading:"+i.getHeading()+", length: "+i.getLength());
							temp1++;
						}
						int turn = ro.turnRightDegree(di.get(0).getHeading(),ro.radianToDegree1(realCamera.getRobot().heading));
						//												if (turn < 0) {
						//													ro.turnLeft(turn);
						//												} else if(turn > 0) {
						ro.turnRight(turn);
						//												}
						if(di.size() == 1) {
							ro.open();
						}

						ro.forward((int)(di.get(0).getLength()*realCamera.getMap().pixelSize));

						for(int i = 1; i < di.size(); i++) {

							if(di.get(i).getLength() == 0) {
								System.out.println("robotHeading before update: "+ro.radianToDegree1(realCamera.getRobot().heading));
								realCamera.update();
								System.out.println("RobotHeading after Update: "+ro.radianToDegree1(realCamera.getRobot().heading));
								turn = ro.turnRightDegree(di.get(i).getHeading(),ro.radianToDegree1(realCamera.getRobot().heading));
								if(turn != 360 || turn != 0) {
									//														if (turn < 0) {
									//															ro.turnLeft(turn);
									//														} else if(turn > 0) {
									ro.turnRight(turn);
								}

							} else {

								turn = ro.turnRightDegree(di.get(i).getHeading(), di.get(i-1).getHeading());
								//														if (turn < 0) {
								//															ro.turnLeft(turn);
								//														} else if(turn > 0) {
								ro.turnRight(turn);
								//														}
								if (i == di.size()-1) { // hvornår åbner lågerne sig
									ro.open();
								}
								ro.forward((int)(di.get(0).getLength()*realCamera.getMap().pixelSize));
							}
						}
						ro.close();

						if(bfs.getCloseToWall()) {
							ro.reverse();
						}

						ballCount++;
						System.out.println("Ballcount = " + ballCount);

					}
				}
				/* else {
					/** Drive to goal and release balls **//*

					bfs = new BFS(board.getGrid(), 'G');
					path = bfs.findPath(closeBalls);
					fs = new FindingSequence(robotControl, realCamera.getRobot().heading, realCamera.getMap().pixelSize);

					di = fs.sequence(path);

					fs.goalDrive(di);

					ballCount = 0;
					endGame = true;
				}*/

				board.getField(frontField.getX(), frontField.getY()).setValue(' ');
				board.getField(backField.getX(), backField.getY()).setValue(' ');
				board.clearRobot(realCamera.getRobot().position);
				board.clearBalls(realCamera.getBalls());


				iterations++;
				if(ballCount > 1) {
					endGame = true;
				}
				realCamera.update();
				//			 if(ballsOnTrack - realCamera.getBalls().size() == 1) {
				//				 ballCount++;
				//			 }

				//				if(realCamera.getBalls().size() == 0) {
				//					bfs = new BFS(board.getGrid(), 'G');
				//					path = bfs.findPath(closeBalls);
				//					fs = new FindingSequence(robotControl, realCamera.getRobot().heading, realCamera.getMap().pixelSize);
				//
				//					di = fs.sequence(path);
				//
				//					fs.goalDrive(di);
				//
				//					ballCount = 0;
				//					endGame = true;
				//				}
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



