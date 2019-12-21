package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.lang.Math; 

import org.json.simple.JSONObject;

import java.util.Random;

public class WorldMap implements IPositionChangeObserver{
	protected int width;
	protected int height;
	protected int jungleRatio; //w procentach
	protected int startEnergy;
	protected int moveEnergy;
	protected int plantEnergy;
	protected Vector2d jungleLowerLeft;
	protected Vector2d jungleUpperRight;
	protected int day;
	protected int deaths;
	protected double avgLifespan;
	protected double avgChildren;
	protected double avgEnergy;
	
	private Random generate = new Random();
	
    protected Map<Vector2d, List<Animal>> animalsCoords = new HashMap<>();
    protected List<Vector2d> coordsList = new ArrayList<>();
    protected List<Vector2d> grassList = new ArrayList<>();
    protected List<Animal> animals = new ArrayList<>();
    		
    protected int jungleSize;
    
    protected int numberOfAnimals;
    
    protected Map<Gene, Integer> genesCount = new HashMap<>();
    
    //do sledzenia statystyk danego zwierzecia
    
    protected Animal currentTrackAnimal;
    protected List<Animal> currentKids;
    protected List<Animal> currentScion;
	
	
	public WorldMap(JSONObject jsonObject, int n) {	
		this.numberOfAnimals = 0;
		
		//czytanie jsona
		
		this.width = Integer.parseInt((String) jsonObject.get("width"));
		this.height = Integer.parseInt((String) jsonObject.get("height")); 
		this.startEnergy = Integer.parseInt((String) jsonObject.get("startEnergy")); 
		this.moveEnergy = Integer.parseInt((String) jsonObject.get("moveEnergy")); 
		this.plantEnergy = Integer.parseInt((String) jsonObject.get("plantEnergy")); 
		this.jungleRatio = Integer.parseInt((String) jsonObject.get("jungleRatio"));
		
		//wyliczanie pozycji dzungli
		
		this.jungleSize = (int) Math.sqrt(this.height*this.width*this.jungleRatio/100);
		this.jungleLowerLeft=new Vector2d((this.width-jungleSize)/2,(this.height-jungleSize)/2);
		this.jungleUpperRight=new Vector2d(this.jungleLowerLeft.getX()+jungleSize, this.jungleLowerLeft.getY()+jungleSize);
	
		
		//umiejscawianie zwierz¹t
		
		for(int i=0; i<n; i++) {
			Vector2d pos = new Vector2d(generate.nextInt(width+1),generate.nextInt(height+1));
			while(this.isOccupied(pos)) {
				pos = new Vector2d(generate.nextInt(width+1),generate.nextInt(height+1));
			}
			Gene gene = new Gene();
			Animal a = new Animal(startEnergy, pos, gene, this);
			this.place(a);
			
		}
		
		this.day=0;
		this.deaths=0;
	}
	
	void trackNewAnimal(Animal animal) {
		this.currentTrackAnimal=animal;
		this.currentKids=new ArrayList<>();
		this.currentScion=new ArrayList<>();
	}

    public boolean isOccupied(Vector2d position) {
        return this.animalsCoords.containsKey(position);
    }
    
    public Animal objectAt(Vector2d position, int n) {
        return animalsCoords.get(position).get(n);
    }
    
    public Animal maxObjectAt(Vector2d position) {
    	Animal result = animalsCoords.get(position).get(0);
    	int i=1;
    	while(i<animalsCoords.get(position).size()) {
    		if(result.getEnergy()<animalsCoords.get(position).get(i).getEnergy()) {
    			result = animalsCoords.get(position).get(i);
    		}
    		i++;
    	}
        return result;
    }

    public boolean place(Animal animal) {

    	List<Animal> l = this.animalsCoords.get(animal.getPosition());
    	if (l==null) {
    		l = new ArrayList<Animal>();
    		this.animalsCoords.put(animal.getPosition(), l);
    		this.coordsList.add(animal.getPosition());
    	}
    	l.add(animal);
        animal.addObserver(this);
        this.numberOfAnimals++;
        Integer x = this.genesCount.get(animal.getGene());
        if(x==null) x=0;
        x++;
        this.genesCount.remove(animal.getGene());
        this.genesCount.put(animal.getGene(), x);
        this.avgEnergy=((double)this.avgEnergy*((double)this.numberOfAnimals-1.0)+(double)animal.getEnergy())/(double)this.numberOfAnimals;
        this.animals.add(animal);
        return true;
    }

    public void positionChanged(Vector2d oldPosition, Vector2d newPosition, Animal a) {
    	List<Animal> l = this.animalsCoords.get(oldPosition);
    	Animal g = l.get(0);
    	int i=0;
    	while(g!=a) {
    		i++;
    		g=l.get(i);
    	}
    	l.remove(a);
    	if(l.isEmpty()) {
    		this.coordsList.remove(oldPosition);
    		this.animalsCoords.remove(oldPosition);
    	}
       
        List <Animal> m = this.animalsCoords.get(newPosition);
    	if (m==null) {
    		m = new ArrayList<Animal>();
    		this.animalsCoords.put(newPosition, m);
    		this.coordsList.add(newPosition);
    	}
    	m.add(a);
    }
    
