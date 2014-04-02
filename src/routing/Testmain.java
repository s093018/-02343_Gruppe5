package routing;

import java.util.Iterator;

public class Testmain {
	
	/**
	 * Main method for test cases:
	 */
	public static void main(String[] arg)
	{
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

		Board board = new Board();
		board.setBoard(map);
		BFS M = new BFS(board.getBoard());  
		System.out.println(M);
		Iterator<String> it = M.findPath().iterator();
		System.out.println(M);
		
		System.out.println("Found path!");
		while(it.hasNext()) {
			System.out.println(it.next());
		}

		/*
		M = new BFS	(simpleMaze);
		System.out.println(M);
		M.solve();
		System.out.println(M);

		M = new BFS(difficultMaze);  
		System.out.println(M);
		M.solve();
		System.out.println(M);
		 */
	}

}
