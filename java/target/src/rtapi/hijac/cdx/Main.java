/**
 * @author Frank Zeyda
 */
package hijac.cdx;

import javax.safetycritical.JopSystem;
import javax.safetycritical.Mission;
import javax.safetycritical.Safelet;

import hijac.cdx.CDxMission;
import hijac.cdx.CDxSafelet;

/**
 * Entry point of the SCJ application.
 */
public class Main {
  public static void main(String[] args) {
	  
		Safelet safelet = new CDxSafelet();
		JopSystem js = new JopSystem();
		js.startMission(safelet);
	  
	  
//    SafeletExecuter.run(new CDxSafelet());
  }
}
