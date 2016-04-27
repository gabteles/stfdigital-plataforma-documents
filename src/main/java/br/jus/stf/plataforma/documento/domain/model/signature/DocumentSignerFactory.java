package br.jus.stf.plataforma.documento.domain.model.signature;

import org.springframework.stereotype.Component;

import br.jus.stf.plataforma.documento.domain.model.validation.CertificateValidation;

@Component
public class DocumentSignerFactory {

	public DocumentSigner create(DocumentSignerId signerId, SigningSpecification spec, CertificateValidation validation) {
		return new DocumentSigner(signerId, spec, validation);
	}

}
