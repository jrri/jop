package test.mixed;

import javax.safetycritical.JopSystem;
import javax.safetycritical.Safelet;

public class MixedLevelApp {

	public static void main(String args[]) {

		Safelet s = new MixedLevelSafelet();
		JopSystem js = new JopSystem();
		js.startMission(s);
	}
}
