package javax.realtime;

import javax.safetycritical.annotate.SCJAllowed;

/**
 * The BoundAsyncLongEventHandler class is not directly available to the safety
 * critical Java application developers. Hence none of its methods or
 * constructors are publicly available. This class differs from
 * BoundAsyncEventHandler in that when it is fired, a long integer is provided
 * for use by the released event handler(s).
 * 
 * @author Martin Schoeberl, Juan Rios
 * @version SCJ 0.93
 * 
 */
@SCJAllowed
public class BoundAsyncLongEventHandler extends AsyncLongEventHandler {

}
