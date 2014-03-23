package jopscjeval;

import javax.realtime.AbsoluteTime;
import javax.realtime.Clock;
import javax.realtime.PriorityScheduler;
import javax.realtime.RealtimeClock;
import javax.realtime.RelativeTime;
import javax.safetycritical.ManagedMemory;
import javax.safetycritical.Mission;
import javax.safetycritical.annotate.Level;
import javax.safetycritical.annotate.SCJAllowed;

import com.jopdesign.sys.Const;
import com.jopdesign.sys.GC;
import com.jopdesign.sys.Memory;
import com.jopdesign.sys.Native;
import com.jopdesign.sys.RtThreadImpl;

/**
 * Test that allocations in scoped memory is of linear time, that is, the time
 * it takes to allocate a chunk of memory is proportional to the size of the
 * chunk of memory.
 * 
 * @author Juan Rios jrri@dtu.dk
 * 
 */
public class Test001 extends Mission {

	int periodMs = 150;
	int periodNs = 470000;
	int MISSION_MEMORY = 32768;

	int MAX_PRIO = PriorityScheduler.instance().getMaxPriority();

	@Override
	@SCJAllowed(Level.SUPPORT)
	protected void initialize() {

		new GenericPEH(MAX_PRIO - 1, 100000, 0, 16384, 14336, "PEH") {

			Clock rtc = RealtimeClock.getRealtimeClock();
			AbsoluteTime start = new AbsoluteTime();
			AbsoluteTime end = new AbsoluteTime();
			
			long startL;
			long endL;

			int size = 256;
			int reps = 1000;
			
			long[] val = new long[reps];

			RelativeTime[] values = new RelativeTime[reps];
			
			int t = 0;
			long delta = 0;

			@Override
			@SCJAllowed
			public void handleAsyncEvent() {

				int[] data = new int[size];
//				int numReps = reps;
				
				long s,e;
				
//				Native.wr(1, Const.IO_INTCLEARALL);

				for (int i = 0; i < reps; i++) {

//					Native.wr(0, Const.IO_INTMASK);	
					Native.wr(0, Const.IO_INT_ENA);
//					Native.wr(1, Const.IO_INTCLEARALL);
					//rtc.getTime(start);
//					s = Native.rd(Const.IO_US_CNT);
					
//					delta = 0;
//					for (int j = 0; j < 2; j++) {
						// data[j] = 12345;
						s = Native.rd(Const.IO_US_CNT);

						data[0] = 12345;
						data[1] = 12345;
						data[2] = 12345;
						data[3] = 12345;
						data[4] = 12345;
						data[5] = 12345;
						data[6] = 12345;
						data[7] = 12345;
						data[8] = 12345;
						data[9] = 12345;
						data[10] = 12345;
						data[11] = 12345;
						data[12] = 12345;
						data[13] = 12345;
						data[14] = 12345;
						data[15] = 12345;
						data[16] = 12345;
						data[17] = 12345;
						data[18] = 12345;
						data[19] = 12345;
						data[20] = 12345;
						data[21] = 12345;
						data[22] = 12345;
						data[23] = 12345;
//						data[24] = 12345;
//						data[25] = 12345;
//						data[26] = 12345;
//						data[27] = 12345;
//						data[28] = 12345;
//						data[29] = 12345;
//						data[30] = 12345;
//						data[31] = 12345;
//						data[32] = 12345;
//						data[33] = 12345;
//						data[34] = 12345;
//						data[35] = 12345;
//						data[36] = 12345;
//						data[37] = 12345;
//						data[38] = 12345;
//						data[39] = 12345;
//						data[40] = 12345;
//						data[41] = 12345;
//						data[42] = 12345;
//						data[43] = 12345;
//						data[44] = 12345;
//						data[45] = 12345;
//						data[46] = 12345;
//						data[47] = 12345;
//						data[48] = 12345;
//						data[49] = 12345;
//						data[50] = 12345;
//						data[51] = 12345;
//						data[52] = 12345;
//						data[53] = 12345;
//						data[54] = 12345;
//						data[55] = 12345;
//						data[56] = 12345;
//						data[57] = 12345;
//						data[58] = 12345;
//						data[59] = 12345;
//						data[60] = 12345;
//						data[61] = 12345;
//						data[62] = 12345;
//						data[63] = 12345;
//						data[64] = 12345;
//						data[65] = 12345;
//						data[66] = 12345;
//						data[67] = 12345;
//						data[68] = 12345;
//						data[69] = 12345;
//						data[70] = 12345;
//						data[71] = 12345;
//						data[72] = 12345;
//						data[73] = 12345;
//						data[74] = 12345;
//						data[75] = 12345;
//						data[76] = 12345;
//						data[77] = 12345;
//						data[78] = 12345;
//						data[79] = 12345;
//						data[80] = 12345;
//						data[81] = 12345;
//						data[82] = 12345;
//						data[83] = 12345;
//						data[84] = 12345;
//						data[85] = 12345;
//						data[86] = 12345;
//						data[87] = 12345;
//						data[88] = 12345;
//						data[89] = 12345;
//						data[90] = 12345;
//						data[91] = 12345;
//						data[92] = 12345;
//						data[93] = 12345;
//						data[94] = 12345;
//						data[95] = 12345;
//						data[96] = 12345;
//						data[97] = 12345;
//						data[98] = 12345;
//						data[99] = 12345;
//						data[100] = 12345;
//						data[101] = 12345;
//						data[102] = 12345;
//						data[103] = 12345;
//						data[104] = 12345;
//						data[105] = 12345;
//						data[106] = 12345;
//						data[107] = 12345;
//						data[108] = 12345;
//						data[109] = 12345;
//						data[110] = 12345;
//						data[111] = 12345;
//						data[112] = 12345;
//						data[113] = 12345;
//						data[114] = 12345;
//						data[115] = 12345;
//						data[116] = 12345;
//						data[117] = 12345;
//						data[118] = 12345;
//						data[119] = 12345;
//						data[120] = 12345;
//						data[121] = 12345;
//						data[122] = 12345;
//						data[123] = 12345;
//						data[124] = 12345;
//						data[125] = 12345;
//						data[126] = 12345;
//						data[127] = 12345;
//						data[128] = 12345;
//						data[129] = 12345;
//						data[130] = 12345;
//						data[131] = 12345;
//						data[132] = 12345;
//						data[133] = 12345;
//						data[134] = 12345;
//						data[135] = 12345;
//						data[136] = 12345;
//						data[137] = 12345;
//						data[138] = 12345;
//						data[139] = 12345;
//						data[140] = 12345;
//						data[141] = 12345;
//						data[142] = 12345;
//						data[143] = 12345;
//						data[144] = 12345;
//						data[145] = 12345;
//						data[146] = 12345;
//						data[147] = 12345;
//						data[148] = 12345;
//						data[149] = 12345;
//						data[150] = 12345;
//						data[151] = 12345;
//						data[152] = 12345;
//						data[153] = 12345;
//						data[154] = 12345;
//						data[155] = 12345;
//						data[156] = 12345;
//						data[157] = 12345;
//						data[158] = 12345;
//						data[159] = 12345;
//						data[160] = 12345;
//						data[161] = 12345;
//						data[162] = 12345;
//						data[163] = 12345;
//						data[164] = 12345;
//						data[165] = 12345;
//						data[166] = 12345;
//						data[167] = 12345;
//						data[168] = 12345;
//						data[169] = 12345;
//						data[170] = 12345;
//						data[171] = 12345;
//						data[172] = 12345;
//						data[173] = 12345;
//						data[174] = 12345;
//						data[175] = 12345;
//						data[176] = 12345;
//						data[177] = 12345;
//						data[178] = 12345;
//						data[179] = 12345;
//						data[180] = 12345;
//						data[181] = 12345;
//						data[182] = 12345;
//						data[183] = 12345;
//						data[184] = 12345;
//						data[185] = 12345;
//						data[186] = 12345;
//						data[187] = 12345;
//						data[188] = 12345;
//						data[189] = 12345;
//						data[190] = 12345;
//						data[191] = 12345;
//						data[192] = 12345;
//						data[193] = 12345;
//						data[194] = 12345;
//						data[195] = 12345;
//						data[196] = 12345;
//						data[197] = 12345;
//						data[198] = 12345;
//						data[199] = 12345;
//						data[200] = 12345;
//						data[201] = 12345;
//						data[202] = 12345;
//						data[203] = 12345;
//						data[204] = 12345;
//						data[205] = 12345;
//						data[206] = 12345;
//						data[207] = 12345;
//						data[208] = 12345;
//						data[209] = 12345;
//						data[210] = 12345;
//						data[211] = 12345;
//						data[212] = 12345;
//						data[213] = 12345;
//						data[214] = 12345;
//						data[215] = 12345;
//						data[216] = 12345;
//						data[217] = 12345;
//						data[218] = 12345;
//						data[219] = 12345;
//						data[220] = 12345;
//						data[221] = 12345;
//						data[222] = 12345;
//						data[223] = 12345;
//						data[224] = 12345;
//						data[225] = 12345;
//						data[226] = 12345;
//						data[227] = 12345;
//						data[228] = 12345;
//						data[229] = 12345;
//						data[230] = 12345;
//						data[231] = 12345;
//						data[232] = 12345;
//						data[233] = 12345;
//						data[234] = 12345;
//						data[235] = 12345;
//						data[236] = 12345;
//						data[237] = 12345;
//						data[238] = 12345;
//						data[239] = 12345;
//						data[240] = 12345;
//						data[241] = 12345;
//						data[242] = 12345;
//						data[243] = 12345;
//						data[244] = 12345;
//						data[245] = 12345;
//						data[246] = 12345;
//						data[247] = 12345;
//						data[248] = 12345;
//						data[249] = 12345;
//						data[250] = 12345;
//						data[251] = 12345;
//						data[252] = 12345;
//						data[253] = 12345;
//						data[254] = 12345;
//						data[255] = 12345;
						
						e = Native.rd(Const.IO_US_CNT);
//						delta += e - s ;
					
//					}
						
//					e = Native.rd(Const.IO_US_CNT);
//					Native.wr(-1, Const.IO_INTMASK);		
					Native.wr(1, Const.IO_INT_ENA);
					
//					rtc.getTime(end);

					val[t] = e - s;
//					System.out.println(t);
//					System.out.println(i);
//					val[t] = delta;
					

					ManagedMemory.executeInAreaOf(values, new Runnable() {

						@Override
						public void run() {
							values[t] = end.subtract(start);

						}
					});
					
					t++;

				}
				Native.wr(1, Const.IO_INT_ENA);

				ManagedMemory.enterPrivateMemory(1024, new Runnable() {
					@Override
					public void run() {

						double sum = 0;
						double max = Long.MIN_VALUE;
						double min = Long.MAX_VALUE;

						double avg = 0;
						double stdev_sum = 0;
						double stdev = 0;
						
						double data = 0;
						
						for (int i = 0; i < reps; i++) {
							
//							data = (values[i].getMilliseconds() * 1000
//									+ values[i].getNanoseconds() / 1000);
							data = val[i];
							sum += data;
							
							final double d = data;
							
							if(data < min)
								min = data;
							
							if(data > max)
								max = data;
						}

						avg = sum / reps;
						
						
						
						for (int i = 0; i < reps; i++) {
//							data = (values[i].getMilliseconds() * 1000
//									+ values[i].getNanoseconds() / 1000);
							data = val[i];
							stdev_sum += (data - avg)*(data - avg);
						}
						
						stdev = Math.sqrt(stdev_sum / (reps - 1));
						
						// min
						System.out.print("\t" + min + " &");
						// max
						System.out.print("\t" + max + " &");
						// avg
						System.out.print("\t" + avg + " &");
						// stdev
						System.out.print("\t" + stdev + " \\\\");
						
						System.out.println();


					}
				});

//				size *= 2;

//				if (size > 8192)
					Mission.getCurrentMission().requestTermination();

			}

		}.register();

		// new LTMemoryTest(15, periodMs, periodNs, 262140, 262140, "PEH")
		// .register();
	}

	@Override
	@SCJAllowed
	public long missionMemorySize() {
		return MISSION_MEMORY;
	}

}
