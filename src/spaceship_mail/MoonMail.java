package spaceship_mail;

import java.awt.Graphics2D;
import java.awt.image.ImageObserver;

import general_logic.GameSprites.ImageData;
import core_logic.GalacticMail;
import core_logic.Main;

public class MoonMail {
	
	public double pos_X;
	public double pos_Y;
	
	public double sprite_width;
	public double sprite_height;	

	int angle = 0;
	
	double SPD_X = 0, SPD_Y = 0;
	
	boolean is_delivered = false;
	
	public boolean isDelivered() {
		return is_delivered;
	}
	
	private final double Min_SPD_X = 1.0;
	private final double Min_SPD_Y = 1.0;

	private ImageData moon_sprite;

	private OutOfScreenHandler moon_mail_outOfScreen;

	// Speed will be affected by current level.
	public MoonMail(GalacticMail galactic_mail)
	{
		int which = (int)(1 + Math.random() * 8);
		
		if(which > 7) { which = 7; }
		
		moon_sprite = Main.gameSprites.get("Moon Mail Work " + which);				
		
		double Max_SPD_X = 0.5 + galactic_mail.property.current_level * 0.35;
		double Max_SPD_Y = 0.5 + galactic_mail.property.current_level * 0.35;
		
		if(Max_SPD_X > 8.0) { Max_SPD_X = 8.0; }
		if(Max_SPD_Y > 8.0) { Max_SPD_Y = 8.0; }
		
		SPD_X = Min_SPD_X + Math.random() * (Max_SPD_X - Min_SPD_X);
		SPD_Y = Min_SPD_Y + Math.random() * (Max_SPD_Y - Min_SPD_Y);		
				
		pos_X = 100 + Math.random() * (GalacticMail.SCREEN_WIDTH - 150);
		pos_Y = 100 + Math.random() * (GalacticMail.SCREEN_HEIGHT - 180);
		
		angle = (int)(Math.random() * (360 / Spaceship.ANGLE_FACTOR));
		
		sprite_width = moon_sprite.width;
		sprite_height = moon_sprite.height;
		
		moon_mail_outOfScreen = new OutOfScreenHandler(moon_sprite);
	}
	
	// Default moon mail (The start of every level)
	public MoonMail()
	{
		moon_sprite = Main.gameSprites.get("Moon Mail");				

		sprite_width = moon_sprite.width;
		sprite_height = moon_sprite.height;

		pos_X = GalacticMail.SCREEN_WIDTH  / 2 - (moon_sprite.width / 2);
		pos_Y = GalacticMail.SCREEN_HEIGHT / 2 - moon_sprite.height;

		SPD_X = 0.0;
		SPD_X = 0.0;

		moon_mail_outOfScreen = new OutOfScreenHandler(moon_sprite);
	}
	
	public void update(GalacticMail galactic_mail)
	{
		double radian = Spaceship.getCustomRadian(angle);
		
		double Speed_X = SPD_X * Math.sin(radian);
		double Speed_Y = SPD_Y * Math.cos(radian);
				
		pos_X += Speed_X; 
		pos_Y -= Speed_Y; 
	
		if(moon_mail_outOfScreen.handleOutOfScreen(pos_X, pos_Y, Speed_X, Speed_Y))
		{
			pos_X = moon_mail_outOfScreen.pos_X;
			pos_Y = moon_mail_outOfScreen.pos_Y;	
		}
	}
		
	public void draw(Graphics2D g2d, ImageObserver obs)
	{			
		g2d.drawImage(moon_sprite.getSprite(), (int) pos_X, (int) pos_Y, obs);		
		
		// Only the hitbox area is concerned.
		if(GalacticMail.is_debug) {
			
			final int adjust1 = 3, adjust2 = 3, adjust3 = 5, adjust4 = 7;
			g2d.drawRect((int)(pos_X + adjust1), (int)(pos_Y + adjust2), (int)(sprite_width - adjust3), (int)(sprite_height - adjust4));
		}
	}
}
