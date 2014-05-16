package com.jopdesign.io;

import com.jopdesign.sys.Const;
import com.jopdesign.sys.JVMHelp;
import com.jopdesign.sys.Native;

public class HwArrayIOFactory {

	// Handles should be the first static fields!
	private static int I2C_A_PTR;
	private static int I2C_A_LEN;

	private static int I2C_B_PTR;
	private static int I2C_B_LEN;

	private int[] i2cA;
	private int[] i2cB;

	HwArrayIOFactory() {
		i2cA = makeHWArray(16, Const.I2C_A_BASE, 0);
		i2cB = makeHWArray(16, Const.I2C_B_BASE, 1);
	};

	// that has to be overridden by each sub class to get
	// the correct cp
	private static int[] makeHWArray(int len, int address, int idx) {
		int cp = Native.rdIntMem(Const.RAM_CP);
		return JVMHelp.makeHWArray(len, address, idx, cp);
	}

	static HwArrayIOFactory single = new HwArrayIOFactory();

	public static HwArrayIOFactory getFactory() {
		return single;
	}

	public int[] getDevice(int address) {

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
