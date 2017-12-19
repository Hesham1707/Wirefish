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
import org.jnetpcap.protocol.tcpip.Http;
import org.jnetpcap.protocol.tcpip.Tcp;
import org.jnetpcap.protocol.tcpip.Udp;

/**
 *
 * @author user1
 */
public class PacketP {
    static int id=-1;
    PcapPacket packet;
    String Protocol = "";
    String SourceIP = "", destinationIP = "";
    int portSource, PortDst;
    Tcp tcp = new Tcp();
    Ethernet eh = new Ethernet();
    Ip4 ip = new Ip4();
    Udp udp = new Udp();
    Http http=new Http();
    long time;
    Timestamp timestamp;
    int lengthCaptured;
    int length;
    String Header = "";
    
    // Hossam 
    String EthDescription  = "No description found";
    String TcpUdpDescription  = "No description found";
    String HttpDescription  = "No description found";
    String IpV4Description  = "No description found";

    public PacketP(PcapPacket p) {
        this.packet = p;
        this.id++;
        //get source ip and destination
        
        if (packet.hasHeader(eh)) {
            this.setProtocol("Ethernet");  
            this.SourceIP = FormatUtils.mac(eh.source());
            this.destinationIP = FormatUtils.mac(eh.destination());
            this.EthDescription = packet.getHeader(eh).toString();
            if (packet.hasHeader(ip)) {
                this.setProtocol("IP4");
                this.SourceIP = FormatUtils.ip(ip.source());
                this.destinationIP = FormatUtils.ip(ip.destination());
                this.IpV4Description = packet.getHeader(ip).toString();
            }
            if (packet.hasHeader(tcp)) {
                this.setProtocol("TCP");
                this.portSource = tcp.source();
                this.PortDst = tcp.destination();
                this.TcpUdpDescription = packet.getHeader(tcp).toString();
                Inti();
            }//know the transport is UDP and get source and destination port number
            else if (packet.hasHeader(udp)) {
                this.setProtocol("UDP");
                //System.out.println(packet.getHeader(udp));
                this.portSource = udp.source();
                this.PortDst = udp.destination();
                this.TcpUdpDescription = packet.getHeader(udp).toString();
            }
            if(packet.hasHeader(http))
                this.setProtocol("HTTP");
            
            this.Inti();
           // this.TcpUdpDescription = packet.getHeader(http).toString();
        }
          
    }

    private void setProtocol(String protocol) {
        this.Protocol = protocol;
    }

    public String getProtocol() {
        return Protocol;
    }
    

    private void Inti() {
        time = packet.getCaptureHeader().timestampInMillis();//time of capture packet
        timestamp = new Timestamp(time);
        lengthCaptured = packet.getCaptureHeader().caplen();//acual length of packet
        length = packet.getCaptureHeader().wirelen();//length on wire
        Header = "id: "+this.id+" ,Time: " + timestamp.toString() + " ,SourceIP: " + this.SourceIP + " ,DestinationIP: " + this.destinationIP + " ,Protocol: " + this.Protocol + " ,Length: " + this.length;
    }

}
