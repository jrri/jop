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

package scopeuse.ex3;

import javax.safetycritical.ManagedMemory;

import com.jopdesign.sys.Memory;

public class Worker implements Runnable{

	RetObject rObj;
	
	Worker(){
	}

	@Override
	public void run() {
		
		// do some work...
		
		//TODO: This example should use newInstance()
		//TODO: ManagedMemory.getCurrentManagedMemory()
		ManagedMemory.executeInAreaOf(this, new Runnable() {
			
			@Override
			public void run() {

				rObj = new RetObject();
			}
		});
	}
}

class RetObject {
	
	long a = System.currentTimeMillis();
}