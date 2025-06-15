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
	@FXML private ProgressIndicator progressLocal;
	@FXML private ProgressIndicator progressSystem;
	
	@FXML private RadioButton arpRadio;
	@FXML private RadioButton mdnsRadio;
	
	//for running tasks
	private Task<Void> currentMdnsTask;
	private JmDNS runningJmDNS = null;
	
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
			info.setHeaderText("Portscanner Version 1.0");
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
		
		
		//Method to cancel search
		@FXML protected void abortScan(ActionEvent event) {
			//controll
		    System.out.println("Abort clicked. currentMdnsTask: " + currentMdnsTask);

			
			if (currentMdnsTask != null &&  currentMdnsTask.isRunning()) {
				 System.out.println("Cancelling current scan...");
				 currentMdnsTask.cancel();
				
		         localHosts.setText("Scan stopped.");

		     // if JmDNS is still active -> close it
		     if (runningJmDNS != null) {

		         try {
		             runningJmDNS.close();
		             
		             //logging
		             System.out.println("JmDNS was closed manual.");
		            } catch (IOException e) {
		            	System.err.println("Error while closing JmDNS: " + e.getMessage());
		            }
		            runningJmDNS = null;
		        }
		        // GUI reset:
		        Platform.runLater(() -> {
			        progressLocal.progressProperty().unbind();
			        progressLocal.setProgress(0);
			        progressLocal.setVisible(false);
			        localHosts.setText("Scan stopped.");
		        });
			} else {
			 localHosts.setText("Nothing to stop!");
			}
		}
		
		
		//Portscan on your own system
		@FXML protected void gettingOpenPorts(ActionEvent event) {
		
			Task<Void> scanTask = new Task<>() {
				@Override
				protected Void call() throws Exception {
			
					//GUI-info for scanning
					Platform.runLater(() -> { 
						openPortsText.setText("Scanning...");
						
						//show an visual indicator
						progressSystem.setVisible(true);
					});
					
					//our own PC is the target for test
					String targetHost = "127.0.0.1";
					
					//Port range 1 to 65535
					int startPort = 1;
					int endPort = 65535;
					
					//Timeout if now response in milliseconds
					int timeout = 10;
					
					List<Integer> openPorts = new ArrayList<>();
				
					StringBuilder output = new StringBuilder();
		
					for(int port = startPort; port <= endPort; port++) {
						
						try (Socket socket = new Socket()) {
							socket.connect(new InetSocketAddress(targetHost, port), timeout);
							openPorts.add(port);
							output.append("Port ").append(port).append(" is open.\n");
							
							//update for visuall
							updateProgress(port - startPort, endPort - startPort);
							
						} catch (IOException e) {
						}
					}
				openPortsText.setText(output.toString());
				
				// GUI: hiding progress
				Platform.runLater(() -> {
					progressSystem.progressProperty().unbind();
					progressSystem.setProgress(0);
					progressSystem.setVisible(false);
				});
				
				return null;
				}
				
			};
			//updates the progress animation
			progressSystem.setProgress(-1); 
			progressSystem.progressProperty().bind(scanTask.progressProperty());
			
			//Start task in a new thread
	        Thread thread = new Thread(scanTask);
	
	        // ends automatic with the app
	        thread.setDaemon(true);
	        thread.start();
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
		protected void startMdnsScan(Task<?> task) throws IOException, InterruptedException {
		    InetAddress localHost = InetAddress.getLocalHost();
		    runningJmDNS = JmDNS.create(localHost);

		    String discoveryService = "_services._dns-sd._udp.local.";

		    runningJmDNS.addServiceListener(discoveryService, new ServiceListener() {
		        @Override
		        public void serviceAdded(ServiceEvent event) {
		            String type = event.getName() + "." + event.getType();
		            //logging
		            System.out.println("Found service: " + type);

		            // using listner für JmDNS
		            runningJmDNS.addServiceListener(type, new ServiceListener() {
		                @Override
		                public void serviceResolved(ServiceEvent event) {
		                    ServiceInfo info = event.getInfo();
		                    String name = info.getName();
		                    String address = info.getInetAddresses().length > 0
		                            ? info.getInetAddresses()[0].getHostAddress()
		                            : "unknown";
		                    int port = info.getPort();

		                    System.out.println("Found: " + name + " at " + address + ":" + port);

		                    Platform.runLater(() -> {
		                        localHosts.appendText("Service: " + name + "\n");
		                        localHosts.appendText("Address: " + address + "\n");
		                        localHosts.appendText("Port: " + port + "\n\n");
		                    });
		                }

		                @Override
		                public void serviceAdded(ServiceEvent e) { }
		                @Override
		                public void serviceRemoved(ServiceEvent e) { }
		            });
		        }

		        @Override public void serviceResolved(ServiceEvent e) { }
		        @Override public void serviceRemoved(ServiceEvent e) { }
		    });

		    // Timeout to find
		    int waitMs = 100;
		    int totalTime = 10_000;

		    for (int waited = 0; waited < totalTime; waited += waitMs) {
		        if (task.isCancelled()) {
		        	//logging
		            System.out.println("mDNS-Scan stopped.");
		            break;
		        }
		        Thread.sleep(waitMs);
		    }

		    // Cleaning
		    runningJmDNS.close();
		    runningJmDNS = null;
		}

		
		// Scanning for other hosts in the local network
		@FXML protected void gettingHosts(ActionEvent event) throws IOException {
			
			//logging
			System.out.println("Starting scan task...");
			//for that GUI does not freeze, there is a own task in the background
			currentMdnsTask = new Task<>() {
				@Override
				protected Void call() throws Exception {
					
					//GUI-info for scanning
					Platform.runLater(() -> { 
						localHosts.setText("Scanning...");
						//logging
						System.out.println("Scan task is running...");
						//show an visual indicator
						progressLocal.setVisible(true);
					});
				
					//with ARP Methode
					if (arpRadio.isSelected()) {
					    startArpScan();
					    
					//with mDNS Methode
					} else if (mdnsRadio.isSelected()) {
						startMdnsScan(this);
						return null;
					} else {
		                Platform.runLater(() -> localHosts.setText("Please choose a scan method."));
		            }
					// GUI: hiding progress
					Platform.runLater(() -> {
						progressLocal.progressProperty().unbind();
						progressLocal.setProgress(0);
						progressLocal.setVisible(false);
					});
	
		            return null;
				}
			};
			//updates the progress animation
			progressLocal.setProgress(-1); 
			progressLocal.progressProperty().bind(currentMdnsTask.progressProperty());
			
			//Start task in a new thread
	        Thread thread = new Thread(currentMdnsTask);
	
	        // ends automatic with the app
	        thread.setDaemon(true);
	        thread.start();
		}
}
