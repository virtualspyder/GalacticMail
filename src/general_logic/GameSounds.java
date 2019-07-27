package general_logic;

import game_resources.GameResources;

import java.net.URL;
import java.util.HashMap;

import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import core_logic.Main;

/*
   AudioInputStream can play only wav files. MediaPlayer can be used to play wav and mp3 files.
   It is important for a Java program to be lightweight. Using mp3 files can save a lot space compared to wav files.
   
   To be able to use MediaPlayer, this line must be added. 
   The code must be executed once before creating an unlimited number of instances of MediaPlayer. 
   
   static private final JFXPanel jfxPanel = new JFXPanel();
 */

// This class holds the collection of all loaded game sounds. Other classes will play these sounds when necessary.
public class GameSounds
{
	static private final JFXPanel jfxPanel = new JFXPanel();
	private HashMap<String, MediaPlayer> sound_collection = new HashMap<String, MediaPlayer>();
	
	private void loadSound(String resource, String name, boolean loopable) 
	{		
		final String home_dir = "";
		String resource_file = home_dir + resource;
		
		URL url = GameResources.class.getResource(resource_file);

		if(url == null) {
			Main.ErrorLog("Fatal error: A sound file could not be loaded during program initialization. '" + resource_file + "'");						
			System.exit(100);
		}
		
		Media media = new Media(url.toString());		
		MediaPlayer mediaPlayer = new MediaPlayer(media);
		
		// Make this MediaPlayer instance loopable.
		if(loopable == true)
		{
			mediaPlayer.setOnEndOfMedia(new Runnable() {
				@Override
				public void run() 
				{
					mediaPlayer.seek(Duration.ZERO);
					mediaPlayer.play();	
				}
			});	
		}
		else
		{
			mediaPlayer.setOnEndOfMedia(new Runnable() {
				@Override
				public void run() 
				{
					mediaPlayer.seek(Duration.ZERO);
					mediaPlayer.stop();	
				}
			});				
		}
		
		sound_collection.put(name, mediaPlayer);
	}
		
	public GameSounds()
	{		
		 loadSound("Sounds/Galactic Mail Theme.mp3", "Galactic Mail Theme", true);
		 loadSound("Sounds/Main Menu.mp3", "Main Menu", true);
		 loadSound("Sounds/Bonus.wav", "Bonus", false);		
		 loadSound("Sounds/Spaceship Explosion.mp3", "Spaceship Explosion", false);
		 loadSound("Sounds/Spaceship Launch.wav", "Spaceship Launch", false);
	}
	
	public void play(String sound_name)
	{
		MediaPlayer mediaPlayer = sound_collection.get(sound_name);	

		if(mediaPlayer == null)
		{
			Main.ErrorLog("Fatal error: Attempting to play a sound name which does not exist. '" + sound_name + "'");
			System.exit(100);				
		}
		
		mediaPlayer.stop();
		mediaPlayer.play();		
	}
	
	public void stop(String sound_name)
	{
		MediaPlayer mediaPlayer = sound_collection.get(sound_name);	
		
		if(mediaPlayer == null)
		{
			Main.ErrorLog("Fatal error: Attempting to stop a sound name which does not exist. '" + sound_name + "'");
			System.exit(100);				
		}

		mediaPlayer.seek(Duration.ZERO);
		mediaPlayer.stop();
	}
}