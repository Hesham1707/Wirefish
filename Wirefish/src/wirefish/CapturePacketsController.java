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
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import org.jnetpcap.Pcap;
import org.jnetpcap.PcapIf;
import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.packet.PcapPacketHandler;

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
    
    PcapPacketHandler<String> jpacketHandler = new PcapPacketHandler<String>() {
        public void nextPacket(PcapPacket packet, String user) {
            String RT="Recevied Pactket at "+packet.getCaptureHeader().timestampInMillis()+" caplen= "+packet.getCaptureHeader().caplen()+" len= "+packet.getCaptureHeader().wirelen();
            System.out.printf("Received packet at %s caplen=%-4d len=%-4d %s\n",
                    new Date(packet.getCaptureHeader().timestampInMillis()),
                    packet.getCaptureHeader().caplen(), // Length actually captured  
                    packet.getCaptureHeader().wirelen(), // Original length   
                    user // User supplied object  
            );
            try{
            items.add(RT);
            CapList.setItems(items);
            }
            catch(Exception ex){}
        }
    };
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        Thread CaptureThread = new Thread() {
            public void run() {
                pcap.loop(pcap.LOOP_INFINITE, jpacketHandler, "HESHAM rocks!");
            }
        };
                CaptureThread.start();
    }    
    
}
