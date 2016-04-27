package br.jus.stf.plataforma.documento.interfaces.commands;

public class ProvideToSignCommand {

	private String signerId;
	private Long documentId;

	public String getSignerId() {
		return signerId;
	}

	public void setSignerId(String signerId) {
		this.signerId = signerId;
	}

	public Long getDocumentId() {
		return documentId;
	}

	public void setDocumentId(Long documentId) {
		this.documentId = documentId;
	}

}
