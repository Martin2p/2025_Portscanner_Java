package src;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;



public class FXMLController {
	
	/*
	 * Declarations
	*/
	
	@FXML private Stage myStage;
	@FXML private Label outputIP;
	@FXML private TextArea freePortsText;
	@FXML private TextArea openPortsText;
	@FXML private TextArea localHosts;
	
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
			info.setHeaderText("Portscanner Version Alpha 2");
			info.show();
		}
		
		//Clear-Method
		@FXML protected void clear(ActionEvent event) {
			freePortsText.clear();
			openPortsText.clear();
		}
		
		@FXML protected void gettingIP(ActionEvent event) {
			try {
	            String ipAdress = InetAddress.getLocalHost().getHostAddress();
	            outputIP.setText(ipAdress);
	            
	        } catch (UnknownHostException e) {
	            e.printStackTrace();
	        }
		}
	
		@FXML protected void gettingFreePorts(ActionEvent event) {
		
			//Port range 1 to 65535
			int startPort = 1;
			int endPort = 65535;
			
			List<Integer> freePorts = new ArrayList<>();
		
			StringBuilder output = new StringBuilder();
			
			for(int port = startPort; port <= endPort; port++) {
				
				if(isPortFree(port)) {
					freePorts.add(port);
					 output.append("Port ").append(port).append(" is free.\n");
			        }
				}
				freePortsText.setText(output.toString());
			}
			
		
		//Method for port is free or not
		private static boolean isPortFree(int port) {
			
			try (ServerSocket serverSocket = new ServerSocket(port)) {
				serverSocket.setReuseAddress(true);
				return true;
			} catch (IOException e) {
				return false;
			}
		}
		

		@FXML protected void gettingOpenPorts(ActionEvent event) {
		
			//our own PC is the target for test
			String targetHost = "127.0.0.1";
			
			//Port range 1 to 65535
			int startPort = 1;
			int endPort = 65535;
			
			//Timeout if now responce in milliseconds
			int timeout = 200;
			
			List<Integer> openPorts = new ArrayList<>();
		
			StringBuilder output = new StringBuilder();

			
			
			for(int port = startPort; port <= endPort; port++) {
				
				try (Socket socket = new Socket()) {
					socket.connect(new InetSocketAddress(targetHost, port), timeout);
					openPorts.add(port);
					output.append("Port ").append(port).append(" is open.\n");
					
				} catch (IOException e) {
					openPortsText.setText("Timeout!");
				}
			}
			openPortsText.setText(output.toString());
		}
		
		
		@FXML protected void gettingHosts(ActionEvent event) {
			
		}
		
}
