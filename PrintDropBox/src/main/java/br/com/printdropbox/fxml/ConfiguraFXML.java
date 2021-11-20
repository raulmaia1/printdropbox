package br.com.printdropbox.fxml;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class ConfiguraFXML {
	@FXML
	private TextField textDropBox, textLocalBD;
	@FXML
	private PasswordField passToken;
	
	@FXML
	private void salvar() {
		System.out.println("Ok");
	}
}
