package jopscjeval;

import javax.safetycritical.ManagedMemory;
import javax.safetycritical.Services;
import javax.safetycritical.annotate.Level;
import javax.safetycritical.annotate.Phase;
import javax.safetycritical.annotate.SCJAllowed;
import javax.safetycritical.annotate.SCJRestricted;

import com.jopdesign.sys.Const;
import com.jopdesign.sys.Native;

public class Worker extends GenericPEH {
	
	int delayMs;
//	int delay_ns;
	
	int releases = 0;

	public Worker(int prio, long periodMs, int periodNs, int bsSize,
			int memSizestorage, int workMs, String name) {
		super(prio, periodMs, periodNs, bsSize, memSizestorage, name);
		
		delayMs = workMs;
//		delay_ns = delayMs*1000;
		
	}
	
	@Override
	@SCJAllowed
	public void handleAsyncEvent() {
		
		releases++;
//		int s,e;
//		s = Native.rd(Const.IO_US_CNT);
		
		/* thousands of ns */
//		Services.nanoSpin(delay_ns);
		
		 for(int j=0;j<delayMs;j++)
			 for(int i = 0; i < 3156; i++); // ~ 1 ms
		
//		e = Native.rd(Const.IO_US_CNT);
		
//		System.out.println(e-s);
//		System.out.println(getName());
	}
		
	@Override
	@SCJAllowed(Level.SUPPORT)
	@SCJRestricted(phase = Phase.CLEANUP)
	public void cleanUp() {
		ManagedMemory.enterPrivateMemory(512, new Runnable() {
			
			@Override
			public void run() {
				System.out.println(getName()+": "+ releases + ", " + missedDeadlines);
			}
		});
		
	}

}
