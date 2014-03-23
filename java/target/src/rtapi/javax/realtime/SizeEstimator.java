package javax.realtime;

import javax.safetycritical.annotate.SCJAllowed;
import javax.safetycritical.annotate.SCJRestricted;

import com.jopdesign.sys.Config;
import com.jopdesign.sys.Const;
import com.jopdesign.sys.GC;
import com.jopdesign.sys.Native;
import com.jopdesign.sys.SysHelper;

/**
 * This class maintains a conservative upper bound of the amount of memory
 * required to store a set of objects. SizeEstimator is a ceiling on the amount
 * of memory that is consumed when the reserved objects are created. Many
 * objects allocate other objects when they are constructed. SizeEstimator only
 * estimates the memory requirement of the object itself; it does not include
 * memory required for any objects allocated at construction time. If the Java
 * implementation allocates a single Java object in several parts not separately
 * visible to the application (if, for example, the object and its monitor are
 * sep- arate), the size estimate shall include the sum of the sizes of all the
 * invisible parts that are allocated from the same memory area as the object.
 * Alignment considerations, and possibly other order-dependent issues may cause
 * the allocator to leave a small amount of unusable space. Consequently, the
 * size estimate cannot be seen as more than a close estimate, but SCJ requires
 * that the size estimate shall represent a conservative upper bound.
 */
@SCJAllowed
public final class SizeEstimator {

	static SysHelper _sysHelper;

	public static void setSysHelper(SysHelper sysHelper) {
		_sysHelper = sysHelper;
	}

	private long estimate = 0;

	/**
	 * Creates a new SizeEstimator object in the current allocation context.
	 */
	@SCJAllowed
	@SCJRestricted(maySelfSuspend = false)
	public SizeEstimator() {
	}

	/**
	 * JSR 302 tightens the semantic requirements on the implementation of
	 * getEstimate. For compliance with JSR 302, getEstimate() must return a
	 * conservative upper bound on the amount of memory required to represent
	 * all of the memory reservations associated with this SizeEstimator object.
	 * 
	 * @return
	 */
	@SCJAllowed
	@SCJRestricted(maySelfSuspend = false)
	public long getEstimate() {
		return estimate;
	}

	/**
	 * 
	 * Adds the required memory size of num instances of a clazz object to the
	 * cur- rently computed size of the set of reserved objects.
	 * 
	 * @param clazz
	 *            is the class to take into account.
	 * @param num
	 *            is the number of instances of clazz to estimate.
	 */
	@SCJAllowed
	@SCJRestricted(maySelfSuspend = false)
	public void reserve(Class clazz, int num) {

		int ptr = Native.toInt(clazz);
		int clinfo_ptr = Native.rd(ptr + 2);

		estimate += num * (_sysHelper.getHandlerSize() + Native.rd(clinfo_ptr));

	}

	/**
	 * 
	 * Adds the value returned by size.getEstimate to the currently computed
	 * size of the set of reserved objects.
	 * 
	 * @param size
	 *            is the value returned by getEstimate.
	 */
	@SCJAllowed
	@SCJRestricted(maySelfSuspend = false)
	public void reserve(SizeEstimator size) {
	}

	/**
	 * Adds num times the value returned by size.getEstimate to the currently
	 * com- puted size of the set of reserved objects.
	 * 
	 * @param size
	 *            is the size returned by size.getEstimate.
	 * @param num
	 *            is the number of times to reserve the size denoted by size.
	 */
	@SCJAllowed
	@SCJRestricted(maySelfSuspend = false)
	public void reserve(SizeEstimator size, int num) {
	}

	/**
	 * Adds the size of an instance of an array of length reference values to
	 * the currently computed size of the set of reserved objects.
	 * 
	 * @param length
	 *            is the number of entries in the array
	 */
	@SCJAllowed
	@SCJRestricted(maySelfSuspend = false)
	public void reserveArray(int length) {
	}

	/**
	 * 
	 * Adds the required memory size of an additional instance of an array of
	 * length primitive values of Class type to the currently computed size of
	 * the set of re- served objects. Class values for the primitive types shall
	 * be chosen from these class types; e.g., Byte.TYPE, Integer.TYPE, and
	 * Short.TYPE. The reservation shall leave room for an array of length of
	 * the primitive type corresponding to type.
	 * 
	 * @param length
	 *            is the number of entries in the array.
	 * @param type
	 *            is the class representing a primitive type.
	 */
	@SCJAllowed
	@SCJRestricted(maySelfSuspend = false)
	public void reserveArray(int length, Class type) {
	}
}
