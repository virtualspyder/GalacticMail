package spaceship_mail;

import core_logic.GalacticMail;
import core_logic.Main;
import general_logic.CollisionDetector;
import general_logic.CollisionDetector.Rect;
import general_logic.GameSprites.ImageData;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.ImageObserver;

public class Spaceship 
{
	public double pos_X;
	public double pos_Y;
	
	private MoonMail staying_moon = new MoonMail();
		
	public int sprite_width = 0;
	public int sprite_height = 0;
	
	// It can be either "Spaceship Off" or "Spaceship Fly". Both sprites have to be the same size.
	private ImageData spaceship_sprite_current = Main.gameSprites.get("Spaceship Off");	
	
	private OutOfScreenHandler spaceship_outOfScreen = new OutOfScreenHandler(spaceship_sprite_current);

	public boolean flying_state = false; 
	public boolean is_destroyed = false;
		
	// A brief cool down will ensure that the spaceship stays on a moon for a minimum duration.
	int cooldown_moon_staying = 21;
	
	// Idle state
	private void calculateSpaceshipPosition()
	{
		pos_X = staying_moon.pos_X + (staying_moon.sprite_width - sprite_width) / 2;
		pos_Y = staying_moon.pos_Y + (staying_moon.sprite_height - sprite_height) / 2;				
	}
	
	public Spaceship()
	{
		sprite_width = spaceship_sprite_current.width;
		sprite_height = spaceship_sprite_current.height;
				
		calculateSpaceshipPosition();		
	}
	
	public boolean isDestroyed() {
		return is_destroyed;
	}
	
	// Each 30 angle will cover 60 degree.
	// 180 angle will cover 360 degree.
	public double angle = 0;

	// A low angle factor means a spaceship can rotate smoothly (just enough so the process is not slow).	
	static final int ANGLE_FACTOR = 1;

	public static double getCustomRadian(double angle)
	{
		double radian = Math.toRadians(angle * ANGLE_FACTOR);
		return radian;
	}
	
	public double getSpaceshipRadian() {
		return getCustomRadian(angle);
	}

	public class KeyRequests
	{
		public boolean left = false, right = false, down = false;
		public boolean enter_key = false;
	}
	
	public KeyRequests key_requests = new KeyRequests();
	
	public void draw(Graphics2D g2d, ImageObserver obs)
	{			
		if(GalacticMail.is_debug) {			
			g2d.drawRect((int)pos_X, (int)pos_Y, (int)(sprite_width), (int)(sprite_height));
		}
		
		if(isDestroyed()) {
			return;
		}
		
		// The wide area the spaceship can be launched.
		if(GalacticMail.is_debug) 
		{			
			final double w = spaceship_launch_area.x2 - spaceship_launch_area.x1;
			final double h = spaceship_launch_area.y2 - spaceship_launch_area.y1;
			
			g2d.setColor(Color.GREEN);
			g2d.drawRect((int) spaceship_launch_area.x1, (int) spaceship_launch_area.x2, (int) w, (int) h);
			g2d.setColor(Color.WHITE);
		}
		
		if(flying_state == false) {		
			staying_moon.draw(g2d, obs);
		}
		
		Image spaceship_sprite = spaceship_sprite_current.sprite;
						
		AffineTransform no_rotation_config = g2d.getTransform(); // Save this config before rotating the viewpoint.
		
		g2d.rotate(getSpaceshipRadian(), pos_X + (sprite_width / 2), pos_Y + (sprite_height / 2));

		g2d.drawImage(spaceship_sprite, (int) pos_X, (int) pos_Y, obs);
				
		// Reset the viewpoint - it must not remain rotated.
		g2d.setTransform(no_rotation_config);		
	}

	final CollisionDetector.Rect spaceship_launch_area = new CollisionDetector.Rect(14, 14, GalacticMail.SCREEN_WIDTH - 45, GalacticMail.SCREEN_HEIGHT - 65);
	
	private void updateIdleState(GalacticMail galactic_mail)
	{
		staying_moon.update(galactic_mail);
		calculateSpaceshipPosition();
		
		final int modifier = ANGLE_FACTOR * 5;
		if(key_requests.left == true)
		{
			angle -= modifier;
			if(angle < 0) {
				angle = (360 / ANGLE_FACTOR) + angle;
			}
		}
		else if (key_requests.right == true)
		{
			angle += modifier;
			if(angle > (360 / ANGLE_FACTOR)) {
				angle -= (360 / ANGLE_FACTOR);
			}			
		}
		
		if(galactic_mail.property.money > 0) {
			galactic_mail.property.money -= 1;			
		}
		
		if(cooldown_moon_staying > 0) {
			cooldown_moon_staying -= 1;
			return;
		}
				
		// If a spaceship is too near a border, it cannot be launched.
		if(CollisionDetector.check(
				spaceship_launch_area, 
				new Rect(pos_X, pos_Y, sprite_width, sprite_height)) == false
		)
		{
			return;
		}

		// Money will decrease at a slower rate if a player is within a border, since the player likely cannot launch the spaceship.
		if(galactic_mail.property.money > 0) {
			galactic_mail.property.money -= 1;						
		}

		// Launch the spaceship
		if(key_requests.enter_key == true)
		{
			// Enter the Flying mode.
			flying_state = true;
			
			Main.gameSounds.play("Spaceship Launch");
					
			spaceship_sprite_current = Main.gameSprites.get("Spaceship Fly");	
		}
	}
	

