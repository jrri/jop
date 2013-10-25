package libs.check.scj;

import com.jopdesign.io.I2CFactory;
import com.jopdesign.io.I2Cport;

import libs.io.IOException;
import libs.io.InputStream;

public class I2cInputStream extends InputStream{
	
	private I2Cport i2cPort;

	public I2cInputStream() throws IOException {
		
		i2cPort = I2CFactory.getFactory().getI2CportA();
		
		if(i2cPort != null){
			i2cPort.initialize(0, false);
		}else{
			throw new IOException();
		}
	}

	@Override
	public int read() throws IOException {
		
		while(available() == 0){
			;
		}
		
		return i2cPort.rx_fifo_data;
		
	}
	
	@Override
	public int available() throws IOException {
		return (i2cPort.rx_occu & I2Cport.OCCU_RD ) >>> 16;
	}
	
	@Override
	public void close() throws IOException {
		i2cPort.close();
	}

}
