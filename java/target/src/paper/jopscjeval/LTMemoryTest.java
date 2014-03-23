package jopscjeval;

import javax.safetycritical.ManagedMemory;
import javax.safetycritical.Mission;
import javax.safetycritical.annotate.SCJAllowed;

import com.jopdesign.sys.Const;
import com.jopdesign.sys.Memory;
import com.jopdesign.sys.Native;
import com.jopdesign.sys.RtThreadImpl;
import com.jopdesign.sys.Scheduler;

public class LTMemoryTest extends GenericPEH {
	
	int runs = 16;
	int[] times;
	int i = 1;
	int iter = 0;

	public LTMemoryTest(int prio, long periodMs, int periodNs, int bsSize, int memSize,
			String name) {
		super(prio, periodMs, periodNs, bsSize, memSize, name);
		
		times = new int[runs];
		
	}
	
	@Override
	@SCJAllowed
	public void handleAsyncEvent() {
		
		i = 2*i;
		int start, end;
		int size = i;
		int[] data = new int[size];

		
		System.out.print(size);
		System.out.print("\t");
//		
//		Scheduler s = Scheduler.sched[RtThreadImpl.sys.cpuId];
//		Memory sc = s.ref[s.active].currentArea;
//		int ptr = sc.allocPtr;
		
		if(iter < runs-1){
			start = Native.rd(Const.IO_US_CNT);
//			for(int j = 0; j<size;j++)
//				Native.wr(123456, ptr+j);
			for(int j = 0; j<size;j++)
				data[j] = 12345;
			
			
			end = Native.rd(Const.IO_US_CNT);
			
			System.out.println(end-start);
			iter++;
			
			ManagedMemory.executeInOuterArea(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					
				}
			});
			
		}else{
			Mission.getCurrentMission().requestTermination();
		}
		
		
		
	}

}
