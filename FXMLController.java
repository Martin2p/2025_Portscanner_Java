package src;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;


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

	@FXML protected String gettingOpenPorts(ActionEvent event) {
		
			//Port range 1 to 65535
			int startPort = 1;
			int endPort = 65535;
			
			List<Integer> freePorts = new ArrayList<>();
		
			for(int port = startPort; port <= endPort; port++) {
				if(isPortFree(port)) {
					freePorts.add(port);
				}
			}
			
			//for (int i = 0; ) 
			//outputPorts.setText(freePorts);
		}
		
		//Method for port is open or not
		private static boolean isPortFree(int port) {
			
			try (ServerSocket serverSocket = new ServerSocket(port)) {
				serverSocket.setReuseAddress(true);
				return true;
			} catch (IOException e) {
				return false;
			}
		}
}
