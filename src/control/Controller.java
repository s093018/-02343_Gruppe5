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
	private boolean endGame = false;
	private int ballCount = 0;
	private int tempBallCount = 0;
	private final int MAX_NO_BALLS = 6;

	public Controller () {
		this.testCamera = new TestCamera();
		try {
			robotControl = new robot.Control();
		} catch (Exception e) {	e.printStackTrace(); }
	}
	public void run() {

		while(!endGame) {
			if(ballCount > MAX_NO_BALLS) {
				if(tempBallCount <= 6) {

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

					fs.drive(di);

					tempBallCount++;
					ballCount++;

				} else {
					/** Drive to goal and release balls **/
					bfs = new BFS(board.getGrid(), 'G');
					path = bfs.findPath();

					fs = new FindingSequence(robotControl, testCamera.getRobot().heading, testCamera.getMap().pixelSize);

					di = fs.sequence(path);

					fs.goalDrive(di);

					tempBallCount = 0;
				}

			}
			fs = new FindingSequence(robotControl, testCamera.getRobot().heading, testCamera.getMap().pixelSize);
//			fs.celebration();
			fs.shutdown();
			endGame = true;
		}
	}
}
