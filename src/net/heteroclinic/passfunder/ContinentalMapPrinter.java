package net.heteroclinic.passfunder;

import java.io.PrintWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author zhikai We use this class to output the continental map.
 */
public class ContinentalMapPrinter {
	public static final String stringContinentalMap = 
			  " 1 2 3 4 5\n"
			+ "2 2 3 4 5\n" 
			+ "3 2 223 4 5";
	public static final String stringContinentalMap2 = 
			  " 231 2 3 4 5\n"
			+ "2 2 3 4 5\n" 
			+ "3 2 223 4 5";
	public static final String stringContinentalMap3 = 
			  " 1241 2 3 4 5\n"
			+ "2 2 3 4 5\n" 
			+ "3 2 223 4 5";
	public static final String stringContinentalMap4 = 
			  " 1 2 3 4 5\n"
			+ "2 2 3 4 5\n" 
			+ "3 2 223 4 5345";
	public static final String stringContinentalMap5 = 
			  " 1 2 32 4 5\n"
			+ "2 222 3 4 5\n" 
			+ "3 2 223 8848 5";
	
	/**
	 * Input a String with substrings spaced by white-spaces. 
	 * Get the max length substring.
	 * @param input
	 * @return empty string if not found.
	 * TODO- it will be good to debug through the selected test cases.
	 */
	public static String findTheFirstLongestNumericString(String input) {
		String result = "";
		int currentMaxLenth = 0;
		Pattern pattern = Pattern.compile("(?s)[1-9]+");
		Matcher matcher = pattern.matcher(input);
		int cursor = 0;
		while (matcher.find(cursor)) {
			currentMaxLenth = matcher.end() - matcher.start();
			result = input.substring(matcher.start(), matcher.end());
			cursor = matcher.end();
			pattern = Pattern.compile("(?s)[1-9]{" + currentMaxLenth
					+ "}[1-9]+");
			matcher = pattern.matcher(input);
		}
		return result;
	}
	
	public static void testSuite02TestFindTheFirstLongestNumericString (PrintWriter pw) {
		pw.println(ContinentalMapPrinter.findTheFirstLongestNumericString(ContinentalMapPrinter.stringContinentalMap));
		pw.println(ContinentalMapPrinter.findTheFirstLongestNumericString(ContinentalMapPrinter.stringContinentalMap2));
		pw.println(ContinentalMapPrinter.findTheFirstLongestNumericString(ContinentalMapPrinter.stringContinentalMap3));
		pw.println(ContinentalMapPrinter.findTheFirstLongestNumericString(ContinentalMapPrinter.stringContinentalMap4));
		pw.println(ContinentalMapPrinter.findTheFirstLongestNumericString(ContinentalMapPrinter.stringContinentalMap5));
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
		PrintWriter pw = new PrintWriter(System.out, true);
		// testSuite02
		//testSuite02TestFindTheFirstLongestNumericString ( pw);
		//		223
		//		231
		//		1241
		//		5345
		//		8848
		
		// True is outflush
		alignOutputContinentalMap(pw, stringContinentalMap);
		pw.println();
		alignOutputContinentalMap(pw, stringContinentalMap2);
		pw.println();
		alignOutputContinentalMap(pw, stringContinentalMap3);
		pw.println();
		alignOutputContinentalMap(pw, stringContinentalMap4);
		pw.println();
		alignOutputContinentalMap(pw, stringContinentalMap5);
		pw.println();

	}
}
