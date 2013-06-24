package javax.realtime;

import javax.safetycritical.annotate.SCJAllowed;
import javax.safetycritical.annotate.SCJRestricted;

import static javax.safetycritical.annotate.Level.LEVEL_1;

/**
 * The ClockEvent interface may be used by subclasses of Clock to indicate to
 * the clock infrastructure that the clock has either reached a designated time,
 * or has experienced a discontinuity. Invocations of the methods in
 * ClockCallBack are serialized.
 * 
 * The callback is de-registered before a method in it is invoked, and the Clock
 * blocks any attempt by another thread to register another callback while
 * control is in a callback.
 * 
 * @note For user defined clocks (RTSJ 1.1)
 */
@SCJAllowed(LEVEL_1)
public interface ClockCallBack {

	/**
	 * Clock has reached the designated time. This clock event is de-registered
	 * before this method is invoked.
	 * 
	 * @param clock
	 *            the clock that has reached a designated time.
	 * 
	 * @version SCJ 0.93
	 * 
	 * @note For user defined clocks (RTSJ 1.1)
	 */
	@SCJAllowed(LEVEL_1)
	@SCJRestricted(maySelfSuspend = false)
	void atTime(Clock clock);

	/**
	 * clock experienced a time discontinuity (it changed its time value other
	 * than by ticking). The clock has de-registered this clock event.
	 * 
	 * @param clock
	 *            the clock that has experienced a discontinuity.
	 * @param updatedTime
	 *            the signed length of the time discontinuity.
	 * 
	 * @note For user defined clocks (RTSJ 1.1)
	 */
	@SCJAllowed(LEVEL_1)
	@SCJRestricted(maySelfSuspend = false)
	void discontinuity(Clock clock, AbsoluteTime updatedTime);
}
