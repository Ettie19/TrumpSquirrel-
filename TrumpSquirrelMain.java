//************************************************************************
// File: TrumpSquirrelMain.java       
// 
// Author: Jen Chen and Ettie Nikolova  	Email: jen.chen@yale.edu 
//												   ettie.nikolova@yale.edu
// 
// Class: TrumpSquirrelMain
// Dependency: JApplet, TrumpSquirrelGame, Countdown
//
// Description  :  
//  
//  Creates applet that compiles TrumpSquirrel game component and 
//	Countdown panel display. Starts and stops timer based on button events.
//
//************************************************************************

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;

import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.Timer;

public class TrumpSquirrelMain extends JApplet implements ActionListener {

	//Set timer for the game 
	private Timer timer;
	
	private TrumpSquirrelGame game;
	private static Countdown countdown;
	private int refresh = 20; 
	public static boolean feedClicked = false;
	
	public void destroy() {
		
	}
	
	public void init() {
		
		game = new TrumpSquirrelGame();		
		timer = new Timer(refresh, this);
		countdown = new Countdown();
		
		//add game component and countdown frame
		setSize(1000, 600);
		setLayout(new BorderLayout());
		add(game, BorderLayout.CENTER);
		add(countdown, BorderLayout.EAST);
		
		//start the game
		countdown.startButton.addActionListener(new ActionListener() { 
		    public void actionPerformed(ActionEvent e) { 
		        feedClicked = true;
		    } 
		});
		
		//reset the game
		countdown.restartButton.addActionListener(new ActionListener() { 
		    public void actionPerformed(ActionEvent e) { 
		        feedClicked = false;
		        TrumpSquirrelGame.restart();
		    } 
		});
		
	}
	
	public void start() {
		
		timer.start();
	}
	
	public void stop() {

		timer.stop();

	}
	
	public void actionPerformed(ActionEvent e) {
		
        if(feedClicked) {
			game.update();
			countdown.update();
		}
		
	}

	
} //end class



