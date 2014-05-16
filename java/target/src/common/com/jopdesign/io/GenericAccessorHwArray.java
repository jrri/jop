package com.jopdesign.io;

import javax.realtime.RawIntArray;

public class GenericAccessorHwArray extends HardwareObject implements RawIntArray{
	
	int[] data;

	@Override
	public int get(long offset) {
		// TODO Auto-generated method stub
		return data[(int) offset];
	}

	@Override
	public void get(int[] array) {
		for(int i = 0; i < data.length; i++)
			array[i] = data[i];
	}

	@Override
	public void put(int value, long offset) {
		data[(int) offset] = value;
	}

	@Override
	public void put(int[] array) {
		for(int i = 0; i < data.length; i++)
			data[i] = array[i];
	}

}
