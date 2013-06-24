/*
  This file is part of the TCK for JSR 302: Safety Critical JavaTM Technology
  	see <http://jcp.org/en/jsr/detail?id=302>

  Copyright (C) 2008, The Open Group
  Author: Martin Schoeberl (martin@jopdesign.com)

  License TBD.
 */

/**
 * 
 */
package scjtck;

import javax.realtime.PriorityParameters;
import javax.realtime.PriorityScheduler;
import javax.safetycritical.Mission;
import javax.safetycritical.LinearMissionSequencer;
import javax.safetycritical.StorageParameters;
//import javax.safetycritical.MissionDescriptor;
import javax.safetycritical.MissionSequencer;
import javax.safetycritical.Safelet;
//import javax.safetycritical.SingleMissionSequencer;
import javax.safetycritical.Terminal;

/**
 * @author Martin Schoeberl
 * 
 */
public abstract class TestCase extends Mission implements Safelet {

	private boolean ok;
	Terminal term;

	public TestCase() {
		ok = true;
		term = Terminal.getTerminal();
	}

	/**
	 * @return the name of the test case
	 */
	public String getName() {
		return "Unknown";
	}

	/**
	 * Accumulate the test results
	 * 
	 * @param result
	 * @return the good/bad result
	 */
	public boolean test(boolean result) {
		ok = ok && result;
		return ok;
	}

	/**
	 * Just print out some info to the standard terminal with the marker "Info:"
	 * 
	 * @param s
	 *            the info string
	 */
	public void info(CharSequence s) {
		term.write("Info: ");
		term.write(getName());
		term.write(" - ");
		term.writeln(s);
	}

	/**
	 * Print the result and stop the mission.
	 */
	public void finish() {
		term.write("Result: ");
		term.write(getName());
		if (ok) {
			term.writeln(" - passed");
		} else {
			term.writeln(" - failed");
		}
		// it's time to stop the mission
		requestTermination();
	}

	public MissionSequencer<Mission> getSequencer() {
		// We assume this method is invoked only once
		return new LinearMissionSequencer<Mission>(new PriorityParameters(
				PriorityScheduler.instance().getMinPriority()),
				new StorageParameters(512, null, 0, 0), false, this);
	}
}
