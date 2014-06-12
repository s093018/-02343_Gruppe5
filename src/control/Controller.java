package control;

import imageProcessing.*;

import java.io.*;
import java.util.*;

import robot.Control;
import routing.*;

public class Controller {

	private FindingSequence fs;
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
	private final int MAX_NO_BALLS = 2;
	private List<Point> closeBalls;
	int ballsOnTrack;

	public Controller () {
		try {
			this.realCamera = new RealCamera();
			robotControl = new robot.Control();
		} catch (Exception e) {	e.printStackTrace(); }

	}

	public void run() {

		try {
			System.out.println("Started!");
//			map = realCamera.getMap().obstacle;
//				board = new Board(map);
//				board.fillInGoals(realCamera.getGoals());
//				board.fakeWallsBuild(realCamera.getRobot().robotWidth);
//				board.moveGoals(realCamera.getGoals(), 10, 'F', 10);
			map = realCamera.getMap().obstacle;
			
			while(!endGame) {
				board = new Board(map);
				
				Point tempPoint = realCamera.frontPoint.convert();
				Field frontField = new Field(tempPoint.pixel_x, tempPoint.pixel_y, 'X');
				board.setField(tempPoint.pixel_x, tempPoint.pixel_y, frontField);
				tempPoint = realCamera.backPoint.convert();
				Field backField = new Field(tempPoint.pixel_x, tempPoint.pixel_y, 'Y');
				board.setField(tempPoint.pixel_x, tempPoint.pixel_y, backField);

				board.fillInBalls(realCamera.getBalls());
				board.fillInRobotPosition(realCamera.getRobot().position);
				board.fillInGoals(realCamera.getGoals());
				
//				board.moveGoals(realCamera.getGoals(), 10, 'F', 10);
				closeBalls = board.ballsCloseToObstacle(realCamera.getBalls(), 5);
	//			board.fakeWallsBuild(realCamera.getRobot().robotWidth);
				ballsOnTrack = realCamera.getBalls().size();
				
				if(ballCount <= MAX_NO_BALLS) {

					Iterator<Point> it = closeBalls.iterator();
					while(it.hasNext()) {
						Point p = it.next();
						board.buildObstacleAroundBall(p, 5);
						System.out.println("Closeball found at [" + p.pixel_x + "," + p.pixel_y + "]");
					}

					bfs = new BFS(board.getGrid(), 'B');  
					path = bfs.findPath(closeBalls);
					
					System.out.println("Heading in radian: "+realCamera.getRobot().heading);
					System.out.println("Heading: "+board.radianToDegree1(realCamera.getRobot().heading));

					String filepath = "/Users/Christian/Desktop/outputPath.txt";
					File f = new File(filepath);
					FileWriter fw = null;
					try  {
						fw =  new  FileWriter(f);
						fw.write(board.toString());
					}  catch  (IOException e) {
						e.printStackTrace();
						fs.shutdown();
						System.out.println("Emergency shutdown");
						try {
							fw.close();
						} catch (IOException e1) {
							e1.printStackTrace();
							fs.shutdown();
							System.out.println("Emergency shutdown");
						} 
					}

					fs = new FindingSequence(robotControl, realCamera.getRobot().heading, realCamera.getMap().pixelSize);

					if(path != null) {
						di = fs.sequence(path);
						fs.drive(di, bfs.getCloseToWall());
						System.out.println("Ballcount = " + ballCount);
					}

					
				}/* else {
					/** Drive to goal and release balls **//*

					bfs = new BFS(board.getGrid(), 'G');
					path = bfs.findPath(closeBalls);
					fs = new FindingSequence(robotControl, realCamera.getRobot().heading, realCamera.getMap().pixelSize);

					di = fs.sequence(path);

					fs.goalDrive(di);

					ballCount = 0;
					endGame = true;
				}*/
				
				
			 realCamera.update();
			 if(ballsOnTrack - realCamera.getBalls().size() == 1) {
				 ballCount++;
			 }
			 
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


			fs.shutdown();
			System.out.println("shutdown done");
		} catch (Exception e) {
			e.printStackTrace();
			fs.shutdown();
			System.out.println("Emergency shutdown");
		} 		
	}
}


