/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wirefish;



import java.sql.Timestamp;

import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.packet.format.FormatUtils;

import org.jnetpcap.protocol.lan.Ethernet;
import org.jnetpcap.protocol.network.Ip4;
import org.jnetpcap.protocol.tcpip.Tcp;
import org.jnetpcap.protocol.tcpip.Udp;

/**
 *
 * @author user1
 */
public class PacketP {

    PcapPacket packet;
    String Protocol = "";
    String SourceIP = "", destinationIP = "";
    int portSource, PortDst;
    Tcp tcp = new Tcp();
    Ethernet eh = new Ethernet();
    Ip4 ip = new Ip4();
    Udp udp = new Udp();
    
//    HTTP http=new HTTP();
    long time;
    Timestamp timestamp;
    int lengthCaptured;
    int length;
    String Header = "";

    public PacketP(PcapPacket p) {
        this.packet = p;
        //get source ip and destination
        
        if (packet.hasHeader(eh)) {
            this.setProtocol("Ethernet");  
            this.SourceIP = FormatUtils.mac(eh.source());
            this.destinationIP = FormatUtils.mac(eh.destination());
            System.out.println("Source :"+this.SourceIP);
            if (packet.hasHeader(ip)) {
                this.setProtocol("IP4");
                this.SourceIP = FormatUtils.ip(ip.source());
                this.destinationIP = FormatUtils.ip(ip.destination());
            }
            if (packet.hasHeader(tcp)) {
                this.setProtocol("TCP");
                this.portSource = tcp.source();
                this.PortDst = tcp.destination();
                Inti();
            }//know the transport is UDP and get source and destination port number
            else if (packet.hasHeader(udp)) {
                this.setProtocol("UDP");
                this.portSource = udp.source();
                this.PortDst = udp.destination();
                
            }
            if(this.portSource==80)
                this.setProtocol("HTTP");
            this.Inti();
        }

    }

    private void setProtocol(String protocol) {
        this.Protocol = protocol;
    }

    private void Inti() {
        time = packet.getCaptureHeader().timestampInMillis();//time of capture packet
        timestamp = new Timestamp(time);
        lengthCaptured = packet.getCaptureHeader().caplen();//acual length of packet
        length = packet.getCaptureHeader().wirelen();//length on wire
        Header = "Time: " + timestamp.toString() + " ,SourceIP: " + this.SourceIP + " ,DestinationIP: " + this.destinationIP + " ,Protocol: " + this.Protocol + " ,Length: " + this.length;
    }

}
