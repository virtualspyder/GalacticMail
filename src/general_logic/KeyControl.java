package general_logic;

import core_logic.GalacticMail;
import core_logic.Main;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

// This is a reusable class. List and handle all necessary keyboard codes.
public class KeyControl extends KeyAdapter {
	
	GalacticMail galactic_mail = null;
	
	public KeyControl(GalacticMail this_game) {
		galactic_mail = this_game;
	}
	
	// A key: Rotate the spaceship while it is idle, or make the spaceship slightly head to the left direction while it is flying.
	// D key: Rotate the spaceship while it is idle, or make the spaceship slightly head to the right direction while it is flying.
	// Enter key: Make the spaceship depart from the moon it is staying at or make the game proceed to next level.  
	// M key: This key is used to mute/play the game music. Note that the main menu's music cannot be muted.
	
	public void keyPressed(KeyEvent e)
	{
		int key = e.getKeyCode();
		
		switch(key)
		{
			case KeyEvent.VK_A:    galactic_mail.property.spaceship_player.key_requests.left = true;    break;
			case KeyEvent.VK_D:    galactic_mail.property.spaceship_player.key_requests.right = true;    break;
			case KeyEvent.VK_ENTER:    galactic_mail.property.spaceship_player.key_requests.enter_key = true;    break;
			case KeyEvent.VK_ESCAPE:    System.exit(0);    break;
		}
	}

	public void keyReleased(KeyEvent e)
	{
		int key = e.getKeyCode();
		
		switch(key)
		{
			case KeyEvent.VK_A:    galactic_mail.property.spaceship_player.key_requests.left = false;    break;
			case KeyEvent.VK_D:    galactic_mail.property.spaceship_player.key_requests.right = false;    break;
			case KeyEvent.VK_ENTER:    galactic_mail.property.spaceship_player.key_requests.enter_key = false;    break;
			case KeyEvent.VK_M:    
				
				if(galactic_mail.property.game_welcome == true) {
					break;
				}
				
				galactic_mail.property.music_enabled = !galactic_mail.property.music_enabled;
				
				if(galactic_mail.property.music_enabled == false) {
					Main.gameSounds.stop("Galactic Mail Theme");
				}
				else {
					Main.gameSounds.play("Galactic Mail Theme");					
				}
			break;			
		}
	}
	
}
