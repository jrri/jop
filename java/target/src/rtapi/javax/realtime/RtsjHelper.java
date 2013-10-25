package javax.realtime;

import javax.safetycritical.ManagedEventHandler;
import javax.safetycritical.ManagedLongEventHandler;

/**
 * A helper class to allow sharing of package protected methods between classes
 * in javax.realtime and javax.safetycritical
 * 
 * @author Juan Rios
 * @version SCJ 0.93
 * 
 */
public final class RtsjHelper {
	
	/*
	 * Register all classes that need access to the exposed methods from
	 * javax.realtime
	 */
	static {
		RtsjHelper rtsjHelper = new RtsjHelper();
		ManagedEventHandler.setRtsjHelper(rtsjHelper);
		ManagedLongEventHandler.setRtsjHelper(rtsjHelper);
	}
	
	private RtsjHelper(){
		
	}
	
	/* Methods to be exposed to friend packages */
	
	public RelativeTime getPeriod(PeriodicParameters periodicParameters) {
		return periodicParameters.getPeriod();
	}
	
	public HighResolutionTime getStart(PeriodicParameters periodicParameters){
		return periodicParameters.getStart();
	}
	
	RelativeTime getDeadline(ReleaseParameters releaseParameters) {
		return releaseParameters.getDeadline();
	}
	
	AsyncEventHandler getDeadlineMissHandler(ReleaseParameters releaseParameters) {
		return releaseParameters.getDeadlineMissHandler();
	}
	
	public int getAffinitySetProcessor(AffinitySet set){
		return set.processorNumber;
	}
	
}
