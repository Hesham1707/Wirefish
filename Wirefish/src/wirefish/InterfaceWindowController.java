package wirefish;

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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import org.jnetpcap.Pcap;
import org.jnetpcap.PcapIf;
import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.packet.PcapPacketHandler;
import static wirefish.Wirefish.StageOpened;

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

    public static ArrayList<PcapIf> alldevs = new ArrayList<PcapIf>(); // Will be filled with NICs  
    StringBuilder errbuf = new StringBuilder(); // For any error msgs  
    private ArrayList<Label> lab = new ArrayList();
    ObservableList<String> items = FXCollections.observableArrayList();
    public static int index;

    public ArrayList<PcapIf> getDevices() {
        int r = Pcap.findAllDevs(alldevs, errbuf);
        return alldevs;
    }

    public void selectDevice(String selected) {
        ArrayList<PcapIf> devices = getDevices();
        int snaplen = 64 * 1024;           // Capture all packets, no trucation  
        int flags = Pcap.MODE_PROMISCUOUS; // capture all packets  
        int timeout = 10 * 1000;           // 10 seconds in millis  

        for (int i = 0; i < devices.size(); i++) {
            if (devices.get(i).getDescription().equals(selected)) {
                index = i;
                System.out.println("SUCCESS OPENING" + devices.get(i).getDescription());
                break;
            }
        }
    }

    @FXML
    private void handleButtonAction(ActionEvent event) {
        try {
            selectDevice(LV.getSelectionModel().getSelectedItem());
            FXMLLoader loader = new FXMLLoader(getClass().getResource("CapturePackets.fxml"));
            Parent root = loader.load();
            Scene sc1 = new Scene(root);
            StageOpened.setScene(sc1);
        } catch (IOException ex) {
            Logger.getLogger(InterfaceWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void handleButton1Action(ActionEvent event) {
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
