package test;

import javax.realtime.RawInt;
import javax.safetycritical.annotate.Level;
import javax.safetycritical.annotate.SCJAllowed;
import javax.safetycritical.annotate.SCJRestricted;

import com.jopdesign.io.HwArrayIOFactory;

public class IODeviceHwArray implements RawInt {

	private int pos;
	private int[] device;

	public IODeviceHwArray(int address) {
		pos = address & 0x0000000F;
		/*
		 * Get device according to its base address. Each device can have up to
		 * 16 registers
		 */
		this.device = HwArrayIOFactory.getFactory().getDevice(
				address & 0xFFFFFFF0);
	}

	@Override
	@SCJAllowed(Level.LEVEL_0)
	@SCJRestricted(mayAllocate = false, maySelfSuspend = false)
	public int get() {
		return device[pos];
	}

	@Override
	@SCJAllowed(Level.LEVEL_0)
	@SCJRestricted(mayAllocate = false, maySelfSuspend = false)
	public void put(int value) {
		device[pos] = value;
	}

}
