import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JFrame;

import model.ChessModel;
import view.ChessView;

public class ChessMain {
	
	public static void main(String[] args) {
		
		/*
		 * Multi-threading for better performance
		 */
		
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JFrame window = new JFrame("Chess");
				
				ChessModel model = new ChessModel();
				ChessView view = new ChessView(model);
				model.setView(view);

				window.setContentPane(view);
				window.pack();
				Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
				window.setLocation((screenSize.width - window.getWidth())/2, (screenSize.height - window.getHeight())/2);
				window.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
				window.setResizable(false);  
				window.setVisible(true);
				
				/*
				 * Mozart Piano Concerto no. 21 in C major, K. 467 - II. Andante by Markus Staab
				https://musopen.org/music/2635-piano-concerto-no-21-in-c-major-k-467/
				Attribution 3.0 Unported (CC BY 3.0) 
				https://creativecommons.org/licenses/by/3.0/
				Music promoted on https://www.chosic.com/free-music/all/ 
				 */
				
				playMusic("Piano.wav");
			}
		});

	}
	
	// Borrowed from RPG game reviewed in-class
	public static void playMusic(String fileName) {
		try {
			File soundFile = new File(fileName);
			AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
			Clip clip = AudioSystem.getClip(); 
			clip.open(audioIn);
			clip.start();
			clip.loop(Clip.LOOP_CONTINUOUSLY);
		} 
		catch (Exception e) { 
			e.printStackTrace(); 
		} 
	}
	
}
