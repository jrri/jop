package com.jopdesign.sys;

public class MemoryTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		MemoryTest mt = new MemoryTest();
		Helper h = new Helper(mt);
		
		Memory immortal = Memory.immortal;
		Memory mission = new Memory(1024,2048);
		
		mt.printPointers(immortal);
		System.out.println("--------------------------");
		mt.printPointers(mission);
		System.out.println("--------------------------");

		System.out.println("After resize: ");
		mt.printPointers(immortal);
		System.out.println("--------------------------");
		mt.printPointers(mission);
		System.out.println("--------------------------");

		
//		mission.enter(h);
		

	}
	
	public void printPointers(Memory m){
		System.out.println(m.startPtr);
		System.out.println(m.allocPtr);
		System.out.println(m.endLocalPtr);
		System.out.println(m.allocBsPtr);
		System.out.println(m.endBsPtr);
	}
	
}

class Helper implements Runnable {
	
	MemoryTest mt;
	
	Helper(MemoryTest mt){
		this.mt = mt;
	}

	@Override
	public void run() {
		
		Memory priv = new Memory(512);
		mt.printPointers(priv);
		
		for(int i = 0; i<10; i++){
			new Object();
		}
		
		
	
		
	}

	
}