package test;

import javax.realtime.RawInt;
import javax.safetycritical.annotate.Level;
import javax.safetycritical.annotate.SCJAllowed;
import javax.safetycritical.annotate.SCJRestricted;

import com.jopdesign.sys.Native;

public class IODeviceNative implements RawInt {

	int address;

	public IODeviceNative(int address) {
		this.address = address;
	}

	@Override
	@SCJAllowed(Level.LEVEL_0)
	@SCJRestricted(mayAllocate = false, maySelfSuspend = false)
	public int get() {
		return Native.rdMem(address);
	}

	@Override
	@SCJAllowed(Level.LEVEL_0)
	@SCJRestricted(mayAllocate = false, maySelfSuspend = false)
	public void put(int value) {
		Native.wrMem(value, address);
	}

}