    public void deleteGrass(Vector2d oldPos)
    {
    	this.grassList.remove(oldPos);
    }
    
    public void deleteAnimal(Animal animal) {
    	List<Animal> l = this.animalsCoords.get(animal.getPosition());
    	Animal g = l.get(0);
    	int i=0;
    	while(g!=animal) {
    		i++;
    		g=l.get(i);
    	}
    	l.remove(g);
    	if(l.isEmpty()) {
    		this.coordsList.remove(animal.getPosition());
    		this.animalsCoords.remove(animal.getPosition());
    	}
    	this.numberOfAnimals--;
    	Integer x = this.genesCount.get(animal.getGene());
    	this.genesCount.remove(animal.getGene());
        x--;
        this.genesCount.put(animal.getGene(), x);
        this.deaths++;
        this.avgLifespan=(this.avgLifespan+(this.day-animal.getBirthDay()))/this.deaths;
        this.avgChildren=(double)(this.avgChildren*(this.numberOfAnimals+1)-(double)animal.getChildrenNumber())/(double)this.numberOfAnimals;
        this.avgEnergy=((double)this.avgEnergy*((double)this.numberOfAnimals+1.0)-(double)animal.getEnergy())/(double)this.numberOfAnimals;
        this.animals.remove(animal);
    }

    public void generateGrass(boolean jungle)
    {
        Vector2d grassPoss;
        int x,y;
        int i=0; //temp
        do {
        	i++;
        	if(jungle==false) {
        		int j=0; //temp
        		do {
        			x = generate.nextInt(this.width+1);
        			y = generate.nextInt(this.height+1);
        			grassPoss=new Vector2d(x,y);
        			j++;
        		} while(j<width*height /*temp*/ && this.belongsToJungle(grassPoss));
        	}
        	else {
        		x=generate.nextInt(this.jungleSize+1);
        		y=generate.nextInt(this.jungleSize+1);
        		x+=this.jungleLowerLeft.getX();
        		y+=this.jungleLowerLeft.getY();
        		grassPoss=new Vector2d(x,y);
        	}
        }while (i<width*height /*temp*/ && isOccupied(grassPoss));
    	this.grassList.add(grassPoss);
    }
    
    boolean belongsToJungle(Vector2d vec) {
    	if(vec.getX()>=this.jungleLowerLeft.getX() 
    			&& vec.getX()<=this.jungleUpperRight.getX() 
    			&& vec.getY()>=this.jungleLowerLeft.getY() 
    			&& vec.getY()<=this.jungleUpperRight.getY()) return true;
    	return false;
    }
    
    boolean isGrass(Vector2d pos) {
        
        if(this.grassList.contains(pos)) return true;
        return false;
    }
    
    List<Animal> maxEnergy(Vector2d pos){
    	List<Animal> l = new ArrayList<>();
    	Animal a = getMax(pos);
    	l.add(a);
    	List<Animal> e = this.animalsCoords.get(pos);
    	for(int i=0; i<e.size(); i++) {
    		if(e.get(i).getEnergy()==a.getEnergy() && e.get(i)!=a) {
    			l.add((Animal) e.get(i));
    		}
    	}
    	return l;
    }
    
    Animal getMax(Vector2d pos) {
    	List<Animal> e = this.animalsCoords.get(pos);
    	if(e==null) return null;
    	Animal max = null;
    	for(int i=0; i<e.size(); i++) {
    		if(max==null || e.get(i).getEnergy()>max.getEnergy()) {
    			max=(Animal)e.get(i);
    		}
    	}
    	return max;
    }
    
    Animal get2nd(Vector2d pos, Animal max) {
    	if(max==null) return null;
    	List<Animal> e = this.animalsCoords.get(pos);
    	Animal second = null;
    	for(int i=0; i<e.size(); i++) {
    		if(e.get(i)!=max && (second==null || e.get(i).getEnergy()>second.getEnergy())) {
    			second=e.get(i);
    		}
    	}
    	return second;
    	
    }
    
    boolean belongs(Vector2d vec) {
    	if(vec.getX()>=0 && vec.getX()<=this.width && vec.getY()>=0 && vec.getY()<=this.height) return true;
    	return false;
    }
    
    int getCurrentKidsNumber() {
    	return this.currentKids.size();
    }
    
    int getCurrentScionNumber() {
    	return this.currentScion.size();
    }
    