	private void updateFlyingState()
	{
		final double Default_SPD = 5.0;	
		
		if(key_requests.left == true)
		{
			if(--angle < 0) {
				angle = (360 / ANGLE_FACTOR);
			}
		}
		else if (key_requests.right == true)
		{
			if(++angle > (360 / ANGLE_FACTOR)) {
				angle = 0;
			}			
		}
		
		double radian = getSpaceshipRadian();
		
		double SPD_X = Default_SPD * Math.sin(radian);
		double SPD_Y = Default_SPD * Math.cos(radian);
						
		pos_X += SPD_X; 
		pos_Y -= SPD_Y; 
	
		if(spaceship_outOfScreen.handleOutOfScreen(pos_X, pos_Y, SPD_X, SPD_Y))
		{
			pos_X = spaceship_outOfScreen.pos_X;
			pos_Y = spaceship_outOfScreen.pos_Y;	
		}
	}
	
	int resurrection_tick = 0;
	
	public void updateCollision(GalacticMail galactic_mail)
	{
		int i;
		GalacticMail.Property property = galactic_mail.property;
		
		CollisionDetector.Rect spaceship_rect = new CollisionDetector.Rect(pos_X, pos_Y, sprite_width, sprite_height);
	
		for(i = 0; i < property.moons.size(); i++)
		{
			MoonMail moon = property.moons.get(i);
			
			// Calculate a moon's hitbox area.
			final int adjust1 = 3, adjust2 = 3, adjust3 = 5, adjust4 = 7;

			double x2 = moon.pos_X + adjust1;
			double y2 = moon.pos_Y + adjust2;
			double w2 = moon.sprite_width - adjust3;
			double h2 = moon.sprite_height - adjust4;
		
			if(CollisionDetector.check(spaceship_rect, new Rect(x2, y2, w2, h2)))
			{
				moon.is_delivered = true;
				Main.gameSounds.play("Bonus");			
				
				// Transfer the information.
				staying_moon = moon;
				
				// Enter the Idle mode.
				flying_state = false;
				cooldown_moon_staying = 18;
				
				spaceship_sprite_current = Main.gameSprites.get("Spaceship Off");	
				
				galactic_mail.property.money += 500;
				
				property.moons.remove(i);
				
				// All deliveries have been completed. Proceed to the next level.
				if(property.moons.size() == 0)
				{
					property.game_win = true;			
					property.enterKey_delay = 20;
				}

				// No further collisions will be handled.
				return;
			}
		}
		
		for(i = 0; i < property.asteroids.size(); i++)
		{
			Asteroid asteroid = property.asteroids.get(i);
			
			// Calculate an asteroid's hitbox area.

			final int adjust1 = 4, adjust2 = 4, adjust3 = 7, adjust4 = 7;
			double x2 = asteroid.pos_X + adjust1;
			double y2 = asteroid.pos_Y + adjust2;
			double w2 = asteroid.sprite_width - adjust3;
			double h2 = asteroid.sprite_height - adjust4;

			if(CollisionDetector.check(spaceship_rect, new Rect(x2, y2, w2, h2)))
			{
				is_destroyed = true;
				resurrection_tick = 85;
				
				Main.gameSounds.play("Spaceship Explosion");		
				
				// Calculate the position of the explosion.
				ImageData exp = Main.gameSprites.get("Explosion A1");

				double center_X = pos_X + sprite_width / 2;
				double center_Y = pos_Y + sprite_height / 2;
				
				double explosion_X = center_X - exp.width / 2;
				double explosion_Y = center_Y - exp.width / 2;
								
				galactic_mail.property.effects.add(new ExplosionEffect(explosion_X, explosion_Y, "Spaceship"));
								
				return;
			}
		}	
	}
	
	
	public void update(GalacticMail galactic_mail)
	{
		if(isDestroyed())
		{
			if(resurrection_tick > 0) {
				resurrection_tick -= 1;
			}
			else
			{
				if(galactic_mail.property.num_lives > 0)
				{
					galactic_mail.property.num_lives -= 1;

					if(galactic_mail.property.num_lives > 0) {
						galactic_mail.property.spaceship_player = new Spaceship();
					}
					else
					{
						galactic_mail.property.game_over = true;					
						galactic_mail.property.scoreboard.addNewScore(galactic_mail.property.money);
						galactic_mail.property.enterKey_delay = 20;
					}
				}
			}
			return;				
		}
		
		// No keys are processed if a level has been won.
		if(galactic_mail.property.game_win == true) 
		{
			staying_moon.update(galactic_mail);
			calculateSpaceshipPosition();

			return;
		}
		
		if(flying_state == false) 
		{
			updateIdleState(galactic_mail);			
		}
		else
		{			
			updateFlyingState();						

			updateCollision(galactic_mail);
		}
	}
}
