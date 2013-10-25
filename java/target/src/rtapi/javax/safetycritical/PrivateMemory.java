package javax.safetycritical;

import javax.safetycritical.annotate.SCJAllowed;
import javax.safetycritical.annotate.SCJRestricted;

import com.jopdesign.sys.Memory;
import com.jopdesign.sys.SysHelper;

/**
 * This class cannot be directly instantiated by the application; hence there
 * are no public constructors. Every PeriodicEventHandler is provided with one
 * instance of PrivateMemory, its root private memory area. A schedulable object
 * active within a private memory area can create nested private memory areas
 * through the enterPrivateMemory method of ManagedMemory.
 * 
 * The rules for nested entering into a private memory are that the private
 * memory area must be the current allocation context, and the calling
 * schedulable object has to be the owner of the memory area. The owner of the
 * memory area is defined to be the schedulable object that created it.
 * 
 * @version SCJ 0.93
 */
@SCJAllowed
public class PrivateMemory extends ManagedMemory {
	
	PrivateMemory(int size, int bsSize) {
		super(size, bsSize);
//		memory = _sysHelper.getMemory(size, bsSize);
//		_sysHelper.setManagedMemory(memory, this);
	}

	PrivateMemory(int size) {
		this(size, 0);
	}
	
	PrivateMemory(){
		super();
	}
	
	void enter(Runnable logic){
		_sysHelper.enter(memory, logic);
		inner = null;
	}

}
