package net.heteroclinic.passfunder;

import java.io.PrintWriter;

/**
 * TODO Need re-design the marking of connected same tiles. 
 * @author zhikai
 */

class SimpleMap {
	public static final int closed = 8848;
	public static final int elementMaxLength = new String(""+closed + 1).length();
	public static final int waterBodyLimit = 10000;
	protected int [][] data;
	public SimpleMap(String input) {
		String [] rows = input.trim().split("\\n");
		int totalRows = rows.length;
		data = new int [totalRows][];
		int i = 0;
		for ( String row : rows) {
			String [] elements = row.trim().split("\\s+");
			data[i] = new int [elements.length];
			for (int j = 0; j <elements.length; j++) {
				data[i][j] = Integer.parseInt(elements[j]);
			}
			i++;
		}
	}
	public void printData (PrintWriter pw) {
		if (null == data)
			return;
		for (int i = 0 ; i < data.length; i++) {
			for (int j = 0; j <data[i].length; j++) {
				pw.printf("%"+elementMaxLength+"d ", data[i][j]);
 			}
			pw.println();
		}
	}
}

public class IdentifyContinuousBody {
	/* For Examples
	(1)
    0     0     0     0     1     2     0 
    0     0     1     1     3     2     1 
    1     0     0     2     2     1     0 
    0     2     2     2     1     2     2 

	(2)
    0     0     0     0     0     1     0 
    0     0     0     0     2     1     0 
    0     0     0     1     1     0     0 
    0     1     1     1  8848     1     1
    
    (3)
    1     0     0     0     0     1     0 
    0     1     1     0     2     1     0 
    0     0     0     0     1     0     0 
    0     1     0     1  8848     1     1
    
	 */
	public static final String testString1 =
			"   1     0     0     0     0     1     0 \n"
			+" 	0     1     1     0     2     1     0 \n" 
			+"	0     0     0     0     1     0     0 \n" 
			+"	0     1     0     1  8848     1     1";
	
	

	public static void main(String[] args) {
		int testCaseCount = 1;
		// True is autoflush
		PrintWriter pw = new PrintWriter(System.out, true);

		// Test 1
		pw.printf("Test case No.%d\n",testCaseCount);
		SimpleMap simpleMap = new SimpleMap(testString1 );
		simpleMap.printData(pw);
		testCaseCount++;
		pw.println();
		

	}

}
