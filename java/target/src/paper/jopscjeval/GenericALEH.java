package jopscjeval;

import javax.realtime.AperiodicParameters;
import javax.realtime.PriorityParameters;
import javax.safetycritical.AperiodicLongEventHandler;
import javax.safetycritical.StorageParameters;

public abstract class GenericALEH extends AperiodicLongEventHandler {

	public GenericALEH(int priority, int bsSize, int memSizestorage, String name) {
		super(new PriorityParameters(priority), new AperiodicParameters(), 
				new StorageParameters(bsSize, null, memSizestorage, 0, 0), name);
	}


}