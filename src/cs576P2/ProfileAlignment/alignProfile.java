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
	private static final char gapMarker = '_';
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
			s2a = reader.readLine();
			s2b = reader.readLine();
			reader = new BufferedReader(new FileReader(s2file));
			s1a = reader.readLine();
			s1b = reader.readLine();
		}catch(Exception e){
			System.out.println("Failed to open file");
			throw new Exception("Shit");
		}		
		s1a = " " + s1a;
		s1b = " " + s1b;
		s2a = " " + s2a;
		s2b = " " + s2b;

		//Calculates the scores and returns the grid.
		Cell[][] cellGrid = new Cell[s1a.length()][s2a.length()];
		createScores(cellGrid); //Fills in the first two rows
		makeGrid(s1a, s1b, s2a, s2b, cellGrid); //Fills in the rest of the grid
		System.out.print("       ");
		
		for (int i = 0; i < s2a.length(); i++){
		System.out.print(s2a.charAt(i) + "     ");
		}
		System.out.println("        ");
		System.out.print("       ");
		for (int i = 0; i < s2a.length(); i++){
			System.out.print(s2b.charAt(i) + "     ");
			}
		System.out.println("");
		for(int i = 0; i < cellGrid.length; i++){
			if(i < s1a.length()) System.out.print(s1a.charAt(i) + " " + s1b.charAt(i) + "   ");
			for(int j = 0; j < cellGrid[0].length; j++){
				System.out.print(cellGrid[i][j].score + " | ");
			}
			System.out.println("     ");
		}
		Cell cornerCell = cellGrid[cellGrid.length-1][cellGrid[0].length-1];
		//shortestPath(cornerCell, cellGrid);

	}

	

	//Must return a grid of scores
	private static Cell[][] makeGrid(String s1a, String s1b, String s2a,
			String s2b, Cell[][] cellGrid) throws Exception {


		//For each column
		for(int i = 1; i < cellGrid.length; i++){
			//for each row
			for(int j = 1; j < cellGrid[0].length; j++){
				Double upScore =  cellGrid[i-1][j].score + gapPenalty;
				Double leftScore = cellGrid[i][j-1].score + gapPenalty;
				Double currentScore = returnScore(s1a.charAt(i), s1b.charAt(i), s2a.charAt(j), s2b.charAt(j));
				cellGrid[i][j] = new Cell(0.0, i, j, null);
				Double diagScore = cellGrid[i-1][j-1].score + currentScore;
				
				if(leftScore.compareTo(upScore) == 0 && leftScore.compareTo(diagScore) == 0){
					cellGrid[i][j].prevCell1 = cellGrid[i-1][j];
					cellGrid[i][j].prevCell2 = cellGrid[i-1][j-1];
					cellGrid[i][j].prevCell3 = cellGrid[i][j-1];
					cellGrid[i][j].score = diagScore;
				}
			
				//Left is highest
				else if(leftScore > upScore && leftScore > diagScore){
					cellGrid[i][j].prevCell1 = cellGrid[i][j-1];
					cellGrid[i][j].score = leftScore;
				}
				//Up is highest
				else if(upScore > leftScore && upScore > diagScore){
					cellGrid[i][j].prevCell1 = cellGrid[i-1][j];
					cellGrid[i][j].score = upScore;
				}
				//Diagonal is highest
				else if(diagScore > leftScore && diagScore > upScore){
					Cell pointCell = cellGrid[i-1][j-1];
					cellGrid[i][j].prevCell1 = pointCell;
					cellGrid[i][j].score = diagScore; 
				}
				//Left and Diagonal match
				else if(diagScore.compareTo(leftScore) == 0 && diagScore > upScore){
					cellGrid[i][j].prevCell1 = cellGrid[i-1][j-1];
					cellGrid[i][j].prevCell2 = cellGrid[i][j-1];
					cellGrid[i][j].score = diagScore;
				}
				//Left and Up match
				else if(upScore.compareTo(leftScore) == 0 && leftScore > diagScore){
					cellGrid[i][j].prevCell1 = cellGrid[i-1][j];
					cellGrid[i][j].prevCell2 = cellGrid[i][j-1];
					cellGrid[i][j].score = upScore;
					
				}
				//diagonal and up match
				else if(diagScore.compareTo(upScore) == 0 && diagScore > leftScore){
					cellGrid[i][j].prevCell1 = cellGrid[i-1][j];
					cellGrid[i][j].prevCell2 = cellGrid[i-1][j-1];
					cellGrid[i][j].score = diagScore;
				}
				else{
					throw new Exception();
				}


			}
		}		

		return cellGrid;
	}


	//Method returns the value for a given pair of sequences
	public static double returnScore(char s1a, char s1b, char s2a, char s2b){
		Double score = 0.0;
		int firstScore = 0;
		int secondScore = 0;
		int thirdScore = 0;
		//If they match each other
		//A1 --> A1,A2, B1 --> B2, B3 --> B3, B4--> B4
		if(s1a == s2a && (s1a != gapMarker || s2a != gapMarker ))firstScore += match;
		else if(s1a == gapMarker || s2a == gapMarker) firstScore+= gapPenalty;
		else firstScore += mismatch;
		if(s1a == s2b && (s1a != gapMarker || s2b != gapMarker ))firstScore += match;
		else if(s1a == gapMarker || s2b == gapMarker) firstScore+= gapPenalty;
		else firstScore += mismatch;
		if(s1b == s2a && (s1b != gapMarker || s2a != gapMarker ))firstScore += match;
		else if(s1b == gapMarker || s2a == gapMarker) firstScore+= gapPenalty;
		else firstScore += mismatch;
		if(s1b == s2b && (s1b != gapMarker || s2b != gapMarker ))firstScore += match;
		else if(s1b == gapMarker || s2b == gapMarker) firstScore+= gapPenalty;
		else firstScore += mismatch;

		score = (firstScore + 0.0) / 4;

		//Comparing A1 to Gaps
		secondScore = gapPenalty;
		thirdScore = gapPenalty;

		return score + secondScore + thirdScore;
	}
	protected static void createScores(Cell[][] welTable){
		//Initialize the first row
		for (int i = 0; i < welTable.length; i++){
			welTable[i][0] = new Cell(i * gapPenalty + 0.0, 0, i, null);
			if(i != 0)welTable[i][0].prevCell1 = welTable[i-1][0];
		}
		//Initialize the first column
		for (int i = 1; i < welTable[0].length; i++){
			welTable[0][i] = new Cell(i * gapPenalty + 0.0, i, 0, null);
			if(i != 0)welTable[0][i].prevCell1 = welTable[0][i-1];
		}
		int x = 0;
		x++;
	
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
		public Cell prevCell1;
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
			this.prevCell1 = c;
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

























































