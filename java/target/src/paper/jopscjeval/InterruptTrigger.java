package jopscjeval;

import javax.realtime.AbsoluteTime;
//import javax.realtime.RealtimeClock;
import javax.safetycritical.annotate.SCJAllowed;

import com.jopdesign.io.SysDevice;
import com.jopdesign.sys.Const;
import com.jopdesign.sys.Native;

public class InterruptTrigger extends GenericPEH {

	int id;
	int winner;
	SysDevice sys;
	AbsoluteTime start;
	SharedData shared;

	int now;

	public InterruptTrigger(int prio, long periodMs, int periodNs, int bsSize,
			int memSizestorage, String name, int id, int winner,
			SharedData shared) {
		super(prio, periodMs, periodNs, bsSize, memSizestorage, name);

		this.id = id;
		this.winner = winner;
		this.shared = shared;
	}

	@Override
	@SCJAllowed
	public void handleAsyncEvent() {

		/*
		 * Only the "winner" thread fires the interrupt, the others do a busy
		 * loop
		 */
		if (id == winner & ImmortalEntry.relNo < ImmortalEntry.relLimit - 1) {
			ImmortalEntry.relNo++;
			// System.out.println(ImmortalEntry.relNo);
			fireInterrupt();
		} else {
			int delay = 20000;
			for (int i = 0; i < delay; i++)
				;
		}

	}

	void fireInterrupt() {

		/* Using the jop's microsecond counter */
		int now = Native.rd(Const.IO_US_CNT);

		/* Using SCJ's time API */
		// AbsoluteTime now =
		// ImmortalEntry.rtc.getTime(shared.start[ImmortalEntry.relNo]);

		/* Fire low priority interrupt */
		ImmortalEntry.sys.intNr = 2;
		shared.startTimes[ImmortalEntry.relNo] = (long) now;

	}

}
