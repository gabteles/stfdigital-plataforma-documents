package br.jus.stf.plataforma.documento.domain.model.validation;

import br.jus.stf.plataforma.documento.domain.model.Document;

public interface DocumentValidator {

	DocumentValidation validate(Document document);
	
}
