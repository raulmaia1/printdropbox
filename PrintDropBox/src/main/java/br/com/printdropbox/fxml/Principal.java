package br.com.printdropbox.fxml;

import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Predicate;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;

import org.apache.commons.io.FilenameUtils;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPageable;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class Principal implements Initializable {
	@FXML
	private Label labelImpressora;
	@FXML
	private VBox vBoxLista;

	@FXML
	private Button buttonMonitora;

	private static final String ACCESS_TOKEN = "hy9DJwxpI-4AAAAAAAAAARdhRIgTbB5CytDE_t3KGh6VrePsNxhxGNKHSZC-cTI7";
	private static final String DIRETORIO = "/home/edu/Dropbox/Aplicativos/Print_CSR/CSR";
	private PrintService myPrintService;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		this.myPrintService = PrintServiceLookup.lookupDefaultPrintService();
		Platform.runLater(() -> {
			labelImpressora.setText(this.myPrintService.getName());
		});
		atualizaVBox();
	}

	@FXML
	private void monitora() {
		Platform.runLater(() -> {
			buttonMonitora.setDisable(true);
		});

		Task<Void> task = new Task<Void>() {
			@Override
			public Void call() {
				while (true) {
					if (new File(DIRETORIO).listFiles().length > 0) {
						imprimirDocumentos();
					}
					Platform.runLater(() -> {
						vBoxLista.getChildren().clear();
					});
					try {
						Thread.sleep(2000);
						atualizaVBox();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				}
			};

		new Thread(task).start();

	}

	private void atualizaVBox() {

		DbxRequestConfig config = DbxRequestConfig.newBuilder("csr").build();
		DbxClientV2 client = new DbxClientV2(config, ACCESS_TOKEN);

		try {

			ListFolderResult result = client.files().listFolder("/csr");
			while (true) {
				for (Metadata metadata : result.getEntries()) {
					Platform.runLater(() -> {
						Label label1 = new Label();
						label1.setText(metadata.getName());
						label1.getStyleClass().add("titulo-layout-secundario");
						vBoxLista.getChildren().add(label1);
					});
					/// System.out.println(metadata.getPathLower());
				}

				if (!result.getHasMore()) {
					break;
				}

				result = client.files().listFolderContinue(result.getCursor());
			}

		} catch (DbxException e) {
			e.printStackTrace();
		}
	}

	private void imprimirDocumentos() {
		File fileDir = new File(DIRETORIO);

		Predicate<File> imprimir = ((fileDiretorio) -> {

			for (File file : fileDiretorio.listFiles()) {
				String extension = FilenameUtils.getExtension(file.getAbsolutePath());

				if (extension.equals("pdf")) {
//				https://stackoverflow.com/questions/16293859/print-a-pdf-file-using-printerjob-in-java

					try {
						PDDocument document = Loader.loadPDF(file);

						PrinterJob job = PrinterJob.getPrinterJob();
						job.setPageable(new PDFPageable(document));
						job.setPrintService(myPrintService);
						job.print();

					} catch (PrinterException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}

			for (File file : fileDiretorio.listFiles()) {
				file.delete();
			}
			return true;
		});

		imprimir.test(fileDir);

	}

}
