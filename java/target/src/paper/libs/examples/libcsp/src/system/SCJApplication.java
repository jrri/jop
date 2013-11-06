package libs.examples.libcsp.src.system;

import javax.safetycritical.JopSystem;

import libs.examples.libcsp.csp.app.ping.PingMission;

public class SCJApplication {
	
	public static void main(String[] args) {
		JopSystem js = new JopSystem();
		js.startMission(new PingMission());
	}
}