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
			+ "3 2 223 4 5";
	
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

	public static void testSuite01PrintStringContinentalMap(PrintWriter pw) {
		// First pass, check max length
		for (String line : stringContinentalMap.split("\\n")) {
			pw.println(line);
		}
	}

	public static void main(String[] args) {
		PrintWriter pw = new PrintWriter(System.out, true);
//		// DONE testSuite02TestFindTheFirstLongestNumericString ( pw);
//		223
//		231
//		1241
//		5345
//		222
//		// True is outflush
//		testSuite01PrintStringContinentalMap(new PrintWriter(System.out, true));

	}
}
