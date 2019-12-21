package main;

import java.awt.*;

import javax.swing.JPanel;
import javax.swing.JLabel;
import java.io.PrintWriter;

public class ButtonPanel extends JPanel{

	public static final int HEIGHT = 100;
	public static final int WIDTH = 300;
	private JLabel animals;
	private JLabel grass;
	private JLabel dominantGene;
	private JLabel avgLifespan;
	private JLabel avgChildren;
	private JLabel avgEnergy;
	private JLabel currentAnimalGene;
	private JLabel currentAnimalDead;
	private JLabel currentKidsNumber;
	private JLabel currentScionNumber;
	
	private WorldMap map;
	
	private Animal currentAnimal;
	private boolean currentAnimalIsDead;
	
	public ButtonPanel(WorldMap map) {
		this.map=map;
		
		
		this.animals = new JLabel("Liczba zwierzat: " + Integer.toString(this.map.getAnimalsNumber()));
		this.grass = new JLabel("Liczba roslin: " + Integer.toString(this.map.getGrassNumber()));
		this.dominantGene = new JLabel("brak");
		this.avgLifespan = new JLabel("zadne zwierze nie umarlo");
		this.avgChildren = new JLabel("0");
		this.avgEnergy = new JLabel("0");
		this.currentAnimalGene = new JLabel("");
		this.currentAnimalDead = new JLabel("");
		this.currentKidsNumber = new JLabel("");
		this.currentScionNumber = new JLabel("");
		

		setLayout(new FlowLayout());
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		
		add(grass);
		add(animals);
		add(dominantGene);
		add(avgLifespan);
		add(avgChildren);
		add(avgEnergy);
		add(new JLabel("Sledzenie zwierzecia:"));
		add(this.currentAnimalGene);
		add(this.currentAnimalDead);
		add(this.currentKidsNumber);
		add(this.currentScionNumber);
		
		this.setLayout(new GridLayout(13,1));
	}

	public void update(Animal animal) {
		this.animals.setText("Liczba zwierzat: " + Integer.toString(this.map.getAnimalsNumber()));
		this.grass.setText("Liczba roslin: " + Integer.toString(this.map.getGrassNumber()));
		if(this.map.getDominantGene()==null) this.dominantGene.setText("Wszystkie zwierzeta umarly");
		else this.dominantGene.setText("Dominujacy genotyp: " +this.map.getDominantGene().toString());
		if(this.map.getAvgLifespan()!=0) this.avgLifespan.setText("Sredni czas zycia: " + Double.toString(this.map.getAvgLifespan()));
		this.avgChildren.setText("Srednia ilosc dzieci: " + Double.toString(this.map.getAvgChildren()));
		this.avgEnergy.setText("Srednia ilosc energii: " + Double.toString(this.map.getAvgEnergy()));
		if(animal!=this.currentAnimal) {
			this.currentAnimalIsDead=false;
			this.currentAnimal=animal;
			this.currentAnimalGene.setText(animal.getGene().toString());
			this.currentAnimalDead.setText("Zwierze zyje");
			map.trackNewAnimal(animal);
		}
		if(this.currentAnimal!=null && this.currentAnimalIsDead==false) {
			if(!this.map.animalExists(this.currentAnimal)) {
				this.currentAnimalDead.setText("Zwierze umarlo w dniu: "+ Integer.toString(this.map.getDay()));
				this.currentAnimalIsDead=true;
				
			}
			
		
			this.currentKidsNumber.setText("Liczba dzieci od poczatku sledzenia: "+ Integer.toString(this.map.getCurrentKidsNumber()));
			this.currentScionNumber.setText("Liczba potomkow od poczatku sledzenia: "+ Integer.toString(this.map.getCurrentScionNumber()));
		}
	}
	
	public void saveStats(PrintWriter save) {
		save.println("Liczba zwierzat: " + Integer.toString(this.map.getAnimalsNumber()));
		save.println("Liczba roslin: " + Integer.toString(this.map.getGrassNumber()));
		if(this.map.getDominantGene()==null) save.println("Wszystkie zwierzeta umarly");
		else save.println("Dominujacy genotyp: " +this.map.getDominantGene().toString());
		save.println("Sredni czas zycia: " + Double.toString(this.map.getAvgLifespan()));
		save.println("Srednia ilosc dzieci: " + Double.toString(this.map.getAvgChildren()));
		save.println("Srednia ilosc energii: " + Double.toString(this.map.getAvgEnergy()));
	}
	
	
}