/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wirefish;

import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.protocol.tcpip.Tcp;

/**
 *
 * @author user1
 */
public class PacketP {

    PcapPacket packet;
    String Protocol;
    int portNum;

    public PacketP(PcapPacket p) {
        this.packet = p;
        Tcp t = new Tcp();
        if (p.hasHeader(t)) {
            portNum=t.source();
            if (t.source() == 80) {
                Protocol = "Http";
            }
        }
    }

    @Override
    public String toString() {
        return "the Protocol: " + Protocol+" , Port Number : "+this.portNum;
    }

}