    public void simulateDay() {
    	this.day++;
    	generateGrass(false);
    	generateGrass(true);
    	//obrót i ruch
    	for(int i=0; i<animals.size();i++) {
    		animals.get(i).move();
    		this.avgEnergy=((double)this.avgEnergy*(double)this.numberOfAnimals-(double)this.moveEnergy)/(double)this.numberOfAnimals;
    	}
    	//JEDZENIE
    	for(int i=0; i<coordsList.size(); i++) {
    		if(isGrass(coordsList.get(i))) {
    			List<Animal> maxAnimal = maxEnergy(coordsList.get(i));
    			for(int j=0; j<maxAnimal.size(); j++) maxAnimal.get(j).addEnergy(plantEnergy/maxAnimal.size());
    			this.deleteGrass(coordsList.get(i));
    			this.avgEnergy=((double)this.avgEnergy*(double)this.numberOfAnimals+(double)this.plantEnergy)/(double)this.numberOfAnimals;
    		}
    	}
    	//rozmnoz
    	for(int i=0; i<coordsList.size(); i++) {
    		Animal a = getMax(coordsList.get(i));
    		Animal b = get2nd(coordsList.get(i), a);
    		if(b!=null && 10*b.getEnergy()>=5*this.startEnergy) {
    			Reproduction r = new Reproduction(a,b,this);
    			Animal child = r.reproduce();
    			this.avgChildren=(double)(this.avgChildren*this.numberOfAnimals+2)/(double)this.numberOfAnimals;
        		a.addChild();
        		b.addChild();
        		this.avgEnergy=((double)this.avgEnergy*(double)this.numberOfAnimals-(double)a.getEnergy()/4.0)/(double)this.numberOfAnimals;
        		this.avgEnergy=((double)this.avgEnergy*(double)this.numberOfAnimals-(double)b.getEnergy()/4.0)/(double)this.numberOfAnimals;
        		this.place(child);
        		if(this.currentTrackAnimal!=null) {
	        		if(this.currentTrackAnimal==a || this.currentTrackAnimal==b) {
	        			this.currentKids.add(child);
	        			this.currentScion.add(child);
	        		}
	        		else if(this.currentScion.contains(a) || this.currentScion.contains(b)) {
	        			this.currentScion.add(child);
	        		}
        		}
    		}
    		
    		
    	}
    	//usun martwe
    	for(int i=0; i<coordsList.size(); i++) {
    		List<Animal> l = this.animalsCoords.get(coordsList.get(i));
    		for(int j=0; j<l.size(); j++) {
    			if(l.get(j) instanceof Animal && ((Animal) l.get(j)).getEnergy()<=0) {
    				this.deleteAnimal((Animal) l.get(j));
    			}
    		}
    	}
    	this.day++;
    }
    
    boolean animalExists(Animal animal) {
    	List<Animal> list = animalsCoords.get(animal.getPosition());
    	if(list!=null && list.contains(animal)) return true;
    	System.out.println("N");
    	return false;
    }
    
    Vector2d otherside(Vector2d pos) {
    	int x=pos.getX();
    	int y=pos.getY();
    	if(x<0) x+=(this.width+1);
    	if(y<0) y+=(this.height+1);
    	if(y>this.height) y-=(this.height+1);
    	if(x>this.width) x-=(this.width+1);
    	return new Vector2d(x,y);
    }
    
    List<Vector2d> getAnimalsCoords(){
    	return this.coordsList;
    }
    
    List<Vector2d> getGrassCoords(){
    	return this.grassList;
    }

    int getStartEnergy() {
    	return this.startEnergy;
    }
    
    public int getWidth() {
    	return this.width;
    }
    
    public int getHeight() {
    	return this.height;
    }
    
    public int getAnimalsNumber() {
    	return this.numberOfAnimals;
    }
    
    public int getGrassNumber() {
    	return this.grassList.size();
    }
    
    public int getDay() {
    	return this.day;
    }
    
    public double getAvgLifespan() { 
    	return this.avgLifespan;
    }
    
    public double getAvgChildren() {
    	return this.avgChildren;
    }
    
    public double getAvgEnergy() {
    	return this.avgEnergy;
    }
    
    public Gene getDominantGene() {
    	int max = -1;
    	Gene result = null;
    	for(int i=0; i<coordsList.size(); i++) {
    		List<Animal> l = animalsCoords.get(coordsList.get(i));
    		for(int j=0; j<l.size(); j++) {
    			if(genesCount.get(l.get(j).getGene())>max) {
    				max=genesCount.get(l.get(j).getGene());
    				result=l.get(j).getGene();
    			}
    		}
    	}
    	return result;
    	
    }
    
    public boolean isDominantGeneAt(Vector2d pos) {
    	List<Animal> list = animalsCoords.get(pos);
    	Gene dominantGene = this.getDominantGene();
    	for(int i=0; i<list.size(); i++) {
    		if(list.get(i).getGene().equals(dominantGene) ) return true;
    	}
    	return false;
    }
    
    
}
