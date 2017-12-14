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
import org.jnetpcap.protocol.tcpip.Tcp.Timestamp;
import org.jnetpcap.protocol.tcpip.Udp;


/**
 *
 * @author user1
 */
public class PacketP {

    PcapPacket packet;
    String Protocol="",transport="";
    String SourceIP="",destinationIP="";
    int portSource,PortDst;
    Tcp tcp = new Tcp();
    Ip4 ip = new Ip4();
    Udp udp = new Udp();
    long time;
    int length;
        
    public PacketP(PcapPacket p) {
        this.packet = p;
        //get source ip and destination
        if (packet.hasHeader(ip) == false) {
            this.SourceIP= Arrays.toString(ip.source());
            this.destinationIP= Arrays.toString(ip.destination()); 
        }
        //know the transport is TCP and get source and destination port number
        if (packet.hasHeader(tcp) == false) {
            this.transport="TCP";  
            this.portSource=tcp.source();
            this.PortDst=tcp.destination();
        }//know the transport is UDP and get source and destination port number
        else if (packet.hasHeader(udp)){
            this.transport="UDP";
            this.portSource=udp.source();
            this.PortDst=udp.destination();
        }
        time = packet.getCaptureHeader().timestampInMillis();//time of capture packet
        length=packet.getCaptureHeader().caplen();//acual length of packet
       

    }

    @Override
    public String toString() {
        return "the Protocol: " + Protocol + " ,Source PortNumber: " + this.portSource+ " ,Destination PortNumber: " + this.PortDst+" ,SourceIP: "+this.SourceIP+" ,DestinationIP: "+this.destinationIP;
    }

}
