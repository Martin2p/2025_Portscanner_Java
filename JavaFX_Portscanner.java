package src;

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
			
	
		myStage.setTitle("Portscanner - Version 0.5");

		myStage.setScene(myScene);

		myStage.setResizable(false);

		myStage.show();
	}
		
	public static void main(String[] args) {
		//Start
		launch(args);
	}
}
