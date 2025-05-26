package src;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class FXMLController {
	
	/*
	 * Deklarationen
	*/
	
	@FXML private Stage meineBuehne;
	@FXML private Label ausgabeWert;
	@FXML private Label ausgabeRom;
	
	@FXML private TextField eingabeRom;
	@FXML private TextField eingabeWert;
	
	
	/*
	 * Die Methoden	
	*/
		
		//die Methode setzt die Bühne auf den übergebenen Wert
		public void setMeineStage(Stage meineStage) {
			this.meineBuehne = meineStage;
		}
		
		//Methode zum Beenden
		@FXML protected void beendenKlick(ActionEvent event) {
			Platform.exit();
		}
		
		//Methode für die Software-Info
		@FXML protected void infoKlick(ActionEvent event) {
			Alert info = new Alert(AlertType.INFORMATION, "Von Martin Tastler");
			info.setHeaderText("Zahlenumrechner Version 1.0");
			info.show();
		}
}
		