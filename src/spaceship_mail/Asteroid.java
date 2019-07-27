package spaceship_mail;

import core_logic.GalacticMail;
import core_logic.Main;
import general_logic.GameSprites.ImageData;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.ImageObserver;

public class Asteroid {
	
	public double pos_X;
	public double pos_Y;
	
	public double sprite_width;
	public double sprite_height;	

	int angle = 0;
	double angle_self = 0; // Rotates itself without affecting its direction.
	double rotate_amount = 0;
	
	double SPD_X = 0, SPD_Y = 0;

	private final double Min_SPD_X = 0.3;
	private final double Min_SPD_Y = 0.3;
	
	private ImageData asteroid_sprite = Main.gameSprites.get("Asteroid");

	private OutOfScreenHandler asteroid_outOfScreen = new  OutOfScreenHandler(asteroid_sprite);

	// Speed will be affected by current level.
	public Asteroid(GalacticMail galactic_mail)
	{
		double Max_SPD_X = 0.5 + galactic_mail.property.current_level * 0.35;
		double Max_SPD_Y = 0.5 + galactic_mail.property.current_level * 0.35;
		
		if(Max_SPD_X > 8.0) { Max_SPD_X = 8.0; }
		if(Max_SPD_Y > 8.0) { Max_SPD_Y = 8.0; }
		
		SPD_X = Min_SPD_X + Math.random() * (Max_SPD_X - Min_SPD_X);
		SPD_Y = Min_SPD_Y + Math.random() * (Max_SPD_Y - Min_SPD_Y);		
				
		pos_X = 50 + Math.random() * (GalacticMail.SCREEN_WIDTH - 50);
		pos_Y = 50 + Math.random() * (GalacticMail.SCREEN_HEIGHT - 50);
		
		angle = (int)(Math.random() * (360 / Spaceship.ANGLE_FACTOR));		
		angle_self = Math.random() * (360 / Spaceship.ANGLE_FACTOR);
		
		rotate_amount = 0.1 + Math.random() * (0.4 * galactic_mail.property.current_level);	
		
		if(rotate_amount > 8.0) { rotate_amount = 8.0; }
		
		if(Math.random() > 0.5) {
			rotate_amount = -rotate_amount;
		}
		
		sprite_width = asteroid_sprite.width;
		sprite_height = asteroid_sprite.height;
	}
	
	public void update(GalacticMail galactic_mail)
	{
		double radian = Spaceship.getCustomRadian(angle);
		
		double Speed_X = SPD_X * Math.sin(radian);
		double Speed_Y = SPD_Y * Math.cos(radian);
				
		pos_X += Speed_X; 
		pos_Y -= Speed_Y; 
		
		angle_self += rotate_amount;

		if(rotate_amount > 0) 
		{
			if(angle_self > (360 / Spaceship.ANGLE_FACTOR)) {
				angle_self -= (360 / Spaceship.ANGLE_FACTOR);
			}	
		}
		else
		{
			if(angle_self < 0) {
				angle_self = (360 / Spaceship.ANGLE_FACTOR) + angle_self;
			}			
		}
		
		if(asteroid_outOfScreen.handleOutOfScreen(pos_X, pos_Y, Speed_X, Speed_Y))
		{
			pos_X = asteroid_outOfScreen.pos_X;
			pos_Y = asteroid_outOfScreen.pos_Y;	
		}
	}

	
	public void draw(Graphics2D g2d, ImageObserver obs)
	{				
		double radian = Spaceship.getCustomRadian(angle_self);
								
		AffineTransform no_rotation_config = g2d.getTransform(); // Save this config before rotating the viewpoint.
		
		g2d.rotate(radian, pos_X + (sprite_width / 2), pos_Y + (sprite_height / 2));

		g2d.drawImage(asteroid_sprite.getSprite(), (int) pos_X, (int) pos_Y, obs);
				
		// Reset the viewpoint - it must not remain rotated.
		g2d.setTransform(no_rotation_config);		

	}
}
