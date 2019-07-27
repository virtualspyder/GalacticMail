package general_logic;

import core_logic.Main;
import game_resources.GameResources;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.HashMap;

// This class holds the collection of all loaded game sprites. Other classes will need these sprites.
public class GameSprites extends Component  
{	
	static public class ImageData
	{
		public ImageData(Image img, int w, int h) {
			sprite = img;
			width = w;
			height = h;
		}
		
		public ImageData() 
		{
			sprite = null;
			width = 0;
			height = 0;			
		}
		
		public Image getSprite() {
			return sprite;
		}
		
		// "final" prevents all accidental modifications.
		public final Image sprite;
		public final int width, height;		
	}
	
	private HashMap<String, ImageData> sprite_collection = new HashMap<String, ImageData>();

	private void loadSprite(String resource, String name) 
	{		
		final String home_dir = "";
		String resource_file = home_dir + resource;
		
		URL url = GameResources.class.getResource(resource_file);

		if(url == null) {
			Main.ErrorLog("Fatal error: A resource file could not be loaded during program initialization. '" + resource_file + "'");						
		}
		
		// Image width & image height are conveniently retrieved here.
		Image img = getToolkit().getImage(url);
		
		ImageIcon icon = new ImageIcon(img);
		
		int img_width = icon.getIconWidth();
		int img_height = icon.getIconHeight();		
		
		sprite_collection.put(name, new ImageData(img, img_width, img_height));		
	}
	
	public ImageData get(String sprite_name)
	{
		ImageData img = sprite_collection.get(sprite_name);		
	
		if(img == null)
		{
			Main.ErrorLog("Fatal error: Attempting to retrieve a loaded sprite which does not exist. '" + sprite_name + "'");
			System.exit(100);						
		}	
		
		return img;
	}
		
	public GameSprites()
	{
		 loadSprite("Game Logo.png", "Game Logo");

		 loadSprite("ImgSpaceship/Spaceship Off.png", "Spaceship Off");
		 loadSprite("ImgSpaceship/Spaceship Fly.png", "Spaceship Fly");
		 loadSprite("ImgSpaceship/Spaceship Lives.png", "Spaceship Lives");

		 loadSprite("ImgSpace/Asteroid.png", "Asteroid");
		 loadSprite("ImgSpace/Background.png", "Background Space");

		 loadSprite("ImgMoon/Moon Mail.png", "Moon Mail");
		 loadSprite("ImgMoon/Moon Mail Work 1.png", "Moon Mail Work 1");
		 loadSprite("ImgMoon/Moon Mail Work 2.png", "Moon Mail Work 2");
		 loadSprite("ImgMoon/Moon Mail Work 3.png", "Moon Mail Work 3");
		 loadSprite("ImgMoon/Moon Mail Work 4.png", "Moon Mail Work 4");
		 loadSprite("ImgMoon/Moon Mail Work 5.png", "Moon Mail Work 5");
		 loadSprite("ImgMoon/Moon Mail Work 6.png", "Moon Mail Work 6");
		 loadSprite("ImgMoon/Moon Mail Work 7.png", "Moon Mail Work 7");

		 loadSprite("ImgExplosion/Explosion A-01.png", "Explosion A1");	
		 loadSprite("ImgExplosion/Explosion A-02.png", "Explosion A2");	
		 loadSprite("ImgExplosion/Explosion A-03.png", "Explosion A3");	
		 loadSprite("ImgExplosion/Explosion A-04.png", "Explosion A4");	
		 loadSprite("ImgExplosion/Explosion A-05.png", "Explosion A5");	
		 loadSprite("ImgExplosion/Explosion A-06.png", "Explosion A6");	
		 loadSprite("ImgExplosion/Explosion A-07.png", "Explosion A7");	
		 loadSprite("ImgExplosion/Explosion A-08.png", "Explosion A8");	
		 loadSprite("ImgExplosion/Explosion A-09.png", "Explosion A9");	
		 loadSprite("ImgExplosion/Explosion A-10.png", "Explosion A10");	
		 loadSprite("ImgExplosion/Explosion A-11.png", "Explosion A11");	
		 loadSprite("ImgExplosion/Explosion A-12.png", "Explosion A12");	
	}	
}
