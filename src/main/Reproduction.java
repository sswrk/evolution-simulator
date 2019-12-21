package main;

public class Reproduction {
	
	private Animal a;
	private Animal b;
	private WorldMap map;
	public Reproduction(Animal a, Animal b, WorldMap map) {
		this.a=a;
		this.b=b;
		this.map=map;
	}
	Vector2d childPos() {
		Vector2d result = this.a.getPosition();
		result.subtract(new Vector2d(0,1));
		if(map.belongs(result) && !map.isOccupied(result)) return result;
		result.add(new Vector2d(1,0));
		if(map.belongs(result) && !map.isOccupied(result)) return result;
		result.add(new Vector2d(0,1));
		if(map.belongs(result) && !map.isOccupied(result)) return result;
		result.add(new Vector2d(0,1));
		if(map.belongs(result) && !map.isOccupied(result)) return result;
		result.subtract(new Vector2d(1,0));
		if(map.belongs(result) && !map.isOccupied(result)) return result;
		result.subtract(new Vector2d(1,0));
		if(map.belongs(result) && !map.isOccupied(result)) return result;
		result.subtract(new Vector2d(0,1));
		if(map.belongs(result) && !map.isOccupied(result)) return result;
		result.subtract(new Vector2d(0,1));
		if(map.belongs(result) && !map.isOccupied(result)) return result;
		result.add(new Vector2d(1, 0));
		if(map.belongs(result)) return result;
		result.add(new Vector2d(1, 0));
		if(map.belongs(result)) return result;
		result.subtract(new Vector2d(0, 1));
		if(map.belongs(result)) return result;
		result.subtract(new Vector2d(0, 1));
		if(map.belongs(result)) return result;
		result.subtract(new Vector2d(1, 0));
		if(map.belongs(result)) return result;
		result.subtract(new Vector2d(1, 0));
		if(map.belongs(result)) return result;
		result.add(new Vector2d(0, 1));
		if(map.belongs(result)) return result;
		result.add(new Vector2d(0, 1));
		return result;
		
    }
    
    Animal reproduce() {
    	int energy = a.getEnergy()/4 + b.getEnergy()/4;
    	a.addEnergy(-a.getEnergy()/4);
    	b.addEnergy(-b.getEnergy()/4);
    	Vector2d pos = childPos();
    	Gene gene = a.getGene().childGene(b.getGene());
    	Animal child = new Animal(energy, pos, gene, this.map);
    	return child;
    	
    }
}
