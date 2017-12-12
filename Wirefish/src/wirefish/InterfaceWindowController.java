/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wirefish;

import java.awt.Panel;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.jnetpcap.Pcap;
import org.jnetpcap.PcapIf;

public class InterfaceWindowController implements Initializable {

    @FXML
    private Pane Pane1;
    @FXML
    private Button confirm;
    @FXML
    private ListView<String> LV;

    ArrayList<PcapIf> alldevs = new ArrayList<PcapIf>(); // Will be filled with NICs  
    StringBuilder errbuf = new StringBuilder(); // For any error msgs  
    private ArrayList<Label> lab = new ArrayList();
    ObservableList<String> items = FXCollections.observableArrayList();

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
                Pcap pcap = Pcap.openLive(devices.get(i).getName(), snaplen, flags, timeout, errbuf);
                System.out.println("SUCCESS OPENING"+devices.get(i).getDescription());
//                if (pcap == null) {
//                    System.err.printf("Error while opening device for capture: " + errbuf.toString());
//                    return;
//                }
                break;
            }
        }

    }

    @FXML
    private void handleButtonAction(ActionEvent event) {
        selectDevice(LV.getSelectionModel().getSelectedItem());
//    String abc=LV.getSelectionModel().getSelectedItem();
//    System.out.println(abc);
    }

    public void initialize(URL url, ResourceBundle rb) {

        ArrayList<PcapIf> alldevs = getDevices();
        for (int i = 0; i < alldevs.size(); i++) {
            items.add(alldevs.get(i).getDescription());
        }

        LV.setItems(items);
        
    }

}
