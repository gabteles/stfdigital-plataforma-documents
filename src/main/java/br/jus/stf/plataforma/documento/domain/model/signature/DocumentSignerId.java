package br.jus.stf.plataforma.documento.domain.model.signature;

import org.apache.commons.lang3.Validate;

public class DocumentSignerId {

	private final String id;
	
	public DocumentSignerId(String id) {
		Validate.notEmpty(id);
		
		this.id = id;
	}
	
	public String id() {
		return id;
	}
	
}
