/*
  This file is part of JOP, the Java Optimized Processor
    see <http://www.jopdesign.com/>

  Copyright (C) 2008-2011, Martin Schoeberl (martin@jopdesign.com)

  This program is free software: you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/**
 * 
 */
package javax.safetycritical;

import java.util.Vector;

import javax.realtime.AbsoluteTime;
import javax.realtime.Clock;
import javax.realtime.ImmortalMemory;
import javax.realtime.MemoryArea;

import com.jopdesign.sys.Memory;
import com.jopdesign.sys.RtThreadImpl;

import joprt.RtThread;

/**
 * This class represents JOP's SCJ framework.
 * 
 * @author Martin Schoeberl, Juan Rios
 * @version SCJ 0.93
 * 
 * @note Martin: I'm not sure that so much code shall be in this JOP specific
 *       class.
 * 
 */
public class JopSystem {

	public void startMission(Safelet<Mission> scj) {

		TerminationHelper terminationHelper = new TerminationHelper();
		SequencerHelper sequencerHelper = new SequencerHelper();

		/*
		 * The Safelet’s immortalMemorySize method is invoked to determine the
		 * desired size of ImmortalMemory. If the actual size of the remaining
		 * ImmortalMemory is smaller than the value returned from
		 * immortalMemorySize, Safelet initialization immediately aborts.
		 */
		if (scj.immortalMemorySize() > ImmortalMemory.instance()
				.memoryRemaining()) {
			Terminal.getTerminal().writeln(
					"[SYSTEM]: Not enough ImmortalMemory for the application");
			return;
		}

		scj.initializeApplication();

		/*
		 * Infrastructure invokes the Safelet’s getSequencer method, with the
		 * ImmortalMemory area as the current allocation context. The value
		 * returned represents the MissionSequencer that runs this application.
		 * If null is returned, the application immediately halts.
		 */
		MissionSequencer<Mission> missionSequencer = scj.getSequencer();
		if (missionSequencer == null) {
			/* Abort application, JVM finishes */
			Terminal.getTerminal().writeln(
					"[SYSTEM]: No sequencer, application aborted");
			return;
		}

		/* For L0 and L1, there is only one sequencer */
		Mission.currentSequencer = missionSequencer;

		missionSequencer.terminationHelper = terminationHelper;
		sequencerHelper.sequencer = missionSequencer;

		/*
		 * Initial MissionMemory is sized according to the sequencer's storage
		 * parameters
		 */
		int size = (int) missionSequencer.storage.getTotalBackingStoreSize();

		while (terminationHelper.nextMission
				&& !MissionSequencer.terminationRequest) {
			ManagedMemory.enterPrivateMemory(size, sequencerHelper);
			RtThreadImpl.reInitialize();
		}

		Terminal.getTerminal().writeln("[SYSTEM]: Application finished");

	}

	class TerminationHelper {
		boolean nextMission = true;
	}

	class SequencerHelper implements Runnable {

		MissionSequencer<Mission> sequencer;

		@Override
		public void run() {
			sequencer.handleAsyncEvent();
		}
	}

}
