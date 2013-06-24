package javax.safetycritical;

import javax.safetycritical.annotate.SCJAllowed;
import javax.safetycritical.annotate.SCJRestricted;

import com.jopdesign.sys.Memory;
import com.jopdesign.sys.SysHelper;

/**
 * MissionMemory is basically an empty class and might go.
 * 
 */
@SCJAllowed
public class MissionMemory extends ManagedMemory {

	static SysHelper _sysHelper;

	public static void setHelper(SysHelper sysHelper) {
		_sysHelper = sysHelper;
	}

	/**
	 * Package private constructor
	 * 
	 * @param size
	 *            is the amount of memory that this area can hold.
	 * @param bsSize
	 *            is the total size of the backing store for this area
	 */
	MissionMemory(int size, int bsSize) {
		memory = _sysHelper.getMemory(size, bsSize);
	}

	/**
	 * Package private constructor
	 * 
	 * @param size
	 *            is the amount of memory that this area can hold.
	 */
	MissionMemory(int size) {
		this(size, 0);
	}

	void enter(Runnable logic) {
		_sysHelper.enter(memory, logic);
	}

}
