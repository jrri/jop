package com.jopdesign.io;

import com.jopdesign.sys.Const;
import com.jopdesign.sys.JVMHelp;
import com.jopdesign.sys.Native;

public class GenericHwArrayIOFactory {
	
	// Handles should be the first static fields!
	private static int A0_PTR;
	private static int A0_LEN;

	private static int A1_PTR;
	private static int A1_LEN;

	GenericAccessorHwArray IODevice_0 = new GenericAccessorHwArray();
	GenericAccessorHwArray IODevice_1 = new GenericAccessorHwArray();
			
	GenericHwArrayIOFactory() {
			IODevice_0.data = makeHWArray(16, Const.I2C_A_BASE, 0);
			IODevice_1.data = makeHWArray(16, Const.I2C_B_BASE, 1);
	};

	// that has to be overridden by each sub class to get
	// the correct cp
	private static int[] makeHWArray(int len, int address, int idx) {
		int cp = Native.rdIntMem(Const.RAM_CP);
		return JVMHelp.makeHWArray(len, address, idx, cp);
	}

	static GenericHwArrayIOFactory single = new GenericHwArrayIOFactory();

	public static GenericHwArrayIOFactory getFactory() {
		return single;
	}

	public GenericAccessorHwArray getDevice(int address) {
		
		int index = address & 0xFFFFFFF0;
		
		switch (index) {
		case Const.I2C_A_BASE:
			return IODevice_0;
		case Const.I2C_B_BASE:
			return IODevice_1;
		default:
			return null;
		}
	}

}
