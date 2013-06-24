package javax.realtime;

/**
 * A helper class to allow sharing of package protected methods between classes
 * in javax.realtime and javax.safetycritical
 * 
 * @author Juan Rios
 * @version SCJ 0.93
 * 
 */
public final class RtsjHelper {
	
	RtsjHelper(){
		
	}
	
	/* Methods to be exposed from javax.realtime to javax.safetycritical */
	
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
	
}
