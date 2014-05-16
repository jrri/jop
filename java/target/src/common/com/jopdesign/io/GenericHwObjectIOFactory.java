package com.jopdesign.io;

import com.jopdesign.sys.Const;
import com.jopdesign.sys.JVMHelp;
import com.jopdesign.sys.Native;

public class GenericHwObjectIOFactory {
	
	// Handles should be the first static fields!
	private static int OBJ_0_PTR;
	private static int OBJ_0_MTAB;

	private static int OBJ_1_PTR;
	private static int OBJ_1_MTAB;

	private static int OBJ_2_PTR;
	private static int OBJ_2_MTAB;

	private static int OBJ_3_PTR;
	private static int OBJ_3_MTAB;

	private static int OBJ_4_PTR;
	private static int OBJ_4_MTAB;

	private static int OBJ_5_PTR;
	private static int OBJ_5_MTAB;

	private static int OBJ_6_PTR;
	private static int OBJ_6_MTAB;

	private static int OBJ_7_PTR;
	private static int OBJ_7_MTAB;

	private static int OBJ_8_PTR;
	private static int OBJ_8_MTAB;

	// We need one object per register
	int registers = 9;
	GenericRawMemAccessorHwObject[] IODevice = new GenericRawMemAccessorHwObject[registers]; 
			
	GenericHwObjectIOFactory() {
		
		for(int i = 0; i < registers; i ++){
			IODevice[i] = (GenericRawMemAccessorHwObject) makeHWObject(new GenericRawMemAccessorHwObject(),
					Const.I2C_A_BASE + i, i);
		}
		
	};

	// that has to be overridden by each sub class to get
	// the correct cp
	private static Object makeHWObject(Object o, int address, int idx) {
		int cp = Native.rdIntMem(Const.RAM_CP);
		return JVMHelp.makeHWObject(o, address, idx, cp);
	}

	static GenericHwObjectIOFactory single = new GenericHwObjectIOFactory();

	public static GenericHwObjectIOFactory getFactory() {
		return single;
	}

	public GenericRawMemAccessorHwObject getDevice(int address) {
		int index = address & 0x0000000F;
		return IODevice[index];
	}

}
