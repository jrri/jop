package jopscjeval;

import javax.realtime.PeriodicParameters;
import javax.realtime.PriorityParameters;
import javax.realtime.RelativeTime;
import javax.safetycritical.Mission;
import javax.safetycritical.PeriodicEventHandler;
import javax.safetycritical.StorageParameters;

public abstract class GenericPEH extends PeriodicEventHandler {

//	public static class Builder {
//		// Required parameters
//		private final int prio;
//		private final long periodMs;
//		private final int bsSize;
//		private final int memSizestorage;
//
//		// Optional
//		private int periodNs = 0;
//		private long offMs = 0;
//		private int offNs = 0;
//		private String name = null;
//
//		public Builder(int prio, long periodMs, int bsSize, int memSizestorage) {
//
//			this.prio = prio;
//			this.periodMs = periodMs;
//			this.bsSize = bsSize;
//			this.memSizestorage = memSizestorage;
//
//		}
//
//		public Builder periodNs(int val) {
//			periodNs = val;
//			return this;
//		}
//
//		public Builder offMs(long val) {
//			offMs = val;
//			return this;
//		}
//
//		public Builder offNs(int val) {
//			offNs = val;
//			return this;
//		}
//
//		public Builder name(String val) {
//			name = val;
//			return this;
//		}
//
//		public GenericPEH build() {
//			return new GenericPEH(this);
//		}
//	}
//
//	private GenericPEH(Builder builder) {
//		
//		super(new PriorityParameters(builder.prio), new PeriodicParameters(
//				new RelativeTime(builder.offMs, builder.offNs),
//				new RelativeTime(builder.periodMs, builder.periodNs)),
//				new StorageParameters(builder.bsSize, null,
//						builder.memSizestorage, 0, 0), builder.name);
//
//	}

	public GenericPEH(int prio, long periodMs, int periodNs, long offMs,
			int offNs, int bsSize, int memSizestorage, String name) {

		super(new PriorityParameters(prio), new PeriodicParameters(
				new RelativeTime(offMs, offNs), new RelativeTime(periodMs,
						periodNs)), new StorageParameters(bsSize, null,
				memSizestorage, 0, 0), name);
	}

	public GenericPEH(int prio, long periodMs, int periodNs, int bsSize,
			int memSizestorage, String name) {
		this(prio, periodMs, periodNs, 0, 0, bsSize, memSizestorage, name);
	}

}
