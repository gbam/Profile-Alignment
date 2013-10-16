package cs576P2.ProfileAlignment;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class alignProfile {
	private static final int match = 2;
	private static final int mismatch = 1;
	private static final int gapPenalty = 0;
	private static final char gapMarker = '-';
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		if(args.length != 4){
			System.out.println("Not Correct # of Arguements");
			System.exit(0);
		}
		String seq1FileName = args[0];
		String seq2FileName = args[2];
		String ending1 = args[1];
		String ending2 = args[3];
		String s1a = "";
		String s1b = "";
		String s2a = "";
		String s2b = "";
		try{
			File s1file = new File(seq1FileName + "." + ending1);
			File s2file = new File(seq2FileName + "." + ending2);
			BufferedReader  reader = new BufferedReader(new FileReader(s1file));
			s1a = reader.readLine();
			s1b = reader.readLine();
			reader = new BufferedReader(new FileReader(s2file));
			s1a = reader.readLine();
			s1b = reader.readLine();
		}catch(Exception e){
			System.out.println("Failed to open file");
			throw new Exception("Shit");
		}		
		s1a = " " + s1a;
		s1a = " " + s1b;
		s2a = " " + s2a;
		s2b = " " + s2b;

		//Calculates the scores and returns the grid.
		Cell[][] cellGrid = new Cell[s1a.length()][s2a.length()];
		createScores(cellGrid); //Fills in the first two rows
		makeGrid(s1a, s1b, s2a, s2b, cellGrid); //Fills in the rest of the grid
		Cell cornerCell = cellGrid[cellGrid.length-1][cellGrid[0].length-1];


	}


	//Must return a grid of scores
	private static Cell[][] makeGrid(String s1a, String s1b, String s2a,
			String s2b, Cell[][] cellGrid) {


		//For each column
		for(int j = 1; j < cellGrid[0].length; j++){
			//for each row
			for(int i = 1; i < cellGrid[0].length; i++){
				cellGrid[i][j] = new Cell(returnScore(s1a.charAt(i), s1b.charAt(i), s2a.charAt(j), s2b.charAt(j)), j, i, null);

			}
		}		

		return cellGrid;
	}


	//Method returns the value for a given pair of sequences
	public static double returnScore(char s1a, char s1b, char s2a, char s2b){
		int score = 0;
		int firstScore = 0;
		int secondScore = 0;
		int thirdScore = 0;
		//If they match each other
		//A1 --> A1,A2, B1 --> B2, B3 --> B3, B4--> B4
		if(s1a == s2a && (s1a != gapMarker || s2a != gapMarker ))firstScore += match;
		else if(s1a == gapMarker || s2a == gapMarker) firstScore+= gapPenalty;
		else firstScore += mismatch;
		if(s1a == s2b && (s1a != gapMarker || s2a != gapMarker ))firstScore += match;
		else if(s1a == gapMarker || s2a == gapMarker) score+= gapPenalty;
		else firstScore += mismatch;
		if(s2a == s1a && (s1a != gapMarker || s2a != gapMarker ))firstScore += match;
		else if(s1a == gapMarker || s2a == gapMarker) firstScore+= gapPenalty;
		else firstScore += mismatch;
		if(s2b == s1b && (s1a != gapMarker || s2a != gapMarker ))firstScore += match;
		else if(s1a == gapMarker || s2a == gapMarker) firstScore+= gapPenalty;
		else firstScore += mismatch;

		firstScore = firstScore/ 4;

		//Comparing A1 to Gaps
		secondScore = gapPenalty;
		thirdScore = gapPenalty;

		return firstScore + secondScore + thirdScore;
	}
	protected static void createScores(Cell[][] welTable){
		//Initialize the first row
		for (int i = 0; i < welTable.length; i++){
			welTable[i][0] = new Cell(i * gapPenalty + 0.0, i, 0, null);
			if(i != 0) 	welTable[i][0].prevCell = 	welTable[i-1][0];

		}
		//Initialize the first column
		for (int i = 0; i < welTable[0].length; i++){
			welTable[0][i] = new Cell(i * gapPenalty + 0.0, i, 0, null);

			if(i != 0) 	welTable[0][i].prevCell = 	welTable[0][i-1];
		}
	}


	private static List<Cell> shortestPath(Cell c, Cell[][] cellTable){

		PriorityQueue<Path> pq = new PriorityQueue<Path>(10, new Comparator<Path>() {
			public int compare(Path x, Path y) {
				// Assume neither string is null. Real code should
				// probably be more robust
				if (x.cost < y.cost)return -1;
				if (x.cost > y.cost)return 1;
				return 0;
			}
		});

		
		Path p = new Path();//Make a new path
		p.paths.add(c);//Starting at the first element
		pq.add(p);
		while(!pq.isEmpty()){
			Path currentPath = pq.poll();
			//Check if we're at root
			int cpSize = currentPath.paths.size(); 
			Cell lastCell = currentPath.paths.get(cpSize);
			int lcCol = lastCell.col;
			int lcRow = lastCell.row;
			if(lcCol == 0 && lcRow == 0){ //If it's the last cell we're done
				return currentPath.paths;
			}
			//We know we are somewhere in the middle!
			if(lcCol - 1 >= 0 && lcRow - 1 >= 0){
				Cell up = cellTable[lcCol][lcRow-1];
				Path pUp = copyPath(currentPath);
				pUp.paths.add(up);
				shortestPath(pUp, cellTable);
				Cell left = cellTable[lcCol-1][lcRow];
				Cell diagonal = cellTable[lcCol-1][lcRow-1];
			}
			//We know we're at the left edge
			else if(lcCol - 1 >= 0){
				
			}
			else if(lcRow- 1 >= 0){
				
			}
			
			
			//Check all neighbors for unvisited nodes, if unvisited, make a new path and queue it;
			
		}
		
		return null;

	}
	public static Path copyPath(Path p){
		Path copyP = new Path();
		copyP.cost = p.cost;
		for(Cell c: copyP.paths){
			copyP.paths.add(c);
		}
		return copyP;
	}
	public static class Cell{
		public Cell prevCell;
		public Cell prevCell2;
		public Cell prevCell3;
		public Double score;
		public int row;
		public int col;
		public boolean visited;

		Cell(Double score, int row, int col, Cell c){
			this.score = score;
			this.row = row;
			this.col = col;
			this.prevCell = c;
			this.visited = false;
		}


	}

	public static class Path implements Comparator<Path>{
		public List<Cell> paths;
		public int cost;
		public Path(){
			paths = new ArrayList<Cell>();
			cost = 0;
		}
		@Override
		public int compare(Path x, Path y)
		{
			// Assume neither string is null. Real code should
			// probably be more robust
			if (x.cost < y.cost)
			{
				return -1;
			}
			if (x.cost > y.cost)
			{
				return 1;
			}
			return 0;
		}
	}
}

























































