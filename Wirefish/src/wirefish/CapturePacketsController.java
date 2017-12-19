package wirefish;

import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import org.jnetpcap.Pcap;
import org.jnetpcap.PcapDumper;
import org.jnetpcap.PcapIf;
import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.packet.PcapPacketHandler;
import static wirefish.InterfaceWindowController.alldevs;
import static wirefish.InterfaceWindowController.index;
import static wirefish.Wirefish.StageOpened;

/**
 * FXML Controller class
 *
 * @author user1
 */
public class CapturePacketsController implements Initializable {

    @FXML
    private ListView<String> CapList;
    @FXML
    private Label hexatext;
    @FXML
    private Label EthTap;
    @FXML
    private Label IPv4Tap;
    @FXML
    private Label UDPTCPtap;
    @FXML
    private Label HttpTap;

    ObservableList<String> items = FXCollections.observableArrayList();
    Pcap pcap;
    Thread CaptureThread;
    ArrayList<PacketP> packets = new ArrayList<PacketP>();

    @FXML
    private void run() {
        int id = CapList.getSelectionModel().getSelectedIndex();
        hexatext.setText(packets.get(id).packet.toHexdump().toString());
        EthTap.setText(packets.get(id).EthDescription);
        IPv4Tap.setText(packets.get(id).IpV4Description);
        UDPTCPtap.setText(packets.get(id).TcpUdpDescription);
        HttpTap.setText(packets.get(id).HttpDescription);

    }

    @FXML
    private void handleStopAction(ActionEvent event) {
        pcap.close();
        CaptureThread.stop();
        System.out.println("CAPTURE STOPPED");
    }

    public void LoadFile() {
        FileChooser filechooser = new FileChooser();
        File sFile = filechooser.showOpenDialog(null);
        String fileName = sFile.getAbsolutePath();
        StringBuilder eBuffer = new StringBuilder();  
             Pcap pcap = Pcap.openOffline(fileName,eBuffer);
        PcapPacketHandler<String> jpacketHandler = new PcapPacketHandler<String>() {

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
        
         try {
           pcap.loop(Pcap.LOOP_INFINITE, jpacketHandler, "");
        }
        catch(Exception ex)
        {
            System.out.println("eof");
        }

    }

    //load
//        StringBuilder offlineErrBuffer = new StringBuilder(); // For any error msgs
//        //1-load offline fileS
//        Pcap pcapp = Pcap.openOffline(fromOutsideFilename, offlineErrBuffer);
//        //2-check if all OK
//        if (pcapp == null) {
//            System.err.printf("Error while opening device for capture: " + offlineErrBuffer.toString());
//            return;
//        }
//        //3- Create Packet handler ( same for openLive Capturing )
//
//        PcapPacketHandler<String> packetsFromFileHandler = new PcapPacketHandler<String>() {
//            public void nextPacket(PcapPacket packet, String user) {
//                CaptureThread = new Thread() {
//                    public void run() {
//                        int snaplen = 64 * 1024;           // Capture all packets, no trucation  
//                        int flags = Pcap.MODE_PROMISCUOUS; // capture all packets  
//                        int timeout = 10 * 1000;           // 10 seconds in millis 
//                        StringBuilder errbuf = new StringBuilder();
//                        pcap = Pcap.openLive(alldevs.get(index).getName(), snaplen, flags, timeout, errbuf);
//                        pcap.loop(pcap.LOOP_INFINITE, jpacketHandler, "HESHAM rocks!");
//
//                    }
//                };
//                CaptureThread.start();
//            }
//        }
//        );
//        pcapp.loop (Pcap.LOOP_INFINITE, packetsFromFileHandler,"");
//        public void save{
//        String fileName = "packetsCaptured" + (Capturer.fileNum) + ".pcap";
//        Capturer.fileNum++;
//        PcapDumper dumper = this.pcap.dumpOpen(fileName);
//        PcapPacketHandler<String> SavingPacketsHandler = new PcapPacketHandler<String>() {
//            @Override
//            public void nextPacket(PcapPacket packet, String user) {
//                dumper.dump(packet.getCaptureHeader(), packet);
//            }
//        };
//        pcap.loop(Capturer.packetsCounter, SavingPacketsHandler, "");
//        dumper.close();
//    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        CaptureThread = new Thread() {
            public void run() {
                int snaplen = 64 * 1024;           // Capture all packets, no trucation  
                int flags = Pcap.MODE_PROMISCUOUS; // capture all packets  
                int timeout = 10 * 1000;           // 10 seconds in millis 
                StringBuilder errbuf = new StringBuilder();
                pcap = Pcap.openLive(alldevs.get(index).getName(), snaplen, flags, timeout, errbuf);
                String ofile = "tmp-capture-file.pcap";
//    
                PcapDumper dumper = pcap.dumpOpen(ofile);
                PcapPacketHandler<String> jpacketHandler = new PcapPacketHandler<String>() {
                    @Override
                    public void nextPacket(PcapPacket packet, String user) {
                        dumper.dump(packet.getCaptureHeader(), packet);
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
                pcap.loop(pcap.LOOP_INFINITE, jpacketHandler, "HESHAM rocks!");

            }
        };
        CaptureThread.start();

    }
}
