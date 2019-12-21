package main;

import java.awt.*;
import java.awt.geom.*;
import java.awt.Color;
import java.awt.event.*;

import javax.swing.JPanel;

import java.util.List;

public class MapPanel extends JPanel {
	
	private WorldMap map;
	private int scale;
	Animal currentAnimal = null;
	private boolean stopped;
	
	public MapPanel(WorldMap map) {
		this.map=map;
		this.scale=400/max(this.map.getHeight(),this.map.getWidth());
		if(this.scale==0) this.scale++;
		setPreferredSize(new Dimension(this.map.getWidth()*this.scale, this.map.getHeight()*this.scale));
		this.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                Point point = e.getPoint();
                clicked(point);
            }
        });
		this.stopped=false;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		
		List<Vector2d> animalsCoords = map.getAnimalsCoords();
		List<Vector2d> grassCoords = map.getGrassCoords();
		
		
		
		for(int i=0; i<grassCoords.size(); i++) {
			Rectangle2D rectangle = new Rectangle2D.Double(grassCoords.get(i).getX()*this.scale, grassCoords.get(i).getY()*this.scale, 
					this.scale, this.scale);
			g2d.setPaint(Color.green);
			g2d.fill(rectangle);
			g2d.draw(rectangle);
		}
		
		if(this.stopped==false) {
		
			for(int i=0; i<animalsCoords.size(); i++) {
				Ellipse2D circle = new Ellipse2D.Double(animalsCoords.get(i).getX()*this.scale, animalsCoords.get(i).getY()*this.scale, 
						this.scale, this.scale);
				Animal a = this.map.getMax(animalsCoords.get(i));
				g2d.setPaint(getColor(a.getEnergy(), map.getStartEnergy()));
				g2d.fill(circle);
				g2d.draw(circle);
	
			}
		
		}
		
		else {
			
			for(int i=0; i<animalsCoords.size(); i++) {
				
				
				Ellipse2D circle = new Ellipse2D.Double(animalsCoords.get(i).getX()*this.scale, animalsCoords.get(i).getY()*this.scale, 
						this.scale, this.scale);
				if(this.map.isDominantGeneAt(animalsCoords.get(i))) g2d.setPaint(Color.orange);
				else {
					Animal a = this.map.getMax(animalsCoords.get(i));
				g2d.setPaint(getColor(a.getEnergy(), map.getStartEnergy()));
				}
				g2d.fill(circle);
				g2d.draw(circle);
				
			}
		}
		
	}
	
	void clicked(Point e) {
		Vector2d vector = new Vector2d((int)((double)e.getX()/this.scale), (int)((double)e.getY()/this.scale));
		if(this.map.isOccupied(vector)) {
			Animal animal = this.map.getMax(vector);
			this.currentAnimal=animal;
		}
	}
	
	void setStopped() {
		this.stopped=true;
		
	}
	
	void setStarted() {
		this.stopped=false;
	}
	
	Animal getCurrentAnimal(){
		return this.currentAnimal;
	}
	
	Color getColor(int e, int start) {
		if(e>=2*start) return new Color(0, 128, 255);
		if(2*e>=3*start) return new Color(0, 13, 255);
		if(e>=start) return new Color(93, 0, 255);
		if(3*e>=2*start) return new Color(195, 0, 255);
		if(2*e>=start) return new Color(255, 0, 119);
		return new Color(255, 0, 0);
	}
	
	int max(int a, int b) {
		if(a>b) return a;
		return b;
	}
}
