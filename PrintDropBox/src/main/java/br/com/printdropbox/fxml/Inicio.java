package br.com.printdropbox.fxml;

import com.dustinredmond.fxtrayicon.FXTrayIcon;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Inicio extends Application {


	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Principal.class.getResource("principal.fxml"));
		Scene scene = new Scene(loader.load());
		primaryStage.setScene(scene);
		Principal controller = loader.getController();
		controller.setStagePrincipal(primaryStage);
		
		try {
			FXTrayIcon fxTrayIcon = new FXTrayIcon.Builder(primaryStage, Principal.class.getResource("32px-Icons8_flat_print.png"))
					.menuItem("Abrir", e -> primaryStage.show())
					.addExitMenuItem()
					.applicationTitle("Print")
					.show().build();
			
			fxTrayIcon.show();
			
			 
			
		}catch (RuntimeException e) {
			primaryStage.show();
		}
		

	}
	
	
	
	@Override
	public void stop() throws Exception {
		       
       
		Principal.task1.cancel();
		
		super.stop();
	}
	

}
