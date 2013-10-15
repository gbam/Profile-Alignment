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
			if(args.length != 3){
				System.out.println("Not Correct # of Arguements");
				System.exit(0);
			}
			String seq1FileName = args[0];
			String seq2FileName = args[1];
			String ending = args[2];
			String s1a = "";
			String s1b = "";
			String s2a = "";
			String s2b = "";
			try{
				File s1file = new File(seq1FileName + ending);
				File s2file = new File(seq2FileName + ending);
				BufferedReader  reader = new BufferedReader(new FileReader(s1file));
				s1a = reader.readLine();
				s1b = reader.readLine();
				reader = new BufferedReader(new FileReader(s2file));
				s1a = reader.readLine();
				s1b = reader.readLine();
			}catch(Exception e){
				System.out.println("Failed to open file");
			}		
			s1a = " " + s1a;
			s1a = " " + s1b;
			s2a = " " + s2a;
			s2b = " " + s2b;
			
			//Calculates the scores and returns the grid.
			int[][] alignmentGrid = makeGrid(s1a, s1b, s2a, s2b);
			
			//For each column
			List<Double> columnScores = new ArrayList<Double>();
			for(int i = 1; i < alignmentGrid[0].length; i++){
				//for each row
				Double score = 0.0;
				for(int j = 1; j < alignmentGrid.length; j++){
					score = alignmentGrid[j][i] + score;
				}
				columnScores.add(score);
			}
			//Use a string to keep a list of highest scores deliminated by a comma
			String highestScores = "";
			Double highestScore = Double.MIN_VALUE;
			for(int i = 1; i < columnScores.size(); i++){
				Double currentScore = columnScores.get(i);
				//If it's greater, we have a new high score
				if(currentScore > highestScore){
					highestScores = i + "";
					highestScore = currentScore;
				}
				//Equal, add it to the list
				else if(currentScore == highestScore){
					highestScores = highestScores + "," + i;
				}
				//Else it's not the highest so nobody gives a shit!
			}

	}


//Must return a grid of scores
		private static int[][] makeGrid(String s1a, String s1b, String s2a,
			String s2b) {
			int[][] returnGrid = new int[s1a.length() * 2 + 1][s2a.length() * 2 + 1];
			
		// TODO Auto-generated method stub
			
			
		return null;
	}



		public double returnScore(char s1a, char s1b, char s2a, char s2b){
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
}
