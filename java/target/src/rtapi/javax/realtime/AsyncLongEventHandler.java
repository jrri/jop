package javax.realtime;

import javax.safetycritical.annotate.SCJAllowed;
import javax.safetycritical.annotate.SCJProtected;

import static javax.safetycritical.annotate.Level.LEVEL_0;

/**
 * In SCJ, all asynchronous events must have their handlers bound when they are
 * created (during the initialization phase). The binding is permanent. Thus,
 * the AsyncLongEventHandler constructors are hidden from public view in the SCJ
 * specification. This class differs from AsyncEventHandler in that when it is
 * fired, a long integer is provided for use by the released event handler(s).
 * 
 * @author Juan Rios
 * @version SCJ 0.93
 * 
 */
@SCJAllowed(LEVEL_0)
public class AsyncLongEventHandler extends AbstractAsyncEventHandler {
	
	/**
	 * Infrastructure code. Must not be called.
	 */
	@Override
	@SCJProtected
	public final void run() {
	}

	/**
	 * This method must be overridden by the application to provide the handling
	 * code.
	 * 
	 * @param data
	 *            is the data that was passed when the associated event was
	 *            fired.
	 */
	@SCJAllowed
	public void handleAsyncEvent(long data) {
	}
}
