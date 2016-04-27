package br.jus.stf.plataforma.documento.interfaces.commands;

public class PostSignCommand {

	private String signerId;
	private String signatureAsHex;

	public String getSignerId() {
		return signerId;
	}

	public void setSignerId(String signerId) {
		this.signerId = signerId;
	}

	public String getSignatureAsHex() {
		return signatureAsHex;
	}

	public void setSignatureAsHex(String signatureAsHex) {
		this.signatureAsHex = signatureAsHex;
	}

}
