package routing;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Queue;

public class TreasureD {
	private Queue<Vertex> q = new LinkedList<Vertex>();
	public int[][] state_matrix;

	public int countReachableTreasures(int N, char[][] map) {

		int row, col, treasure_count = 0;
		Vertex v;
		state_matrix = new int[N][N];

		q.add(v = findEntrance(map));

		while (!q.isEmpty()) {
			v = q.remove();
			setState(row = v.row, col = v.col);
			if (getState(row, col) != 0) {
				addNeighbours(map, v);
				setState(row, col);
				if (v.treasure) {

				}

			}
		}
		return treasure_count;
	}

	private void addNeighbours(char[][] map, Vertex v) {
		int row = v.row, col = v.col, N = map[0].length;
		if (col > 0 && map[row][col - 1] != '#' && getState(row, col - 1) == 0) {
			setState(row, col - 1);
			q.add(new Vertex(map[row][col - 1], row, col - 1, 'W'));
		}
		if (col < N - 1 && map[row][col + 1] != '#' && getState(row, col + 1) == 0) {
			setState(row, col + 1);
			q.add(new Vertex(map[row][col + 1], row, col + 1, 'E'));
		}
		if (row < N - 1 && map[row + 1][col] != '#' && getState(row + 1, col) == 0) {
			setState(row + 1, col);
			q.add(new Vertex(map[row + 1][col], row + 1, col, 'S'));
		}
		if (row > 0 && map[row - 1][col] != '#' && getState(row - 1, col) == 0) {
			setState(row - 1, col);
			q.add(new Vertex(map[row - 1][col], row - 1, col, 'N'));
		}
	}
	private Vertex findEntrance(char[][] map) {
		int N = map[0].length;
		char c = 'I';

		for (int row = 0; row < N; row++) {
			for (int col = 0; col < N; col++) {
				if (map[row][col] == c) {
					return new Vertex(c, row, col, c);
				}
			}
		}
		return null;
	}

	private void setState(int row, int col) {
		state_matrix[row][col]++;
	}

	private int getState(int row, int col) {
		return state_matrix[row][col];
	}

	// ##################################################
	// # You do not need to modify anything below here. #
	// ##################################################
	public static void main(String[] args) throws Exception {
		new TreasureD().run();
	}

	private void run() throws Exception {
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

		int N = Integer.parseInt(in.readLine());
		char[][] map = new char[N][N];

		for (int i = 0; i < N; i++) {
			String line = in.readLine();
			for (int j = 0; j < N; j++) {
				map[i][j] = line.charAt(j);
			}
		}
		System.out.println(countReachableTreasures(N, map));
	}
}
