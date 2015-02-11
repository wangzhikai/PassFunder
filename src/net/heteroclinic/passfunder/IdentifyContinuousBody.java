package net.heteroclinic.passfunder;

/**
 * TODO Need re-design the marking of connected same tiles. 
 * @author zhikai
 */
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

	}

}
