package jopscjeval.nonscj;

import jopscjeval.Utils;

import com.jopdesign.io.IOFactory;
import com.jopdesign.io.SysDevice;
import com.jopdesign.sys.Const;
import com.jopdesign.sys.Native;

public class InterruptLatency {
	
	static int lim = 1000;
	static long[] s = new long[lim];
	static long[] e = new long[lim];
	static int ctr = 0;
	
	public static void main(String[] args) {
		
		int nowMain;
		
		IOFactory factory = IOFactory.getFactory();		
		SysDevice sys = factory.getSysDevice();
		
		InterruptHandler ih = new InterruptHandler();
		factory.registerInterruptHandler(1, ih);
		
		factory.enableInterrupt(1);
		
		for(ctr = 0; ctr < lim; ctr++){
			nowMain = Native.rd(Const.IO_US_CNT); 
			sys.intNr = 1;
			
			/* The value can be assigned after servicing the interrupt */
			s[ctr] = nowMain;
			
		}
		
			
			Utils.statistics(s, e);

		
	}
	
	static class InterruptHandler implements Runnable {

		@Override
		public void run() {
			
			int nowISR = Native.rd(Const.IO_US_CNT);
			e[ctr] = nowISR;
			
		}
		
		
		
		
	}

}
