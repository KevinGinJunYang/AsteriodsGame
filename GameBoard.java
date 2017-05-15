
import java.awt.BorderLayout;

import java.awt.Color;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;



import java.util.ArrayList;


import java.util.concurrent.ScheduledThreadPoolExecutor;


import java.util.concurrent.TimeUnit;

import javax.swing.JComponent;
import javax.swing.JFrame;



import javax.sound.sampled.Clip;



import javax.sound.sampled.AudioSystem;



import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import java.io.IOException;
import java.net.*;

import javax.swing.*;

public class GameBoard extends JFrame{
	
	private static final long serialVersionUID = 1L;
	
	
	
	public static int boardWidth = 1000;
	public static int boardHeight = 800;
	
	
	public static boolean keyHeld = false;
	
	
	
	public static int keyHeldCode;
	
	
	public static ArrayList<PhotonTorpedo> torpedos = new ArrayList<PhotonTorpedo>();
	

	
	String thrustFile = "file:./src/thrust.au";
	String laserFile = "file:./src/laser.aiff";
	
	public static void main(String [] args)
    {
            new GameBoard();
            
    }
	
	public GameBoard()
    {
    
    	
        this.setSize(boardWidth, boardHeight);
        this.setTitle("Java Asteroids");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
       
        
        addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
			
				
			}

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode()==87)
			    {
					System.out.println("Forward");
					
				
					playSoundEffect(thrustFile);
					
					keyHeldCode = e.getKeyCode();
			    	keyHeld = true;
					
			    } 
			
				
					else if (e.getKeyCode()==83){
			    	System.out.println("Backward");
			    	
			    	keyHeldCode = e.getKeyCode();
			    	keyHeld = true;
			    	
			    } 
			    
	d
			    
			    else if (e.getKeyCode()==68){
			    	System.out.println("Rotate Right");
			    	
			    	keyHeldCode = e.getKeyCode();
			    	keyHeld = true;
			    	
			    } 
		
			    
			    else if (e.getKeyCode()==65){
			    	System.out.println("Rotate Left");
			    	
			    	keyHeldCode = e.getKeyCode();
			    	keyHeld = true;
			    }
				
			
				
			    else if (e.getKeyCode()==KeyEvent.VK_ENTER){
			    	System.out.println("Shoot");
			    	
			   
			    	
			    	playSoundEffect(laserFile);
			    	
			
			    	torpedos.add(new PhotonTorpedo(GameDrawingPanel2.theShip.getShipNoseX(),
			    			GameDrawingPanel2.theShip.getShipNoseY(), 
			    			GameDrawingPanel2.theShip.getRotationAngle()));
			    	
			    	System.out.println("RotationAngle " + GameDrawingPanel2.theShip.getRotationAngle());
			    	
			    }
				
			}

	
			
			public void keyReleased(KeyEvent e) {
		
				keyHeld = false;
				
			}
        	
        });
        
        GameDrawingPanel2 gamePanel = new GameDrawingPanel2();


        
        this.add(gamePanel, BorderLayout.CENTER);
        
  
        
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(5);
		
    
        
		executor.scheduleAtFixedRate(new RepaintTheBoard2(this), 0L, 20L, TimeUnit.MILLISECONDS);
        
    
        
        this.setVisible(true);
    }
	

	
	public static void playSoundEffect(String soundToPlay){
    	
    
		URL soundLocation;
		try {
			soundLocation = new URL(soundToPlay);
		 
        	    Clip clip = null;
				
				clip = AudioSystem.getClip();
				
        	    AudioInputStream inputStream;

				inputStream = AudioSystem.getAudioInputStream(soundLocation);

				clip.open( inputStream );
					
			
				clip.loop(0);
				
				
	        	clip.start();
				} 
				
				catch (MalformedURLException e1) {
				
					e1.printStackTrace();
				}
        	    
        	    catch (UnsupportedAudioFileException | IOException e1) {
				
					e1.printStackTrace();
				}
        	    
        	    catch (LineUnavailableException e1) {
			
					e1.printStackTrace();
				}
    	
    }
		
	
}



class RepaintTheBoard2 implements Runnable{

	GameBoard theBoard;
	
	public RepaintTheBoard2(GameBoard theBoard){
		this.theBoard = theBoard;
	}

	@Override
	public void run() {
		
	
		
		theBoard.repaint();
		
	}
	
}

@SuppressWarnings("serial")



class GameDrawingPanel2 extends JComponent { 
	
	public static ArrayList<Rock> rocks = new ArrayList<Rock>();
	

	
	int[] polyXArray = Rock.sPolyXArray;
	int[] polyYArray = Rock.sPolyYArray;
	

	static SpaceShip theShip = new SpaceShip();
	

	
	int width = GameBoard.boardWidth;
	int height = GameBoard.boardHeight;
	

	public GameDrawingPanel2() { 
		
		for(int i = 0; i < 10; i++){
			
	
			
			int randomStartXPos = (int) (Math.random() * (GameBoard.boardWidth - 40) + 1);
			int randomStartYPos = (int) (Math.random() * (GameBoard.boardHeight - 40) + 1);
			
		
			
			rocks.add(new Rock(Rock.getpolyXArray(randomStartXPos), Rock.getpolyYArray(randomStartYPos), 13, randomStartXPos, randomStartYPos));
			
			Rock.rocks = rocks; 
			
		}
		
	} 
	
	public void paint(Graphics g) { 
		
		
		
		Graphics2D graphicSettings = (Graphics2D)g; 
		
		AffineTransform identity = new AffineTransform();
		
	
		
		graphicSettings.setColor(Color.BLACK);
		graphicSettings.fillRect(0, 0, getWidth(), getHeight());
		

		
		graphicSettings.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); 
		

		
		graphicSettings.setPaint( Color.WHITE ); 
		
		
		
		for(Rock rock : rocks){
			

			
			if(rock.onScreen){
			
			
			rock.move(theShip, GameBoard.torpedos); 
			
	
			
			graphicSettings.draw(rock); 
			
			}
			
		} 
		

		
		if(GameBoard.keyHeld == true && GameBoard.keyHeldCode == 68){
			
			theShip.increaseRotationAngle();
			
		} else
			
		
			
		if(GameBoard.keyHeld == true && GameBoard.keyHeldCode == 65){
				
			theShip.decreaseRotationAngle();
				
		} else
			
		if (GameBoard.keyHeld == true && GameBoard.keyHeldCode == 87){
			
		
			theShip.setMovingAngle(theShip.getRotationAngle());
			
			theShip.increaseXVelocity(theShip.shipXMoveAngle(theShip.getMovingAngle())*0.1);
			theShip.increaseYVelocity(theShip.shipYMoveAngle(theShip.getMovingAngle())*0.1);
			
		} 
		
		theShip.move();
		
		
		
		graphicSettings.setTransform(identity);
		

		
		graphicSettings.translate(theShip.getXCenter(),theShip.getYCenter());
		

		
		graphicSettings.rotate(Math.toRadians(theShip.getRotationAngle()));
		
		graphicSettings.draw(theShip);
		
	
		
		for(PhotonTorpedo torpedo : GameBoard.torpedos){
			
		
			
			torpedo.move(); 
			
		
			
			if(torpedo.onScreen){
			
				
				
				graphicSettings.setTransform(identity);
				
			
				
				graphicSettings.translate(torpedo.getXCenter(),torpedo.getYCenter());
				
				graphicSettings.draw(torpedo);
			
			}
			
		}
		
		
	} 
	
	
}