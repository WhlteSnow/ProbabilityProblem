package finalproj;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;

public class Driver {

	public static Grid grid;	//Grid environment created from user inputted file
	static HashSet<Position> positionList = new HashSet<Position>();	//HashSet contains all possible locations of drone

	/**
	 * Method initializes the possible locations for the drone into the positionList
	 * @param row - number of rows in the file
	 * @param col - number of columns in the file
	 * */
	public static void createPositions(int row, int col) {
		for(int i = 0; i <row; i++) {
			for(int j = 0; j <col;j++) {
				if(grid.grid[i][j] == 0) {
					positionList.add(new Position(i,j));
				}
			}
		}
	}
	
	/**
	 * Method updates the position of the possible location of the drones.
	 * @param move - direction to command drones to move
	 * @return true if any potential drone can still move in that direction, false otherwise
	 * */
	public static boolean updatePositions(String move) {
		int canStillMove = 0;
		ArrayList<Position> newList = new ArrayList<Position>();
		for (Iterator<Position> i = positionList.iterator(); i.hasNext();) {
			Position element = i.next();
			newList.add(element);
		}
		positionList.clear();
		switch(move) {
			case "D":
				for(int i = 0; i < newList.size();i++) {
					Position temp = newList.get(i);
					if(temp.x == grid.grid.length -1) {	
						continue;
					}
					if(grid.grid[temp.x+1][temp.y] == 0) {	
						temp.setValue(temp.x+1, temp.y);
					}
					if(temp.x != grid.grid.length -1 && grid.grid[temp.x+1][temp.y] == 0) {	
						canStillMove++;
					}
				}
				break;
			case "U":
				for(int i = 0; i < newList.size();i++) {
					Position temp = newList.get(i);
					if(temp.x == 0) {	
						continue;
					}
					if(grid.grid[temp.x-1][temp.y] == 0) {	
						temp.setValue(temp.x-1, temp.y);
					}
					if(temp.x != 0 && grid.grid[temp.x-1][temp.y] == 0) {	
						canStillMove++;
					}
				}
				break;
			case "L":
				for(int i = 0; i < newList.size();i++) {
					Position temp = newList.get(i);
					if(temp.y == 0) {	
						continue;
					}
					if(grid.grid[temp.x][temp.y-1] == 0) {	
						temp.setValue(temp.x, temp.y-1);
					}
					if(temp.y != 0 && grid.grid[temp.x][temp.y-1] == 0) {	
						canStillMove++;
					}
				}
				break;
			case "R":
				for(int i = 0; i < newList.size();i++) {
					Position temp = newList.get(i);
					if(temp.y == grid.grid[0].length-1) {	
						continue;
					}
					if(grid.grid[temp.x][temp.y +1] == 0) {	
						temp.setValue(temp.x, temp.y+1);
					}
					if(temp.y != grid.grid[0].length-1 &&grid.grid[temp.x][temp.y+1] == 0) {
						canStillMove++;
					}
				}
				break;
			default:
				break;
		}
		positionList.addAll(newList);
		
		return canStillMove > 0;
	}
	
