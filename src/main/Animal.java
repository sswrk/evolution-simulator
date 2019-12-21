package main;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class Animal {
	
	private int energy;
    private MapDirection dir=MapDirection.NORTH;
    private Vector2d position;
    private List<IPositionChangeObserver> observers = new ArrayList<>();
    private Gene genes;
    private WorldMap map;
    private int dayBorn;
    private int childrenNumber;
    
    private Random generate = new Random();

    public Animal(int startEnergy, Vector2d position, Gene genes, WorldMap map) {
        this.energy=startEnergy;
        this.position=position;
        this.genes=genes;
        this.dir=dir.random();
        this.map=map;
        this.dayBorn=map.getDay();
        this.childrenNumber=0;
    }


    public Vector2d getPosition() {
        return this.position;
    }

    public int getEnergy() {
    	return this.energy;
    }
    
    public void addEnergy(int energyToAdd) {
    	this.energy+=energyToAdd;
    }

    Gene getGene() {
    	return this.genes;
    }


    public void move() {
    	int rotates=genes.getGene().charAt(generate.nextInt(32)) - '0';
    	
    	for(int i=0; i<rotates; i++) this.dir = this.dir.next();
    	
    	Vector2d oldPos = this.position;

        Vector2d moveCoords = this.dir.toUnitVector();

        moveCoords = this.position.add(moveCoords);
        
        this.position = moveCoords;
        
        if(!map.belongs(this.position)) this.position=map.otherside(this.position);
        
        positionChanged(oldPos);
        
        this.energy--;
        
    }

    public void addObserver(IPositionChangeObserver observer)
    {
        this.observers.add(observer);
    }

    public void removeObserver(IPositionChangeObserver observer)
    {
        this.observers.remove(observer);
    }

    private void positionChanged(Vector2d oldPosition)
    {
        for (IPositionChangeObserver obs: this.observers)
        {
               obs.positionChanged(oldPosition, this.position, this);
        }

    }
    
    public int getBirthDay() {
    	return this.dayBorn;
    }
    
    public void addChild() {
    	this.childrenNumber++;
    }
    
    public int getChildrenNumber() {
    	return this.childrenNumber;
    }
}
