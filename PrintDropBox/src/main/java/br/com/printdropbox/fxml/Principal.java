package br.com.printdropbox.fxml;

import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;

import org.apache.commons.io.FilenameUtils;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPageable;

import br.com.printdropbox.dao.ConfiguracaoDaoJDBC;
import br.com.printdropbox.task.ImpressaoTask;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Principal implements Initializable {
	@FXML
	private Label labelImpressora;
	@FXML
	private VBox vBoxLista;
	@FXML
	private Button btnConfigura;

	public List<File> listaImpressao = new ArrayList<>();
	private String diretorio;
	private PrintService myPrintService;

	private Stage stagePrincipal;
	public static Task<Void> task1;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		new ConfiguracaoDaoJDBC().getConfiguracao().ifPresent(cfg -> {
			diretorio = cfg.getLocalPasta();
			String impressora = cfg.getImpressora();

			System.out.println(impressora);
			Arrays.asList(PrintServiceLookup.lookupPrintServices(null, null)).stream()
					.filter(p -> p.getName().equals(impressora)).findAny().ifPresent(print -> {
						this.myPrintService = print;
						Platform.runLater(() -> {
							labelImpressora.setText(this.myPrintService.getName());
						});
					});

		});

//		https://stackoverflow.com/a/44155625
//		Design classe anonima para manipular componentes da classe Principal

		ImpressaoTask impressaoTask = ((task) -> {

			while (true) {
				if (task.isCancelled()) {
					break;
				} else {
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					for (File file : new File(diretorio).listFiles()) {
						if (!listaImpressao.contains(file)) {
							imprimirDocumento(file);
							listaImpressao.add(file);
							adicionaLabelETempoDeDoisSegundos(file.getName());
						}
					}
					System.out.println("---");
				}
			}
		});

		Principal.task1 = new Task<Void>() {

			@Override
			protected Void call() throws Exception {
				impressaoTask.verificarImpressao(this);
				return null;
			}

		};

		Thread thread1 = deletarPdfs(diretorio);
		thread1.start();

		try {
			thread1.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		Thread thread2 = new Thread(task1);
		thread2.start();
	}

	private void imprimirDocumento(File file) {

		System.out.println("--------");
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		System.out.println("--------");
		String extension = FilenameUtils.getExtension(file.getAbsolutePath());

		if (extension.contains("pdf") || extension.contains("PDF")) {
//				https://stackoverflow.com/questions/16293859/print-a-pdf-file-using-printerjob-in-java

			try {
				PDDocument document = Loader.loadPDF(file);

				PrinterJob job = PrinterJob.getPrinterJob();
				job.setPageable(new PDFPageable(document));
				job.setPrintService(myPrintService);
				job.print();

				System.out.println(job);

			} catch (PrinterException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@FXML
	private void abri_configuracao() {
		Stage stage = new Stage();
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Principal.class.getResource("configura.fxml"));
		try {
			Scene scene = new Scene(loader.load());
			stage.setScene(scene);

			ConfiguraFXML controller = loader.getController();
			controller.setStage(stage, stagePrincipal);
			stage.showAndWait();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void adicionaLabelETempoDeDoisSegundos(String nomeArquivo) {

		Platform.runLater(() -> {
			Label label1 = new Label("Ok - " + nomeArquivo);
			label1.getStyleClass().add("titulo-layout-secundario");
			vBoxLista.getChildren().add(label1);

		});
	}

	public void setStagePrincipal(Stage stagePrincipal) {
		this.stagePrincipal = stagePrincipal;
	}

	private Thread deletarPdfs(String diretorio) {
		Task<Void> task2 = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				for (File file : new File(diretorio).listFiles()) {
					file.delete();
					System.out.println(file);
				}
				return null;
			}

		};

		return new Thread(task2);
	}
}
