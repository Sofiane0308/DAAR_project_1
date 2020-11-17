package project1;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Scanner;

import org.junit.jupiter.api.*;

class Tests {
	
	private static int actualScore = 0;
	private static int maxScore = 0;

	@AfterAll
	static void tearDownAfterAll() throws Exception {
		System.out.println("Final score: " + Integer.toString(actualScore) + " / " + Integer.toString(maxScore));
	}
	//Test a regex of words that exist in the babylon text
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
			ArrayList<MatchResponse> response = a.search("books/babylon.txt");
			assertEquals(30, response.size());
		} catch (Exception e) {
			fail();
		}
		actualScore += 10;
	}
	//Test a regex of a word that does not exist in the babylon text
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
			ArrayList<MatchResponse> response = a.search("books/babylon.txt");
			assertEquals(0, response.size());
		} catch (Exception e) {
			fail();
		}
		actualScore += 10;
	}
	//Test a regex of a word that exists in the babylon text in lower case but not in upper case
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
			ArrayList<MatchResponse> response = a.search("books/babylon.txt");
			assertEquals(0, response.size());
		} catch (Exception e) {
			fail();
		}
		actualScore += 10;
	}
	//Test a regex without operands of a word that exists in the babylon text
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
			ArrayList<MatchResponse> response = a.search("books/babylon.txt");
		} catch (Exception e) {
			fail();
		}
		actualScore += 10;
	}
	//Test end states after minimisation of the regex "a|bc*"
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
			fail();
		}
		actualScore += 10;
	}
	//Test a regex with illegal characters
	@Test
	void runRegEx_IllegalCharacters() throws Exception {
		maxScore += 10;
		RegEx.regEx = "|*";
		try {
			RegExTree ret = RegEx.parse();
			Automaton a = RegEx.RegExTreeToAutomaton(ret);
			a.toTable();
			a.eliminateEpsilonTransitions();
			a.minimize();
			fail();
		} catch (Exception e) {
			actualScore += 10;
		}
	}
}
