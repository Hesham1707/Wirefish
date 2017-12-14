/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wirefish;

import java.util.ArrayList;
import java.util.List;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.jnetpcap.*;


/**
 *
 * @author Hesham-Desktop
 */
public class Wirefish extends Application {
    public static Stage StageOpened;
    public static Scene sc;
    private static InterfaceWindowController wc=new InterfaceWindowController();
        @Override
    public void start(Stage primaryStage) throws Exception {
        StageOpened=primaryStage;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("InterfaceWindow.fxml"));
        Parent root = loader.load();
        sc = new Scene(root);
        StageOpened.setScene(sc);
        StageOpened.show();
    }

    

    public static void main(String[] args) {
        launch(args); 
        
       
    }
    
}
