package scjtck;

import javax.realtime.AbsoluteTime;
import javax.realtime.Clock;
import javax.realtime.HighResolutionTime;
import javax.realtime.RelativeTime;
import javax.safetycritical.Safelet;
import javax.safetycritical.annotate.Level;
import javax.safetycritical.annotate.Phase;
import javax.safetycritical.annotate.SCJAllowed;
import javax.safetycritical.annotate.SCJRestricted;

public class TestClocks extends TestCase implements Safelet{
	
	
	public String getName(){
		return "Test clocks";
		
	}
	
	@Override
	@SCJAllowed(Level.SUPPORT)
	protected void initialize() {
		
//		testCompareTo1();
//		testCompareTo2();
		testAdd();
		System.out.println("testAdd: "+testAdd());
		
//		Clock clk = Clock.getRealtimeClock();
//		
//		RelativeTime rt = new RelativeTime();
//		clk.getResolution(rt);
//		System.out.println(rt.getMilliseconds());
//		System.out.println(rt.getNanoseconds());
//		System.out.println(rt.toString());
		
		
//		clk.getEpochOffset();
//		clk.getResolution();
//		clk.getResolution(dest);
//		clk.getTime();
//		clk.getTime(dest);
		
//		Clock clk_2 = clk.getRealtimeClock();
//		
//		if(clk == clk_2){
////			System.out.println("good");
//		}
//		
//		AbsoluteTime rtc = Clock.getRealtimeClock().getTime();
//		AbsoluteTime preTime = new AbsoluteTime(rtc);
//		
//
//		
//		System.out.println("Clocks compared: "+timeA.compareTo(timeB));
//		
//		System.out.println(rtc.toString());
//		System.out.println(preTime.toString());

//		long milis = 0, nanos = 0, current_milis = 0,current_nanos = 0;
//		
		// If the initial values are negative this test does not work
//		for(int i=0; i<1000; i++){
//			clk.getTime(preTime);
//			current_milis = preTime.getMilliseconds();
//			current_nanos = preTime.getNanoseconds();
//			
//			if(milis < current_milis){
//				
//			}else if (milis == current_milis){
//				if(nanos < current_nanos){
//					
//				}else if(nanos == current_nanos){
//					
//				}else{
//					System.out.println("bad");
//					break;
//				}
//			}else{
//				System.out.println("bad");
//				break;
//			}
			
//			System.out.println(preTime.getMilliseconds()+":"+preTime.getNanoseconds());
//		}
//		
//		System.out.println(preTime.getMilliseconds());
//		System.out.println(preTime.getNanoseconds());
//		
//		System.out.println(rtc.getMilliseconds());
//		System.out.println(rtc.getNanoseconds());
//
//		
	}
	
	public boolean testCompareTo1(){
		boolean ok = true;
		
		AbsoluteTime timeA = new AbsoluteTime(100,0);
		AbsoluteTime timeB = new AbsoluteTime(100,0);
		AbsoluteTime timeC = new AbsoluteTime(99,0);
		AbsoluteTime timeD = new AbsoluteTime(101,0);
		AbsoluteTime timeE = new AbsoluteTime(100,10);
		AbsoluteTime timeF = new AbsoluteTime(100,10);
		
		// Equal times
		if (timeA.compareTo(timeB) != 0)
			ok = false;
		if (timeE.compareTo(timeF) != 0)
			ok = false;
		// timeA bigger than timeC
		if (timeA.compareTo(timeC) != 1)
			ok = false;
		// timeA smaller than timeD
		if (timeA.compareTo(timeD) != -1)
			ok = false;
		// timeA smaller than timeE in nanosecond part
		if (timeA.compareTo(timeE) != -1)
			ok = false;
		// timeF bigger than timeB in nanosecond part
		if (timeF.compareTo(timeB) != 1)
			ok = false;
		return ok;
	}
	
	public boolean testCompareTo2(){
		boolean ok = true;
		AbsoluteTime timeA = new AbsoluteTime(100,0);
		Object obj = new Object();
		
		try {
			timeA.compareTo(obj);
		} catch (ClassCastException cc) {
			
		}
		
		try{
			timeA.compareTo(null);
		} catch (IllegalArgumentException ia) {
			
		}
		
		RelativeTime timeB = new RelativeTime();
		
		System.out.println(timeA.compareTo(timeB));
		
		return ok;
	}
	
	public boolean testAdd(){
		boolean ok = true;
		
		AbsoluteTime timeA = new AbsoluteTime(100,0);
		AbsoluteTime timeB = new AbsoluteTime(Long.MAX_VALUE,0);
		AbsoluteTime timeC = new AbsoluteTime(Long.MIN_VALUE,0);
		RelativeTime relTimeA = new RelativeTime(500,0);
		
		AbsoluteTime newTime = new AbsoluteTime();
		
		newTime = timeA.add(1000, 100);
		
		if((newTime.getMilliseconds() != 1100) | (newTime.getNanoseconds() != 100))
			ok = false;
		
		timeA.add(2000, 200, newTime);
		if((newTime.getMilliseconds() != 2100) | (newTime.getNanoseconds() != 200))
			ok = false;
		
		newTime = timeA.add(relTimeA);
		if((newTime.getMilliseconds() != 600) | (newTime.getNanoseconds() != 0))
			ok = false;
		
		timeA.add(relTimeA, timeA);
		if((newTime.getMilliseconds() != 600) | (newTime.getNanoseconds() != 0))
			ok = false;
		
		// Overflows
		try{
			timeB.add(1, 0);
		} catch (Exception ae) {
			if(!(ae instanceof ArithmeticException))
				ok = false;
		}
		
		try{
			timeC.add(-1, 0);
		} catch (Exception ae) {
			if(!(ae instanceof ArithmeticException))
				ok = false;
		}
		
		return ok;
	}

	@Override
	@SCJAllowed(Level.SUPPORT)
	@SCJRestricted(phase = Phase.INITIALIZATION)
	public void initializeApplication() {
		// TODO Auto-generated method stub
		
	}

	@Override
	@SCJAllowed(Level.SUPPORT)
	public long immortalMemorySize() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	@SCJAllowed
	public long missionMemorySize() {
		// TODO Auto-generated method stub
		return 0;
	}

}
