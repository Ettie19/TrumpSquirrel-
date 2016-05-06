//************************************************************************
// File: Countdown.java      
// 
// Author: Jen Chen and Ettie Nikolova  	Email: jen.chen@yale.edu 
//												   ettie.nikolova@yale.edu
// 
// Class: Countdown
// Dependency: JPanel, TrumpSquirrelMain
//
// Description  :  
//  
//  Creates panel displayed in TrumpSquirrel applet and displays buttons
//	for start/restart functions, as well as countdown timer.
//
//************************************************************************

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Countdown extends JPanel {
	
	private JFrame frame;
	public static JButton startButton;
	public static JButton restartButton;
	private JLabel stopwatch;
	
	public static int gameTimeInit = 10;
	public static int gameTime;
	private int countUpdate = 0;

	public Countdown() {

		gameTime = gameTimeInit;
		setLayout(new GridLayout(3, 1));
		
		startButton = new JButton("Feed Me");
		restartButton = new JButton("Reset the feeding");
		stopwatch = new JLabel("Time Remaining: " + gameTime);
		
		add(startButton);
		add(restartButton);
		add(stopwatch);
		
	}
	
	//countdown display
	public void update() {
		
		if(countUpdate % 50 == 0 && countUpdate != 0 && gameTime > 0) {
			gameTime--;
		}
		
		if(gameTime == 0) {
			TrumpSquirrelMain.feedClicked = false;
		}
		
		stopwatch.setText("Time Remaining: " + gameTime);
		
		countUpdate++;
		
	}
	
}
