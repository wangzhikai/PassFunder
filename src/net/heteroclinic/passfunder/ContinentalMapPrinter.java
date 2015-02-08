package net.heteroclinic.passfunder;

import java.io.PrintWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author zhikai We use this class to output the continental map.
 * TODO Add '0' support
 * TODO Use replaceAll than splitting 
 * 
 * TODO CONSIDERING Add "0[1-9]+" handling, OR, general data validation handle
 */
public class ContinentalMapPrinter {
	public static final String stringContinentalMap1 = 
			  " 1 0 3 4 5\n"
			+ "2 2 33 400 5\n" 
			+ "32 2 223 4 5";
	public static final String stringContinentalMap2 = 
			  " 231 2 3 4 5\n"
			+ "2 0 3 400 5\n" 
			+ "3 2 223 4 5";
	public static final String stringContinentalMap3 = 
			  " 1024 2 3 4 5\n"
			+ "2 0 3 4 5\n" 
			+ "3 1 223 4 5";
	public static final String stringContinentalMap4 = 
			  " 1 20 3 4 5\n"
			+ "2 5344 3 4 5\n" 
			+ "3 2 223 4 5345";
	public static final String stringContinentalMap5 = 
			  " 0 200 32 4 5\n"
			+ "2 222 3 4 5\n" 
			+ "300 2 223 8848 0";
	public static final String [] stringContinentalMapInputArray = new String [] {
		stringContinentalMap1,stringContinentalMap2,stringContinentalMap3,stringContinentalMap4,stringContinentalMap5
	};
	
	/**
	 * Input a String with substrings spaced by white-spaces. 
	 * Get the max length substring.
	 * @param input
	 * @return empty string if not found.
	 */
	public static String findTheFirstLongestNumericString(String input) {
		String result = "";
		int currentMaxLenth = 0;
		Pattern pattern = Pattern.compile("(?s)[0-9]+");
		Matcher matcher = pattern.matcher(input);
		int cursor = 0;
		while (matcher.find(cursor)) {
			currentMaxLenth = matcher.end() - matcher.start();
			result = input.substring(matcher.start(), matcher.end());
			cursor = matcher.end();
			pattern = Pattern.compile("(?s)[0-9]{" + currentMaxLenth
					+ "}[0-9]+");
			matcher = pattern.matcher(input);
		}
		return result;
	}
	
	public static void testSuite02TestFindTheFirstLongestNumericString (PrintWriter pw) {
		for (String input :  stringContinentalMapInputArray) {
			pw.println(ContinentalMapPrinter.findTheFirstLongestNumericString(input));
		}
		// Get the first longest, not the largest
		//		400
		//		231
		//		1024
		//		5344
		//		8848
		}
	
	/**
	 * Align the elements of a numerical matrix in String format by the width of the largest element.
	 * @param pw
	 * @param input
	 */
	public static void alignOutputContinentalMap(PrintWriter pw, String input) {
		// First pass, check max length
		int maxSubstringLength = findTheFirstLongestNumericString(input).length();
		for (String line : input.split("\\n")) {
			for (String word : line.trim().split("\\s+")) {
				pw.printf("%"+maxSubstringLength+"s ", word); 
			}
			pw.println();
		}
	}

	public static void main(String[] args) {
		
		//True is outflush
		PrintWriter pw = new PrintWriter(System.out, true);
		// testSuite02
		// DONE: testSuite02TestFindTheFirstLongestNumericString ( pw);
		testSuite02TestFindTheFirstLongestNumericString ( pw);

		
		// testSuite01
		// 
		//		alignOutputContinentalMap(pw, stringContinentalMap);
		//		pw.println();
		//		alignOutputContinentalMap(pw, stringContinentalMap2);
		//		pw.println();
		//		alignOutputContinentalMap(pw, stringContinentalMap3);
		//		pw.println();
		//		alignOutputContinentalMap(pw, stringContinentalMap4);
		//		pw.println();
		//		alignOutputContinentalMap(pw, stringContinentalMap5);
		//		pw.println();
		//		1241    2    3    4    5 
		//		   2    2    3    4    5 
		//		   3    2  223    4    5 
		//
		//		   1    2    3    4    5 
		//		   2    2    3    4    5 
		//		   3    2  223    4 5345 
		//
		//		   1    2   32    4    5 
		//		   2  222    3    4    5 
		//		   3    2  223 8848    5 
	}
}
