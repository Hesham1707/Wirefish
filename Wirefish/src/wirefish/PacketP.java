/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wirefish;

import static java.net.Proxy.Type.HTTP;
import java.sql.Timestamp;
import java.util.Arrays;
import org.jnetpcap.packet.JPacket;
import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.packet.format.FormatUtils;
import static org.jnetpcap.protocol.JProtocol.HTTP;
import static org.jnetpcap.protocol.lan.Ethernet.EthernetType.IP4;
import org.jnetpcap.protocol.network.Ip4;
import org.jnetpcap.protocol.tcpip.Tcp;
import org.jnetpcap.protocol.tcpip.Udp;

/**
 *
 * @author user1
 */
public class PacketP {

    PcapPacket packet;
    String Protocol = "", transport = "";
    String SourceIP = "", destinationIP = "";
    int portSource, PortDst;
    Tcp tcp = new Tcp();
    Ip4 ip = new Ip4();
    Udp udp = new Udp();
    long time;
    Timestamp timestamp;
    int lengthCaptured;
    int length;
    String Header="";

    public PacketP(PcapPacket p) {
        this.packet = p;
        //get source ip and destination
        if (packet.hasHeader(ip)) {
            this.SourceIP = FormatUtils.ip(ip.source());
            this.destinationIP = FormatUtils.ip(ip.destination());
        }
            //know the transport is TCP and get source and destination port number
            if (packet.hasHeader(tcp)) {
                this.transport = "TCP";
                if(tcp.source()==80){
                     this.Protocol = "HTTP";
                }
                else
                    this.Protocol = "TCP";
                this.portSource = tcp.source();
                this.PortDst = tcp.destination();
                Inti();
            }//know the transport is UDP and get source and destination port number
            else if (packet.hasHeader(udp)) {
                this.transport = "UDP";
                this.Protocol = "UDP";
                this.portSource = udp.source();
                this.PortDst = udp.destination();
                Inti();
            }
                    
            

    }
    
    private void Inti(){
            time = packet.getCaptureHeader().timestampInMillis();//time of capture packet
            timestamp=new Timestamp(time);
            lengthCaptured = packet.getCaptureHeader().caplen();//acual length of packet
            length = packet.getCaptureHeader().wirelen();//length on wire
            Header="Time: " + timestamp.toString() + " ,SourceIP: " + this.SourceIP + " ,DestinationIP: " + this.destinationIP + " ,Protocol: "+this.Protocol+" ,Length: " + this.length;
    }

}
