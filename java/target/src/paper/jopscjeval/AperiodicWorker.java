package jopscjeval;

import javax.realtime.AbsoluteTime;
import javax.realtime.Clock;
import javax.realtime.RealtimeClock;
import javax.safetycritical.annotate.SCJAllowed;

public class AperiodicWorker extends GenericALEH{
	
	int maxEvents;
	long[] servs;
	long[] rels;
	
	int rel, serv;
	
	Clock rtc;

	public AperiodicWorker(int priority, int bsSize, int memSizestorage,
			String name, int maxEvents) {
		super(priority, bsSize, memSizestorage, name);
		
		this.maxEvents = maxEvents;
		rels = new long[maxEvents];
		servs = new long[maxEvents];
		rtc = RealtimeClock.getRealtimeClock();
		
	}

	@Override
	@SCJAllowed
	public void handleAsyncLongEvent(long data) {

		for (int j = 0; j < 200; j++)
			for (int i = 0; i < 3749; i++)
				;

		AbsoluteTime end = rtc.getTime();
		servs[(int) data] = end.getMilliseconds();
		serv++;

	}

}
