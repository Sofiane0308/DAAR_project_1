package project1;

import java.util.Scanner;
import java.util.ArrayList;
import java.lang.Exception;

public class RegEx {
	// MACROS
	static final int CONCAT = 0xC04CA7;
	static final int ETOILE = 0xE7011E;
	static final int ALTERN = 0xA17E54;
	static final int PROTECTION = 92;
	static final int EPSILON = 1;

	static final int PARENTHESEOUVRANT = 0x16641664;
	static final int PARENTHESEFERMANT = 0x51515151;
	static final int DOT = 0xD07;

	// REGEX
	protected static String regEx;

	// CONSTRUCTOR
	public RegEx() {
	}

	// MAIN
	public static void main(String arg[]) {
		//System.out.println("Welcome to Bogota, Mr. Thomas Anderson.");
		String text_path = "";
		if (arg.length > 2) {
			regEx = arg[0];
			text_path = arg[1];
		} else {
			Scanner scanner = new Scanner(System.in);
			System.out.print("  >> Please enter a regEx: ");
			regEx = scanner.next();
			
			System.out.print("  >> Please enter a text file path: ");
			text_path = scanner.next();
		}
		/*System.out.println("  >> Parsing regEx \"" + regEx + "\".");
		System.out.println("  >> ...");*/

		if (regEx.length() < 1) {
			System.err.println("  >> ERROR: empty regEx.");
		} else {
			//System.out.print("  >> ASCII codes: [" + (int) regEx.charAt(0));
			/*for (int i = 1; i < regEx.length(); i++)
				System.out.print("," + (int) regEx.charAt(i));
			System.out.println("].");*/
			try {
				RegExTree ret = parse();
				//System.out.println("  >> Tree result: " + ret.toString() + ".");
				Automaton a = RegExTreeToAutomaton(ret);
				//System.out.println("=== Transition table ===");
				a.toTable();
				/*print2D(a.getTable());
				System.out.println("=== Final states ===");
				System.out.println(a.getEnd());
				System.out.println("=== Eliminating epsilon-transitions ===");*/
				a.eliminateEpsilonTransitions();
				/*print2D(a.getTable());
				System.out.println("=== Final states ===");
				System.out.println(a.getEnd());
				System.out.println("=== Minimized table ===");*/
				a.minimize();
				/*print2D(a.getTable());
				System.out.println("=== Final states ===");
				System.out.println(a.getEnd());*/
				ArrayList<MatchResponse> response = a.search(text_path);
				response.forEach((rep)-> System.out.println(rep.getLineString()));
				
			} catch (Exception e) {
				e.printStackTrace();
				System.err.println("  >> ERROR: syntax error for regEx \"" + regEx + "\".");
			}
		}

		/*System.out.println("  >> ...");
		System.out.println("  >> Parsing completed.");
		System.out.println("Goodbye Mr. Anderson.");*/

	}
	//Transform a regex tree to an automaton by recurcivly 1st son, 2nd son then root
	private static Automaton RegExTreeToAutomaton(RegExTree tree) throws Exception {
		if (tree.subTrees.isEmpty()) {
			return buildOperandAutomaton(tree.root);
		} else if (tree.root == RegEx.ETOILE) {
			return buildStarAutomaton(RegExTreeToAutomaton(tree.subTrees.get(0)));
		} else if (tree.root == RegEx.CONCAT) {
			return buildConcatAutomaton(RegExTreeToAutomaton(tree.subTrees.get(0)),
					RegExTreeToAutomaton(tree.subTrees.get(1)));
		} else if (tree.root == RegEx.ALTERN) {
			return buildAlternateAutomaton(RegExTreeToAutomaton(tree.subTrees.get(0)),
					RegExTreeToAutomaton(tree.subTrees.get(1)));
		} else {
			throw new Exception();
		}

	}
	//Build R* automaton
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
	//Build a R1.R2 automaton
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
	//Build R1|R2 automaton
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
	//Build a character automaton
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

