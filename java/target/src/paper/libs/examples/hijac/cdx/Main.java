/**
 * @author Frank Zeyda
 */
package libs.examples.hijac.cdx;


import javax.safetycritical.JopSystem;
import javax.safetycritical.Safelet;

import libs.examples.hijac.cdx.CDxMission;
import libs.examples.hijac.cdx.CDxSafelet;

/**
 * Entry point of the SCJ application.
 */
public class Main {
  public static void main(String[] args) {
	  
		Safelet<CDxMission> safelet = new CDxSafelet();
		JopSystem js = new JopSystem();
		js.startMission(safelet);
	  
	  
//    SafeletExecuter.run(new CDxSafelet());
  }
}
