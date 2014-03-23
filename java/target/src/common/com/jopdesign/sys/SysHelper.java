package com.jopdesign.sys;

import javax.realtime.ImmortalMemory;
import javax.realtime.RealtimeThread;
import javax.realtime.Schedulable;
import javax.realtime.SizeEstimator;
import javax.safetycritical.AperiodicEventHandler;
import javax.safetycritical.AperiodicLongEventHandler;
import javax.safetycritical.ManagedMemory;
import javax.safetycritical.Mission;
import javax.safetycritical.PeriodicEventHandler;
import javax.safetycritical.PrivateMemory;

public final class SysHelper {

	/*
	 * Register all classes that need access to the exposed methods from
	 * com.jopdesign.sys
	 */
	static {
		SysHelper sysHelper = new SysHelper();
		ImmortalMemory.setSysHelper(sysHelper);
		ManagedMemory.setSysHelper(sysHelper);
		PeriodicEventHandler.setSysHelper(sysHelper);
		AperiodicEventHandler.setSysHelper(sysHelper);
		AperiodicLongEventHandler.setSysHelper(sysHelper);
		RealtimeThread.setSysHelper(sysHelper);
		SizeEstimator.setSysHelper(sysHelper);

		javax.realtime.Scheduler.setSysHelper(sysHelper);
	}

	private SysHelper() {

	}

	// Methods to be exposed to friend packages
	public Memory getMemory(int size, int bsSize) {
		return new Memory(size, bsSize);
	}

	public Memory getMemory() {
		return new Memory();
	}

	public Memory getMemory(int size) {
		return new Memory(size);
	}

	public Memory getImmortalMemory() {
		return Memory.immortal;
	}

	public synchronized void enterPrivateMemory(Memory m, int size,
			Runnable logic) {
		m.enterPrivateMemory(size, logic);
	}

	public void executeInArea(Memory m, Runnable logic) {
		m.executeInArea(logic);
	}

	public void enter(Memory m, Runnable logic) {
		m.enter(logic);
	}

	public Memory getCurrentMemory() {
		return Memory.getCurrentMemory();
	}

	public Memory getMemoryArea(Object object) {
		return Memory.getMemoryArea(object);
	}

	public int size(Memory mem) {
		return mem.size();
	}

	public int memoryRemaining(Memory mem) {
		return mem.memoryRemaining();
	}

	public int memoryConsumed(Memory mem) {
		return mem.memoryConsumed();
	}

	public long getRemainingBackingStore(Memory mem) {
		return (long) mem.bStoreRemaining();
	}

	public void setSize(Memory mem, int size) {
		mem.setSize(size);
	}

	public ManagedMemory getManagedMemory(Memory m) {
		return m.managedMemory;
	}

	public void setManagedMemory(Memory m, ManagedMemory managedMem) {
		m.managedMemory = managedMem;
	}

	public RtThreadImpl getCurrentRtThreadImpl() {
		Scheduler s = Scheduler.sched[RtThreadImpl.sys.cpuId];
		int nr = s.active;
		return s.ref[nr];
	}

	public void setSchedulable(RtThreadImpl rtt, Schedulable schedulable) {
		rtt.setSchedOblect(schedulable);
	}

	public Schedulable getSchedulable() {
		Scheduler s = Scheduler.sched[RtThreadImpl.sys.cpuId];
		int nr = s.active;
		return s.ref[nr].getSchedOblect();
	}

	public void setInner(Memory m, Memory inner, ManagedMemory mm) {
		m.inner = inner;
		m.inner.managedMemory = mm;
	}

	/**
	 * Returns the current managed memory area
	 * 
	 * @return The {@link ManagedMemory} object that represnets the current
	 *         allocation context.
	 */
	public ManagedMemory getCurrentManagedMemory() {
		return Memory.getCurrentManagedMemory();
	}
	
	public int getHandlerSize(){
		
		if(Config.USE_SCOPES){
			return GC.HEADER_SIZE;
		}else{
			/* Although we cannot use SCJ without scopes... */
			return GC.HANDLE_SIZE;
		}
		
	}

}
