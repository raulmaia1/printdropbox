package br.com.printdropbox.fxml;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;

import br.com.printdropbox.dao.ConfiguracaoDaoJDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ConfiguraFXML implements Initializable{
	@FXML
	private TextField textPasta;
	@FXML
	private ComboBox<PrintService> comboImpressora;

	private Stage stage;
	private Stage stagePrincipal;
	@FXML
	private void salvar() {
		new ConfiguracaoDaoJDBC().atualiza(textPasta.getText(),comboImpressora.getSelectionModel().getSelectedItem().getName());
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Impressão");
		alert.setHeaderText(null);
		alert.setContentText("Abra o sistema novamente para atualizar suas configurações. ");
		alert.showAndWait();
		
		this.stagePrincipal.close();
		stage.close();
	}
		
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		new ConfiguracaoDaoJDBC().getConfiguracao().ifPresent(configuracao ->{	
			textPasta.setText(configuracao.getLocalPasta());
		});
		
		ObservableList<PrintService> options = 
			    FXCollections.observableArrayList(Arrays.asList(PrintServiceLookup.lookupPrintServices(null, null)));
		
		comboImpressora.getItems().addAll(options);
	
	}
	
	public void setStage(Stage stage, Stage stagePrincipal) {
		this.stage = stage;
		this.stagePrincipal = stagePrincipal;
	}
}
