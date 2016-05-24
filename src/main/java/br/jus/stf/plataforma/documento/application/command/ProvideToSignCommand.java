package br.jus.stf.plataforma.documento.application.command;

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
