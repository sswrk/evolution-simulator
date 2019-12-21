package main;

import java.util.List;
import javax.swing.*;
import java.awt.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import java.io.File;
import java.io.IOException;

import java.io.PrintWriter;
public class Simulation implements ActionListener{
	
	public int delay = 100;
	public JFrame window;
	public Timer timer;
	MapPanel renderMap1;
	MapPanel renderMap2;
	ButtonPanel buttons1;
	ButtonPanel buttons2;
	private JButton stopButton;
	private JButton saveStats;
	
	List<WorldMap> maps;
	
	public Simulation(List<WorldMap> maps) {
		this.maps=maps;
		
		stopButton = new JButton("STOP");
		saveStats = new JButton("ZAPISZ STATYSTYKI DO PLIKU");


		stopButton.addActionListener(this);
		saveStats.addActionListener(this);
		
		timer = new Timer(delay, this);
		
		window = new JFrame("WorldMap");
		
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		if(maps.size()==1 )window.setMinimumSize(new Dimension(1000, 400));
		else window.setMinimumSize(new Dimension(1000, 800));
		window.setExtendedState(window.getExtendedState() | JFrame.MAXIMIZED_BOTH);
        window.setLocationRelativeTo(null);    
        window.setLayout(new GridLayout(2,2));
        
        this.renderMap1 = new MapPanel(this.maps.get(0));
        this.buttons1 = new ButtonPanel(this.maps.get(0));
        
        window.add(renderMap1);
        window.add(buttons1);
        
        this.renderMap2 = renderMap1;
        
        if(this.maps.size()>1) {
        	this.renderMap2 = new MapPanel(this.maps.get(1));
            this.buttons2 = new ButtonPanel(this.maps.get(1));
            
            window.add(renderMap2);
            window.add(buttons2);
        }
        
        buttons1.add(stopButton);
        buttons1.add(saveStats);
        
        window.setVisible(true);
		
	}
	
	public void simulateAll(){
		timer.start();	
		
	}
	
	public void saveStatsToFile() throws IOException{
		File file = new File("stats.txt");
        if(!file.exists()) file.createNewFile();
        PrintWriter save = new PrintWriter("stats.txt");
        save.println("Mapa 1: ");
        buttons1.saveStats(save);
        if(this.maps.size()>1) {
        	save.println("Mapa 2: ");
        	buttons2.saveStats(save);
        }
        save.close();
	}
	
	public void actionPerformed(ActionEvent e) {
		
		Object source = e.getSource();
		

		if(source == stopButton) {
			if(timer.isRunning()) {
				renderMap1.setStopped();
				if(this.maps.size()>1) renderMap2.setStopped();
				timer.stop();
				this.stopButton.setText("START");
			}
			else{
				renderMap1.setStarted();
				if(this.maps.size()>1) renderMap2.setStarted();
				timer.restart();
				this.stopButton.setText("STOP");
			}
		}
		
		if(source == saveStats) {
			try {
				saveStatsToFile();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		
		for(int i=0; i<maps.size() && i<2; i++) {
			if(i==0) {
				Animal animal = this.renderMap1.getCurrentAnimal();
				this.renderMap1.repaint();
				this.buttons1.update(animal);
			}
			if(i==1) {
				Animal animal = this.renderMap2.getCurrentAnimal();
				this.renderMap2.repaint();
				buttons2.update(animal);
			}
			
			maps.get(i).simulateDay();
		}
	  }
	
}
