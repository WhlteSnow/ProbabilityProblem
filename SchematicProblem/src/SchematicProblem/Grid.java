package finalproj;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Grid {
	
	//trace is an ArrayList created for BFS purposes. 
	public ArrayList<ArrayList<Position>> trace = new ArrayList<ArrayList<Position>>();
	public int[][] grid;
	int dimensionsRow;
	int dimensionsCol;
	
	public Grid(int dimensionsRow, int dimensionsCol) {
		this.grid = new int[dimensionsRow][dimensionsCol];
		this.dimensionsRow = dimensionsRow;
		this.dimensionsCol = dimensionsCol;
	}
	
	public void constructGrid(int row, String sequence) {
		trace.add(new ArrayList<Position>());
		for(int i = 0 ; i<sequence.length();i++) {
			if(sequence.charAt(i) == 'X') {
				this.grid[row][i] = 1;			//1 signifies the space is blocked
			}
			else {
				this.grid[row][i] = 0;			//0 signifies the space is open for traversal
			}
			trace.get(row).add(new Position(row,i));
		}
	}

	/**
	 * Method performs BFS search from a given position to destination
	 * @param source - starting position
	 * @param dest - ending position
	 * @return number of moves to get to destination from source or the maximum integer value if it is not reachable
	 * */
	public int shortestPathBFS(Position source, Position dest) {
		if(source.equals(dest)) {
			return 0;
		}
		if(grid[source.x][source.y] == 1) {	//Cannot start inside a wall
			return Integer.MAX_VALUE;
		}
		int lengthOfPath = 0;
		boolean visited[][] = new boolean[this.dimensionsRow][this.dimensionsCol];
		Queue<Position> queue = new LinkedList<>();
		visited[source.x][source.y] = true;
		queue.add(source);
		
		while(!queue.isEmpty()) {
			Position temp = queue.remove();
			int row = temp.x;
			int col = temp.y;
			if(visited[dest.x][dest.y]) {
				continue;
			}
			if(row + 1 < this.dimensionsRow && grid[row+1][col] != 1 && !visited[row+1][col]) {
				trace.get(row +1).get(col).predecessor = temp;
				visited[row+1][col] = true;
				queue.add(new Position(row+1,col));
			}
			if (row - 1 >= 0 && grid[row-1][col] != 1 && !visited[row - 1][col]) {
				trace.get(row -1).get(col).predecessor = temp;
				visited[row - 1][col] = true;
				queue.add(new Position(row - 1, col));
			}
			if (col - 1 >= 0 && grid[row][col-1] != 1 && !visited[row][col - 1]) {
				trace.get(row).get(col - 1).predecessor = temp;
				visited[row][col - 1] = true;
				queue.add(new Position(row, col - 1));
			}
			if (col + 1 < this.dimensionsCol && grid[row][col+1] != 1 && !visited[row][col + 1]) {
				trace.get(row).get(col + 1).predecessor = temp;
				visited[row][col + 1] = true;
				queue.add(new Position(row, col + 1));
			}
		}
		
		Position temp = trace.get(dest.x).get(dest.y);
		while(!source.equals(temp)) {
			temp = trace.get(temp.x).get(temp.y).predecessor;
			lengthOfPath++;
		}		
		
		return lengthOfPath;
	}
	
	public void printGrid() {
		for(int i = 0; i < grid.length;i++) {
			for(int j = 0; j < grid.length; j++) {
				System.out.print(grid[i][j]);
			}
			System.out.println();
		}
	}
}
