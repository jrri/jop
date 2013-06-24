package com.jopdesign.sys;

public final class SysHelper {
	
	SysHelper(){
		
	}
	
	// Methods to be exposed to friend packages
	public Memory getMemory(int size, int bsSize){
		return new Memory(size, bsSize);
	}
	
	public Memory getMemory(int size){
		return new Memory(size);
	}
	
	public Memory getImmortalMemory(){
		return Memory.immortal;
	}
	
	public void enterPrivateMemory(Memory m, int size, Runnable logic){
		m.enterPrivateMemory(size, logic);
	}
	
	public void executeInArea(Memory m, Runnable logic){
		m.executeInArea(logic);
	}
	
	public void enter(Memory m, Runnable logic){
		m.enter(logic);
	}
	
	public Memory getCurrentMemory(){
		return Memory.getCurrentMemory();
	}
	
	public Memory getMemoryArea(Object object){
		return Memory.getMemoryArea(object);
	}
	
	public int size(Memory mem){
		return mem.size();
	}
	
	public int memoryRemaining(Memory mem){
		return mem.memoryRemaining();
	}
	
	public int memoryConsumed(Memory mem){
		return mem.memoryConsumed();
	}
	
	public long getRemainingBackingStore(Memory mem){
		return (long) mem.bStoreRemaining();
	}
	
	public void setSize(Memory mem, int size){
		mem.setSize(size);
	}

}
