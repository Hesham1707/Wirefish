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
   
    public ArrayList<PcapIf> getDevices() {
        ArrayList<PcapIf> alldevs = new ArrayList<PcapIf>(); // Will be filled with NICs  
        StringBuilder errbuf = new StringBuilder(); // For any error msgs  

        int r = Pcap.findAllDevs(alldevs, errbuf);

        if (r == Pcap.NOT_OK || alldevs.isEmpty()) {
            if (alldevs.isEmpty()) {
                System.out.println("NOT OK");
            }
            System.err.printf("Can't read list of devices, error is %s", errbuf.toString());
            return alldevs;
        }

        System.out.println("Network devices found:");

        int i = 0;
        for (PcapIf device : alldevs) {
            String description
                    = (device.getDescription() != null) ? device.getDescription()
                    : "No description available";
            System.out.printf("#%d: %s [%s]\n", i++, device.getName(), description);
        }

        PcapIf device = alldevs.get(0); // We know we have atleast 1 device  
        System.out
                .printf("\nChoosing '%s' on your behalf:\n",
                        (device.getDescription() != null) ? device.getDescription()
                        : device.getName());
        return alldevs;
    }
    
    @FXML
    private void handleButtonAction(ActionEvent event) {
        System.out.println("You clicked me!");
        Pane1.setVisible(false);
    }
    
    public void initialize(URL url, ResourceBundle rb) {
        
//        lab.add(new Label("YAHOOOOOOOOO"));   
        Button b = new Button("Hello");
       
        Pane1.getChildren().add(b);
        
        ArrayList<PcapIf> alldevs = getDevices();
        for (int i = 0; i < alldevs.size(); i++) {
            alldevs.get(i).getName();
            
        }
    }

}
