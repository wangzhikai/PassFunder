package net.heteroclinic.passfunder;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
/**
 * This is a brain-tease. To find given array elements (zero, one or many), from them there are monotone (less than not equal) decreasing continuous path(s), leading to all oceans. 
 * Connected zeros is one ocean.
 * The solution is growing the sea level, until the map is full of water or prohibited tiles/elements marked as 'closed'.
 * The qualifying element(s): throughout the flooding process, any element adjacent to all existing oceans qualifies. Note, as flooding, the number of oceans may decrease.
 * End condition: full of water or 'closed', if there are other tiles, they are qualified solution tiles.
 */
//TODO There is a BIG bug. Identify water bodies
//DONE Continuous flooding QED
//DONE Handle basin : in StringData1 e.g. (3,4) = 2 is a basin. No water can flow from it. So it is a not a flood-able element.
//DONE Rewrite post flood condition check
//DONE Test above
//DONE Change System.out to PrintWriter
//DONE printData format
//DONE Revise the selection of qualified elements, and end condition of the story
//TODO Implement the selection of qualified elements, and end condition of the story
//TODO Manually design other cases
//Far sights:
//TODO Automatically generate a map, there is solution, but not easy to guess

public class ContinentalMap {
	public static final int closed = 8848;
	public static final int elementMaxLength = new String(""+closed + 1).length();
	public static final String stringData1 
		=     "0 0 0 1 2 3 0 \n"
			+ "0 1 2 2 4 3 2 \n"
			+ "2 1 1 3 3 2 0 \n"
			+ "0 3 3 3 2 3 3";
	public static final String stringData2 
	=    "0 0 2 0\n"
			+ " 0 1 1 3\n"
			+ " 0 2 1 3\n"
			+ " 1 2 3 3\n"
			+ " 2 4 3 2\n"
			+ " 3 3 2 3\n"
			+ " 0 2 0 3";
	protected int [][] data;
	
	public static final int waterBodyLimit = 10000; 
	
	// TODO markOceans 
	// TODO test
	public Map<Integer,List<Integer>> markOceans () {
		// store <i*l+j, oceanId>
		Map<Integer,Integer> checkedWaterBodies = new LinkedHashMap<Integer,Integer> ();
		// store <oceanId,howManyWaterBodiesHere>
		Map<Integer,List<Integer>> oceanPedia = new LinkedHashMap<Integer,List<Integer>> ();
		// We come from north-west
		for (int i = 0 ; i < data.length; i++) {
			for (int j = 0; j <data[i].length; j++) {
				if ( 0 == data[i][j] ) {
					// check west
					if ( j-1 >=0 && data[i][j-1]==0 && checkedWaterBodies.containsKey( i*waterBodyLimit + j-1)) {
						int currentOceanId = checkedWaterBodies.get( i*waterBodyLimit + j-1);
						checkedWaterBodies.put(i*waterBodyLimit + j, currentOceanId);
						oceanPedia.get(currentOceanId).add(i*waterBodyLimit + j);
					}
					// check north
					else if ( i-1>=0 && data[i-1][j]==0 && checkedWaterBodies.containsKey((i-1)*waterBodyLimit + j) ) {
						int currentOceanId = checkedWaterBodies.get( (i-1)*waterBodyLimit + j);
						checkedWaterBodies.put(i*waterBodyLimit + j, currentOceanId);
						oceanPedia.get(currentOceanId).add(i*waterBodyLimit + j);
					}
					// none above is water, a new ocean discovered
					else {
						int newOceanId = oceanPedia.size()+1;
						List<Integer> newList = new ArrayList<Integer>();
						newList.add(i*waterBodyLimit + j);
						oceanPedia.put(newOceanId, newList);
						checkedWaterBodies.put(i*waterBodyLimit + j, newOceanId);	
					}
				}
 			}
		}
		return oceanPedia;
	}	
	public int countOceans () {
		// store <i*l+j, oceanId>
		Map<Integer,Integer> checkedWaterBodies = new LinkedHashMap<Integer,Integer> ();
		// store <oceanId,howManyWaterBodiesHere>
		Map<Integer,Integer> oceanPedia = new LinkedHashMap<Integer,Integer> ();
		// We come from north-west
		for (int i = 0 ; i < data.length; i++) {
			for (int j = 0; j <data[i].length; j++) {
				if ( 0 == data[i][j] ) {
					// check west
					if ( j-1 >=0 && data[i][j-1]==0 && checkedWaterBodies.containsKey( i*waterBodyLimit + j-1)) {
						int currentOceanId = checkedWaterBodies.get( i*waterBodyLimit + j-1);
						checkedWaterBodies.put(i*waterBodyLimit + j, currentOceanId);
						int newCount = oceanPedia.get(currentOceanId)+1;
						oceanPedia.put(currentOceanId, newCount);
					}
					// check north
					else if ( i-1>=0 && data[i-1][j]==0 && checkedWaterBodies.containsKey((i-1)*waterBodyLimit + j) ) {
						int currentOceanId = checkedWaterBodies.get( (i-1)*waterBodyLimit + j);
						checkedWaterBodies.put(i*waterBodyLimit + j, currentOceanId);		
						int newCount = oceanPedia.get(currentOceanId)+1;
						oceanPedia.put(currentOceanId, newCount);
					}
					// none above is water, a new ocean discovered
					else {
						int newOceanId = oceanPedia.size()+1;
						oceanPedia.put(newOceanId, 1);
						checkedWaterBodies.put(i*waterBodyLimit + j, newOceanId);	
					}
				}
 			}
		}
		return oceanPedia.size();
	}
	
