package libs.check.scj;

import javax.safetycritical.JopSystem;
import javax.safetycritical.Safelet;


public class Launcher {

	public static void main(String args[]) {

		Safelet s = new GenericSafelet();
		JopSystem js = new JopSystem();
		js.startMission(s);
	}
}
