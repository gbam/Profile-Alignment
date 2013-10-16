package cs576P2.ProfileAlignment;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class alignProfile {
	private static final int match = 2;
	private static final int mismatch = 1;
	private static final int gapPenalty = 0;
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
		List<Paths> paths = new ArrayList<Paths>();
		backTrace(cornerCell, paths, new Paths());
		List<Double> totalScore = new ArrayList<Double>();
		for (Paths pp: paths){
			Double tempScore = 0.0;
			for (Cell cc: pp.paths){
				System.out.print(" || " + cc.row + "," + cc.col);
				tempScore += cc.score;
			}
			totalScore.add(tempScore);
			System.out.print(" 			Score: " + tempScore);
			System.out.println("");
		}
		Double lowestIndex = Double.MIN_VALUE;
		Paths lowestPath = null;
		for(int i = 0; i < totalScore.size(); i ++)
			if(totalScore.get(i) >= lowestIndex){
				lowestIndex = totalScore.get(i);
				lowestPath = paths.get(i);
				
		}

		System.out.println("");
		System.out.println("");
		System.out.println("");
		int tempScore = 0;
		for (Cell cc: lowestPath.paths){
				System.out.print(" || " + cc.row + "," + cc.col);
				tempScore += cc.score;
			
		}
		System.out.print(" 			Score: " + tempScore);
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

		if(s1a == s2a)score += match;
		else if(s1a == '-' || s2a == '-') score+= gapPenalty;
		else score += mismatch;
		if(s1a == s2b)score += match;
		else if(s1a == '-' || s2a == '-') score+= gapPenalty;
		else score += mismatch;
		if(s2a == s1a)score += match;
		else if(s1a == '-' || s2a == '-') score+= gapPenalty;
		else score += mismatch;
		if(s2b == s1b)score += match;
		else if(s1a == '-' || s2a == '-') score+= gapPenalty;
		else score += mismatch;

		return score / 4;
	}
	public static class Cell{
		public Cell prevCell;
		public Cell prevCell2;
		public Cell prevCell3;
		public Double score;
		public int row;
		public int col;

		Cell(Double score, int row, int col, Cell c){
			this.score = score;
			this.row = row;
			this.col = col;
			this.prevCell = c;
		}


	}

	public static class Paths{
		public List<Cell> paths;
		public Paths(){
			paths = new ArrayList<Cell>();
		}
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


	private static void backTrace(Cell c, List<Paths> possiblePaths, Paths path){


		//Case we made it back to origin
		if(c.prevCell == null && c.col == 0 && c.row == 0){
			path.paths.add(c);
			possiblePaths.add(path);
			return;
		}
		else{
			Paths newPath = new Paths();
			for (Cell cell: path.paths){
				newPath.paths.add(cell);

			}
			newPath.paths.add(c);
			backTrace(c.prevCell, possiblePaths, newPath);
			if(c.prevCell2 != null){
				backTrace(c.prevCell2, possiblePaths, newPath);
			}
			if(c.prevCell3 != null){
				backTrace(c.prevCell3, possiblePaths, newPath);
			}


		}

	}

}

























































