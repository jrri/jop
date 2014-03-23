package jopscjeval;

import javax.safetycritical.ManagedInterruptServiceRoutine;
import javax.safetycritical.StorageParameters;

public abstract class GenericISR extends ManagedInterruptServiceRoutine{

	public GenericISR(long bsStore, long memSize) {
		super(new StorageParameters(bsStore, null, memSize, 0, 0));
	}

}
