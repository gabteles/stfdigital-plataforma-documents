package br.jus.stf.plataforma.documento.interfaces.validators;

import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.jus.stf.plataforma.documento.application.DocumentValidatorApplicationService;
import br.jus.stf.plataforma.documento.domain.PdfInputStreamDocument;
import br.jus.stf.plataforma.documento.domain.model.pki.PkiIds;
import br.jus.stf.plataforma.documento.domain.model.pki.PkiType;
import br.jus.stf.plataforma.documento.domain.model.validation.DocumentValidation;

@Component
public abstract class GenericSignatureValidator {

	@Autowired
	protected DocumentValidatorApplicationService documentValidatorApplicationService;
	
	public void initialize(SignedDocument constraintAnnotation) {
		
	}

	public boolean isValid(InputStream value) {
		DocumentValidation validation = documentValidatorApplicationService.validateDocumentSignature(new PdfInputStreamDocument(value), new PkiIds(PkiType.ICP_BRASIL.id(), PkiType.ICP_PLATAFORMA.id()));
		return validation.valid();
	}
	
}
