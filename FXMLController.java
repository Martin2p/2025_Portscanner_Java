package src;

/*
Copyright © 2025 Martin Tastler

DEUTSCH:
Dieses Programm und der Quellcode dürfen ausschließlich für private und nicht-kommerzielle Zwecke verwendet werden. 
Jede kommerzielle Nutzung, Veränderung, Verbreitung oder Veröffentlichung ist ohne ausdrückliche schriftliche Erlaubnis des Autors untersagt.

Das Kopieren oder Verwenden einzelner Codebestandteile für andere Projekte ist ebenfalls nicht gestattet, sofern keine vorherige Zustimmung vorliegt.
Bei Interesse an einer kommerziellen Nutzung oder Lizenzierung wenden Sie sich bitte an den Autor.

ENGLISH:
This software and its source code may only be used for private and non-commercial purposes.
Any commercial use, modification, distribution, or publication is strictly prohibited without the prior written permission of the author.

Copying or using individual parts of the code in other projects is also not permitted unless approved in advance.

For commercial licensing inquiries or permission requests, please contact the author.

-----------------------------------------
Author / Autor: Martin Tastler

martin.tastler@posteo.de

Date: June 2025

*/



import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceInfo;
import javax.jmdns.ServiceListener;


public class FXMLController {
	
	/*
	 * Declarations
	*/
	
	@FXML private Stage myStage;
	@FXML private TextArea outputIP;
	@FXML private TextArea freePortsText;
	@FXML private TextArea openPortsText;
	@FXML private TextArea localHosts;
	@FXML private ProgressIndicator progressIndicator;
	
	@FXML private RadioButton arpRadio;
	@FXML private RadioButton mdnsRadio;
	
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
			localHosts.clear();
			outputIP.setText(null);
		}
		
		@FXML protected void gettingIP(ActionEvent event) {
			try {
	            String ipAdress = InetAddress.getLocalHost().getHostAddress();
	            outputIP.setText(ipAdress);
	            
	        } catch (UnknownHostException e) {
	            e.printStackTrace();
	        }
		}
	
		//checking for open ports on your own system
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
		
		
		//Portscan on your own system
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
		
		
		//Networkscan with ARP-Table
		protected void startArpScan() {
			
			//After Port-Scan: getting more devices with ARP-Table
			StringBuilder arpOutput = new StringBuilder();
			arpOutput.append("\n--- Devices from ARP Table ---\n");
			
			
			//checking Operation System
			String os = System.getProperty("os.name").toLowerCase();
			String command = null;
			
			if (os.contains("win")) {
	                command = "arp -a";
	        } else if (os.contains("mac") || os.contains("nix") || os.contains("nux")) {
	                command = "ip neigh";
	        } else {
	                arpOutput.append("Not provided OS: ").append(os).append("\n");
	                Platform.runLater(() -> localHosts.appendText(arpOutput.toString()));
	      
	        }
			
			try {
				 Process arpProcess = Runtime.getRuntime().exec(command);

			    //reading ARP-Table
			    try (BufferedReader reader = new BufferedReader(
			    		
			            new InputStreamReader(arpProcess.getInputStream()))) {

			            String line;
			            while ((line = reader.readLine()) != null) {
			                arpOutput.append(line).append("\n");
			            }
			     }

			} catch (IOException e) {
			        arpOutput.append("Failed to read ARP-Table: ").append(e.getMessage()).append("\n");
			}

			  // Show result to GUI
			  Platform.runLater(() -> localHosts.appendText(arpOutput.toString()));
			}
		

		//Networkscan with mDNS
		protected void startMdnsScan() throws IOException {
			//use local IP Addresses
			InetAddress localHost = InetAddress.getLocalHost();
			
			//starting JmDNS instance
			try (JmDNS jmdns = JmDNS.create(localHost)) {
				
				//types like _http._tcp.local. oder _printer._tcp.local
				String serviceType = "_services._dns-sd._udp.local.";
				jmdns.addServiceListener(serviceType, new ServiceListener() {
	                @Override
	                public void serviceAdded(ServiceEvent event) {
	                    System.out.println("Service hinzugefügt: " + event.getName());
	                    // Optional: Serviceinfos 
	                    jmdns.requestServiceInfo(event.getType(), event.getName(), true);
	                }

	                @Override
	                public void serviceRemoved(ServiceEvent event) {
	                    System.out.println("Service entfernt: " + event.getName());
	                }

	                @Override
	                public void serviceResolved(ServiceEvent event) {
	                    ServiceInfo info = event.getInfo();
	                    System.out.println("Service gefunden:");
	                    System.out.println("  Name: " + info.getName());
	                    System.out.println("  Adresse: " + info.getInetAddresses()[0].getHostAddress());
	                    System.out.println("  Port: " + info.getPort());
	                }
	            });
			}
		}

		
		// Scanning for other hosts in the local network
		@FXML protected void gettingHosts(ActionEvent event) throws IOException {
			
			//for that GUI does not freeze, there is a own task in the background
			Task<Void> scanTask = new Task<>() {
				@Override
				protected Void call() throws Exception {
					
					//GUI-info for scanning
					Platform.runLater(() -> { 
						localHosts.setText("Scanning...");
						//show an visual indicator
						progressIndicator.setVisible(true);
						progressIndicator.setProgress(-1); 
					});
				
					//with ARP Methode
					if (arpRadio.isSelected()) {
					    startArpScan();
					
					//with mDNS Methode
					} else if (mdnsRadio.isSelected()) {
					    startMdnsScan();
					} else {
		                Platform.runLater(() -> localHosts.setText("Bitte eine Scan-Methode auswählen."));
		            }
					
					// GUI: Fortschrittsanzeige wieder verstecken
		            Platform.runLater(() -> progressIndicator.setVisible(false));
	
		            return null;
				}
			};
			//updates the progress animation
			 progressIndicator.progressProperty().bind(scanTask.progressProperty());
			
			//Start task in a new thread
	        Thread thread = new Thread(scanTask);
	
	        // ends automatic with the app
	        thread.setDaemon(true);
	        thread.start();
		}
}	