	public ContinentalMap(String input) {
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
	
	/**
	 * DONE check if an element is adjacent to water
	 * DONE test
	 * The element is water or not does not matter.
	 * @param i
	 * @param j
	 * @return
	 */
	public boolean isAtwater (int i, int j) {
		boolean result = false;
		// check west
		if ( j-1 >=0 && data[i][j-1]==0 ) {
			result = true;
		}
		// check north
		else if ( i-1>=0 && data[i-1][j]==0 ) {
			result = true;
		}
		// check east
		else if ( j+1 < data[i].length && data[i][j+1]==0 ) {
			result = true;
		}
		// check south
		else if ( i+1 < data.length && data[i+1][j]==0 ) {
			result = true;
		}
		return result;
	}
 	
	public static void testIsAtWaterFunction(ContinentalMap continentalMap, PrintWriter pw) {
		for (int i = 0 ; i < continentalMap.data.length; i++) {
			for (int j = 0; j <continentalMap.data[i].length; j++) {
				if (continentalMap.isAtwater (i, j) ) {
					pw.printf("(%d,%d)\n",i,j);
				}
 			}
		}
	}
	
	/**
	 * DONE Mark lowlands, adjacent a_{i,j} = 1 
	 * DONE Test
	 */
	public Map<Integer,List<Integer>> markLowLands () {
		// store <i*l+j, lowLandId>
		Map<Integer,Integer> checkedLowLands = new LinkedHashMap<Integer,Integer> ();
		// store <oceanId,howManyWaterBodiesHere>
		Map<Integer,List<Integer>> lowlandPedia = new LinkedHashMap<Integer,List<Integer>> ();
		for (int i = 0 ; i < data.length; i++) {
			for (int j = 0; j <data[i].length; j++) {
				// We come from north-west
				if ( 1 == data[i][j] ) {
					// check west
					if ( j-1 >=0 && data[i][j-1]==1 && checkedLowLands.containsKey( i*waterBodyLimit + j-1)) {
						int currentLowLandId = checkedLowLands.get( i*waterBodyLimit + j-1);
						checkedLowLands.put(i*waterBodyLimit + j, currentLowLandId);
						List<Integer> currentLowlandZone = lowlandPedia.get(currentLowLandId);
						currentLowlandZone.add(i*waterBodyLimit + j);
					}
					// check north
					else if ( i-1>=0 && data[i-1][j]==1 && checkedLowLands.containsKey( (i-1)*waterBodyLimit + j) ) {
						int currentLowLandId = checkedLowLands.get((i-1)*waterBodyLimit + j);
						checkedLowLands.put(i*waterBodyLimit + j, currentLowLandId);
						List<Integer> currentLowlandZone = lowlandPedia.get(currentLowLandId);
						currentLowlandZone.add(i*waterBodyLimit + j);
					}
					// none above is '1', a new lowlandzone is discovered
					else {
						int newLowLandId = lowlandPedia.size()+1;
						
						List<Integer> currentLowlandZone = new ArrayList<Integer>();
						currentLowlandZone.add(i*waterBodyLimit + j);
						lowlandPedia.put(newLowLandId, currentLowlandZone);
						checkedLowLands.put(i*waterBodyLimit + j, newLowLandId);	
					}
				}
 			}
		}	
		return lowlandPedia;
	}
	public static void testMarkLowLandsFunction(ContinentalMap continentalMap, PrintWriter pw) {
		Map<Integer,List<Integer>> lowlandPedia = continentalMap.markLowLands();
		for (Entry<Integer,List<Integer>> entry : lowlandPedia.entrySet() ) {
			pw.println(entry.getKey());
			for (int e: entry.getValue()) {
				int j = e % waterBodyLimit;
				int i = e / waterBodyLimit;
				pw.printf("(%d,%d) ",i,j);
			}
			pw.println();
		}
	}
	 
	/**
	 * DONE continuous flooding
	 */
	public void flood (PrintWriter pw) {
		flood (pw,false);
	}
	public void flood (PrintWriter pw, boolean printDebug) {
		if (printDebug) {
			printData(pw);
			pw.println();
			pw.println("----------");
		}
		// First pass, identify flood-able zones (connected '1's, adjacent to at least one ocean, same thing as identifying oceans)
		Map<Integer,List<Integer>> lowlandPedia =markLowLands();
		// Check water adjacency
		Map<Integer,List<Integer>> floodableZones = new LinkedHashMap<Integer,List<Integer>> ();
		Map<Integer,List<Integer>> basinZones = new LinkedHashMap<Integer,List<Integer>> ();
		for (Entry<Integer,List<Integer>> entry : lowlandPedia.entrySet() ) {
			boolean isAtwater = false;
			for (int e: entry.getValue()) {
				int j = e % waterBodyLimit;
				int i = e / waterBodyLimit;
				if ( isAtwater(i, j) ) {
					isAtwater = true;
					break;
				}
			}
			if (isAtwater) {
				floodableZones.put(entry.getKey(),entry.getValue());
			} else {
				basinZones.put(entry.getKey(),entry.getValue());
			}
		}
		
		// Second pass, flood flood-able zone, mark basins as closed (8848)
		for (Entry<Integer,List<Integer>> entry : floodableZones.entrySet() ) {
			for (int e: entry.getValue()) {
				int j = e % waterBodyLimit;
				int i = e / waterBodyLimit;
				data[i][j] = data[i][j] -1;
				if ( data[i][j]!=0)
					throw new RuntimeException("This is an awkward assertion, but it says you are flooding the wrong tile.");
			}
		}
		if (printDebug) {
			printData(pw);
			pw.println();
			pw.println("----------");
		}
		
		// Mark basins as closed (8848)
		for (Entry<Integer,List<Integer>> entry : basinZones.entrySet() ) {
			for (int e: entry.getValue()) {
				int j = e % waterBodyLimit;
				int i = e / waterBodyLimit;
				if (data[i][j]!=1)
					throw new RuntimeException("This is an awkward assertion, but it says you marked a wrong basin.");
				data[i][j] = closed;
			}
		}
		if (printDebug) {
			printData(pw);
			pw.println();
			pw.println("----------");
		}
		// Third pass, de-elevation zones that are not ocean and not 'closed' by 1. 
		for (int i = 0 ; i < data.length; i++) {
			for (int j = 0; j < data[i].length; j++) {
				if (data[i][j] != 0 && data[i][j] !=closed) {
					data[i][j] -= 1;
					if (data[i][j]<=0)
						throw new RuntimeException("This is an awkward assertion, but it says the flooding not work properly.");
				}
 			}
		}	
		if (printDebug) {
			printData(pw);
			pw.println();
			pw.println("----------");
		}
	}
	
	// DONE
	public static void testFloodFunctionByManualWatch (ContinentalMap continentalMap, boolean printDebug, PrintWriter pw) {
		while (!continentalMap.checkFloodedCondition()) {
			int oceansCount = continentalMap.countOceans ();
			pw.println("Oceans count = " +oceansCount);
			continentalMap.flood(pw,true);
		}
	}
	
	public void printData (PrintWriter pw) {
		if (null == data)
			return;
		for (int i = 0 ; i < data.length; i++) {
			for (int j = 0; j <data[i].length; j++) {
				pw.printf("%"+this.elementMaxLength+"d ", data[i][j]);
 			}
			pw.println();
		}
	}
	
	public boolean checkFloodedCondition () {
		boolean result = true;
		loop1 : for (int i = 0 ; i < data.length; i++) {
			loop2 : for (int j = 0; j <data[i].length; j++) {
				if (data[i][j] != 0 && data[i][j] !=closed) {
					result = false;
					break loop1;
				}
 			}
		}
		return result;
	}
	
	//TODO getNeighboringOceans
	public List<Integer> getNeighboringOceans (int i, int j, Map<Integer,List<Integer>> oceanPedia) {
		List<Integer> oceanIds = new ArrayList<Integer>();
		// check west
		if ( j-1 >=0 && data[i][j-1]==0 ) {
			oceanIds.add(i*waterBodyLimit + j-1);
		}
		// check north
		if ( i-1>=0 && data[i-1][j]==0 ) {
			oceanIds.add( (i-1)*waterBodyLimit + j);
		}
		// check east
		else if ( j+1 < data[i].length && data[i][j+1]==0 ) {
			oceanIds.add(i*waterBodyLimit + j+1);
		}
		// check south
		else if ( i+1 < data.length && data[i+1][j]==0 ) {
			oceanIds.add( (i+1)*waterBodyLimit + j);
		}
		return oceanIds;
	}
	
	// TODO checkEndCondition
	// TODO test
	public boolean checkEndCondition (Map<Integer,Integer> returnSolutionsHere) {
		Map<Integer,List<Integer>> oceanPedia = this.markOceans();
		
		boolean result = true;
		loop1 : for (int i = 0 ; i < data.length; i++) {
			loop2 : for (int j = 0; j <data[i].length; j++) {
				List<Integer> neighboringOceans = this.getNeighboringOceans(i, j, oceanPedia); 
				if (data[i][j] != 0 && data[i][j] !=closed
						&& neighboringOceans.size() != oceanPedia.size()) {
					result = false;
					break loop1;
				}
				if ( data[i][j] != 0 &&
						neighboringOceans.size() > 0 &&
						neighboringOceans.size() == oceanPedia.size() 
						&& !returnSolutionsHere.containsKey(i*waterBodyLimit + j)) {
					returnSolutionsHere.put(i*waterBodyLimit + j, i*waterBodyLimit + j);
				}
 			}
		}
		return result;
	}
	
	// TODO Test finding the solutions
	public static void testFindSolutions(ContinentalMap continentalMap,PrintWriter pw, boolean printDebug) {
		Map<Integer,Integer> returnSolutionsHere = new LinkedHashMap<Integer,Integer>();
		int c = 0;
		while (!continentalMap.checkEndCondition(returnSolutionsHere)) {
			c++;
			pw.printf("----Flood #%d started----\n",c);
			continentalMap.flood(pw,printDebug);
			pw.printf("----Flood #%d   ended----\n",c);
		}
		pw.println("The solutions:");
		for (Entry <Integer,Integer> entry:returnSolutionsHere.entrySet()) {
			int k = entry.getKey();
			int j = k % waterBodyLimit;
			int i = k / waterBodyLimit;
			pw.printf("(%d,%d) ",i,j);
		}
		pw.println();
	}
	
	
	public void showResult (PrintWriter pw) {
		loop1 : for (int i = 0 ; i < data.length; i++) {
			loop2 : for (int j = 0; j <data[i].length; j++) {
				if (data[i][j] != 0 && data[i][j]!= closed)  {
					pw.printf("Element at (%d,%d) \n",i,j);
				}
 			}
		}
	}
	
	public static void main(String[] args) {
		int testCaseCount = 1;
		// True is autoflush
		PrintWriter pw = new PrintWriter(System.out, true);

		// Test 1
		pw.printf("Test case No.%d\n",testCaseCount);
		ContinentalMap continentalMap = new ContinentalMap(stringData1 );
		testIsAtWaterFunction(continentalMap,pw);
		testCaseCount++;
		pw.println();
		
		//ContinentalMap continentalMap = new ContinentalMap(stringData1 );
		// Test 2
		pw.printf("Test case No.%d\n",testCaseCount);
		testMarkLowLandsFunction( continentalMap,pw);
		testCaseCount++;
		pw.println();
		
		// Test 3
		pw.printf("Test case No.%d\n",testCaseCount);
		testFloodFunctionByManualWatch(continentalMap, true, pw);
		testCaseCount++;
		pw.println();
		
		// Test 4
		pw.printf("Test case No.%d\n",testCaseCount);
		testFindSolutions(new ContinentalMap(stringData1 ), pw, true);
		testCaseCount++;

		

//		// Test 3
//		pw.printf("Test case No.%d\n",testCaseCount);
//		testFloodFunctionByManualWatch(continentalMap, true, pw);
//		testCaseCount++;
//		pw.println();

	}
}
