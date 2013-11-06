package libs.check.scj.scope;

import com.jopdesign.io.I2Cport;

import libs.io.IOException;
import libs.io.OutputStream;

public class I2cOutputStream extends OutputStream{

	I2Cport i2cPort;
	int slAddress;
	
	public I2cOutputStream(int slAddress) {
		this.slAddress = slAddress;
	}
	
	@Override
	public void write(int b) throws IOException {
		
		i2cPort.write(slAddress, b);
		waitForWrite();
		
	}
	
	public void write(byte b[]) throws IOException {
		
		i2cPort.write(slAddress,  b);
		waitForWrite();
		
	}
	
	void waitForWrite(){

		while((i2cPort.status & I2Cport.BUS_BUSY) != 0){
			
		}
		
		i2cPort.control = i2cPort.control & I2Cport.CLEAR_STRT;
		
	}


}
