package net.heteroclinic.passfunder;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
/**
 * This is a brain-tease. To find from array elements, monotone (less than not equal) decreasing from adjacent element, leading to all '0's. 
 * Connected zeros is one ocean.
 * The solution is growing the sea level, when all oceans become one BODY, the none-basin (none-prohibited) element is the answer.
 * @author zhikai
 *
 */

//DONE Identify water bodies
//TODO Handle basin
//TODO Manually design other cases
//     -- trivial move
//     -- basin
//TODO Automatically generate a map     

public class ContinentalMap {
	public static final int closed = 8848;
	public static final String stringData1 
		=     "0 0 0 1 2 3 0 \n"
			+ "0 1 2 2 4 3 2 \n"
			+ "2 1 1 3 3 2 0 \n"
			+ "0 3 3 3 2 3 3";
	protected int [][] data;
	
	public static final int waterBodyLimit = 10000; 
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
	
	public void flood () {
		for (int i = 0 ; i < data.length; i++) {
			for (int j = 0; j <data[i].length; j++) {
				if ( data[i][j] > 0)  
					data[i][j] -= 1;
 			}
		}
	}
	
	public void printData () {
		if (null == data)
			return;
		String stringData = "";
		for (int i = 0 ; i < data.length; i++) {
			String row = "";
			for (int j = 0; j <data[i].length; j++) {
				row += data[i][j] + " ";
 			}
			stringData += row.trim()+"\n";
		}
		System.out.println(stringData);
	}
	
	public boolean checkFloodedCondition () {
		boolean result = true;
		loop1 : for (int i = 0 ; i < data.length; i++) {
			loop2 : for (int j = 0; j <data[i].length; j++) {
				if (data[i][j] != 0) {
					result = false;
					break loop1;
				}
 			}
		}
		return result;
	}
	public static void main(String[] args) {
		// Init a map
		ContinentalMap continentalMap = new ContinentalMap(stringData1 );
		// Print map
		while (!continentalMap.checkFloodedCondition()) {
			System.out.println("Oceans count = " + continentalMap.countOceans ());
			continentalMap.printData();
			continentalMap.flood();
		}

	}

}
