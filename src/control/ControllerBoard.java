package control;

import imageProcessing.*;

import java.io.*;
import java.util.*;

import robot.Control;
import routing.*;

public class ControllerBoard {

	private RealCamera realCamera;
	private Board board;
	private BFS2 bfs;

	private Field frontField, backField;
	private ArrayList<Integer> path;
	private ArrayList<DriverInstructions> di;
	private char [][] map;
	private boolean endGame = false;
	private int ballCount = 0;
	private final int MAX_NO_BALLS =0;
	private List<Point> closeBalls;
	File f;
	int ballsOnTrack;
	int iterations = 0;

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

				board.moveGoals(realCamera.getGoals(), 10, 'F', 10);
				closeBalls = board.ballsCloseToObstacle(realCamera.getBalls(), 10);
				board.fakeWallsBuild((int)realCamera.getRobot().robotWidth);
				ballsOnTrack = realCamera.getBalls().size();

				if(ballCount <= MAX_NO_BALLS) {

					Iterator<Point> it = closeBalls.iterator();
					while(it.hasNext()) {
						Point p = it.next();
						board.buildObstacleAroundBall(p, 10);
						System.out.println("Closeball found at [" + p.pixel_x + "," + p.pixel_y + "]");
					}

					bfs = new BFS2(board.getGrid(), 'B');  
					//					path = bfs.findPath(closeBalls);
					path = bfs.findPath();

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

					if(path != null) {
						endGame = true;
					}
				}

				board.getField(frontField.getX(), frontField.getY()).setValue(' ');
				board.getField(backField.getX(), backField.getY()).setValue(' ');
				board.clearRobot(realCamera.getRobot().position);
				board.clearBalls(realCamera.getBalls());

				iterations++;

				System.out.println("NYT BILLEDE NY RUTE");
				realCamera.update();
				endGame = true;
			}
			System.out.println("shutdown done");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Emergency shutdown");
		} 		
	}
}



