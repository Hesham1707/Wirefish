/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wirefish;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import org.jnetpcap.Pcap;
import org.jnetpcap.PcapIf;
import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.packet.PcapPacketHandler;
import static wirefish.InterfaceWindowController.alldevs;
import static wirefish.InterfaceWindowController.index;

/**
 * FXML Controller class
 *
 * @author user1
 */
public class CapturePacketsController implements Initializable {

    @FXML
    private ListView<String> CapList;
    @FXML

    ObservableList<String> items = FXCollections.observableArrayList();
    Pcap pcap;
    Thread CaptureThread;
    ArrayList<PacketP> packets = new ArrayList<PacketP>();

    @FXML
    private void handleStopAction(ActionEvent event) {
        pcap.close();
        CaptureThread.stop();
        System.out.println("CAPTURE STOPPED");
    }

    PcapPacketHandler<String> jpacketHandler = new PcapPacketHandler<String>() {
        @Override
        public void nextPacket(PcapPacket packet, String user) {
            PacketP p = new PacketP(packet);
            packets.add(p);
            String RT = p.Header;
            System.out.printf(RT);

            
            if (!RT.equals("")) {
                items.add(RT);
                CapList.setItems(items);
            }
        }
    };

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        

        
        CaptureThread = new Thread() {
            public void run() {
                int snaplen = 64 * 1024;           // Capture all packets, no trucation  
                int flags = Pcap.MODE_PROMISCUOUS; // capture all packets  
                int timeout = 10 * 1000;           // 10 seconds in millis 
                StringBuilder errbuf = new StringBuilder();
                pcap = Pcap.openLive(alldevs.get(index).getName(), snaplen, flags, timeout, errbuf);
                pcap.loop(pcap.LOOP_INFINITE, jpacketHandler, "HESHAM rocks!");

            }
        };
        CaptureThread.start();
    }

}
