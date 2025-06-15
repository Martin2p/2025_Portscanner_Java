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
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class JavaFX_Portscanner extends Application {

	@Override
	public void start(Stage myStage) throws Exception {
			
		//creating an instance of FXMLLoader
		FXMLLoader portscanner = new FXMLLoader(getClass().getResource("Portscanner.fxml"));
			
		//loading data
		Parent root = portscanner.load();
			
		//getting the controller 
		FXMLController trController = portscanner.getController();
			
		//give it to the stage
		trController.setMyStage(myStage);
			
		//create the scene
		Scene myScene = new Scene(root, 750, 560);
			
		myStage.setTitle("Portscanner - Version 1.0");
		myStage.setScene(myScene);
		myStage.setResizable(false);
		myStage.show();
	}
		
	public static void main(String[] args) {
		//Start
		launch(args);
	}
}
