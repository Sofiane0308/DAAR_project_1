package project1;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.List;

public class Automaton {
    private int start;
    private ArrayList<Integer> end;
    private int current;
    private Integer[][] transitions;
	private ArrayList<Integer>[][] table;
    static final int EPSILON = 1;
		
    //Create new states by going through epsilon by epsilon
	private ArrayList<Integer> createNewStatesWithoutEpsilonTransitions(int state, ArrayList<Integer> states,
			ArrayList<Integer>[][] table) {
		if(!states.contains(state)) {
			states.add(state);
		}
		//check if the current state has an epsilon transition
		if (table[state + 1][1] != null) {
			for (int nextState: table[state + 1][1]) {
				if (!states.contains(nextState)) {
					states.add(nextState);
				}
				//check recursively for the following epsilon transitions
				if (table[nextState + 1][1] != null) {
					states = createNewStatesWithoutEpsilonTransitions(nextState, states, table);
					//table[nextState + 1][1] = null;
				}
			}
		}
		return states;
	}
	//Remove letter transitions for non closure transitions
	public ArrayList<Integer>[][] cleanTable(ArrayList<Integer>[][] table) {
		ArrayList<Integer>[][] newTable = table;
		for (int i=1;i<table.length;i++) {
			if(table[i][0].size() >= 2) {
				int length = table[i][0].size();
				List<Integer> temp = table[i][0].subList(1, length);
				for (int state: temp) {
					for (int j=2;j<table[0].length;j++) {
						if (table[state + 1][j] != null) {
							table[state + 1][j] = null;
							break;
						}
					}
				}
			}
		}
		return newTable;
	}
	//Create new end states after epsilon elimination
	public ArrayList<Integer> getNewEndStates(int state, ArrayList<Integer> end, ArrayList<Integer>[][] table) {
		for (int i=state+1;i<table.length;i++) {
			for (int j=2;j<table[0].length;j++) {
				if (table[i][j] != null) {
					//check if the current state has a letter transition
					boolean hasLetterTransition = false;
					for(int k=2;k<table[0].length;k++) {
						if (table[table[i][j].get(0) + 1][k] != null) {
							hasLetterTransition = true;
							break;
						}
					}
					//add end state
					if ((!hasLetterTransition || table[table[i][j].get(0) + 1][0].get(0) == table[i][j].get(0))) {
						if (!end.contains(table[i][j].get(0))) {
							end.add(table[i][j].get(0));
							break;
						}
					}
					//recursively check for end states
					else {
						end = getNewEndStates(table[i][j].get(0), end, table);
						break;
					}
				}
			}
		}
		return end;
	}
	//Eliminate the epsilon transitions
	public void eliminateEpsilonTransitions() {
		ArrayList<Integer>[][] table = this.table;
		for (int i=1;i<table.length;i++) {
			//state with epsilon transitions
			if (table[i][1] != null) {
				//state with forward epsilon transitions
				if (table[i][1].get(0) < table[i][0].get(0)) {
					for (int j=2;j<table[0].length;j++) {
						//state with a closure transition
						if (table[table[i][1].get(0) + 1][j] != null) {
							table[i][j] = table[table[i][1].get(0) + 1][j];
							table[table[i][1].get(0) + 1][j] = null;
							break;
						}
					}
				}
				//state with a backward epsilon transition
				else {
					ArrayList<Integer> states = new ArrayList<Integer>();
					table[i][0] = createNewStatesWithoutEpsilonTransitions(table[i][0].get(0), states, table);
				}
				//remove the epsilon transitions
				table[i][1] = null;
			}
			//state without epsilon transitions
			else {
				for (int j=2;j<table[0].length;j++) {
					if (table[i][j] != null) {
						for (int k=1;k<table.length;k++) {
							//add letter transition for merged epsilon transitions
							if (table[k][0].contains(table[i][0].get(0)) && table[k][j] == null) {
								table[k][j] = table[i][j];
								//table[i][j] = null;
								break;
							}
						}
						//table[i][j] = null;
						break;
					}
				}
			}
		}
		//remove letter transitions for non closure transitions
		table = cleanTable(table);
		setTable(table);
		ArrayList<Integer> end = new ArrayList<Integer>();
		end = getNewEndStates(0, end, table);
		setEnd(end);
	}
	
    public Automaton(Integer[][] transitions, int start, ArrayList<Integer> end) {
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
    void toTable(){
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
    	setTable(table);
    }
    
    public ArrayList<Integer>[][] getTable() {
		return this.table;
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
    
	private void setTable(ArrayList<Integer>[][] table) {
		this.table = table;
	}

    public ArrayList<Integer> getEnd() {
        return end;
    }

    public void setEnd(ArrayList<Integer> end) {
        this.end = end;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }
    
	public String toString() {
		if (this.table == null || this.table.length == 0 || this.table[0].length == 0) return "The table representation of the automaton is empty.";
	    String result = "|" + this.table[0][0].toString();
	    for (int j=1;j<this.table[0].length;j++) {
	    	result += "|"+ this.table[0][j].toString();
	    }
	    result += "| \n";
	    
	    for (int i=1;i<this.table.length;i++) {
	    	for (int j=0;j<this.table[i].length;j++) {
	    		result += "|" + this.table[i][j].toString();
	    	}
	    	result += "| \n";
	    }
	    return result +"|";
	}
}