package project1;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;

class Tests {
	
	private static int actualScore = 0;
	private static int maxScore = 0;
	
	@AfterAll
	static void tearDownAfterAll() throws Exception {
		System.out.println("Final score: " + Integer.toString(actualScore) + " / " + Integer.toString(maxScore));
	}

	@Test
	void runRegExFoundText_LegalCase() throws Exception {
		fail("Not yet implemented");
	}
	@Test
	void runRegExUnfoundText_LegalCase() throws Exception {
		fail("Not yet implemented");
	}
	@Test
	void runRegExFoundLowerCaseText_LegalCase() throws Exception {
		fail("Not yet implemented");
	}

	@Test
	void getEndTransitionsAfterEpsilonElimination_LegalCase() throws Exception {
		fail("Not yet implemented");
	}
	@Test
	void getPriorities_LegalCase() throws Exception {
		fail("Not yet implemented");
	}
	@Test
	void runRegEx_IllegalText() throws Exception {
		fail("Not yet implemented");
	}
	@Test
	void runRegEx_IllegalOperands() throws Exception {
		fail("Not yet implemented");
	}
	@Test
	void runRegEx_NoText() throws Exception {
		fail("Not yet implemented");
	}
	@Test
	void runRegEx_NoOperands() throws Exception {
		fail("Not yet implemented");
	}
}
