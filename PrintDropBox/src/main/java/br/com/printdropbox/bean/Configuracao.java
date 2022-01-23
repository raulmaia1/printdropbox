package br.com.printdropbox.bean;

public class Configuracao {

	private String localPasta;
	private String impressora;
	private Integer id;

	public void setID(Integer id) {
		this.id = id;
	}

	public void setLocalPasta(String localPasta) {
		this.localPasta = localPasta;		
	}

	public Integer getId() {
		return id;
	}
	
	public String getLocalPasta() {
		return localPasta;
	}
	
	public String getImpressora() {
		return impressora;
	}
	
	public void setImpressora(String impressora) {
		this.impressora = impressora;
	}
}
