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
import javax.realtime.RelativeTime;
import javax.safetycritical.PeriodicEventHandler;
import javax.realtime.PeriodicParameters;
import javax.safetycritical.Safelet;
import javax.safetycritical.Terminal;
//import javax.safetycritical.ThreadConfiguration;
import javax.safetycritical.annotate.Level;
import javax.safetycritical.annotate.Phase;
import javax.safetycritical.annotate.SCJAllowed;
import javax.safetycritical.annotate.SCJRestricted;

/**
 * That's the simplest test case I can think of as the class
 * is such simple ;-)
 * 
 * @author Martin Schoeberl
 *
 */
public class TestPeriodicParameters extends TestCase implements Safelet {
	
	public String getName() {
		return "Test periodic paramters";
	}
	
	/**
	 * The whole test fits in initialize. We even don't need to start a mission
	 * to run that test case. A test case that should be easy to pass and gives
	 * a first 'ok' on yet-to-be-started SCJ implementation ;-)
	 */
	@Override
	@SCJAllowed(Level.SUPPORT)
	protected void initialize() {
		
		RelativeTime start = new RelativeTime(123, 456);
		RelativeTime period = new RelativeTime(789, 321);
		
		PeriodicParameters pp = new PeriodicParameters(start, period);
		
		RelativeTime per = pp.getPeriod();
		RelativeTime strt = (RelativeTime) pp.getStart();
		
		test(per.getMilliseconds()==789);
		test(per.getNanoseconds()==321);
		test(strt.getMilliseconds()==123);
		test(strt.getNanoseconds()==456);
		// that's it, nothing more can be done with PeriodicParameters 

		// finish also calls for termination request to stop
		// the test case.
		finish();
	}
	
	public long missionMemorySize() {
		return 0;
	}

//	public int getLevel() {
//		return Safelet.LEVEL_0;
//	}

	@Override
	@SCJAllowed(Level.SUPPORT)
	public long immortalMemorySize() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	@SCJAllowed(Level.SUPPORT)
	@SCJRestricted(phase = Phase.INITIALIZATION)
	public void initializeApplication() {
		// TODO Auto-generated method stub
		
	}

}
