package main;

import java.util.Arrays;
import java.util.Random;

public class Gene {

	private String gene;
	
	private Random generate = new Random();
	
	public Gene() {
		this.gene=generateGene();
	}
	
	public Gene(String gene) {
		this.gene=gene;
	}
	
	String generateGene() {
		int left=8;
		int i=0;
		
		String result = "";
		
		while(left>0) {
			int n = generate.nextInt(32-left+1-i)+1;
			for(int j=0; j<n; j++) {
				char b = (char) (8-left+'0');
				result+=b;
				i++;
			}
			left--;
		}
		
		for(i=result.length(); i<32; i++) {
			result+='7';
		}
		return result;
		
		
	}
	
	String getGene() {
		return this.gene;
	}
	
	Gene childGene(Gene b) {
		//losowanie indeksow do ciêcia
		int c1 = generate.nextInt(31);
		int c2;
		do {c2 = generate.nextInt(31);}
		while(c1==c2);
		if (c2<c1) {
			int temp=c1;
			c1=c2;
			c2=temp;
		}
		String result="";
		//ciêcie
		String a1 = this.gene.substring(0, c1+1);
		String a2 = this.gene.substring(c1+1, c2+1);
		String a3 = this.gene.substring(c2+1, 32);
		String b1 = b.gene.substring(0, c1+1);
		String b2 = b.gene.substring(c1+1, c2+1);
		String b3 = b.gene.substring(c2+1, 32);
		//sklejanie
		boolean isa1 = generate.nextBoolean();
		if(isa1) result+=a1;
		else result+=b1;
		boolean isa2 = generate.nextBoolean();
		if(isa2) result+=a2;
		else result+=b2;
		if(isa2 && isa1) result+=b3;
		else if(!isa2 && !isa1) result+=a3;
		else {
			isa1=generate.nextBoolean();
			if(isa1) result+=a3;
			else result+=b3;
		}
		//dodac sprawdzanie czy sa wszystkie kierunki
		result = this.checkAndRepair(result);
		return new Gene(result);
	}
	
	public  String toString()
    {
        return this.gene;
    }
	
	 public boolean equals(Object other){
		 return(this.toString()==((Gene) other).toString());
	 }
	 
	 String checkAndRepair(String result) {
		 int[] tab = new int[32]; 
		 
	     for (int i = 0; i < 32; i++) { 
	        tab[i] = result.charAt(i) - '0'; 
	     } 
	  
	     Arrays.sort(tab);
	     
	     for(int i=1; i<32; i++) {
	    	 if(tab[i]!=tab[i-1] && tab[i-1]+1!=tab[i]) {
	    		 int n;
	    		 do {
	    			 n=generate.nextInt(31);
	    		 } while(tab[n]!=tab[n+1]);
	    		 tab[n]=tab[i-1]+1;
	    		 Arrays.sort(tab);
	    	 }
	     }
	     
	     String repaired = "";
	     for(int i=0; i<32; i++) {
	       	repaired+=(char)((int)tab[i]+'0');
	      }
	      return repaired;
	 }
}
