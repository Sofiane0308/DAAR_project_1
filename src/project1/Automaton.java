package project1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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

	//Create new end states after epsilon elimination
	public ArrayList<Integer> getNewEndStates() {
		
			ArrayList<Integer> new_final_states = new ArrayList<Integer>();
			for (int i = 1 ; i < table.length ; i ++) {
				ArrayList<Integer> cell = table[i][0]; 
				for(int j = 0 ; j < cell.size() ; j++) {
					if(end.contains(cell.get(j))) {
						new_final_states.add(i-1);
						break;
					}
				}
			}
			return new_final_states;
			
		}

		//Eliminate the epsilon transitions
		public void eliminateEpsilonTransitions() {
			
			for(int i = 1 ; i < table.length ; i++) {
				int k = 0;
				while(k < table[i][0].size()) {
					Integer subState = table[i][0].get(k);
					ArrayList<Integer> subClosure = table[subState+1][1];
					if (subClosure != null) {
						table[i][0].addAll(subClosure);
					}
					for(int j = 2 ; j < table[0].length ; j ++) {
						ArrayList<Integer> newTransition = table[subState + 1][j];
						if(newTransition != null) table[i][j] = newTransition;
					}
					k++;
				}
			}
			for(int i = 1 ; i < table.length ; i++) {
				table[i][1] = null;
			}
			setEnd(getNewEndStates());
			
		}

	public Automaton(Integer[][] transitions, int start, ArrayList<Integer> end) {
		this.start = start;
		this.end = end;
		this.current = start;
		this.transitions = transitions;
	}

	// Create a cell for the table
	ArrayList<Integer> createCell(Integer i) {
		ArrayList<Integer> list = new ArrayList<Integer>();
		list.add(i);
		return list;
	}

	// Convert automaton to table
	void toTable() {
		// collect character list
		Set<Integer> set = new HashSet<Integer>();
		for (int i = 0; i < transitions.length; i++) {
			for (int j = 0; j < transitions[0].length; j++) {
				Integer cell = transitions[i][j];
				if (cell != null && cell != EPSILON)
					set.add(cell);
			}
		}
		int nbCols = set.size() + 2;
		int nbRows = transitions.length + 1;

		ArrayList<Integer>[][] table = new ArrayList[nbRows][nbCols];
		// build character row
		table[0][1] = createCell(EPSILON);
		Iterator<Integer> it = set.iterator();
		int k = 2;
		while (it.hasNext()) {
			table[0][k] = createCell(it.next());
			k++;
		}
		// fill the table
		for (int i = 0; i < transitions.length; i++) {
			table[i + 1][0] = createCell(i);
			for (int j = 0; j < transitions[0].length; j++) {
				Integer c = transitions[i][j];
				if (c != null) {
					// get char col number
					k = 1;
					boolean found = false;
					while (!found && k < table[0].length) {
						if (table[0][k].get(0) == c)
							found = true;
						k++;
					}
					k--;
					if (table[i + 1][k] == null)
						table[i + 1][k] = createCell(j);
					else
						table[i + 1][k].add(j);

				}

			}
		}
		setTable(table);
	}

	// Construct the n_equivalence for an automaton
	ArrayList<ArrayList<Integer>> n_equivalence() {

		ArrayList<ArrayList<Integer>> next_equivalence = null;
		ArrayList<ArrayList<Integer>> current_equivalence = new ArrayList<ArrayList<Integer>>();
		// first equivalence list
		ArrayList<Integer> list = new ArrayList<Integer>();
		for (int i = 1; i < table.length; i++) {
			int state = table[i][0].get(0);
			if ((!end.contains(state)) && (!is_sink(state)))
				list.add(state);
		}
		current_equivalence.add(list);
		current_equivalence.add(new ArrayList<Integer>(end));
		while (!current_equivalence.equals(next_equivalence)) {
			//System.out.println("N_equivalence : "+current_equivalence);
			if (next_equivalence != null)
				current_equivalence = next_equivalence;
			next_equivalence = new ArrayList<ArrayList<Integer>>();
			// For each set of the current equivalence
			for (int i = 0; i < current_equivalence.size(); i++) {
				ArrayList<Integer> set = current_equivalence.get(i);
				// for each state of the set
				// if the set contains only one state
				if (set.size() == 1)
					next_equivalence.add(set);
				else {
					// contains more than one state
					// begin with the first state
					Integer state = set.get(0);
					next_equivalence.add(new ArrayList<Integer>(Arrays.asList(state)));
					// check the other states equivalence
					for (int j = 1; j < set.size(); j++) {
						int k = 0;
						boolean found_equivalent = false;
						state = set.get(j);
						// try the previous states
						while (k < j && !found_equivalent) {
							Integer state_to_compare_with = set.get(k);
							if (are_equivalent(state, state_to_compare_with, current_equivalence)) {
								found_equivalent = true;
								// insert in next
								for (int p = 0; p < next_equivalence.size(); p++) {
									ArrayList<Integer> tmp_set = next_equivalence.get(p);
									if (tmp_set.contains(state_to_compare_with)) {
										tmp_set.add(state);
										break;
									}
								}
							}
							k++;
						}
						// no equivalent states => insert in a new set
						if (!found_equivalent)
							next_equivalence.add(new ArrayList<Integer>(Arrays.asList(state)));
					}

				}
			}

		}

		return next_equivalence;

	}

	private boolean is_sink(int state) {
		if(state == start) return false;
		for (int i = 1; i < table.length; i++) {
			for (int j = 2 ; j < table[0].length ; j++) {
				ArrayList<Integer> cell = table[i][j];
				if((cell != null) && (cell.contains(state))) return false;
			}

		}
		return true;
	}

	// check if 2 states are equivalent within an equivalence
	private boolean are_equivalent(Integer state1, Integer state2, ArrayList<ArrayList<Integer>> equivalence) {

		for (int i = 2; i < table[0].length; i++) {
			ArrayList<Integer> cell1 = table[state1 + 1][i];
			ArrayList<Integer> cell2 = table[state2 + 1][i];
			if (cell1 != null && cell2 != null) {
				Integer next_state1 = cell1.get(0);
				Integer next_state2 = cell2.get(0);
				if (!are_in_the_same_set(next_state1, next_state2, equivalence))
					return false;
			} else if ((cell1 == null && cell2 != null) || (cell1 != null && cell2 == null))
				return false;

		}
		return true;
	}

	// check if 2 states are in the same set of an equivalence
	private boolean are_in_the_same_set(Integer state1, Integer state2, ArrayList<ArrayList<Integer>> equivalence) {
		for (int i = 0; i < equivalence.size(); i++) {
			ArrayList<Integer> set = equivalence.get(i);
			if (set.contains(state1) && set.contains(state2))
				return true;
			else if ((set.contains(state1) && !set.contains(state2)) || (!set.contains(state1) && set.contains(state2)))
				return false;
		}
		return false;
	}

	// minimize
	void minimize() {
		ArrayList<ArrayList<Integer>> n_equivalence = n_equivalence();
		ArrayList<Integer> new_final_states = new ArrayList<Integer>();
		ArrayList<Integer>[][] minimized_table = new ArrayList[n_equivalence.size() + 1][table[0].length];
		// first row
		for (int i = 0; i < table[0].length; i++)
			minimized_table[0][i] = table[0][i];
		// for each set aka new state
		for (int i = 0; i < n_equivalence.size(); i++) {
			ArrayList<Integer> set = n_equivalence.get(i);
			minimized_table[i + 1][0] = new ArrayList(Arrays.asList(i));
			for (int j = 0; j < set.size(); j++) {
				Integer state = set.get(j);
				if ((end.contains(state)) && (!new_final_states.contains(i)))
					new_final_states.add(i);
				for (int k = 2; k < table[0].length; k++) {
					ArrayList<Integer> cell = table[state + 1][k];
					if (cell != null) {
						Integer old_state = cell.get(0);
						minimized_table[i + 1][k] = get_new_state(old_state, n_equivalence);
					}

				}
			}
		}
		setEnd(new_final_states);
		setTable(minimized_table);
	}

	private ArrayList<Integer> get_new_state(Integer old_state, ArrayList<ArrayList<Integer>> n_equivalence) {
		for (int i = 0; i < n_equivalence.size(); i++) {
			ArrayList<Integer> set = n_equivalence.get(i);
			if (set.contains(old_state))
				return new ArrayList(Arrays.asList(i));
		}
		return null;
	}

	ArrayList<MatchResponse> search(String path) throws IOException {
		// Initialisation
		ArrayList<MatchResponse> response = new ArrayList<MatchResponse>();
		BufferedReader in = new BufferedReader(new FileReader(path));
		String str;
		int line = 1;
		int startOfWord = 0;
		// Parcours du texte ligne par ligne
		while ((str = in.readLine()) != null) {
			// Line to char array
			char[] list = str.toCharArray();
			int current = start;
			for (int i = 0; i < list.length; i++) {
				Integer next = accept((int) list[i], current);
				if (next != null) {
					// step forward
					if (current == start) // eventual begin of a match
						startOfWord = i;
					current = next;

				} else {
					// mismatch
					if (end.contains(current)) // acceptable state
						response.add(new MatchResponse(line, startOfWord + 1, str.substring(startOfWord, i)));

					current = start;
				}

			}
			// match in the end of a line
			if (end.contains(current))
				response.add(new MatchResponse(line, startOfWord + 1, str.substring(startOfWord, list.length)));
			line++;
		}

		return response;

	}

	Integer accept(int c, int current) {
		for (int i = 2; i < table[0].length; i++) {
			if (table[0][i].get(0) == c) {
				ArrayList<Integer> cell = table[current + 1][i];
				if (cell != null)
					return cell.get(0);
			}

		}
		return null;
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
		if (this.table == null || this.table.length == 0 || this.table[0].length == 0)
			return "The table representation of the automaton is empty.";
		String result = "|" + this.table[0][0].toString();
		for (int j = 1; j < this.table[0].length; j++) {
			result += "|" + this.table[0][j].toString();
		}
		result += "| \n";

		for (int i = 1; i < this.table.length; i++) {
			for (int j = 0; j < this.table[i].length; j++) {
				result += "|" + this.table[i][j].toString();
			}
			result += "| \n";
		}
		return result + "|";
	}
}