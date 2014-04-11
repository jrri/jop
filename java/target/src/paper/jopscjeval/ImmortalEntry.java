package jopscjeval;

import javax.realtime.AbsoluteTime;
import javax.realtime.Clock;
import javax.realtime.PriorityScheduler;
import javax.realtime.RealtimeClock;
import javax.safetycritical.Terminal;

import com.jopdesign.io.IOFactory;
import com.jopdesign.io.SysDevice;

public class ImmortalEntry {
	
	public static IOFactory fact = null;
	public static SysDevice sys = null;

	public static Terminal term = null;
	
	public static int MAX_PRIO = 0;
	public static int MIN_PRIO = 0;
	
	/* Used to count the number of times an interrupt has been serviced */
	public static int relNo = -1;
	public static int relLimit = 500;

	public static void setup() {

		term = Terminal.getTerminal();
		term.writeln("Setup terminal... ok");
		
		fact = IOFactory.getFactory();
		sys = fact.getSysDevice();

		MAX_PRIO = PriorityScheduler.instance().getMaxPriority();
		MIN_PRIO = PriorityScheduler.instance().getMinPriority();
		
		term.writeln("Setup finished");

	}

}
