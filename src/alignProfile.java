import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;


public class alignProfile {

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
			String s1 = "";
			String s2 = "";

			try{
				File s1file = new File(seq1FileName + ending);
				File s2file = new File(seq2FileName + ending);
				BufferedReader  reader = new BufferedReader(new FileReader(s1file));
				s1 = reader.readLine();
				reader = new BufferedReader(new FileReader(s2file));
				s2 = reader.readLine();
			}catch(Exception e){
				System.out.println("Failed to open file");
			}
			s1 = " " + s1;
			s2 = " " + s2;
		

	}

}
