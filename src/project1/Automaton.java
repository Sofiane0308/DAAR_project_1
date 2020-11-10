package project1;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Automaton {
    private int start;
    private int end;
    private int current;
    private Integer[][] transitions;
    
    
    static final int EPSILON = 1;

    public Automaton(Integer[][] transitions, int start, int end) {
        this.start = start;
        this.end = end;
        this.current = start;
        this.transitions = transitions; 
    }
    //Create a cell for the table
    ArrayList<Integer> createCell(Integer i ){
    	ArrayList<Integer> list = new ArrayList<Integer>();
    	list.add(i);
    	return list;
    }
    //Convert automaton to table
    ArrayList<Integer>[][] toTable(){
    	//collect character list
    	Set<Integer> set =  new HashSet<Integer>() ;
    	for(int i =0; i< transitions.length; i++) {
    		for (int j = 0; j<transitions[0].length; j++) {
    			Integer cell = transitions[i][j];
    			if(cell != null && cell != EPSILON) set.add(cell);
    		}
    	}
    	int nbCols = set.size() + 2;
    	int nbRows = transitions.length + 1; 
    	
    	ArrayList<Integer>[][] table = new ArrayList[nbRows][nbCols];
    	//build character row
    	table[0][1] = createCell(EPSILON);
    	Iterator<Integer> it = set.iterator();
    	int k = 2;
    	while(it.hasNext()) {
    		table[0][k] = createCell(it.next());
    		k++;
    	}
    	//fill the table
    	for(int i=0; i<transitions.length;i++) {
    		table[i+1][0] = createCell(i);
    		for(int j = 0; j<transitions[0].length; j++) {
    			Integer c = transitions[i][j];
    			if(c != null) {
    				//get char col number
        			k = 1;
        			boolean found = false;
        			while(!found && k < table[0].length) {
        				if (table[0][k].get(0)== c) found = true;
        				k++;
        			}
        			k--;
        			if(table[i+1][k] == null) table[i+1][k] = createCell(j);
        			else table[i+1][k].add(j);
        			
    			}
    			
    		}
    	}
    	return table;
    }
    
    public Integer[][] getTransitions() {
        return transitions;
    }

    public void setTransitions(Integer[][] transitions) {
        this.transitions = transitions;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }
}