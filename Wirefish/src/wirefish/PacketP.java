/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wirefish;

import java.util.Arrays;
import org.jnetpcap.packet.JPacket;
import org.jnetpcap.packet.PcapPacket;
import static org.jnetpcap.protocol.JProtocol.IP4;
import static org.jnetpcap.protocol.lan.Ethernet.EthernetType.IP4;
import org.jnetpcap.protocol.network.Ip4;
import org.jnetpcap.protocol.tcpip.Tcp;


/**
 *
 * @author user1
 */
public class PacketP {

    PcapPacket packet;
    String Protocol="";
    int portNum;
    String SourceIP="",destinationIP="";

    public PacketP(PcapPacket p) {
        this.packet = p;
        Tcp tcp = new Tcp();
        Ip4 ip = new Ip4();
        if (packet.hasHeader(ip) == false) {
            return;
        }
        if (packet.hasHeader(tcp) == false) {
            return;
        }
        this.SourceIP= Arrays.toString(ip.source());
        this.destinationIP= Arrays.toString(ip.destination()); 
    }

    @Override
    public String toString() {
        return "the Protocol: " + Protocol + " , Port Number : " + this.portNum+" ,SourceIP:"+this.SourceIP+" ,DestinationIP:"+this.destinationIP;
    }

}
