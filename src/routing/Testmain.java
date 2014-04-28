package routing;

import imageProcessing.TestCamera;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Christoffer - s103148
 */

public class Testmain {

	/**
	 * Main method for test cases:
	 */
	public static void main(String[] arg) {

		TestCamera t = new TestCamera();
		char [][] map = t.getMap().obstacle;

		Board board = new Board(map);
		board.fillInBalls(t.getBalls());
		board.fillInRobotPosition(t.getRobot().position);
		//		board.fillInGoals(t.getGoals().);

		//	Controller ctrl = new Controller();


		BFS M = new BFS(board.getGrid(), 'B');  
		ArrayList<Integer> path = M.findPath(t.getRobot().robotWidth, t.getRobot().robotLength);

		/* For printing the result to a file:
		String filepath = "/Users/Christoffer/Desktop/outputPath.txt";
		File f = new File(filepath);
		FileWriter fw = null;
		try {
			fw = new FileWriter(f);
			fw.write(M.toString());
		} catch (IOException e) { e.printStackTrace(); }
		*/
		
		/** Print the found path */
		if(path.size() > 0) {
			System.out.println("Found path!");
			for (Integer step : path) {
				System.out.println(step);
			}
		} else {
			System.out.println("No path found!");
		}
		/*
		System.out.println();
		ArrayList<DriverInstructions> di = ctrl.getFs().sequence(path);

		// Print the found instructions
		for (DriverInstructions step : di) {
			System.out.println(step.getHeading()+" "+step.getLength());
		}
		ctrl.getFs().drive(di);
		 */
	}
}
