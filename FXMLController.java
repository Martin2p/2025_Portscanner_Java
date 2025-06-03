package src;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.net.InetAddress;
import java.net.UnknownHostException;


public class FXMLController {
	
	/*
	 * Declarations
	*/
	
	@FXML private Stage myStage;
	@FXML private Label outputIP;
	@FXML private Label outputPorts;
	
	
	/*
	 * Methods
	*/
		
		//Method for setting the stage
		public void setMyStage(Stage myStage) {
			this.myStage = myStage;
		}
		
		//Method for closing the program
		@FXML protected void closeClick(ActionEvent event) {
			Platform.exit();
		}
		
		//Method for Software-Info
		@FXML protected void infoClick(ActionEvent event) {
			Alert info = new Alert(AlertType.INFORMATION, "From Martin Tastler");
			info.setHeaderText("Portscanner Version Alpha 1");
			info.show();
		}
		
		@FXML protected String gettingIP(ActionEvent event) {
			try {
	            String ipAdress = InetAddress.getLocalHost().getHostAddress();
	            outputIP.setText(ipAdress);
	            
	        } catch (UnknownHostException e) {
	            e.printStackTrace();
	        }
	        return null; 
		}
}