	// FROM REGEX TO SYNTAX TREE
	protected static RegExTree parse() throws Exception {
		// BEGIN DEBUG: set conditionnal to true for debug example
		if (false)
			throw new Exception();
		RegExTree example = exampleAhoUllman();
		if (false)
			return example;
		// END DEBUG

		ArrayList<RegExTree> result = new ArrayList<RegExTree>();
		for (int i = 0; i < regEx.length(); i++)
			result.add(new RegExTree(charToRoot(regEx.charAt(i)), new ArrayList<RegExTree>()));

		return parse(result);
	}

	private static int charToRoot(char c) {
		if (c == '.')
			return DOT;
		if (c == '*')
			return ETOILE;
		if (c == '|')
			return ALTERN;
		if (c == '(')
			return PARENTHESEOUVRANT;
		if (c == ')')
			return PARENTHESEFERMANT;
		return (int) c;
	}

	private static RegExTree parse(ArrayList<RegExTree> result) throws Exception {
		while (containParenthese(result))
			result = processParenthese(result);
		while (containEtoile(result))
			result = processEtoile(result);
		while (containConcat(result))
			result = processConcat(result);
		while (containAltern(result))
			result = processAltern(result);

		if (result.size() > 1)
			throw new Exception();

		return removeProtection(result.get(0));
	}

	private static boolean containParenthese(ArrayList<RegExTree> trees) {
		for (RegExTree t : trees)
			if (t.root == PARENTHESEFERMANT || t.root == PARENTHESEOUVRANT)
				return true;
		return false;
	}

	private static ArrayList<RegExTree> processParenthese(ArrayList<RegExTree> trees) throws Exception {
		ArrayList<RegExTree> result = new ArrayList<RegExTree>();
		boolean found = false;
		for (RegExTree t : trees) {
			if (!found && t.root == PARENTHESEFERMANT) {
				boolean done = false;
				ArrayList<RegExTree> content = new ArrayList<RegExTree>();
				while (!done && !result.isEmpty())
					if (result.get(result.size() - 1).root == PARENTHESEOUVRANT) {
						done = true;
						result.remove(result.size() - 1);
					} else
						content.add(0, result.remove(result.size() - 1));
				if (!done)
					throw new Exception();
				found = true;
				ArrayList<RegExTree> subTrees = new ArrayList<RegExTree>();
				subTrees.add(parse(content));
				result.add(new RegExTree(PROTECTION, subTrees));
			} else {
				result.add(t);
			}
		}
		if (!found)
			throw new Exception();
		return result;
	}

	private static boolean containEtoile(ArrayList<RegExTree> trees) {
		for (RegExTree t : trees)
			if (t.root == ETOILE && t.subTrees.isEmpty())
				return true;
		return false;
	}

	private static ArrayList<RegExTree> processEtoile(ArrayList<RegExTree> trees) throws Exception {
		ArrayList<RegExTree> result = new ArrayList<RegExTree>();
		boolean found = false;
		for (RegExTree t : trees) {
			if (!found && t.root == ETOILE && t.subTrees.isEmpty()) {
				if (result.isEmpty())
					throw new Exception();
				found = true;
				RegExTree last = result.remove(result.size() - 1);
				ArrayList<RegExTree> subTrees = new ArrayList<RegExTree>();
				subTrees.add(last);
				result.add(new RegExTree(ETOILE, subTrees));
			} else {
				result.add(t);
			}
		}
		return result;
	}

	private static boolean containConcat(ArrayList<RegExTree> trees) {
		boolean firstFound = false;
		for (RegExTree t : trees) {
			if (!firstFound && t.root != ALTERN) {
				firstFound = true;
				continue;
			}
			if (firstFound)
				if (t.root != ALTERN)
					return true;
				else
					firstFound = false;
		}
		return false;
	}

