package libs.check.scj.scope;

import javax.realtime.PeriodicParameters;
import javax.realtime.PriorityParameters;
import javax.safetycritical.PeriodicEventHandler;
import javax.safetycritical.StorageParameters;
import javax.safetycritical.annotate.SCJAllowed;

import libs.io.DataInputStream;
import libs.io.IOException;

public class ClientHandler extends PeriodicEventHandler{

	public ClientHandler(PriorityParameters priority,
			PeriodicParameters release, StorageParameters storage,
			long scopeSize, String name) {
		super(priority, release, storage, scopeSize, name);
	}

	@Override
	@SCJAllowed
	public void handleAsyncEvent() {
		
		String value;
		try {
			DataInputStream dis = new DataInputStream(new I2cInputStream(1));
			while((value = dis.readUTF()) != null) {
			     System.out.println(value);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
