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
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.cell.PropertyValueFactory;
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
    private Label hexatext;
    @FXML
    private Label EthTap;
    @FXML
    private Label IPv4Tap;
    @FXML
    private Label UDPTCPtap;
    @FXML
    private Label HttpTap;
    @FXML
    private TextField filter;
    // the table and its columns 
    @FXML
    TableView<PacketTableD> PacketTable;
    @FXML
    TableColumn noColumn ;
    @FXML
    TableColumn timeColumn ;
    @FXML
    TableColumn sourceColumn ;
    @FXML
    TableColumn destColumn ;
    @FXML
    TableColumn protocolColumn;
    @FXML
    TableColumn lengthColumn ;

    
    ObservableList<String> items = FXCollections.observableArrayList();
    Pcap pcap;
    Thread CaptureThread;
    ArrayList<PacketP> packets = new ArrayList<PacketP>();
    ArrayList<PacketP> Allpackets = new ArrayList<PacketP>();
    
      private final ObservableList<PacketTableD> data =
        FXCollections.observableArrayList();
     //init columns 
       


    @FXML
    private void run() {

        
        int id = PacketTable.getSelectionModel().getSelectedIndex();
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

    PcapPacketHandler<String> jpacketHandler = new PcapPacketHandler<String>() {

        @Override
        public void nextPacket(PcapPacket packet, String user) {
            try {
                PacketP p = new PacketP(packet);
                packets.add(p);
                String RT = p.Header;
                PacketTableD PacketRow = new PacketTableD( p.PacketID+"" , p.time+"", p.SourceIP, p.destinationIP, p.Protocol, p.length+"");
                System.out.println(RT);

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        if (!RT.equals("")) {
                            data.add(PacketRow);
                            PacketTable.setItems(data);
                        }
                    }
                });

            } catch (Exception ex) {
            }
        }

    };

    public void LoadFile() {
        FileChooser filechooser = new FileChooser();
        File sFile = filechooser.showOpenDialog(null);
        String fileName = sFile.getAbsolutePath();
        StringBuilder eBuffer = new StringBuilder();
        Pcap pcap = Pcap.openOffline(fileName, eBuffer);

        try {
            pcap.loop(Pcap.LOOP_INFINITE, jpacketHandler, "");
        } catch (Exception ex) {
            System.out.println("eof");
        }
    }

    @FXML
    public void handleFilter(ActionEvent e) {
        System.out.println("Packets Filtered");
        ObservableList<String> fitems = FXCollections.observableArrayList();
        fitems=items;
        String text = filter.getText();
        System.out.print(text);
        switch (text) {
            case "udp":
            case "UDP":
                fitems = filterProtocol("UDP");
                break;
            case "tcp":
            case "TCP":
                fitems = filterProtocol("TCP");
                break;
            case "http":
            case "HTTP":
                fitems = filterProtocol("HTTP");
                break;
            case "eth":
            case "ETH":
            case "Ethernet":
                fitems = filterProtocol("Ethernet");
                break;
        }
           // CapList.getItems().clear();
          //  CapList.setItems(fitems);
        

    }

    public ObservableList<String> filterProtocol(String protocol) {
        Allpackets=new ArrayList(packets);
        packets.clear();
        ObservableList<String> fitems = FXCollections.observableArrayList();
        for (int i = 0; i <Allpackets.size(); i++) {
            System.out.print("asd");
            if (Allpackets.get(i).getProtocol().equals(protocol)) {
                packets.add(Allpackets.get(i));
                fitems.add(Allpackets.get(i).Header);
            }

        }
        return fitems;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
                        
       
        noColumn.setCellValueFactory(
                new PropertyValueFactory<PacketTableD, String>("no"));
 
       
        timeColumn.setCellValueFactory(
                new PropertyValueFactory<PacketTableD, String>("time"));
 
        
        sourceColumn.setCellValueFactory(
                new PropertyValueFactory<PacketTableD, String>("source"));

        
        destColumn.setCellValueFactory(
                new PropertyValueFactory<PacketTableD, String>("dest"));

        
        protocolColumn.setCellValueFactory(
                new PropertyValueFactory<PacketTableD, String>("protocol")); 
        
       
        lengthColumn.setCellValueFactory(
                new PropertyValueFactory<PacketTableD, String>("length"));
        
        PacketTable.setItems(data);
        //PacketTable.getColumns().addAll(noColumn , timeColumn , sourceColumn , destColumn , protocolColumn ,lengthColumn);

        CaptureThread = new Thread() {
            public void run() {
                int snaplen = 64 * 1024;           // Capture all packets, no trucation  
                int flags = Pcap.MODE_PROMISCUOUS; // capture all packets  
                int timeout = 10 * 1000;           // 10 seconds in millis 
                StringBuilder errbuf = new StringBuilder();
                pcap = Pcap.openLive(alldevs.get(index).getName(), snaplen, flags, timeout, errbuf);
                String ofile = "tmp-capture-file.pcap";
                PcapDumper dumper = pcap.dumpOpen(ofile);
                pcap.loop(pcap.LOOP_INFINITE, jpacketHandler, "HESHAM rocks!");

            }
        };
        CaptureThread.start();

    }
}


