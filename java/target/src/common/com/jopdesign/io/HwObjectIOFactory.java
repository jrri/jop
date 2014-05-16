package com.jopdesign.io;

import com.jopdesign.sys.Const;
import com.jopdesign.sys.JVMHelp;
import com.jopdesign.sys.Native;

public class HwObjectIOFactory {

	// Handles should be the first static fields!
	private static int I2C_A_PTR;
	private static int I2C_A_LEN;

	private static int I2C_B_PTR;
	private static int I2C_B_LEN;

	private I2Cport i2cA;
	private I2Cport i2cB;

	HwObjectIOFactory() {
		i2cA = (I2Cport) makeHWObject(new I2Cport(), Const.I2C_A_BASE, 0);
		i2cB = (I2Cport) makeHWObject(new I2Cport(), Const.I2C_B_BASE, 1);
	};

	// that has to be overridden by each sub class to get
	// the correct cp
	private static Object makeHWObject(Object o, int address, int idx) {
		int cp = Native.rdIntMem(Const.RAM_CP);
		return JVMHelp.makeHWObject(o, address, idx, cp);
	}

	static HwObjectIOFactory single = new HwObjectIOFactory();

	public static HwObjectIOFactory getFactory() {
		return single;
	}

	public I2Cport getDevice(int address) {

		switch (address) {
		case Const.I2C_A_BASE:
			return i2cA;
		case Const.I2C_B_BASE:
			return i2cB;
		default:
			return null;
		}
	}

}
