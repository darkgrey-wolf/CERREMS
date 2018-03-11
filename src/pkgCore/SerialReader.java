package pkgCore;

import gnu.io.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.TooManyListenersException;

/**
 *
 * @author GreyWolf
 */
public class SerialReader implements SerialPortEventListener {
    
    private SerialPort port;
    private CommPortIdentifier portId;
    private BufferedReader input;
    public static final int TIME_OUT = 2000;
    public static final int DATA_RATE = 9600;
    public static final String PORT_NAME_DEFAULT = "COM6";
    //private String portName;
    public SerialReader(String value) throws NoSuchPortException, PortInUseException, UnsupportedCommOperationException, IOException{
      this.portId = null;
      if(value==null)value = SerialReader.PORT_NAME_DEFAULT;
      if(value.equals("")) value = SerialReader.PORT_NAME_DEFAULT;
      CommPortIdentifier currentId;
      Enumeration availablePort = CommPortIdentifier.getPortIdentifiers();
      while(availablePort.hasMoreElements()){
          currentId = (CommPortIdentifier) availablePort.nextElement();
          if(currentId.getName().equals(value)){
              this.portId=currentId;
              break;
          }
      }
      if(this.portId==null){
         throw new NoSuchPortException(); 
      }
          // 
         this.port = (SerialPort) this.portId.open(this.getClass().getName(),SerialReader.TIME_OUT);
         
         this.port.setSerialPortParams(SerialReader.DATA_RATE, 
                                       SerialPort.DATABITS_8, 
                                       SerialPort.STOPBITS_1, 
                                       SerialPort.PARITY_NONE);
         this.input = new BufferedReader(new InputStreamReader(this.port.getInputStream()));
         //this.port.addEventListener(this);
    }
    public synchronized void close(){
        if(this.port!=null){
            this.port.removeEventListener();
            this.port.close();
        }
    }
    public void addEventListener(SerialPortEventListener sl) throws TooManyListenersException{
        this.port.addEventListener(sl);
        this.port.notifyOnDataAvailable(true);
        //this.port.addEventListener(this);
    }
    public String readLine() throws IOException{
        return this.input.readLine();
    }
    /**
     *
     * @param spe
     */
    @Override
    public synchronized void serialEvent(SerialPortEvent spe) {
        //nothing to do
        System.out.println("Called in SerialReader");
    }
    
    
}
