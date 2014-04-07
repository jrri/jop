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
	ManagedMemory inner;

	static SysHelper _sysHelper;

	ManagedMemory(int size, int bsSize) {
		memory = _sysHelper.getMemory(size, bsSize);
		_sysHelper.setManagedMemory(memory, this);
	}

	ManagedMemory() {
		memory = _sysHelper.getMemory();
		_sysHelper.setManagedMemory(memory, this);
	}

	public static void setSysHelper(SysHelper sysHelper) {
		_sysHelper = sysHelper;
	}

	/**
	 * This method causes the calling schedulable object to execute the logic in
	 * a nested private memory area. If a private memory does not exist, one is
	 * created with the specified size; otherwise (if a private memory already
	 * exist), its size is set. Then the private memory area is cleared and
	 * entered.
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

		final ManagedMemory current = _sysHelper.getCurrentManagedMemory();
		
		if (current.inner == null) {
			current.inner = new PrivateMemory();

			/*
			 * The current memory object has a longer lifetime than the current
			 * memory area, so the following assignment is illegal:
			 * 
			 * current.inner = new PrivateMemory();
			 * 
			 * Doing so in this way also eliminates the illegal reference of
			 * enterPrivateMemory in Memory.java.
			 * 
			 * The runnable object is created only when the reusable nested
			 * private memory does not exist.
			 */
//			executeInOuterArea(new Runnable() {

//				@Override
//				public void run() {
//					current.inner = new PrivateMemory();
//				}
//			});

			/*
			 * set the newly created PrivateMemory as the inner memory for the
			 * current memory
			 */
			_sysHelper.setInner(current.memory, current.inner.memory,
					current.inner);
		}

		_sysHelper.enterPrivateMemory(current.memory, (int) size, logic);

		/* For nested uses of enterPrivateMemory */
		current.inner.inner = null;

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
	public static void executeInAreaOf(Object obj, Runnable logic) {
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
	public static void executeInOuterArea(Runnable logic)
			throws InaccessibleAreaException {
		Memory m = _sysHelper.getCurrentMemory();

		// Objects representing memory areas, except for ImmortalMemory,
		// hold a reference to the memory area were they were created
		m = _sysHelper.getMemoryArea(m);
		if (m == null) {
			throw new InaccessibleAreaException(
					"Not possible to move to an area outer than ImmortalMemory");
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
	public long getRemainingBackingStore() {
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

	static void setSize (int size) {
		Memory m = _sysHelper.getCurrentMemory();
		_sysHelper.setSize(m, size);
	}

	public static ManagedMemory getManagedMemory(Object o) {
		Memory m = _sysHelper.getMemoryArea(o);
		return _sysHelper.getManagedMemory(m);
	}
	
	/* NOT ON SPEC, FOR TESTING ONLY */
	public static ManagedMemory getCurrentManagedMemory(){
		return _sysHelper.getCurrentManagedMemory();
	}
	
	public static Memory getCurrentMemory(){
		return _sysHelper.getCurrentMemory();
	}
	

}