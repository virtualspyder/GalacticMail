package spaceship_mail;

import core_logic.GalacticMail;
import core_logic.Main;
import general_logic.GameSprites.ImageData;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Paint extends Component implements Runnable   
{
	private GalacticMail galactic_mail = null;
	
	public class ReturnValues
	{
		Graphics2D g2d;
		BufferedImage bi_obj;	
	}
	ReturnValues return_values = new ReturnValues();
	
	public Font font1 = new Font("Verdana", Font.PLAIN, 28);
	public Font font2 = new Font("Arial Black", Font.PLAIN, 55);
	public Font font3 = new Font("Arial", Font.PLAIN, 45);
	public Font font5 = new Font("Verdana", Font.PLAIN, 31);
	public Font font6 = new Font("Verdana", Font.PLAIN, 26);
	public Font font7 = new Font("Arial", Font.PLAIN, 16);
	public Font font8 = new Font("Arial", Font.PLAIN, 16); // Player red lives - Can be either "BOLD" or "PLAIN".
	
	// Drawing variables
	public Graphics2D g2d_off_screen;
	public BufferedImage bi_screen;
	
	public Paint(GalacticMail this_game) 
	{
		galactic_mail = this_game;		
		game_background = Main.gameSprites.get("Background Space");		
		spaceship_lives = Main.gameSprites.get("Spaceship Lives");		
		galactic_mail_logo = Main.gameSprites.get("Game Logo");
	}
	
	public Thread paint_thread = new Thread(this);

	private ImageData game_background;
	private ImageData spaceship_lives;
	private ImageData galactic_mail_logo;
	
	// A thread is needed so the game window can be constantly drawn.
	// Without this paint thread, only a black window is seen (Resizing it can make the window draw, but it is not automatic).
	@Override
	public void run() 
	{
		while(true)
		{
			try {				
				// Assume that 40 milliseconds are used for drawing. 60 FPS in total.
				galactic_mail.repaint();
				paint_thread.sleep(16);
			} 
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void getReady() 
	{
		paint_thread.start();		
	}
		
	// After calling this function, you have to retrieve two return values.
	private void createMap(int w, int h, Graphics2D g2d, BufferedImage bi_obj) // Width, height
	{		
		if(bi_obj == null || (bi_obj.getWidth() != w || bi_obj.getWidth() != h)) {	
			bi_obj = (BufferedImage) galactic_mail.createImage(w, h);
		}
		
		g2d = bi_obj.createGraphics();
		g2d.setColor(galactic_mail.getBackground());
		
		g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);		
		g2d.clearRect(0, 0, w, h);
	
		return_values.g2d = g2d;
		return_values.bi_obj = bi_obj;
	}
	
	private void renderEntireScreen(BufferedImage map_obj)
	{ 
		// A. Space Background
		g2d_off_screen.drawImage(game_background.sprite, 0, 0, this);	
	
		int i;
		GalacticMail.Property property = galactic_mail.property;
		
		if(galactic_mail.property.enterKey_delay > 0) {
			galactic_mail.property.enterKey_delay -= 1;
		}
		else
		{
			// The code to switch to the next level.
			if(galactic_mail.property.game_win == true) 
			{
				if(property.spaceship_player.key_requests.enter_key == true) {
					
					cooldown_text_flash = 45;
					appear_text = false;
					 
					galactic_mail.property.current_level += 1;
					galactic_mail.initializeLevel();
				}
			}
			else 	if(galactic_mail.property.game_over == true)
			{
				if(property.spaceship_player.key_requests.enter_key == true) 
				{
					property.game_over = false;
					
					property.num_lives = 4;
					property.money = 0;
					property.current_level = 1;
				
					galactic_mail.initializeLevel();
					
					return;		
				}
			}
			else if(galactic_mail.property.game_welcome == true)
			{
				if(property.spaceship_player.key_requests.enter_key == true) 
				{
					Main.gameSounds.stop("Main Menu");
					Main.gameSounds.play("Galactic Mail Theme");
	
					property.num_lives = 4;
					property.game_welcome = false;
				
					galactic_mail.initializeLevel();
					
					return;		
				}
			}
		}
		
		// B. All Asteroids
		for(i = 0; i < property.asteroids.size(); i++) {
			property.asteroids.get(i).update(galactic_mail);	
			property.asteroids.get(i).draw(g2d_off_screen, this);	
		}
				
		// C. All Moon Mails
		for(i = 0; i < property.moons.size(); i++) {
			property.moons.get(i).update(galactic_mail);	
			property.moons.get(i).draw(g2d_off_screen, this);	
		}

		// D. Spaceship
		property.spaceship_player.update(galactic_mail);
		property.spaceship_player.draw(g2d_off_screen, this);
		
		// E. Explosion effects
		for(i = 0; i < property.effects.size(); i++) {
			property.effects.get(i).update();	
			property.effects.get(i).draw(g2d_off_screen, this);
			
			if(property.effects.getFirst().is_finished == true) {
				property.effects.removeFirst();
			}
		}
			
	}
	
	public void drawCenterString(String text, int y, Font font)
	{
		FontMetrics metrics = g2d_off_screen.getFontMetrics(font);
		int x = (GalacticMail.SCREEN_WIDTH - metrics.stringWidth(text)) / 2;
		
		g2d_off_screen.setFont(font);
		g2d_off_screen.drawString(text, x, y);
	}
	
	int cooldown_text_flash = 45;
	boolean appear_text = false;
	
	private void updateFlashText()S
	{
		if(cooldown_text_flash > 0) {
			cooldown_text_flash -= 1;
		}
		else
		{
			cooldown_text_flash = 45;
			appear_text = !appear_text;				
		}		
	}
	
	private void renderText(BufferedImage map_obj)
	{ 
		if(galactic_mail.property.game_welcome == true)
		{	
			updateFlashText();
			
			g2d_off_screen.drawImage(galactic_mail_logo.sprite, 140, 90, this);		
			
			if(appear_text) {
				drawCenterString("Press Enter to start", 480, font3);
			}
			
			return;
		}
		
		g2d_off_screen.setFont(font7);
		
		// Draw a player's number of lives
		g2d_off_screen.drawImage(spaceship_lives.sprite, 5, 5, this);			

		if(galactic_mail.property.num_lives > 2) {
			g2d_off_screen.setColor(Color.WHITE); // Set font color
			g2d_off_screen.drawString(" x " + galactic_mail.property.num_lives, 40, 22);
		}
		else
		{
			// If "font8" is BOLD, then shift will be 2.
			final int shift = 0;
			
			g2d_off_screen.setColor(Color.RED); // Set font color			
			g2d_off_screen.setFont(font8);
			g2d_off_screen.drawString(" x " + galactic_mail.property.num_lives, 40 - shift, 22);
		}
		

		g2d_off_screen.setColor(Color.WHITE);
		
		drawCenterString("$" + galactic_mail.property.money, 38, font1);
		drawCenterString("Level " + galactic_mail.property.current_level, GalacticMail.SCREEN_HEIGHT - 50, font1);

		if(galactic_mail.property.game_win == true) 
		{
			updateFlashText();
			
			drawCenterString("DELIVERY COMPLETED", 150, font2);
			
			if(appear_text) {
				drawCenterString("Press Enter to continue", 250, font3);				
			}			
		}
		else if(galactic_mail.property.game_over == true)
		{	
			updateFlashText();
			
			drawCenterString("DELIVERY FAILED", 150, font2);

			drawScoreboard();
			
			if(appear_text) {
				drawCenterString("Press Enter to restart", 440, font3);
			}
		}		
	}
	
	public void drawScoreboard()
	{
		drawCenterString("SCOREBOARD", 210, font5);				
	
		ArrayList<Integer> top_scores = galactic_mail.property.scoreboard.top_scores;
		
		boolean highlighted = false;
		
		for(int i = 0; i < top_scores.size(); i++)
		{
			g2d_off_screen.setColor(Color.WHITE); 					

			if(highlighted == false)
			{
				if(galactic_mail.property.money ==  top_scores.get(i))
				{
					highlighted = true;
					g2d_off_screen.setColor(Color.YELLOW); 					
				}			
			}
			
			drawCenterString("$" + top_scores.get(i), 250 + (i * 29), font6);	
		}		
	}
	
		
	public void startPainting(Graphics g_painter) // The graphics object of the main window.
	{
		int width = GalacticMail.SCREEN_WIDTH;
		int height = GalacticMail.SCREEN_HEIGHT;
		
		createMap(width, height, g2d_off_screen, bi_screen);	

		// Function called, return values are retrieved.
		g2d_off_screen = return_values.g2d;
		bi_screen = return_values.bi_obj;
		
		g2d_off_screen.rotate(0.0, 0, 0);

		g2d_off_screen.setColor(Color.WHITE);
		
		renderEntireScreen(bi_screen);
		renderText(bi_screen);
		
		g2d_off_screen.dispose();

		g_painter.drawImage(bi_screen, 0, 0, this);		
	}	

}
