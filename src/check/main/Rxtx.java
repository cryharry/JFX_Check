package check.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Enumeration;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class Rxtx implements SerialPortEventListener {
	@FXML
	private Label rfCardNum;
	
	SerialPort serialPort;
	String rfcard = "";
	
	private final String PORT_NAMES[] = {
            "/dev/ttyACM0", //for Ubuntu
            "/dev/tty.usbserial-A9007UX1", // Mac OS X
            "/dev/ttyUSB0", // Linux
            "COM1", // Windows
        };
        private BufferedReader input;
        private OutputStream output;
        private static final int TIME_OUT = 2000;
        private static final int DATA_RATE = 9600;
 
        public void initialize() {
            CommPortIdentifier portId = null;
            Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();
 
            //First, Find an instance of serial port as set in PORT_NAMES.
            while (portEnum.hasMoreElements()) {
                CommPortIdentifier currPortId
                        = (CommPortIdentifier) portEnum.nextElement();
                for (String portName : PORT_NAMES) {
                    if (currPortId.getName().equals(portName)) {
                        portId = currPortId;
                        break;
                    }
                }
            }
            if (portId == null) {
                System.out.println("Could not find COM port.");
            } else {
                System.out.println("Port Name: " + portId.getName() + "\n"
                        + "Current Owner: " + portId.getCurrentOwner() + "\n"
                        + "Port Type: " + portId.getPortType());
            }
 
            try {
                // open serial port, and use class name for the appName.
                serialPort = (SerialPort) portId.open(this.getClass().getName(),
                        TIME_OUT);
 
                // set port parameters
                serialPort.setSerialPortParams(DATA_RATE,
                        SerialPort.DATABITS_8,
                        SerialPort.STOPBITS_1,
                        SerialPort.PARITY_NONE);
 
                // open the streams
                input = new BufferedReader(
                        new InputStreamReader(serialPort.getInputStream()));
                output = serialPort.getOutputStream();
 
                // add event listeners
                serialPort.addEventListener(this);
                serialPort.notifyOnDataAvailable(true);
            } catch (Exception e) {
                System.err.println(e.toString());
            }
        }
	
	@Override
	public void serialEvent(SerialPortEvent spe) {
		String inputLine = "";
		if (spe.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
            try {
            	inputLine = input.readLine();
                inputLine = inputLine.substring(1, inputLine.length()-1);
                SetCard(inputLine);
                System.out.println(rfcard);
            } catch (IOException e) {
            } catch (Exception e) {
            	System.err.println(e.toString());
            }
        }
	}
	
	private void SetCard(String rfcard) {
		this.rfcard = rfcard;
	}
	
	public String GetCard() {
		return this.rfcard;
	}

	public synchronized void close() {
        if (serialPort != null) {
            serialPort.removeEventListener();
            serialPort.close();
        }
    }
}
