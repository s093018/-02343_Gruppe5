package routing;

import imageProcessing.TestCamera;

import java.util.ArrayList;
import java.util.ListIterator;

/**
 * @author Christoffer - s103148
 */

public class Testmain {
	
	/**
	 * Main method for test cases:
	 */
	public static void main(String[] arg) {
		 System.setProperty( "java.library.path", "/Users/Christoffer/Desktop/OpenCV/bin/opencv-248/" );

		TestCamera t = new TestCamera();
		Board board = new Board(t.getMap().obstacle);
		board.fillInBalls(t.getBalls());
		board.fillInRobotPosition(t.getRobot().position);
		
		/*
		char[][] map = {
				{'O', 'O', 'O', 'O', 'O', 'O', 'O', 'O', 'O', 'O'},
				{'O', ' ', ' ', ' ', ' ', ' ', ' ', ' ', 'B', 'O'},
				{'O', ' ', ' ', ' ', ' ', 'O', ' ', ' ', ' ', 'O'},
				{'O', ' ', ' ', ' ', 'O', 'O', 'O', ' ', ' ', 'O'},
				{'O', ' ', ' ', 'O', 'O', 'O', 'O', 'O', ' ', 'O'},
				{'O', ' ', ' ', ' ', 'O', 'O', 'O', ' ', ' ', 'O'},
				{'O', ' ', ' ', 'R', ' ', 'O', ' ', ' ', ' ', 'O'},
				{'O', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', 'O'},
				{'O', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', 'O'},
				{'O', 'O', 'O', 'O', 'O', 'O', 'O', 'O', 'O', 'O'}
		};
		*/
		BFS M = new BFS(board.getGrid(), board.getStart());  
		System.out.println(M);
		ArrayList<String> path = M.findPath();
		System.out.println(M);
		
		for (String string : path) {
			System.out.println(string);
		}
		/** Print the found path */
		ListIterator<String> it = path.listIterator();
		System.out.println("Found path!");
		while(it.hasNext()) {
			System.out.println(it.next());
		}
	}
}
