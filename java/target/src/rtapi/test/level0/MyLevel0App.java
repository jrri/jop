package test.level0;

import javax.safetycritical.*;

public class MyLevel0App {

	public static Terminal term;

	public static void main(String args[]) {

		term = Terminal.getTerminal();

		Safelet<?> s = new Level0Safelet();
		term.writeln("Safelet created");

		JopSystem.startMission(s);

		term.writeln("Main method finished");
	}

}
