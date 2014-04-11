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
/*
		TestCamera t = new TestCamera();
		char [][] map = t.getMap().obstacle;
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[i].length; j++) {
				System.out.print(map[i][j] + " ");
			}
			System.out.println();
		}
		board.fillInBalls(t.getBalls());
		board.fillInRobotPosition(t.getRobot().position);
*/
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
		Board board = new Board(map);

		BFS M = new BFS(board.getGrid(), board.getStart());  
		System.out.println(M.toString());
		ArrayList<Integer> path = M.findPath();
		System.out.println(M);

		/** Print the found path */
		System.out.println("Found path!");
		for (Integer step : path) {
			System.out.println(step);
		}
	}
}
