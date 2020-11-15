package project1;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Application {

	public static void main(String[] args) {
		Automaton a = buildOperandAutomaton('a');
		print2D(a.getTransitions());
		System.out.println("==========");
		a.toTable();
		print2D(a.getTable());
		
	}
    static final int EPSILON = 1;

	private static Automaton buildStarAutomaton(Automaton a1) {

		Integer[][] transitions1 = a1.getTransitions();
		int len1 = transitions1.length;
		int len = len1 + 2;

		Integer[][] transitions = new Integer[len][len];

		for (int i = 0; i < len1; i++) {
			for (int k = 0; k < len1; k++) {
				transitions[i + 1][k + 1] = transitions1[i][k];
			}
		}

		transitions[0][len - 1] = EPSILON;
		transitions[0][1] = EPSILON;
		transitions[len1][1] = EPSILON;
		transitions[len1][len - 1] = EPSILON;
		
		ArrayList<Integer> end = new ArrayList<Integer>();
		end.add(len - 1);
		Automaton a = new Automaton(transitions, 0, end);
		return a;
	}

	private static Automaton buildConcatAutomaton(Automaton a1, Automaton a2) {

		Integer[][] transitions1 = a1.getTransitions();
		Integer[][] transitions2 = a2.getTransitions();
		int len1 = transitions1.length;
		int len2 = transitions2.length;
		int len = len1 + len2;

		Integer[][] transitions = new Integer[len][len];

		for (int i = 0; i < len1; i++) {
			for (int k = 0; k < len1; k++) {
				transitions[i][k] = transitions1[i][k];
			}
		}
		for (int i = 0; i < len2; i++) {
			for (int k = 0; k < len2; k++) {
				transitions[i + len1][k + len1] = transitions2[i][k];
			}
		}

		transitions[len1 - 1][len1] = EPSILON;
		ArrayList<Integer> end = new ArrayList<Integer>();
		end.add(len - 1);
		Automaton a = new Automaton(transitions, 0, end);
		return a;
	}

	private static Automaton buildAlternateAutomaton(Automaton a1, Automaton a2) {

		Integer[][] transitions1 = a1.getTransitions();
		Integer[][] transitions2 = a2.getTransitions();
		int len1 = transitions1.length;
		int len2 = transitions2.length;
		int len = len1 + len2;

		Integer[][] transitions = new Integer[len + 2][len + 2];

		transitions[0][1] = EPSILON;
		transitions[0][len1 + 1] = EPSILON;

		for (int i = 0; i < len1; i++) {
			for (int k = 0; k < len1; k++) {
				transitions[i + 1][k + 1] = transitions1[i][k];
			}
		}
		for (int i = 0; i < len2; i++) {
			for (int k = 0; k < len2; k++) {
				transitions[i + 1 + len1][k + 1 + len1] = transitions2[i][k];
			}
		}

		transitions[len1][len + 1] = EPSILON;
		transitions[len][len + 1] = EPSILON;
		ArrayList<Integer> end = new ArrayList<Integer>();
		end.add(len + 1);
		Automaton a = new Automaton(transitions, 0, end);
		return a;
	}

	private static Automaton buildOperandAutomaton(int c) {
		Integer[][] transitions = { { null, c }, { null, null } };
		ArrayList<Integer> end = new ArrayList<Integer>();
		end.add(1);
		Automaton automaton = new Automaton(transitions, 0, end);
		return automaton;
	}

	public static void print2D(Integer mat[][]) {
		// Loop through all rows
		for (int i = 0; i < mat.length; i++) {
			// Loop through all elements of current row
			for (int j = 0; j < mat[i].length; j++)
				System.out.print(mat[i][j] + " ");
			System.out.println("");
		}

	}
	public static void print2D(ArrayList<Integer>[][] mat) {
		// Loop through all rows
		for (int i = 0; i < mat.length; i++) {
			// Loop through all elements of current row
			for (int j = 0; j < mat[i].length; j++)
				System.out.print(mat[i][j] + " ");
			System.out.println("");
		}

	}

}
