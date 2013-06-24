package javax.safetycritical;

import javax.realtime.InaccessibleAreaException;
import javax.realtime.LTMemory;
import javax.realtime.SizeEstimator;
import javax.safetycritical.annotate.Phase;
import javax.safetycritical.annotate.SCJAllowed;
import javax.safetycritical.annotate.SCJRestricted;


import com.jopdesign.sys.Memory;
import com.jopdesign.sys.SysHelper;

/**
 * This is the base class for all safety critical Java memory areas. This class
 * is used by the SCJ infrastructure to manage all SCJ memory areas.
 * Applications shall not directly extend this class.
 * 
 * @version SCJ 0.93
 * @note This class is not 'really' visible. Do we need it? We need it for the
 *       static methods and enterPrivateMemoery. However, we probably don't need
 *       PrivateMemory. Martin: We might overload all methods from RTSJ classes
 *       that are needed here to keep all implementation code within the SCJ
 *       package. Delegation to the private class Memory.java is fine.
 */
@SCJAllowed
public abstract class ManagedMemory extends LTMemory {
	
	Memory memory;

	static SysHelper _sysHelper;

	public static void setHelper(SysHelper sysHelper) {
		_sysHelper = sysHelper;
	}

	/**
	 * This method causes the calling schedulable object to execute the logic in
	 * a nested private memory area. If a private memory does not exist, one is
	 * created with the specified size; otherwise, its size is set. Then the
	 * private memory area is cleared and entered.
	 * 
	 * The private memory object representing the inner scope memory area may be
	 * reused on subsequent calls to enterPrivateMemory during the lifetime of
	 * the current memory area.
	 * 
	 * @param size
	 *            is the size in bytes of the private memory.
	 * 
	 * @param logic
	 *            is the code to be executed in the private memory.
	 * 
	 * @throws IllegalStateException
	 *             if the currently running thread is forbidden from entering a
	 *             PrivateMemory area, such as when the current thread is
	 *             executing within Mission.initialize or Safelet.getSequencer,
	 */
	@SCJAllowed
	public static void enterPrivateMemory(long size, Runnable logic)
			throws IllegalStateException {
		Memory m = _sysHelper.getCurrentMemory();
		_sysHelper.enterPrivateMemory(m, (int) size, logic);
//		m.enterPrivateMemory((int) size, logic);
	}
	
	/**
	 * Change the allocation context to the outer memory area where the object
	 * obj is allocated and invoke the run method of the logic Runnable.
	 * 
	 * @param obj
	 *            is the object that is allocated in the memory area that is
	 *            entered.
	 * @param logic
	 *            is the code to be executed in the entered memory area.
	 */
	@SCJAllowed
	public static void executeInAreaOf(Object obj, Runnable logic){
		Memory m = _sysHelper.getMemoryArea(obj);
		_sysHelper.executeInArea(m, logic);
	}
	
	/**
	 * Change the allocation context to the immediate outer memory area and
	 * invoke the run method if the Runnable.
	 * 
	 * @param logic
	 *            is the code to be executed in the entered memory area.
	 * @throws InaccessibleAreaException
	 */
	@SCJAllowed
	public static void executeInOuterArea(Runnable logic) throws InaccessibleAreaException{
		Memory m = _sysHelper.getCurrentMemory();
		
		// Objects representing memory areas, except for ImmortalMemory, 
		// hold a reference to the memory area were they were created
		m = _sysHelper.getMemoryArea(m);
		if(m == null){
			throw new InaccessibleAreaException("Not possible to move to an area outer than ImmortalMemory" );
		}
		_sysHelper.executeInArea(m, logic);
	}
	
	/**
	 * This method can be used to manage the use of backing store by any SCJ
	 * memory area.
	 * 
	 * @return the size of the remaining memory available to the current
	 *         ManagedMemory area.
	 */
	@SCJAllowed
	public long getRemainingBackingStore(){
		return _sysHelper.getRemainingBackingStore(memory);
	}
	
	@SCJAllowed
	@SCJRestricted(maySelfSuspend = false)
	public long size() {
		return _sysHelper.size(memory);
	}

	@SCJAllowed
	@SCJRestricted(maySelfSuspend = false)
	public long memoryConsumed() {
		return _sysHelper.memoryConsumed(memory);
	}

	@SCJAllowed
	@SCJRestricted(maySelfSuspend = false)
	public long memoryRemaining() {
		return _sysHelper.memoryRemaining(memory);
	}

	public static long getCurrentSize() {
		Memory m = _sysHelper.getCurrentMemory();
		return _sysHelper.size(m);
	}
	
	static void setSize(int size){
		Memory m = _sysHelper.getCurrentMemory();
		_sysHelper.setSize(m, size);
	}
	
	static ManagedMemory getManagedMemory(Object o){
		Memory m = _sysHelper.getMemoryArea(o);
		return null;//_sysHelper.getCurrentMemory();
	}

}