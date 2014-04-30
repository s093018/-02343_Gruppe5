package control;

import imageProcessing.TestCamera;

import java.util.ArrayList;

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

	ArrayList<Integer> path;
	ArrayList<DriverInstructions> di;
	private char [][] map;
	
	private int ballCount = 0;
	private final int MAX_NO_BALLS = 6;

	public Controller () {
		this.testCamera = new TestCamera();
		try {
			robotControl = new robot.Control();
		} catch (Exception e) {	e.printStackTrace(); }
	}
	public void run() {

		while(true) {
			if(ballCount <= MAX_NO_BALLS) {

				map = testCamera.getMap().obstacle;

				board = new Board(map);
				board.fillInBalls(testCamera.getBalls());
				board.fillInRobotPosition(testCamera.getRobot().position);

				bfs = new BFS(board.getGrid(), 'B');  
				path = bfs.findPath( /*board.getCloseBalls()*/ );

				/** Boolean that tells whether the found ball is close to a wall or not. **/
				bfs.getCloseToWall();

				fs = new FindingSequence(robotControl, testCamera.getRobot().heading, testCamera.getMap().pixelSize);

				di = fs.sequence(path);

				// Print the found instructions
				//		for (DriverInstructions step : di) {
				//			System.out.println(step.getHeading()+" "+step.getLength());
				//		}
				fs.drive(di);

				ballCount++;
				
			} else {
				/** Drive to goal and release balls **/
				bfs = new BFS(board.getGrid(), 'G');
				path = bfs.findPath();
				
				
				
				ballCount = 0;
			}
		}

	}
}
