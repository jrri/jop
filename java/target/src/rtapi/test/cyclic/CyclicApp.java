package test.cyclic;

import javax.safetycritical.CyclicExecutive;
import javax.safetycritical.JopSystem;
import javax.safetycritical.Mission;
import javax.safetycritical.Safelet;
import javax.safetycritical.Terminal;

public class CyclicApp {
	

		public static Terminal term;

		public static void main(String args[]) {

			term = Terminal.getTerminal();
//			JopSystem js = new JopSystem();

			Safelet s = new CyclicSafelet();
			term.writeln("Safelet created");

			JopSystem.startMission(s);

		}


}
