package javax.safetycritical;

import javax.realtime.RelativeTime;
import javax.safetycritical.annotate.Allocate;
import javax.safetycritical.annotate.MemoryAreaEncloses;
import javax.safetycritical.annotate.SCJAllowed;
import javax.safetycritical.annotate.Allocate.Area;
import static javax.safetycritical.annotate.Allocate.Area.THIS;

/**
 * A CyclicSchedule represents a time-driven sequence of firings for
 * deterministic scheduling of periodic event handlers. The static cyclic
 * scheduler repeatedly executes the firing sequence.
 * 
 * @author Juan Rios
 * @version SCJ 0.93
 */
@SCJAllowed
public final class CyclicSchedule {

	private Frame[] frames_;
	private RelativeTime cycleDuration;

	/**
	 * Construct a cyclic schedule by copying the frames array into a private
	 * array within the same memory area as this newly constructed
	 * CyclicSchedule object.
	 * 
	 * The frames array represents the order in which event handlers are to be
	 * scheduled. Note that some Frame entries within this array may have zero
	 * PeriodicEventHandlers associated with them. This would represent a period
	 * of time during which the CyclicExecutive is idle.
	 * 
	 * Memory behavior: This constructor may allocate objects within the same
	 * MemoryArea that holds the implicit this argument. This constructor
	 * requires that the "frames" argument reside in a scope that encloses the
	 * scope of the "this" argument.
	 * 
	 * @param frames
	 *            The frames array represents the order in which event handlers
	 *            are to be scheduled.
	 * @throws IllegalArgumentException
	 *             if any element of the frames array equals null
	 * @throws IllegalStateException
	 *             if invoked in a Level 1 a Level 2 application
	 */
	@Allocate({ Area.THIS })
	@MemoryAreaEncloses(inner = { "this" }, outer = { "frames" })
	@SCJAllowed
	public CyclicSchedule(Frame[] frames) throws IllegalArgumentException,
			IllegalStateException {
		frames_ = new Frame[frames.length];
		System.arraycopy(frames, 0, frames_, 0, frames.length);
		cycleDuration = new RelativeTime();
	}

	/**
	 * 
	 * @return a shared reference to a RelativeTime object representing the sum
	 *         of the durations of all of the Frame objects that comprise this
	 *         CyclicSchedule. The returned RelativeTime object is shared with
	 *         this CyclicSchedule and is intended to be treated as read-only.
	 *         Any modifications to the RelativeTime object will have
	 *         potentially disastrous, but undefined results. The returned
	 *         object resides in the same scope as this CyclicSchedule object.
	 *         Under normal circumstances, this object resides in the same
	 *         MissionMemory area as the CyclicExecutive that it is scheduling.
	 */
	@SCJAllowed
	final RelativeTime getCycleDuration() {

		for (int i = 0; i < frames_.length - 1; i++) {
			cycleDuration = cycleDuration.add(cycleDuration,
					frames_[i].duration_);
		}

		return cycleDuration;
	}

	/**
	 * Returns the array of Frames that represents this particular
	 * CyclicSchedule. The elements of the Frames array are sorted according to
	 * chronological order. So the event handlers represents by entry 0 are
	 * executed before the event handlers of entry 1, and so on.
	 * <p>
	 * The returned array is shared with this CyclicSchedule and is intended to
	 * be treated as read-only. Any modifications to the array will have
	 * potentially disastrous, but undefined results. The returned array resides
	 * in the same scope as this CyclicSchedule object. Under normal
	 * circumstances, this CyclicSchedule object will reside in the
	 * MissionMemory that corresponds to the Level0Mission that is being
	 * scheduled.
	 * 
	 */
	@SCJAllowed
	final Frame[] getFrames() {
		return frames_;
	}
}
