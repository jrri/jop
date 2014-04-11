package jopscjeval;

import javax.realtime.RelativeTime;

public class Utils {
	
	public static void statisctics(RelativeTime[] values){
		double sum = 0;
		double sqSum = 0;
		
		double data = 0;
		int N = 0;
		
		double max = Long.MIN_VALUE;
		double min = Long.MAX_VALUE;

		double avg = 0;
		double stdev = 0;
		
		N = values.length;
		
		for(int i = 1; i < N; i++){
			data = values[i].getMilliseconds() * 1000
					+ values[i].getNanoseconds() / 1000;
			
			if(data < min)
				min = data;
			
			if(data > max)
				max = data;
			
			sum += data;
			sqSum += data * data;
		}
		
		avg = sum/(N-1);
		stdev = Math.sqrt((sqSum/(N-1) - (avg * avg)));
		System.out.println(stdev);
		
		RelativeTime mi = new RelativeTime((long) min/1000, (int) (min % 1000) * 1000);
		RelativeTime ma = new RelativeTime((long) max/1000, (int) (max % 1000) * 1000);
		RelativeTime av = new RelativeTime((long) avg/1000, (int) (avg % 1000) * 1000);
		RelativeTime st = new RelativeTime((long) stdev/1000, (int) (stdev % 1000) * 1000);
		
		System.out.println();
		System.out.println("---------- Statistics ----------");
		System.out.println("\tSamples: "+N);
		
		// min
		System.out.print("\t" + mi);
		// max
		System.out.print("\t" + ma);
		// avg
		System.out.print("\t" + av);
		// stdev
		System.out.print("\t" + st);

		System.out.println();
		System.out.println("-------- End statistics --------");
		System.out.println();
	}
	
	public static void statistics(long[] start, long[] end) {
		
		double sum = 0;
		double data = 0;
		int N = 0;
		double max = Long.MIN_VALUE;
		double min = Long.MAX_VALUE;

		double avg = 0;
		double stdev_sum = 0;
		double stdev = 0;

		N = end.length;

		for (int i = 1; i < N; i++) {

			if(start == null)
				data = end[i];
			else
				data = end[i] - start[i];
			
			if (data > max)
				max = data;

			if (data < min)
				min = data;

			sum += data;

		}

		avg = sum / (N-1);

		for (int i = 1; i < N; i++) {
			if(start == null)
				data = end[i];
			else
				data = end[i] - start[i];
			stdev_sum += (data - avg) * (data - avg);
		}

		stdev = Math.sqrt(stdev_sum / (N - 1));

		System.out.println();
		System.out.println("---------- Statistics ----------");
		
		System.out.println("\tSamples: "+N);
		// min
		System.out.print("\t" + min);
		// max
		System.out.print("\t" + max);
		// avg
		System.out.print("\t" + avg);
		// stdev
		System.out.print("\t" + stdev);

		System.out.println();
		System.out.println("-------- End statistics --------");
		System.out.println();
	}

}
