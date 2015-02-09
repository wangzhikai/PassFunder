package net.heteroclinic.passfunder;

//TODO Identify water bodies

public class ContinentalMap {
	final int closed = 8848;
	public static final String stringData1 
		=     "0 0 0 1 2 3 0 \n"
			+ "0 1 2 2 4 3 2 \n"
			+ "2 1 1 3 3 2 0 \n"
			+ "0 3 3 3 2 3 3";
	protected int [][] data;
	
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
		// TODO init a map
		ContinentalMap continentalMap = new ContinentalMap(stringData1 );
		// TODO print map
		while (!continentalMap.checkFloodedCondition()) {
			continentalMap.printData();
			continentalMap.flood();
		}

	}

}
