package net.heteroclinic.passfunder;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeSet;

/**
 * TODO Need re-design the marking of connected same tiles.
 * 
 * @author zhikai
 */

class SimpleMap {

	public static final int closed = 8848;
	public static final int elementMaxLength = new String("" + closed + 1)
			.length();
	public static final int waterBodyLimit = 10000;
	protected int[][] data;

	public SimpleMap(String input) {
		String[] rows = input.trim().split("\\n");
		int totalRows = rows.length;
		data = new int[totalRows][];
		int i = 0;
		for (String row : rows) {
			String[] elements = row.trim().split("\\s+");
			data[i] = new int[elements.length];
			for (int j = 0; j < elements.length; j++) {
				data[i][j] = Integer.parseInt(elements[j]);
			}
			i++;
		}
	}

	public void printData(PrintWriter pw) {
		if (null == data)
			return;
		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < data[i].length; j++) {
				pw.printf("%" + elementMaxLength + "d ", data[i][j]);
			}
			pw.println();
		}
	}
}

class MarkableMap extends SimpleMap {

	public MarkableMap(String input) {
		super(input);
	}

	// Each array element is tile, same type tiles form a connected zone.
	// <type,<zoneIdofSameType,listofTilesInOneZone>>
	public Map<Integer, Map<Integer, List<Integer>>> markZonesByLandType() {
		// <type,<zoneIdofSameType,listofTilesInOneZone>>
		Map<Integer, Map<Integer, List<Integer>>> positionSystem = new LinkedHashMap<Integer, Map<Integer, List<Integer>>>();
		// <tileId,zoneId>
		Map<Integer, Integer> antiPositionSystem = new LinkedHashMap<Integer, Integer>();

		loop1: for (int i = 0; i < data.length; i++) {
			loop2: for (int j = 0; j < data[i].length; j++) {
				int currentTileId = waterBodyLimit * i + j;
				int currentType = data[i][j];
				if (!antiPositionSystem.containsKey(currentTileId)) {
					// Map<Integer,List<Integer>> currentZone = new
					// LinkedHashMap<Integer,List<Integer>>();
					// List<Integer> tilesInThisZone = new ArrayList<Integer>();
					// tilesInThisZone.add(currentTileId);
					int currentZoneId = -1;
					List<Integer> tilesInThisZone;
					if (null == positionSystem.get(currentType)) {
						Map<Integer, List<Integer>> zonesOfOneType = new LinkedHashMap<Integer, List<Integer>>();
						tilesInThisZone = new ArrayList<Integer>();
						tilesInThisZone.add(currentTileId);
						currentZoneId = 1;
						zonesOfOneType.put(currentZoneId, tilesInThisZone);
						positionSystem.put(currentType, zonesOfOneType);
						antiPositionSystem.put(currentTileId, currentZoneId);
					} else {
						Map<Integer, List<Integer>> zonesOfOneType = positionSystem
								.get(currentType);
						tilesInThisZone = new ArrayList<Integer>();
						tilesInThisZone.add(currentTileId);
						currentZoneId = zonesOfOneType.size() + 1;
						zonesOfOneType.put(currentZoneId, tilesInThisZone);
						positionSystem.put(currentType, zonesOfOneType);
						antiPositionSystem.put(currentTileId, currentZoneId);
					}
					continuationOfOneType(i, j, currentType,currentZoneId,tilesInThisZone,antiPositionSystem);
				}
			}
		}

		return positionSystem;
	}

