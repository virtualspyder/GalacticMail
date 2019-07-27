package spaceship_mail;

import general_logic.GameSprites.ImageData;
import core_logic.GalacticMail;

public class OutOfScreenHandler 
{	
	private final int screen_width = GalacticMail.SCREEN_WIDTH;
	private final int screen_height = GalacticMail.SCREEN_HEIGHT;

	private final int x1, y1, x2, y2;

	public OutOfScreenHandler(ImageData sprite)
	{						
		final int adjust = 0;
						
		int sprite_width = sprite.width - adjust;
		int sprite_height = sprite.height - adjust;
			
		x1 = 0 - sprite_width;
		y1 = 0 - sprite_height;
		x2 = screen_width + sprite_width;
		y2 = screen_height + sprite_height;			
	}
	
	// Two return values after calling handleOutOfScreen().
	public double pos_X;
	public double pos_Y;

	// If there is an event occurring, the method returns true.
	public boolean handleOutOfScreen(double x, double y, double SPD_X, double SPD_Y)
	{
		pos_X = x;
		pos_Y = y;
	
		boolean outOfScreen = false;
		
		if((pos_X < x1 || pos_X > x2) || (pos_Y < y1 || pos_Y > y2)) { outOfScreen = true; }		
				
		if(outOfScreen == false) {
			return false;
		}

		// the spaceship (or an asteroid etc) teleports in the reverse direction to calculate where the spaceship will appear from.
		while(true)
		{
			pos_X -= SPD_X;
			pos_Y += SPD_Y;
			
			 if(SPD_X > 0 && pos_X <= x1) { break; }
			 if(SPD_X < 0 && pos_X >= x2) { break; }
			
			 if(SPD_Y < 0 && pos_Y <= y1) { break; }
			 if(SPD_Y > 0 && pos_Y >= y2) { break; }							
		}
		
		return true;
	}
}