	private static ArrayList<RegExTree> processConcat(ArrayList<RegExTree> trees) throws Exception {
		ArrayList<RegExTree> result = new ArrayList<RegExTree>();
		boolean found = false;
		boolean firstFound = false;
		for (RegExTree t : trees) {
			if (!found && !firstFound && t.root != ALTERN) {
				firstFound = true;
				result.add(t);
				continue;
			}
			if (!found && firstFound && t.root == ALTERN) {
				firstFound = false;
				result.add(t);
				continue;
			}
			if (!found && firstFound && t.root != ALTERN) {
				found = true;
				RegExTree last = result.remove(result.size() - 1);
				ArrayList<RegExTree> subTrees = new ArrayList<RegExTree>();
				subTrees.add(last);
				subTrees.add(t);
				result.add(new RegExTree(CONCAT, subTrees));
			} else {
				result.add(t);
			}
		}
		return result;
	}

	private static boolean containAltern(ArrayList<RegExTree> trees) {
		for (RegExTree t : trees)
			if (t.root == ALTERN && t.subTrees.isEmpty())
				return true;
		return false;
	}

	private static ArrayList<RegExTree> processAltern(ArrayList<RegExTree> trees) throws Exception {
		ArrayList<RegExTree> result = new ArrayList<RegExTree>();
		boolean found = false;
		RegExTree gauche = null;
		boolean done = false;
		for (RegExTree t : trees) {
			if (!found && t.root == ALTERN && t.subTrees.isEmpty()) {
				if (result.isEmpty())
					throw new Exception();
				found = true;
				gauche = result.remove(result.size() - 1);
				continue;
			}
			if (found && !done) {
				if (gauche == null)
					throw new Exception();
				done = true;
				ArrayList<RegExTree> subTrees = new ArrayList<RegExTree>();
				subTrees.add(gauche);
				subTrees.add(t);
				result.add(new RegExTree(ALTERN, subTrees));
			} else {
				result.add(t);
			}
		}
		return result;
	}

	private static RegExTree removeProtection(RegExTree tree) throws Exception {
		if (tree.root == PROTECTION && tree.subTrees.size() != 1)
			throw new Exception();
		if (tree.subTrees.isEmpty())
			return tree;
		if (tree.root == PROTECTION)
			return removeProtection(tree.subTrees.get(0));

		ArrayList<RegExTree> subTrees = new ArrayList<RegExTree>();
		for (RegExTree t : tree.subTrees)
			subTrees.add(removeProtection(t));
		return new RegExTree(tree.root, subTrees);
	}

	// EXAMPLE
	// --> RegEx from Aho-Ullman book Chap.10 Example 10.25
	private static RegExTree exampleAhoUllman() {
		RegExTree a = new RegExTree((int) 'a', new ArrayList<RegExTree>());
		RegExTree b = new RegExTree((int) 'b', new ArrayList<RegExTree>());
		RegExTree c = new RegExTree((int) 'c', new ArrayList<RegExTree>());
		ArrayList<RegExTree> subTrees = new ArrayList<RegExTree>();
		subTrees.add(c);
		RegExTree cEtoile = new RegExTree(ETOILE, subTrees);
		subTrees = new ArrayList<RegExTree>();
		subTrees.add(b);
		subTrees.add(cEtoile);
		RegExTree dotBCEtoile = new RegExTree(CONCAT, subTrees);
		subTrees = new ArrayList<RegExTree>();
		subTrees.add(a);
		subTrees.add(dotBCEtoile);
		return new RegExTree(ALTERN, subTrees);
	}
}

// UTILITARY CLASS
class RegExTree {
	protected int root;
	protected ArrayList<RegExTree> subTrees;

	public RegExTree(int root, ArrayList<RegExTree> subTrees) {
		this.root = root;
		this.subTrees = subTrees;
	}

	// FROM TREE TO PARENTHESIS
	public String toString() {
		if (subTrees.isEmpty())
			return rootToString();
		String result = rootToString() + "(" + subTrees.get(0).toString();
		for (int i = 1; i < subTrees.size(); i++)
			result += "," + subTrees.get(i).toString();
		return result + ")";
	}

	private String rootToString() {
		if (root == RegEx.CONCAT)
			return ".";
		if (root == RegEx.ETOILE)
			return "*";
		if (root == RegEx.ALTERN)
			return "|";
		if (root == RegEx.DOT)
			return ".";
		return Character.toString((char) root);
	}
}
