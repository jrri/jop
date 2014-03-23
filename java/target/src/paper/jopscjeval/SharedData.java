package jopscjeval;

import javax.realtime.AbsoluteTime;

public class SharedData {

	long[] startTimes;
	long[] endTimes;
	
	AbsoluteTime[] start;
	AbsoluteTime[] end;
	
	public SharedData(int limit) {
		this.startTimes = new long[limit];
		this.endTimes = new long[limit];
		
		this.start = new AbsoluteTime[limit];
		this.end = new AbsoluteTime[limit];
		
		for(int i = 0; i < limit; i++){
			start[i] = new AbsoluteTime();
			end[i] = new AbsoluteTime();
		}
		
		
		
	}
	

}
