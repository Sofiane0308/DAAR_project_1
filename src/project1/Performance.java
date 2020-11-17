package project1;

import java.util.ArrayList;

public class Performance {

	public static void main(String[] args) {
		RegEx.regEx = "S(a|r|g)*on";
		try {
			RegExTree ret = RegEx.parse();
			Automaton a = RegEx.RegExTreeToAutomaton(ret);
			a.toTable();
			a.eliminateEpsilonTransitions();
			a.minimize();
			ArrayList<MatchResponse> response = a.search("text.txt");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
