package test;

import javax.realtime.RawInt;
import javax.safetycritical.annotate.Level;
import javax.safetycritical.annotate.SCJAllowed;
import javax.safetycritical.annotate.SCJRestricted;

import com.jopdesign.io.HwObjectIOFactory;
import com.jopdesign.io.I2Cport;

public class IODeviceHwObject implements RawInt {

	private int offset;
	private I2Cport device;

	public IODeviceHwObject(int address) {
		offset = address & 0x0000000F;
		/*
		 * Get device according to its base address. Each device can have up to
		 * 16 registers
		 */
		this.device = HwObjectIOFactory.getFactory().getDevice(
				address & 0xFFFFFFF0);
	}

	@Override
	@SCJAllowed(Level.LEVEL_0)
	@SCJRestricted(mayAllocate = false, maySelfSuspend = false)
	public int get() {

		switch (offset) {
		case 0:
			return device.control;
		case 1:
			return device.status;
		case 2:
			return device.devadd;
			// and the rest...
		default:
			return 0;
		}

	}

	@Override
	@SCJAllowed(Level.LEVEL_0)
	@SCJRestricted(mayAllocate = false, maySelfSuspend = false)
	public void put(int value) {
		switch (offset) {
		case 0:
			device.control = value;
			break;
		case 1:
			device.status = value;
			break;
		case 2:
			device.devadd = value;
			break;
		// and the rest...
		default:
			break;
		}

	}

}