	public void continuationOfOneType(int i, int j, int currentType,
			int currentZoneId,List<Integer> tilesInThisZone,
			Map<Integer, Integer> antiPositionSystem) {
		// check west
		if (j - 1 >= 0 && data[i][j - 1] == currentType 
				&& !antiPositionSystem.containsKey( i * waterBodyLimit + j - 1)) {
			antiPositionSystem.put( i * waterBodyLimit + j - 1, currentZoneId);
			tilesInThisZone.add(i * waterBodyLimit + j - 1);
			continuationOfOneType(i, j-1, currentType,currentZoneId,tilesInThisZone,antiPositionSystem);
		}
		// check north
		if (i - 1 >= 0 && data[i - 1][j] == currentType
				&& !antiPositionSystem.containsKey( (i-1) * waterBodyLimit + j )) {
			antiPositionSystem.put( (i-1) * waterBodyLimit + j, currentZoneId);
			tilesInThisZone.add((i-1) * waterBodyLimit + j);
			continuationOfOneType(i-1, j, currentType,currentZoneId,tilesInThisZone,antiPositionSystem);
		}
		// check east
		if (j + 1 < data[i].length && data[i][j + 1] == currentType
				&& !antiPositionSystem.containsKey( i * waterBodyLimit + j + 1)) {
			antiPositionSystem.put( i * waterBodyLimit + j + 1, currentZoneId);
			tilesInThisZone.add( i * waterBodyLimit + j + 1);
			continuationOfOneType(i, j+1, currentType,currentZoneId,tilesInThisZone,antiPositionSystem);
		}
		// check south
		if (i + 1 < data.length && data[i + 1][j] == currentType
				&& !antiPositionSystem.containsKey( (i+1) * waterBodyLimit + j ) ) {
			antiPositionSystem.put( (i+1) * waterBodyLimit + j, currentZoneId);
			tilesInThisZone.add((i+1) * waterBodyLimit + j);
			continuationOfOneType(i+1, j, currentType,currentZoneId,tilesInThisZone,antiPositionSystem);
		}
	}

}

public class IdentifyContinuousBody {
	/*
	 * For Examples (1) 0 0 0 0 1 2 0 0 0 1 1 3 2 1 1 0 0 2 2 1 0 0 2 2 2 1 2 2
	 * 
	 * (2) 0 0 0 0 0 1 0 0 0 0 0 2 1 0 0 0 0 1 1 0 0 0 1 1 1 8848 1 1
	 * 
	 * (3) 1 0 0 0 0 1 0 0 1 1 0 2 1 0 0 0 0 0 1 0 0 0 1 0 1 8848 1 1
	 */
	public static final String testString1 = 
			  " 1     0     0     0     0     1     0 \n"
			+ " 0     1     1     0     2     1     0 \n"
			+ "	0     0     0     0     1     0     0 \n"
			+ "	0     1     0     1  8848     1     1";
	
	public static final String testString2 = 
			  " 1     0     0     0     0     1     0 \n"
			+ " 1     0     1     0     2     1     0 \n"
			+ "	1     0     0     0     1     0     0 \n"
			+ "	0     1     0     1  8848     0     0";

	public static void main(String[] args) {
		int testCaseCount = 1;
		// True is autoflush
		PrintWriter pw = new PrintWriter(System.out, true);

		// Test 1
		{
			pw.printf("Test case No.%d\n", testCaseCount);
			SimpleMap simpleMap = new SimpleMap(testString1);
			simpleMap.printData(pw);
			testCaseCount++;
			pw.println("==========");
		}
		
		// Test 2
		{
			pw.printf("Test case No.%d\n", testCaseCount);
			MarkableMap markableMap = new MarkableMap(testString1);
			Map<Integer, Map<Integer, List<Integer>>>  positionSystem = markableMap.markZonesByLandType();
			TreeSet<Integer> sortedKeys = new TreeSet<Integer>();
			sortedKeys.addAll(positionSystem.keySet());
			for (int k : sortedKeys) {
				pw.printf("Current tile type %d \n",k);
				for (Entry<Integer, List<Integer>> entry: positionSystem.get(k).entrySet()  ) {
					pw.printf("ZoneId=%d : ", entry.getKey());
					for (int tile: entry.getValue()) {
						pw.printf("%d ", tile);
					}
					pw.println();
				}
				pw.println("-------");
			}
			testCaseCount++;
			pw.println("==========");
		}

		// Test 3
		{
			pw.printf("Test case No.%d\n", testCaseCount);
			MarkableMap markableMap = new MarkableMap(testString2);
			Map<Integer, Map<Integer, List<Integer>>>  positionSystem = markableMap.markZonesByLandType();
			TreeSet<Integer> sortedKeys = new TreeSet<Integer>();
			sortedKeys.addAll(positionSystem.keySet());
			for (int k : sortedKeys) {
				pw.printf("Current tile type %d \n",k);
				for (Entry<Integer, List<Integer>> entry: positionSystem.get(k).entrySet()  ) {
					pw.printf("ZoneId=%d : ", entry.getKey());
					for (int tile: entry.getValue()) {
						pw.printf("%d ", tile);
					}
					pw.println();
				}
				pw.println("-------");
			}
			testCaseCount++;
			pw.println("==========");
		}

	}

}
