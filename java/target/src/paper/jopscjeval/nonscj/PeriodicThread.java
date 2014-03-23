package jopscjeval.nonscj;

import com.jopdesign.sys.Const;
import com.jopdesign.sys.Native;
import com.jopdesign.sys.RtThreadImpl;

import joprt.RtThread;

public class PeriodicThread {
	
	/* Global data structures */
	static int[] times;
	static int runs = 100;
	int period = 150;


	public static void main(String[] args){

		final PeriodicThread app = new PeriodicThread();
		times = new int[runs];
		

		RtThread rt = new RtThread(15, app.period*1000) {
			
			public void run() {
				
				int now,i = -1;
				do {
					i++;
					waitForNextPeriod();
					now = Native.rd(Const.IO_US_CNT);
					times[i] = now;
				}while(i < times.length-1);
				
				System.out.println("-----------------");
				
				int initial, expected,diff;
//				initial = times[0];
				initial = RtThreadImpl.startTime;

				System.out.println("initial: "+times[0]);
				System.out.println("second: "+times[1]);
				
				for (int j = 0; j < times.length; j++) {

//					if (j != 0) {
						expected = initial + (j+1) * app.period * 1000;
						diff = times[j] - expected;
						System.out.println(diff);
//					}
				}
				
				System.out.println("JVM exit!");
			}
		};
		
		RtThreadImpl.register(rt.thr);
		RtThreadImpl.startMission();
		
	}

}
