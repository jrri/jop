package edu.purdue.scjtck;

import javax.safetycritical.JopSystem;

import edu.purdue.scjtck.tck.TestClock600;
import edu.purdue.scjtck.tck.TestException000;
import edu.purdue.scjtck.tck.TestMemory501;
import edu.purdue.scjtck.tck.TestMemory502;
import edu.purdue.scjtck.tck.TestMemory504;
import edu.purdue.scjtck.tck.TestSchedule400;
import edu.purdue.scjtck.tck.TestSchedule401;
import edu.purdue.scjtck.tck.TestSchedule402;
import edu.purdue.scjtck.tck.TestSchedule403;
import edu.purdue.scjtck.tck.TestSchedule404;
import edu.purdue.scjtck.tck.TestSchedule405;
import edu.purdue.scjtck.tck.TestSchedule406;
import edu.purdue.scjtck.tck.TestSchedule409;



public final class Launcher {


	public static void main(final String[] args) {
		
		TestClock600 s = new TestClock600();
		JopSystem.startMission(s);
		
	}

}