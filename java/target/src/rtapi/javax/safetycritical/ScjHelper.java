package javax.safetycritical;

import java.util.Vector;

import com.jopdesign.sys.RtThreadImpl;

public class ScjHelper {

	/*
	 * Register all classes that need access to the exposed methods from
	 * javax.safetycritical
	 */
	static {
		ScjHelper scjHelper = new ScjHelper();
		RtThreadImpl.setScjHelper(scjHelper);
	}

	private ScjHelper() {
	}

	/* Methods to be exposed to friend packages */
	public Vector getMissionEventHandlers(Mission m){
		if(m.hasEventHandlers)
			return m.getHandlers();
		else
			return null;
	}
	
	public Vector getMissionLongEventHandlers(Mission m){
		if(m.hasLongEventHandlers)
			return m.getLongHandlers();
		else
			return null;
	}
	

}
