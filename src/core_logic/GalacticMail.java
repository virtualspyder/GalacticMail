package core_logic;

import general_logic.KeyControl;
import spaceship_mail.Paint;
import spaceship_mail.*;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.Vector;

public class GalacticMail extends JApplet 
{
	public static final int SCREEN_WIDTH = 800;
	public static final int SCREEN_HEIGHT = 600;
	
	public static final boolean is_debug = false;
		
	// This class is created to narrow the number of items to find desired things quicker. (code auto-completion will be faster.)
	static public class Property
	{		
		public int money = 0;
		public boolean music_enabled = true;
		
		public boolean game_win = false;
		public boolean game_over = false;
		public boolean game_welcome = true;

		public int current_level = 1;
		public int num_lives = 4;
		
		public int enterKey_delay = 0;
		
		public Paint paint = null;
		public KeyControl keyboard_listener;
		public Spaceship spaceship_player = new Spaceship();
		
		public Scoreboard scoreboard = new Scoreboard();
		
		public Vector<MoonMail> moons = new Vector<MoonMail>();
		public Vector<Asteroid> asteroids = new Vector<Asteroid>();
		public LinkedList<ExplosionEffect> effects = new LinkedList<ExplosionEffect>();
	}
	
	public Property property = new Property();
	
	public GalacticMail()
	{
		property.paint = new Paint(this);
		property.keyboard_listener = new KeyControl(this);
		
		setFocusable(true);
		setBackground(Color.black);		

		addKeyListener(property.keyboard_listener);
	}
	
	public void paint(Graphics g_painter)
	{		
		property.paint.startPainting(g_painter);				
	}
	
	public void start()
	{
		Main.gameSounds.play("Main Menu");

		initializeLevel();

		property.paint.getReady();		
	}
	
	public void initializeLevel()
	{
		int i;
		
		property.moons.clear();
		property.asteroids.clear();
		
		property.spaceship_player = new Spaceship();
		
		if(property.game_welcome == true)
		{
			for(i = 0; i < 20; i++) {
				property.asteroids.add(new Asteroid(this));			
			}						
			
			property.spaceship_player.is_destroyed = true;
			property.num_lives = 0;
						
			return;
		}
		
		property.game_win = false;
		
		// Create 5 moon mails. If the spaceship reaches all 5 moons, it is a win since all 5 deliveries are completed.
		int N_MOONS = 5;
		
		if(property.current_level >= 10) {
			N_MOONS += 1;
		}
		
		for(i = 0; i < N_MOONS; i++) {
			property.moons.add(new MoonMail(this));			
		}		

		int N_ASTEROIDS = (int)(7 + Math.random() * (property.current_level * 2));
		
		for(i = 0; i < N_ASTEROIDS; i++) {
			property.asteroids.add(new Asteroid(this));			
		}		
		
		// Reward the player by giving a life per 2 levels.
		if(property.current_level % 2 == 1) 
		{
			property.num_lives += 1;			
		}
	}	
}
