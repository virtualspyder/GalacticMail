package spaceship_mail;

import core_logic.GalacticMail;
import core_logic.Main;
import general_logic.GameSprites.ImageData;

import java.awt.*;
import java.awt.image.ImageObserver;
import java.util.Vector;


public class ExplosionEffect 
{
	public double pos_X = 0;
	public double pos_Y = 0;
	
	// After every 5 ticks, the next sprite will be ready. If no new sprites are left, the animation is finished. (60 FPS)

	boolean is_finished = false;
	int current_idx = 0;
	public int frame_tick = 5;
	Vector<ImageData> animation_list = new Vector<ImageData>();
	
	public void update()
	{
		if(is_finished) { return; }
	
		frame_tick -= 1;
		if(frame_tick <= 0)
		{
			frame_tick = 5;
			current_idx += 1;
			
			if(current_idx >= animation_list.size()) {
				is_finished = true;				
			}			
		}	
	}
	
	public boolean isFinished()
	{
		return is_finished;		
	}
	
	public void draw(Graphics2D g2d, ImageObserver obs)
	{
		if(is_finished) { return; }

		if(GalacticMail.is_debug){
			g2d.drawRect((int)pos_X, (int)pos_Y, (int)(animation_list.get(current_idx).width), (int)(animation_list.get(current_idx).height));
		}
		
		g2d.drawImage(animation_list.get(current_idx).sprite, (int) pos_X, (int) pos_Y, obs);
	}
	
	public ExplosionEffect(double x, double y, String type)
	{
		pos_X = x;
		pos_Y = y;
		
		if(type == "Spaceship")
		{
			animation_list.add(Main.gameSprites.get("Explosion A1"));
			animation_list.add(Main.gameSprites.get("Explosion A2"));
			animation_list.add(Main.gameSprites.get("Explosion A3"));
			animation_list.add(Main.gameSprites.get("Explosion A4"));
			animation_list.add(Main.gameSprites.get("Explosion A5"));
			animation_list.add(Main.gameSprites.get("Explosion A6"));
			animation_list.add(Main.gameSprites.get("Explosion A7"));
			animation_list.add(Main.gameSprites.get("Explosion A8"));
			animation_list.add(Main.gameSprites.get("Explosion A9"));
			animation_list.add(Main.gameSprites.get("Explosion A10"));
			animation_list.add(Main.gameSprites.get("Explosion A11"));
			animation_list.add(Main.gameSprites.get("Explosion A12"));
		}		
		else
		{
			Main.ErrorLog("Fatal error: Unknown explosion effect specified. '" + type + "'");
			System.exit(100);								
		}
	}
}
