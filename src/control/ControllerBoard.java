package control;

import imageProcessing.*;

import java.io.*;
import java.util.*;

import routing.*;

public class ControllerBoard {

	private RealCamera realCamera;
	private Board board;
	private BFS3 bfs;

	private Field frontField, backField;
	private ArrayList<Integer> path;
	private char [][] map;
	private boolean endGame = false;
	private int ballCount = 0;
	private final int MAX_NO_BALLS =0;
	private List<Point> closeBalls;
	private int pixelRadius, pixelDistance, pixelLength;
		
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
				board.fillInRobotPosition(realCamera.getRobot().position);
				board.fillInGoals(realCamera.getGoals());

				pixelRadius = (int)realCamera.getRobot().robotWidth/2;
				pixelDistance = (int)realCamera.getRobot().robotLength/2 + 6;
				pixelLength = pixelRadius;
				
				closeBalls = board.ballsCloseToObstacle(realCamera.getBalls(), pixelRadius);
				board.fakeWallsBuild((int)realCamera.getRobot().robotWidth/2);
				board.moveGoals(realCamera.getGoals(), pixelDistance, 'F', pixelLength);
				board.moveBalls(closeBalls, pixelDistance, ' ', pixelLength, pixelRadius);
				for(int i = 0; i < realCamera.getGoals().size(); i++){
				
					Point goalPoints = realCamera.getGoals().get(i).center;
				board.buildPath(goalPoints, pixelLength, ' ');
				}
				
				if(ballCount <= MAX_NO_BALLS) {

//					Iterator<Point> it = closeBalls.iterator();
//					while(it.hasNext()) {
//						Point p = it.next();
//						board.buildObstacleAroundBall(p, pixelRadius);
//						System.out.println("Closeball found at [" + p.pixel_x + "," + p.pixel_y + "]");
//					}
					
					bfs = new BFS3(board.getGrid(), 'B');  
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

	//			board.getField(frontField.getX(), frontField.getY()).setValue(' ');
	//			board.getField(backField.getX(), backField.getY()).setValue(' ');
	//			board.clearRobot(realCamera.getRobot().position);
	//			board.clearBalls(realCamera.getBalls());

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



