package spaceship_mail;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;

public class Scoreboard
{
	public ArrayList<Integer> top_scores = new ArrayList<Integer>();

	public Scoreboard()
	{
		if(readTopScores() == false) {
			createDefaultScores();			
		}		
	}
	
	private void createDefaultScores()
	{
		top_scores.clear();
		
		top_scores.add(1000);
		top_scores.add(800);
		top_scores.add(600);
		top_scores.add(400);
		top_scores.add(200);
	}

	public void addNewScore(int player_score)
	{
		top_scores.add(player_score);
		
		// Sort all top scores (descending order)
		top_scores.sort(null);
		Collections.reverse(top_scores);
		
		if(top_scores.size() > 5) {
			top_scores.remove(top_scores.size() - 1);
		}
		 writeTopScores();
	}
	
	private void writeTopScores() 
	{
		File file = new File("scoreboard.txt");
		
		PrintWriter out;
		try 
		{
			out = new PrintWriter(file.getAbsoluteFile());
	
			for(int i = 0; i < top_scores.size(); ++i) {
				out.println(top_scores.get(i));		
			}
			
			out.close();		
		} 
		catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("writeTopScores - FileNotFoundException e");
		}
	}
	
	private boolean readTopScores()
	{
		File file = new File("scoreboard.txt");
		if(!file.exists()) return false;
				
		try
		{
			// Read all top scores from a file.
			FileInputStream in;
			in = new FileInputStream(file.getAbsolutePath());
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			
			// Each line is a number.
			String line; 
			top_scores.clear();
			
			while ((line = br.readLine()) != null)
			{				
				int top_score = Integer.parseInt(line);							
				top_scores.add(top_score);
			}
			
			br.close();
		}
		catch(NumberFormatException e)
		{
			e.printStackTrace();
			System.out.println("NumberFormatException e");
			return false;			
		}
		catch (FileNotFoundException e) 
		{
			return false;			
		}
		catch (IOException e) {
			return false;			
		}
		
		if(top_scores.size() != 5) {
			return false;
		}
		
		return true;
	}
}
