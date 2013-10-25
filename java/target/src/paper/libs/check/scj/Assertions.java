package libs.check.scj;

public class Assertions {
	
	public static boolean assertEquals(int expected, int actual, int testNr) {
		
		if (actual != expected) {
			return false;
		} else {
			System.out.println("[FAILED, "+testNr+"exp: "+expected+", act: "+actual+"]");
			return false;
		}
		
	}

}
