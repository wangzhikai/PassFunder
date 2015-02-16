package net.heteroclinic.passfunder;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

/**
 * DONE Need re-design the marking of connected same tiles.
 * BFS from each ocean, each ocean has a reachableSet. Than do an intersection of all sets.
 * Flooding is not used for get the solution. But it is useful to find 'closed' zones.
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

class OceanLandMap extends MarkableMap {

	public OceanLandMap(String input) {
		super(input);
	}
	
	public boolean isATileAtwater (int i, int j) {
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
	
	public boolean isAZoneAtwater (List<Integer> tilesInThisZone ) {
		boolean result = false;
		for (int tile: tilesInThisZone ) {
			int j = tile % waterBodyLimit;
			int i = tile / waterBodyLimit;
			if ( isATileAtwater(i,j) ) {
				result = true;
				break;
			}
		}
		return result;
	}
}

class FloodableMap extends OceanLandMap {

	public FloodableMap(String input) {
		super(input);
	}
	
	public boolean getEndOfFloodCondition () {
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
	
	public void flood(Map<Integer, Map<Integer, List<Integer>>>  positionSystem) {
		// for '1' zones, if atwater flood to '0'
		// for '1' zones, if not atwater set to 'closed'
		int floodableType = 1;
		Map<Integer, List<Integer>> floodCandidates =  positionSystem.get(floodableType);
		for (Entry<Integer, List<Integer>> entry : floodCandidates.entrySet() ) {
			List<Integer> tilesInThisZone = entry.getValue();
			if (isAZoneAtwater(tilesInThisZone)) {
				for (int e: tilesInThisZone) {
					int j = e % waterBodyLimit;
					int i = e / waterBodyLimit;
					data[i][j] =0;
				}
			} else {
				for (int e: tilesInThisZone) {
					int j = e % waterBodyLimit;
					int i = e / waterBodyLimit;
					data[i][j] =closed;
				}
			}
		}
		// any zone > 1 && !closed decrease by 1
		for (int k : positionSystem.keySet()) {
			if (k > 1 && k != closed ) {
				for (Entry<Integer, List<Integer>> entry :positionSystem.get(k).entrySet() ) {
					List<Integer> tilesInThisZone = entry.getValue();
					for (int e: tilesInThisZone) {
						int j = e % waterBodyLimit;
						int i = e / waterBodyLimit;
						data[i][j] -= 1;
					}
				}
			}
		}
	}
}

class SmartBFSMap extends OceanLandMap {

	public SmartBFSMap(String input) {
		super(input);
	}
	
	public static final int typesLimit = 10000;
	
	public Set<Integer> getBFSIntersectionOfOceans (boolean printDebug,PrintWriter pw) {
		// DONE Rewrite mark zones method here
		// <type,<zoneIdofSameType,listofTilesInOneZone>>
		Map<Integer, Map<Integer, List<Integer>>> positionSystem = new LinkedHashMap<Integer, Map<Integer, List<Integer>>>();
		// <tileId,zoneId>
		Map<Integer, Integer> antiPositionSystem = new LinkedHashMap<Integer, Integer>();

		loop1: for (int i = 0; i < data.length; i++) {
			loop2: for (int j = 0; j < data[i].length; j++) {
				int currentTileId = waterBodyLimit * i + j;
				int currentType = data[i][j];
				if (!antiPositionSystem.containsKey(currentTileId)) {
					int currentZoneId = -1;
					List<Integer> tilesInThisZone;
					if (null == positionSystem.get(currentType)) {
						Map<Integer, List<Integer>> zonesOfOneType = new LinkedHashMap<Integer, List<Integer>>();
						tilesInThisZone = new ArrayList<Integer>();
						tilesInThisZone.add(currentTileId);
						currentZoneId = 1 + typesLimit*currentType ;
						zonesOfOneType.put(currentZoneId, tilesInThisZone);
						positionSystem.put(currentType, zonesOfOneType);
						antiPositionSystem.put(currentTileId, currentZoneId);
					} else {
						Map<Integer, List<Integer>> zonesOfOneType = positionSystem
								.get(currentType);
						tilesInThisZone = new ArrayList<Integer>();
						tilesInThisZone.add(currentTileId);
						currentZoneId = zonesOfOneType.size() + 1 + typesLimit*currentType;
						zonesOfOneType.put(currentZoneId, tilesInThisZone);
						positionSystem.put(currentType, zonesOfOneType);
						antiPositionSystem.put(currentTileId, currentZoneId);
					}
					continuationOfOneType(i, j, currentType,currentZoneId,tilesInThisZone,antiPositionSystem);
				}
			}
		}

		TreeSet<Integer> sortedKeys = new TreeSet<Integer>();
		sortedKeys.addAll(positionSystem.keySet());

		if (printDebug) {
			for (int k : sortedKeys) {
				pw.printf("Current tile type %d \n",k);
				for (Entry<Integer, List<Integer>> entry: positionSystem.get(k).entrySet()  ) {
					pw.printf("ZoneId=%d Atwater=%b : ", entry.getKey(),isAZoneAtwater(entry.getValue()));
					for (int tile: entry.getValue()) {
						pw.printf("%d ", tile);
					}
					pw.println();
				}
				pw.println("-------");
			}
		}
		// DONE Loop though oceans
		final int startingZoneType = 0;
		Map<Integer, List<Integer>> startingZones = positionSystem.get(startingZoneType);
		Map<Integer, Map<Integer,Integer>> reachablesOfEachStartZone = new LinkedHashMap<Integer, Map<Integer,Integer>>();
		for (Entry<Integer, List<Integer>> startZone: startingZones.entrySet()) {
			// <tileId,tileType>
			Map<Integer,Integer> currentReachableZones = new LinkedHashMap<Integer,Integer>();
			List<Integer> tilesInZone = positionSystem.get( startingZoneType ).get(startZone.getKey());
			// DONE Cotinuation of current zone
			if (printDebug) {
				pw.printf("---- Sweeping Zone %d\n",startZone.getKey());
			}
			getReachableZonesByContinuation(startingZoneType,startZone.getKey(), tilesInZone, positionSystem, antiPositionSystem, currentReachableZones,
					printDebug,pw);
			reachablesOfEachStartZone.put(startZone.getKey(), currentReachableZones);
		}
		
		// DONE calculate the intersection
		Set<Integer> a = new HashSet<Integer>();
		int count = 0;
		for (Map<Integer,Integer> oneZone:reachablesOfEachStartZone.values() ) {
			if (0 == count++) {
				//a = oneZone.keySet();
				a.addAll(oneZone.keySet());
			} else {
				Set<Integer> b = oneZone.keySet();
				a = IdentifyContinuousBody.getInterSectionOfTwoSets(a, b);
			}
		}
		if (printDebug) {
			pw.printf("---- Sweeping results by zoneIds:\n");
			for (int e: a) {
				pw.printf("%d ",e);
			}
			pw.println();
		}

		Set<Integer> result = new LinkedHashSet<Integer>();
		// DONE Output the correct types getTiles from zoneId
		pw.printf("---- Sweeping results by tile/element indices:\n");
		for (int e: a) {
			//pw.printf("%d ",e);
			int zoneId = e;
			int zoneType = e/typesLimit;
			for (int t: positionSystem.get(zoneType).get(e)) {
				int j = t % waterBodyLimit;
				int i = t / waterBodyLimit;
				pw.printf("[%d,%d]",i,j );
				result.add(t);
			}
		}
		pw.println();

		
		return result;
	}
	

	// DONE continuation of each zone (monotone increasing) to build reachable set. 
	// The other name of continuation is homotopy.
	public void getReachableZonesByContinuation(int zoneType,int zoneId,  List<Integer> tilesInZone,
			Map<Integer, Map<Integer, List<Integer>>> positionSystem,
			Map<Integer, Integer> antiPositionSystem, Map<Integer,Integer> currentReachableZones,
			boolean printDebug,PrintWriter pw) {
		// DONE Iterate though current zone's tiles
		//<tileId,tileType>
		Map<Integer,Integer> neighboringTiles = new LinkedHashMap<Integer,Integer>();
		for (int currentTile :tilesInZone ) {
			// DONE For each tile put all neighboring tiles a new set
			int j = currentTile % waterBodyLimit;
			int i = currentTile / waterBodyLimit;
			// check west
			if ( j-1 >=0 ) {
				neighboringTiles.put(i*waterBodyLimit +j-1,data[i][j-1]);
			}
			// check north
			if ( i-1>=0  ) {
				neighboringTiles.put((i-1)*waterBodyLimit +j,data[i-1][j]);
			}
			// check east
			if ( j+1 < data[i].length ) {
				neighboringTiles.put(i*waterBodyLimit +j +1,data[i][j+1]);
			}
			// check south
			if ( i+1 < data.length) {
				neighboringTiles.put((i+1)*waterBodyLimit +j,data[i+1][j]);
			}

		}
		
		Map<Integer,Integer> qualifiedNeighboringZones = new LinkedHashMap<Integer,Integer>();
		// DONE Loop through the set of neighboring tiles
		for (Entry<Integer,Integer> neighborTileEntry :neighboringTiles.entrySet() ) {
			// DONE Get its type and ZoneId
			int neighborType = neighborTileEntry.getValue();
			int neighborId = neighborTileEntry.getKey();
			// DONE Add the ZoneId to set currentReachables, if the neighbor type > current tile type
			if ( neighborType > zoneType ) {
				int neighborZoneId = antiPositionSystem.get(neighborId);
				currentReachableZones.put(neighborZoneId, neighborType);
				qualifiedNeighboringZones.put(neighborZoneId, neighborType);
			}
		}

		// DONE Loop through neighboringZones
		for (Entry<Integer,Integer> zone: qualifiedNeighboringZones.entrySet()) {
		    // DONE Continuation of each zone by recursive call of getReachableZonesByContinuation
			List<Integer> tilesInNeighborZone = positionSystem.get(zone.getValue()).get(zone.getKey());
			getReachableZonesByContinuation(zone.getValue(),zone.getKey(), tilesInNeighborZone,
					 positionSystem, antiPositionSystem, currentReachableZones,printDebug,pw);
		}
		
		if (printDebug) {
			pw.printf("Reachable in this round :");
			for (Entry<Integer,Integer> entry:qualifiedNeighboringZones.entrySet() ) {
				pw.printf("[%d,%d] ",entry.getKey(),entry.getValue() );
			}
			pw.println();
			pw.printf("Accumulated  reachables :");
			for (Entry<Integer,Integer> entry:currentReachableZones.entrySet() ) {
				pw.printf("[%d,%d] ",entry.getKey(),entry.getValue() );
			}
			pw.println();
			
		}
	}
}

public class IdentifyContinuousBody {
	
	public static Set<Integer> getInterSectionOfTwoSets(Set<Integer> a,Set<Integer> b) {
		Set<Integer> t1 = new HashSet<Integer>();
		t1.addAll(a);
		t1.removeAll(b);
		Set<Integer> t2 = new HashSet<Integer>();
		t2.addAll(a);
		t2.removeAll(t1);
		return t2;
	}

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

	public static final String validMap1 
	=     "0 0 0 1 2 3 0 \n"
		+ "0 1 2 2 4 3 2 \n"
		+ "2 1 1 3 3 2 0 \n"
		+ "0 3 3 3 2 3 3";

	public static final String validMap2 
	=     "0 0 0 1 2 3 0 \n"
		+ "0 1 2 4 4 3 2 \n"
		+ "2 1 1 4 3 2 0 \n"
		+ "0 3 3 3 2 3 3";
	
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
		
		// Test 4
		{
			pw.printf("Test case No.%d\n", testCaseCount);
			OceanLandMap oceanLandMap = new OceanLandMap(testString1);
			Map<Integer, Map<Integer, List<Integer>>>  positionSystem = oceanLandMap.markZonesByLandType();
			TreeSet<Integer> sortedKeys = new TreeSet<Integer>();
			sortedKeys.addAll(positionSystem.keySet());
			for (int k : sortedKeys) {
				pw.printf("Current tile type %d \n",k);
				for (Entry<Integer, List<Integer>> entry: positionSystem.get(k).entrySet()  ) {
					pw.printf("ZoneId=%d Atwater=%b : ", entry.getKey(),oceanLandMap.isAZoneAtwater(entry.getValue()));
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
		
		// Test 5
		{
			pw.printf("Test case No.%d\n", testCaseCount);
			OceanLandMap oceanLandMap = new OceanLandMap(validMap1);
			Map<Integer, Map<Integer, List<Integer>>>  positionSystem = oceanLandMap.markZonesByLandType();
			TreeSet<Integer> sortedKeys = new TreeSet<Integer>();
			sortedKeys.addAll(positionSystem.keySet());
			for (int k : sortedKeys) {
				pw.printf("Current tile type %d \n",k);
				for (Entry<Integer, List<Integer>> entry: positionSystem.get(k).entrySet()  ) {
					pw.printf("ZoneId=%d Atwater=%b : ", entry.getKey(),oceanLandMap.isAZoneAtwater(entry.getValue()));
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
		
		// Test 6
		{
			pw.printf("Test case No.%d\n", testCaseCount);
			FloodableMap floodableMap = new FloodableMap(validMap1);
			int floodCount = 0;
			while (!floodableMap.getEndOfFloodCondition()) {
				Map<Integer, Map<Integer, List<Integer>>>  positionSystem = floodableMap.markZonesByLandType();
				pw.printf("---- flood #%d BEG----\n",floodCount);
				floodableMap.printData(pw);
				floodableMap.flood(positionSystem);
				pw.printf("---- flood #%d END----\n",floodCount);
				floodableMap.printData(pw);
				floodCount++;
			}
			testCaseCount++;
			pw.println("==========");
		}

		// Test 7
		{
			pw.printf("Test case No.%d\n", testCaseCount);
			Set<Integer> a = new HashSet<Integer>();
			a.add(1);a.add(3);a.add(5);
			Set<Integer> b = new HashSet<Integer>();
			b.add(3);b.add(6);b.add(9);
			for (int e: getInterSectionOfTwoSets(a,b)) {
				pw.printf("%d ", e);
			}
			pw.println();
			testCaseCount++;
			pw.println("==========");
		}

		// Test 8
		{
			pw.printf("Test case No.%d\n", testCaseCount);
			SmartBFSMap smartBFSMap = new SmartBFSMap(validMap1);
			smartBFSMap.getBFSIntersectionOfOceans(true, pw);
			testCaseCount++;
			pw.println("==========");
		}
		
		// Test 9
		{
			pw.printf("Test case No.%d\n", testCaseCount);
			SmartBFSMap smartBFSMap = new SmartBFSMap(validMap2);
			smartBFSMap.getBFSIntersectionOfOceans(true, pw);
			testCaseCount++;
			pw.println("==========");
		}
	}
}
