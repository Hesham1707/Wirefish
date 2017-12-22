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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
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
import org.jnetpcap.util.PcapPacketArrayList;
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
    private TextArea hexatext;
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
    TableColumn noColumn;
    @FXML
    TableColumn timeColumn;
    @FXML
    TableColumn sourceColumn;
    @FXML
    TableColumn destColumn;
    @FXML
    TableColumn protocolColumn;
    @FXML
    TableColumn lengthColumn;
    @FXML
    Button filterbtn;
    @FXML
    Button startbtn;

    ObservableList<String> items = FXCollections.observableArrayList();
    Pcap pcap;
    Thread CaptureThread;
    ArrayList<PacketP> packets = new ArrayList<PacketP>();
    ArrayList<PacketP> Allpackets = new ArrayList<PacketP>();

    private ObservableList<PacketTableD> data
            = FXCollections.observableArrayList();
    private ObservableList<PacketTableD> FilteredData
            = FXCollections.observableArrayList();
    //init columns 

    @FXML
    private void run() {
        try{
        int id = PacketTable.getSelectionModel().getSelectedIndex();
        hexatext.setText(packets.get(id).packet.toHexdump());
        EthTap.setText(packets.get(id).EthDescription);
        IPv4Tap.setText(packets.get(id).IpV4Description);
        UDPTCPtap.setText(packets.get(id).TcpUdpDescription);
        HttpTap.setText(packets.get(id).HttpDescription);
        }catch(Exception ex){System.out.println("Random error occured, try again");}

    }
    @FXML
    private void handleStartButton(){
        
        filterbtn.setDisable(true);
        startbtn.setDisable(true);
        PacketTable.getItems().clear();
        Allpackets.clear();
        packets.clear();
        start();
        }

    @FXML
    private void handleStopAction(ActionEvent event) {
        try {
            pcap.close();
            CaptureThread.stop();
            System.out.println("CAPTURE STOPPED");
            filterbtn.setDisable(false);
            startbtn.setDisable(false);
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    @FXML
    public void LoadFile() {
        FileChooser filechooser = new FileChooser();
        File sFile = filechooser.showOpenDialog(null);
        Wirefish.fileName = sFile.getAbsolutePath();
        Wirefish.LoadMode = true;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("CapturePackets.fxml"));
            Parent root = loader.load();
            Scene sc1 = new Scene(root);
            StageOpened.setScene(sc1);
        } catch (IOException ex) {
            Logger.getLogger(InterfaceWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    public void handleFilter(ActionEvent e) {
        ObservableList<PacketTableD> FilteredData = FXCollections.observableArrayList();
        System.out.println("Packets Filtered");
        FilteredData = data;
        String text = filter.getText();
        System.out.print(text);
        switch (text) {
            case "udp":
            case "UDP":
                FilteredData = filterProtocol("UDP");
                break;
            case "tcp":
            case "TCP":
                FilteredData = filterProtocol("TCP");
                break;
            case "http":
            case "HTTP":
                FilteredData = filterProtocol("HTTP");
                break;
            case "eth":
            case "ETH":
            case "Ethernet":
                FilteredData = filterProtocol("Ethernet");
                break;
            case "Ipv4":
            case "Ip4":
            case "IPV4":
            case "ip4":
                FilteredData = filterProtocol("Ethernet");
                break;
        }
        PacketTable.getItems().clear();
        PacketTable.setItems(FilteredData);
        // CapList.getItems().clear();
        //  CapList.setItems(fitems);

    }

    public ObservableList<PacketTableD> filterProtocol(String protocol) {
        ObservableList<PacketTableD> FilteredData = FXCollections.observableArrayList();
        Allpackets = new ArrayList(packets);
        packets.clear();
        for (int i = 0; i < Allpackets.size(); i++) {

            if (Allpackets.get(i).getProtocol().equals(protocol)) {
                packets.add(Allpackets.get(i));
                PacketP p = Allpackets.get(i);
                System.out.println("asd");
                PacketTableD PacketRow = new PacketTableD(p.PacketID + "", p.time + "", p.SourceIP, p.destinationIP, p.Protocol, p.length + "");
                FilteredData.add(PacketRow);
            }
        }
        return FilteredData;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        filterbtn.setDisable(true);
        startbtn.setDisable(true);
        hexatext.setEditable(false);
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
        start();
    }
    public void start(){
    if (!Wirefish.LoadMode) {
            PacketP.resetPacketsID();
            CaptureThread = new Thread() {
                public void run() {
                    int snaplen = 64 * 1024;             
                    int flags = Pcap.MODE_PROMISCUOUS; 
                    int timeout = 10 * 1000;           
                    StringBuilder errbuf = new StringBuilder();
                    pcap = Pcap.openLive(alldevs.get(index).getName(), snaplen, flags, timeout, errbuf);
                    String ofile = "Pcap-Save-File.pcap";
                    PcapDumper dumper = pcap.dumpOpen(ofile);
                    PcapPacketHandler<String> jpacketHandler = new PcapPacketHandler<String>() {
                        @Override
                        public void nextPacket(PcapPacket packet, String user) {
                            dumper.dump(packet.getCaptureHeader(), packet);
                            PacketP p = new PacketP(packet);
                            packets.add(p);
                            String RT = p.Header;
                            PacketTableD PacketRow = new PacketTableD(p.PacketID + "", p.time + "", p.SourceIP, p.destinationIP, p.Protocol, p.length + "");
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
                        }

                    };
                    pcap.loop(pcap.LOOP_INFINITE, jpacketHandler, "HESHAM rocks!");

                }
            };
            CaptureThread.start();
        } else {
            PacketP.resetPacketsID();
            Wirefish.LoadMode = false;
            CaptureThread = new Thread() {
                public void run() {
                    System.out.println("Thread opened");
                    StringBuilder eBuffer = new StringBuilder();
                    pcap = Pcap.openOffline(Wirefish.fileName, eBuffer);
                    PcapPacketHandler<String> jpacketHandler = new PcapPacketHandler<String>() {
                        public void nextPacket(PcapPacket packet, String user) {
                            PacketP p = new PacketP(packet);
                            packets.add(p);
                            String RT = p.Header;
                            PacketTableD PacketRow = new PacketTableD(p.PacketID + "", p.time + "", p.SourceIP, p.destinationIP, p.Protocol, p.length + "");
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
                        }

                    };

                    try {

                        pcap.loop(Pcap.LOOP_INFINITE, jpacketHandler, "");

                    } catch (Exception ex) {
                        System.out.println("eof");
                    } finally {
                        pcap.close();
                        CaptureThread.stop();
                    }
                }
            };
            CaptureThread.start();
        }
    }
}
