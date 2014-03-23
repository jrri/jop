package jopscjeval;

public class Utils {

	public static void statistics(long[] start, long[] end) {
		
		double sum = 0;
		double data = 0;
		int N = 0;
		double max = Long.MIN_VALUE;
		double min = Long.MAX_VALUE;

		double avg = 0;
		double stdev_sum = 0;
		double stdev = 0;

		N = start.length;

		for (int i = 0; i < N; i++) {

			data = end[i] - start[i];
			
			if (data > max)
				max = data;

			if (data < min)
				min = data;

			sum += data;

		}

		avg = sum / N;

		for (int i = 0; i < N; i++) {
			data = end[i] - start[i];
			stdev_sum += (data - avg) * (data - avg);
		}

		stdev = Math.sqrt(stdev_sum / (N - 1));

		// min
		System.out.print("\t" + min);
		// max
		System.out.print("\t" + max);
		// avg
		System.out.print("\t" + avg);
		// stdev
		System.out.print("\t" + stdev);

		System.out.println();

	}

}
