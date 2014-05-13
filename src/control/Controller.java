package control;

import imageProcessing.Point;
import imageProcessing.TestCamera;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import robot.Control;
import routing.BFS;
import routing.Board;
import routing.DriverInstructions;
import routing.FindingSequence;

public class Controller {

	private FindingSequence fs;
	private Control robotControl;
	private TestCamera testCamera;
	private Board board;
	private BFS bfs;

	private ArrayList<Integer> path;
	private ArrayList<DriverInstructions> di;
	private char [][] map;
	private boolean endGame = false;
	private int ballCount = 0;
	private final int MAX_NO_BALLS = 6;
	private List<Point> closeBalls;

	public Controller () {
		this.testCamera = new TestCamera();
		try {
			//			robotControl = new robot.Control();
		} catch (Exception e) {	e.printStackTrace(); }

	}

	public void run() {
		System.out.println("Started!");
		while(!endGame) {

			map = testCamera.getMap().obstacle;
			board = new Board(map);
			board.fillInBalls(testCamera.getBalls());
			board.fillInRobotPosition(testCamera.getRobot().position);
			board.fillInGoals(testCamera.getGoals());
			closeBalls = board.ballsCloseToObstacle(testCamera.getBalls(), 5);

			//			System.out.println("game not ended");

			if(ballCount <= MAX_NO_BALLS) {
				Iterator<Point> it = closeBalls.iterator();
				while(it.hasNext()) {
					Point p = it.next();
					board.buildObstacleAroundBall(p, "N" , 5);
					//					System.out.println("Closeball found at [" + p.pixel_x + "," + p.pixel_y + "]");
				}

				bfs = new BFS(board.getGrid(), 'B');  
				path = bfs.findPath(closeBalls);
				System.out.println(path.size());
				/*
				String filepath = "/Users/Christoffer/Desktop/outputPath.txt";
				File f = new File(filepath);
				FileWriter fw = null;
				try  {
					fw =  new  FileWriter(f);
					fw.write(bfs.toString());
				}  catch  (IOException e) {
					e.printStackTrace();
				}
				 */
				fs = new FindingSequence(robotControl, testCamera.getRobot().heading, testCamera.getMap().pixelSize);
				if(path != null) {
					di = fs.sequence(path);
					fs.drive(di, bfs.getCloseToWall());
					ballCount++;
					System.out.println("Ballcount = " + ballCount);
				}
				break;

			} else {
				/** Drive to goal and release balls **/
				bfs = new BFS(board.getGrid(), 'G');
				path = bfs.findPath(closeBalls);
				fs = new FindingSequence(robotControl, testCamera.getRobot().heading, testCamera.getMap().pixelSize);

				di = fs.sequence(path);

				fs.goalDrive(di);
				ballCount = 0;
			}
		}
	}
}