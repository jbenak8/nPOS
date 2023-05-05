package cz.jbenak.npos.communicator.protocols.petrotec;

import com.fazecast.jSerialComm.SerialPort;
import org.springframework.beans.factory.annotation.Value;

/**
 * @author Jan Benák
 * @version 1.0.0.0
 * @since 2023-02-11
 * <p>
 * Definition of operation and constants for Petrotec XDX protocol communication.
 */
public class HDX {

    /**
     * General constants
     */
    @Value("${fuel-dispensers.baud-rate}")
    public int BAUD_RATE;
    public final int PARITY = SerialPort.ODD_PARITY;
    

}
