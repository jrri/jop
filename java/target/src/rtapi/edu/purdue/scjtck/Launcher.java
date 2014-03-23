package edu.purdue.scjtck;

import javax.safetycritical.JopSystem;

import edu.purdue.scjtck.tck.TestMemory501;
import edu.purdue.scjtck.tck.TestMemory504;
import edu.purdue.scjtck.tck.TestSchedule402;
import edu.purdue.scjtck.tck.TestSchedule404;
import edu.purdue.scjtck.tck.TestSchedule405;
import edu.purdue.scjtck.tck.TestSchedule406;



public final class Launcher {


	public static void main(final String[] args) {
		
//		 TestMemory504 s = new TestMemory504();
		TestSchedule402 s = new TestSchedule402();
//		s.initializeApplication();
//		System.out.println("Safelet created");
		
		(new JopSystem()).startMission(s);
		
//		System.out.println("Main method finished");
	}

}