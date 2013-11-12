/**
 * @author Frank Zeyda
 */
package hijac.cdx;

import static javax.safetycritical.annotate.Level.SUPPORT;

import javax.realtime.PriorityParameters;
import javax.safetycritical.MissionSequencer;
import javax.safetycritical.PriorityScheduler;
import javax.safetycritical.StorageParameters;
import javax.safetycritical.annotate.SCJAllowed;

import hijac.cdx.CDxMission;

/**
 * The mission sequencer of the parallel CDx application.
 * 
 * It returns an instance of CDxMission. We note that in a real system this
 * mission does not terminate.
 */
public class CDxMissionSequencer extends MissionSequencer<CDxMission> {
	public boolean mission_done;

	public CDxMissionSequencer() {
		super(new PriorityParameters(PriorityScheduler.instance()
				.getNormPriority()), new StorageParameters(16000, null, 0, 0));

		mission_done = false;
	}

	@Override
	@SCJAllowed(SUPPORT)
	public CDxMission getNextMission() {
		if (!mission_done) {
			mission_done = true;
			return new CDxMission();
		} else {
			return null;
		}
	}
}
