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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import org.jnetpcap.Pcap;
import org.jnetpcap.PcapIf;
import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.packet.PcapPacketHandler;

public class InterfaceWindowController implements Initializable {

    @FXML
    private Pane Pane1;
    @FXML
    private Button confirm;
    @FXML
    private ListView<String> LV;
    @FXML
    private Label title;
    @FXML
    private Button stop;

    ArrayList<PcapIf> alldevs = new ArrayList<PcapIf>(); // Will be filled with NICs  
    StringBuilder errbuf = new StringBuilder(); // For any error msgs  
    private ArrayList<Label> lab = new ArrayList();
    ObservableList<String> items = FXCollections.observableArrayList();
    Pcap pcap;
    Thread CaptureThread;

    public ArrayList<PcapIf> getDevices() {
        int r = Pcap.findAllDevs(alldevs, errbuf);
        return alldevs;
    }

    PcapPacketHandler<String> jpacketHandler = new PcapPacketHandler<String>() {
        public void nextPacket(PcapPacket packet, String user) {
            System.out.printf("Received packet at %s caplen=%-4d len=%-4d %s\n",
                    new Date(packet.getCaptureHeader().timestampInMillis()),
                    packet.getCaptureHeader().caplen(), // Length actually captured  
                    packet.getCaptureHeader().wirelen(), // Original length   
                    user // User supplied object  
            );
        }
    };

    public void selectDevice(String selected) {
        ArrayList<PcapIf> devices = getDevices();
        int snaplen = 64 * 1024;           // Capture all packets, no trucation  
        int flags = Pcap.MODE_PROMISCUOUS; // capture all packets  
        int timeout = 10 * 1000;           // 10 seconds in millis  

        for (int i = 0; i < devices.size(); i++) {
            if (devices.get(i).getDescription().equals(selected)) {
                pcap = Pcap.openLive(devices.get(i).getName(), snaplen, flags, timeout, errbuf);
                System.out.println("SUCCESS OPENING" + devices.get(i).getDescription());

                CaptureThread = new Thread() {
                    public void run() {
                        pcap.loop(pcap.LOOP_INFINITE, jpacketHandler, "HESHAM rocks!");
                    }
                };
                CaptureThread.start();
                break;
            }
        }
    }

    @FXML
    private void handleButtonAction(ActionEvent event) {
        selectDevice(LV.getSelectionModel().getSelectedItem());
    }

    @FXML
    private void handleButton1Action(ActionEvent event) {
        pcap.close();
        CaptureThread.stop();
        System.out.println("CAPTURE STOPPED");
    }

    public void initialize(URL url, ResourceBundle rb) {
        ArrayList<PcapIf> devices = getDevices();
        if (devices.size() == 0) {
            title.setText("No Network devices found");
        } else {
            title.setText("Network devices found:");
            for (int i = 0; i < devices.size(); i++) {
                items.add(devices.get(i).getDescription());
            }
            LV.setItems(items);
        }
    }

}