	/**
	 * Method merges two possible locations of the drones together.
	 * Method will move the drones nearest to each other first.
	 * @return List of moves to merge two possible drone locations
	 * */
	public static ArrayList<String> mergePosition() {
		ArrayList<String> moves = new ArrayList<String>();
		ArrayList<Position> newList = new ArrayList<Position>();
		//Create fresh arraylist to manipulate
		//Fresh arrayList used to run a simulation of moves to get from drone 1 to drone 2 without affecting existing list.
		for (Iterator<Position> i = positionList.iterator(); i.hasNext();) {	
			Position element = i.next();
			newList.add(element);
		}
		
		//find shortest pair of drone locations to merge
		//shortest pair meaning the drones that are closest together via BFS
		int indexFirst = 0;
		int indexSecond = 0;
		int distance = 400;
		for(int i = 0; i <newList.size()-1;i++) {
			for(int j = i+1; j <newList.size();j++) {
				int newDist = grid.shortestPathBFS(newList.get(i), newList.get(j));	
				if(newDist < distance) {
					distance = newDist;
					indexFirst = i;
					indexSecond = j;
				}
			}
		}
		Position first = newList.get(indexFirst);
		Position second = newList.get(indexSecond);
		while(!first.equals(second)) {
			int up = 400;
			int left = 400;
			int right = 400;
			int down = 400;
			//Calculate direction to move in order to merge
			if(first.x > 0) {
				up =grid.shortestPathBFS(new Position(first.x -1, first.y), second);
			}
			if(first.x +1 < grid.grid.length) {
				down =grid.shortestPathBFS(new Position(first.x +1, first.y), second);
			}
			if(first.y > 0) {
				left =grid.shortestPathBFS(new Position(first.x, first.y-1), second);
			}
			if(first.y +1 < grid.grid[0].length) {
				right =grid.shortestPathBFS(new Position(first.x, first.y+1), second);
			}
			
			//Choose direction that moves drone 1 closer to drone 2 and update position for drone 1 and 2
			if(up < left && up < right && up < down) {
				first.setValue(first.x -1, first.y);
				if(second.x > 0 && grid.grid[second.x-1][second.y] != 1) {
					second.setValue(second.x-1, second.y);
				}
				moves.add("U");
			}
			else if(left < up && left < right && left < down) {
				first.setValue(first.x, first.y-1);
				if(second.y > 0 && grid.grid[second.x][second.y-1] != 1) {
					second.setValue(second.x, second.y-1);
				}
				moves.add("L");
			}
			else if(down < up && down < right && down < left) {
				first.setValue(first.x +1, first.y);
				if(second.x +1 < grid.grid.length && grid.grid[second.x +1][second.y] != 1) {
					second.setValue(second.x +1, second.y);
				}
				moves.add("D");
			}
			else {
				first.setValue(first.x, first.y+1);
				if(second.y +1 < grid.grid[0].length && grid.grid[second.x][second.y +1] != 1) {
					second.setValue(second.x, second.y +1);
				}
				moves.add("R");
			}
		}

		//Update the position for all of the drone positions in the actual set after simulation found moveset to merge 2 possible drone locations
		for(int i = 0; i < moves.size();i++) {	
			updatePositions(moves.get(i));
		}
		
		return moves;
	}
	
	/**
	 * Method collapses the possible positions the drone can be onto one square.
	 * @return List of moves to isolate drone position
	 * */
	public static ArrayList<String> isolatePosition() {
		ArrayList<String> moves = new ArrayList<String>();
		//The next 8 do-while loops perform a series of movement to try to merge as many positions together as possible.
		//This is done by moving everyone towards the bottom right corner as much as possible and then the top left corner as much as possible.
		//The idea behind this is to clump the possible positions of being at the middle of a vertical column or horizontal row together.
		do {								
			moves.add("D");
			if(positionList.size() ==1)
				return moves;
		}while(updatePositions("D"));
		
		do {
			moves.add("R");
			if(positionList.size() ==1)
				return moves;
		}while(updatePositions("R"));
		
		do {
			moves.add("D");
			if(positionList.size() ==1)
				return moves;
		}while(updatePositions("D"));
		
		do {
			moves.add("R");
			if(positionList.size() ==1)
				return moves;
		}while(updatePositions("R"));
		
		do {
			moves.add("U");
			if(positionList.size() ==1)
				return moves;
		}while(updatePositions("U"));
		
		do {
			moves.add("L");
			if(positionList.size() ==1)
				return moves;
		}while(updatePositions("L"));
		
		do {
			moves.add("U");
			if(positionList.size() ==1)
				return moves;
		}while(updatePositions("U"));
		
		do {
			moves.add("L");
			if(positionList.size() ==1)
				return moves;
		}while(updatePositions("L"));
		
		int count = 0;
		while(count <100 && positionList.size() !=1) {	//count here is to stop the algorithm if it has been running too long
			moves.addAll(mergePosition());
			count++;
		}
		
		return moves;
	}
	
	public static void main(String[] args) {
		int column = 0;
		int row = 0;
		System.out.println("Please enter your file schematic. (Include the .txt) \nIE:TestOne.txt");
		Scanner scanner = new Scanner(System.in);
		String file = scanner.nextLine();
		scanner.close();
		BufferedReader reader;
		int count = 0;
		try {
			reader = new BufferedReader(new FileReader(file));
			String line = reader.readLine();
			column = line.length();
			while (line != null) {
				row++;
				line = reader.readLine();	
			}

			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		grid= new Grid(row,column);
		row = 0;
		try {
			reader = new BufferedReader(new FileReader(file));
			String line = reader.readLine();
			column = line.length();
			while (line != null) {
				row++;
				grid.constructGrid(count, line);	//create reactor line by line from file
				line = reader.readLine();	
				count++;
			}

			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		
		createPositions(row,column);	//starting positions
		ArrayList<String> moves = isolatePosition();	//isolate position of drone in grid	and returns the moves done to get to that position
		System.out.println("moves: " + moves.size());		//Size of sequence of moves
		System.out.println("moves: " + moves);		//Sequence of moves
		System.out.println(positionList);		//Final position of drone
		
	}
}
