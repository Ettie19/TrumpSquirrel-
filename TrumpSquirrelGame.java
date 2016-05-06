//************************************************************************
// File: TrumpSquirrelGame.java       
// 
// Author: Jen Chen and Ettie Nikolova  	Email: jen.chen@yale.edu 
//												   ettie.nikolova@yale.edu
// 
// Class: TrumpSquirrelGame
// Dependency: JComponent, Countdown
//
// Description  :  
//  
//  Creates TrumpSquirrel game component in which shapes move based on
//	boundaries (acorn) and user mouse movements (squirrel)
//
//************************************************************************

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.TexturePaint;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JComponent;

public class TrumpSquirrelGame extends JComponent {
	
	//Import images 
	private BufferedImage backgroundImage = read("forest.png");
	private BufferedImage acornImage = read("acorn.png");
	private BufferedImage squirrelImage  = read("squirrel.png");
	
	private TexturePaint backgroundp;
	private TexturePaint acornp;
	private TexturePaint squirrelp;
	
	private static int squirrelInitWidth = 100;
	private static int squirrelInitHeight = 100;
	
	//Audio files
	private String[] audio = {"trump1.wav", "trump2.wav", "trump3.wav", "trump4.wav", "trump5.wav",
							  "trump6.wav", "trump7.wav", "trump8.wav", "trump9.wav"};
	private String winAudio = "trump1.wav";
	
	//Create rectangles, which will be filled with images
	private static Rectangle2D.Double background;
	private static Rectangle2D.Double acorn = new Rectangle2D.Double(100, 100, 30, 30);
	private static Rectangle2D.Double squirrel = new Rectangle2D.Double(200, 200, squirrelInitWidth, squirrelInitHeight);
	private static Rectangle2D.Double scorebar = new Rectangle2D.Double(0, 0, 0, 10);
	
	//Set speed and direction
	private double speed = 10.0;
	private int xDirection = 1;
	private int yDirection = 1;
	
	//Create buffered image over which shapes will be drawn
	private BufferedImage buffer; 
	
	//Create variables to be updated as points are scored (squirrel intersects acorn)
	private boolean checkIntersection = true;
	private static int score = 0;
	private int win = 20;
	
	
	public TrumpSquirrelGame() {
		
		//Read and respond to user's mouse movements
		addMouseMotionListener(new MouseMotionListener() {
			
			public void mouseMoved(MouseEvent e) {
				
				double tempX = e.getX() - squirrel.getWidth()/2;
				double tempY = e.getY() - squirrel.getHeight()/2;
				
				if(tempX < getWidth()-squirrel.getWidth()/2) {
					squirrel.x = tempX;
					squirrel.y = tempY;
					Cursor hiddenCursor = getToolkit().createCustomCursor(new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB), new Point(1, 1), "");
					setCursor(hiddenCursor);
				} 
				else {
					setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				}
				
			}

			public void mouseDragged(MouseEvent e) {
				
			}
	
		});
		
		//Resize buffered image when console is resized
		addComponentListener(new ComponentAdapter() {

			public void componentResized(ComponentEvent e) {
				buffer = null;
			}
			
		});

	}//end TrumpSquirrelGame
	
	//Insert graphics onto the screen
	protected void paintComponent (Graphics g) {
		
		if(buffer == null) {
			buffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
		}
		
		Graphics2D g2 = (Graphics2D) buffer.getGraphics();	
		
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				
		g2.setColor(Color.BLACK);
		g2.fillRect(0, 0, getWidth(), getHeight());
		
		//Fill a rectangle with a background image
		background = new Rectangle2D.Double(0, 0, getWidth(), getHeight());
		backgroundp = new TexturePaint(backgroundImage, background);
		g2.setPaint(backgroundp);
		g2.fill(background);
		
		//Fill a rectangle with an acorn image
		acornp = new TexturePaint(acornImage, acorn);
		g2.setPaint(acornp);
		g2.fill(acorn);
		
		//Fill a rectangle with a squirrel image
		squirrelp = new TexturePaint(squirrelImage, squirrel);
		g2.setPaint(squirrelp);
		g2.fill(squirrel);
		
		//Create a scorebar that will increase in size as acorns are eaten
		g2.setPaint(Color.RED);
		g2.fill(scorebar);	
		
		g.drawImage(buffer, 0, 0, null);
				
	}//end paintComponent
	
	//game animation
	public void update() {
		
		//Move the acorn
		if (score != win) {
			acorn.x += xDirection * speed;
			acorn.y += yDirection * speed;
			
			//Check if acorn is in bounds and move it in same direction
			if(acorn.x < 0) {
				xDirection = 1;
				acorn.x = 0;
			}
			
			//Change the acorn's direction when it reaches an edge
			else if(acorn.x > getWidth() - acorn.getBounds().width) {
				xDirection = -1;
				acorn.x = getWidth() - acorn.getBounds().width;
			}
			
			//Check if acorn is in bounds and move it in same direction
			if(acorn.y < 0) {
				yDirection = 1;
				acorn.y = 0;
			}
			
			//Change the acorn's direction when it reaches an edge
			else if(acorn.y > getHeight() - acorn.getBounds().height) {
				yDirection = -1;
				acorn.y = getHeight() - acorn.getBounds().height;
			}
			
			//Create a response when the acorn and squirrel intersect 
			if(acorn.intersects(squirrel.getBounds2D())) {
				double random = 0.0;
				
				//Reset the location of the acorn to a random location when the acorn intersects Trump Squirrel
				if(checkIntersection) {					
					random = Math.random();
					int audioRandom = (int) (Math.random() * audio.length);
					
					playSound(audio[audioRandom]);
					
					if (random < .25) {
						acorn.x = 0;
					} else if (random < .5) {
						acorn.x = getWidth();
					} else if (random < .75) {
						acorn.y = 0;
					} else { 
						acorn.y = getHeight();
					}
					
					//Update score and make scorebar get longer
					score += 1;
					scorebar.width = (getWidth()) / win * score;
					
					//Make Trump Squirrel fatter
					squirrel.width += 10;
				} else {
					checkIntersection = true;
				}
			}
		}
		else if(score == win){
			for (int i = 0; i <= 10; i++) {
				squirrel.width +=1;
				squirrel.height +=1;
			}
			
			playSound(winAudio);
		}
		
		repaint();
		
	}//end update
	
	//read image files
	public BufferedImage read(String file) {
		
		BufferedImage image = null;
		
		try {
			image = ImageIO.read(new File(file));
		} catch (IOException ex) {
			System.out.println("Not a valid file name.");
		}
		
		return image;
	}
	
	//read audio files
	public void playSound(String file) {
		try {
			AudioInputStream audioInputStream = 
				AudioSystem.getAudioInputStream(new File(file).getAbsoluteFile());
			Clip clip = AudioSystem.getClip();
			clip.open(audioInputStream);
			clip.start();
		} catch(Exception ex) {
			System.out.println("Not a valid file name.");
		}
	}
	
	//reset game shapes
	public static void restart() {
		score = 0;
		squirrel.width = squirrelInitWidth;
		squirrel.height = squirrelInitHeight;
		scorebar.width = 0;
		Countdown.gameTime = Countdown.gameTimeInit;
	}

}//end class

