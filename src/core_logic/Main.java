package core_logic;

import general_logic.GameSounds;
import general_logic.GameSprites;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;

public class Main 
{
	public static GameSprites gameSprites = new GameSprites();
	public static GameSounds gameSounds = new GameSounds();
	
	public static void ErrorLog(String error_msg) 
	{
		System.out.println(error_msg);
		JOptionPane.showMessageDialog(null, error_msg,  "Fatal Error", JOptionPane.INFORMATION_MESSAGE);	
		System.exit(150);		
	}

	public static void main(String[] args) 
	{
		GalacticMail galactic_mail = new GalacticMail();
		
		JFrame main_window = new JFrame("Galactic Mail");
		main_window.addWindowListener(new WindowAdapter(){});
		
		if(GalacticMail.is_debug) {
			main_window.setTitle("Galactic Mail (Debugging mode)");					
		}
		
		main_window.getContentPane().add("Center", galactic_mail);
		main_window.pack();

		main_window.setSize(new Dimension(GalacticMail.SCREEN_WIDTH, GalacticMail.SCREEN_HEIGHT));
				
		main_window.setVisible(true);
		main_window.setResizable(false);
		main_window.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
		
		galactic_mail.start();
	}
}
