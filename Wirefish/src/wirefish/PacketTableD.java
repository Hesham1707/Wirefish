/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wirefish;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Hossam
 */
public  class  PacketTableD {

        private final SimpleStringProperty no;
        private final SimpleStringProperty time;
        private final SimpleStringProperty source;
        private final SimpleStringProperty dest;
        private final SimpleStringProperty protocol;
        private final SimpleStringProperty length;
 
        public PacketTableD(String number, String time, String source , String dest , String protocol , String length) {
            this.no = new SimpleStringProperty(number);
            this.time = new SimpleStringProperty(time);
            this.source = new SimpleStringProperty(source);
            this.dest = new SimpleStringProperty(dest);
            this.protocol = new SimpleStringProperty(protocol);
            this.length = new SimpleStringProperty(length);
        }
 
        public String getNo() {
            return no.get();
        }
 
        public void setNo(String No) {
            no.set(No);
        }
 
        public String getTime() {
            return time.get();
        }
 
        public void setTime(String Time) {
            time.set(Time);
        }
 
        public String getSource() {
            return source.get();
        }
 
        public void setSource(String Source) {
            source.set(Source);
        }

        public String getDest() {
            return dest.get();
        }

        public void setDest(String Dest) {
            dest.set(Dest);
        }

        public String getProtocol() {
            return protocol.get();
        }

        public void setProtocol(String Protocol) {
            protocol.set(Protocol);
        }

        public String getLength() {
            return length.get();
        }

        public void setLength(String Length) {
            length.set(Length);
        }        
        
        
    }
