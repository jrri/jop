package libs.check.scj.scope;

import javax.realtime.PeriodicParameters;
import javax.realtime.PriorityParameters;
import javax.safetycritical.PeriodicEventHandler;
import javax.safetycritical.StorageParameters;
import javax.safetycritical.annotate.SCJAllowed;

import libs.io.DataOutputStream;
import libs.io.IOException;

public class ServerHandler extends PeriodicEventHandler {

	public ServerHandler(PriorityParameters priority,
			PeriodicParameters release, StorageParameters storage,
			long scopeSize, String name) {
		super(priority, release, storage, scopeSize, name);
	}

	@Override
	@SCJAllowed
	public void handleAsyncEvent() {

		try {

			DataOutputStream out = new DataOutputStream(new I2cOutputStream(2));
			while (true) {
				for (int i = 0; i < 100; i++) {
					out.writeUTF("Test" + i);
					out.flush();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
