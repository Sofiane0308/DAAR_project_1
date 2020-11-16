package project1;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Scanner;

import org.junit.jupiter.api.*;

class Tests {
	
	private static int actualScore = 0;
	private static int maxScore = 0;
	
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
	//private static String regEx;
	
	@AfterAll
	static void tearDownAfterAll() throws Exception {
		System.out.println("Final score: " + Integer.toString(actualScore) + " / " + Integer.toString(maxScore));
	}
	//Test a regex of a word that exists in the text
	@Test
	void runRegExFoundText_LegalCase() throws Exception {
		maxScore += 10;
		RegEx.regEx = "S(a|r|g)*on";
		try {
			RegExTree ret = RegEx.parse();
			Automaton a = RegEx.RegExTreeToAutomaton(ret);
			a.toTable();
			a.eliminateEpsilonTransitions();
			a.minimize();
			ArrayList<MatchResponse> response = a.search("text.txt");
			assertEquals(30, response.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
		actualScore += 10;
	}
	//Test a regex of a word that does not exist in the text
	@Test
	void runRegExUnfoundText_LegalCase() throws Exception {
		maxScore += 10;
		RegEx.regEx = "Introuvable";
		try {
			RegExTree ret = RegEx.parse();
			Automaton a = RegEx.RegExTreeToAutomaton(ret);
			a.toTable();
			a.eliminateEpsilonTransitions();
			a.minimize();
			ArrayList<MatchResponse> response = a.search("text.txt");
			assertEquals(0, response.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
		actualScore += 10;
	}
	//Test a regex of a word that exists in the text in lower case but not in upper case
	@Test
	void runRegExUpperCaseText_LegalCase() throws Exception {
		maxScore += 10;
		RegEx.regEx = "SARGON";
		try {
			RegExTree ret = RegEx.parse();
			Automaton a = RegEx.RegExTreeToAutomaton(ret);
			a.toTable();
			a.eliminateEpsilonTransitions();
			a.minimize();
			ArrayList<MatchResponse> response = a.search("text.txt");
			assertEquals(0, response.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
		actualScore += 10;
	}
	//Test a regex without operands
	@Test
	void runRegExNoOperands_LegalCase() throws Exception {
		maxScore += 10;
		RegEx.regEx = "Sargon";
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
		actualScore += 10;
	}
	//Test end states after minimisation
	@Test
	void getEndTransitionsAfterMinimisation_LegalCase() throws Exception {
		maxScore += 10;
		RegEx.regEx = "a|bc*";
		try {
			RegExTree ret = RegEx.parse();
			Automaton a = RegEx.RegExTreeToAutomaton(ret);
			a.toTable();
			a.eliminateEpsilonTransitions();
			a.minimize();
			ArrayList<Integer> endStates = new ArrayList<Integer>();
			endStates.add(1);
			endStates.add(2);
			assertEquals(endStates, a.getEnd());
		} catch (Exception e) {
			e.printStackTrace();
		}
		actualScore += 10;
	}
}
