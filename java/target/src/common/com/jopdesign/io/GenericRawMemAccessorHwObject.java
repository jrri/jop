package com.jopdesign.io;

import javax.realtime.RawInt;
import javax.safetycritical.annotate.Level;
import javax.safetycritical.annotate.SCJAllowed;
import javax.safetycritical.annotate.SCJRestricted;

public class GenericRawMemAccessorHwObject extends HardwareObject implements RawInt {

	private int data;

	@Override
	@SCJAllowed(Level.LEVEL_0)
	@SCJRestricted(mayAllocate = false, maySelfSuspend = false)
	public int get() {
		return data;
	}

	@Override
	@SCJAllowed(Level.LEVEL_0)
	@SCJRestricted(mayAllocate = false, maySelfSuspend = false)
	public void put(int value) {
		data = value;
	}

}
