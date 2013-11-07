/**
 * @author Kun Wei, Frank Zeyda
 */
package libs.examples.hijac.cdx;

import libs.examples.hijac.cdx.Error;
//import libs.safeutil.ArrayList;
import libs.safeutil.SafeVector;
import libs.safeutil.extras.PoolObject;

/**
 * This class is used to pre-allocate ArrayList objects in mission memory.
 */
public class ArrayListFactory {

//	private final ArrayList[] store;
	private final SafeVector<PoolObject>[] store;
	private int index;

	/**
	 * 
	 * @param size
	 *            Number of lists
	 * @param capacity
	 *            Elements per list
	 */
	public ArrayListFactory(int size, int capacity) {
//		store = new ArrayList[size];
		store = new SafeVector[size];
		
		for (int i = 0; i < store.length; i++) {
			/* TODO: Why "capacity + 1" here? */
//			store[i] = new ArrayList<Object>(capacity + 1);
			store[i] = new SafeVector<PoolObject>(capacity + 1);
		}
		
		index = 0;
	}

	/* Return a new pre-allocated instance of the ArrayList class. */
	public SafeVector<PoolObject> getNewList() {
		if (index < store.length) {
			return store[index++];
		} else {
			Error.abort("Exceeding storage capacity in ArrayListFactory.");
			return null; // Never reached.
		}
	}
	

//	public ArrayList getNewList() {
//		if (index < store.length) {
//			return store[index++];
//		} else {
//			Error.abort("Exceeding storage capacity in ArrayListFactory.");
//			return null; // Never reached.
//		}
//	}

	/*
	 * Clear the content of each list before clearing the store. This means
	 * returning each element to its pool
	 */
	public void clear() {
		for (int i = 0; i < index; i++) {
			store[i].clear();
		}
		index = 0;
	}
}
