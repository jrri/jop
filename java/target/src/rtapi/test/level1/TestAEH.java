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

package test.level1;

import javax.realtime.AperiodicParameters;
import javax.realtime.PriorityParameters;
import javax.safetycritical.AperiodicEventHandler;
import javax.safetycritical.StorageParameters;
import javax.safetycritical.Terminal;

import test.cyclic.ImmortalEntry;

public class TestAEH extends AperiodicEventHandler{
	
	private int missionNr;
	private int fireCount = 0;

	public TestAEH(PriorityParameters priority,
			AperiodicParameters release, StorageParameters storage, String name, int missionNr) {
		super(priority, release, storage, name);
		// TODO Auto-generated constructor stub
		this.missionNr = missionNr;
	}

	@Override
	public void handleAsyncEvent() {
		// TODO Auto-generated method stub
//		Terminal.getTerminal().writeln(getName()+"Hello");
		fireCount++;
		ImmortalEntry.shared[ImmortalEntry.sys.cpuId][missionNr] = fireCount; 
	}
	
	@Override
	public void cleanUp() {
		// Just a debug message
		System.out.println("Cleanup "+ getName());
		
	}

}
