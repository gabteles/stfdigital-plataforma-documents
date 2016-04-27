package br.jus.stf.plataforma.documento.interfaces.dto;

import org.apache.commons.lang3.Validate;

public class SignedDocumentDto {

	private String documentId;

	public SignedDocumentDto(String id) {
		Validate.notNull(id);

		documentId = id;
	}

	public String getDocumentId() {
		return documentId;
	}

	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}

}
