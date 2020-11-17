package tests;

import project1.RegEx;
import project1.RegExTree;
import project1.Automaton;
import project1.MatchResponse;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class Performance {
	
	//Return the average of each book's durations after all repetitions
    static long average(ArrayList<Long> list, int repetition) {
    	long sum = 0;
    	for (int i=0;i<repetition;i++) {
    		sum += list.get(i);
    	}
    	return sum/repetition;
    }
	
	//Test performance of regex on 20 books
	public static void main(String[] args) {
        String[] pathnames;
        File file = new File("books");
        int repetition = 200;
        //durations of the regex for each book and for each repetition
        ArrayList<Long> durations;
        //durations of the regex for each book and for all repetitions
        ArrayList<Long> babylonBookDurations = new ArrayList<Long>();
        ArrayList<Long> book1Durations = new ArrayList<Long>();
        ArrayList<Long> book2Durations = new ArrayList<Long>();
        ArrayList<Long> book3Durations = new ArrayList<Long>();
        ArrayList<Long> book4Durations = new ArrayList<Long>();
        ArrayList<Long> book5Durations = new ArrayList<Long>();
        ArrayList<Long> book6Durations = new ArrayList<Long>();
        ArrayList<Long> book7Durations = new ArrayList<Long>();
        ArrayList<Long> book8Durations = new ArrayList<Long>();
        ArrayList<Long> book9Durations = new ArrayList<Long>();
        ArrayList<Long> book10Durations = new ArrayList<Long>();
        ArrayList<Long> book11Durations = new ArrayList<Long>();
        ArrayList<Long> book12Durations = new ArrayList<Long>();
        ArrayList<Long> book13Durations = new ArrayList<Long>();
        ArrayList<Long> book14Durations = new ArrayList<Long>();
        ArrayList<Long> book15Durations = new ArrayList<Long>();
        ArrayList<Long> book16Durations = new ArrayList<Long>();
        ArrayList<Long> book17Durations = new ArrayList<Long>();
        ArrayList<Long> book18Durations = new ArrayList<Long>();
        ArrayList<Long> book19Durations = new ArrayList<Long>();
        
        //populate the array with names of books' files
        pathnames = file.list();
        for (int i=0;i<repetition;i++) {
        	durations = new ArrayList<Long>();
	        for (String pathname : pathnames) {
	            //get current time
	        	long start = System.currentTimeMillis();
	        	//some frequent words in the English language
	        	RegEx.regEx = "t(h|e|a)*(n|m)";
	    		try {
	    			RegExTree ret = RegEx.parse();
	    			Automaton a = RegEx.RegExTreeToAutomaton(ret);
	    			a.toTable();
	    			a.eliminateEpsilonTransitions();
	    			a.minimize();
	    			ArrayList<MatchResponse> response = a.search("books/" + pathname);
	    			//add elapsed time in milliseconds for the current book
	    			durations.add(System.currentTimeMillis()-start);
	    		} catch (Exception e) {
	    			e.printStackTrace();
	    		}
	        }
	        //add durations for each book
	        babylonBookDurations.add(durations.get(0));
	        book1Durations.add(durations.get(1));
	        book2Durations.add(durations.get(12));
	        book3Durations.add(durations.get(13));
	        book4Durations.add(durations.get(14));
	        book5Durations.add(durations.get(15));
	        book6Durations.add(durations.get(16));
	        book7Durations.add(durations.get(17));
	        book8Durations.add(durations.get(18));
	        book9Durations.add(durations.get(19));
	        book10Durations.add(durations.get(2));
	        book11Durations.add(durations.get(3));
	        book12Durations.add(durations.get(4));
	        book13Durations.add(durations.get(5));
	        book14Durations.add(durations.get(6));
	        book15Durations.add(durations.get(7));
	        book16Durations.add(durations.get(8));
	        book17Durations.add(durations.get(9));
	        book18Durations.add(durations.get(10));
	        book19Durations.add(durations.get(11));
	    }
        //add the average durations for each book
        ArrayList<Long> durationsAverages = new ArrayList<Long>();
        durationsAverages.add(average(babylonBookDurations, repetition));
        durationsAverages.add(average(book1Durations, repetition));
        durationsAverages.add(average(book10Durations, repetition));
        durationsAverages.add(average(book11Durations, repetition));
        durationsAverages.add(average(book12Durations, repetition));
        durationsAverages.add(average(book13Durations, repetition));
        durationsAverages.add(average(book14Durations, repetition));
        durationsAverages.add(average(book15Durations, repetition));
        durationsAverages.add(average(book16Durations, repetition));
        durationsAverages.add(average(book17Durations, repetition));
        durationsAverages.add(average(book18Durations, repetition));
        durationsAverages.add(average(book19Durations, repetition));
        durationsAverages.add(average(book2Durations, repetition));
        durationsAverages.add(average(book3Durations, repetition));
        durationsAverages.add(average(book4Durations, repetition));
        durationsAverages.add(average(book5Durations, repetition));
        durationsAverages.add(average(book6Durations, repetition));
        durationsAverages.add(average(book7Durations, repetition));
        durationsAverages.add(average(book8Durations, repetition));
        durationsAverages.add(average(book9Durations, repetition));
	        
        System.out.println("=== Books ===");
        System.out.println(Arrays.toString(pathnames));
        System.out.println("=== Durations (in ms) ===");
        System.out.println(durationsAverages);
	}
}
