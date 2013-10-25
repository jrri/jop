package libs.examples.hijac.cdx;

import libs.examples.hijac.cdx.ArrayListFactory;
import libs.examples.hijac.cdx.Constants;
import libs.examples.hijac.cdx.MotionFactory;

/**
 * This class acts as a container for all factories needed to pre-allocate
 * shared objects in mission memory.
 * 
 * @author Frank Zeyda
 */
class PersistentData {

	private final ArrayListFactory listFactory;
	private final MotionFactory motionFactory;

	public PersistentData() {
		
		listFactory = new ArrayListFactory(Constants.NUMBER_OF_PLANES * 3,
				Constants.NUMBER_OF_PLANES);

		motionFactory = new MotionFactory(Constants.NUMBER_OF_PLANES
				* Constants.NUMBER_OF_PLANES);
	}

	public ArrayListFactory getListFactory() {
		return listFactory;
	}

	public MotionFactory getMotionFactory() {
		return motionFactory;
	}
}
