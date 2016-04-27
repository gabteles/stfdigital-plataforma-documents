package br.jus.stf.plataforma.documento.domain.model.signature;

import br.jus.stf.plataforma.documento.domain.model.DocumentType;

public interface SigningSpecification {

	DocumentType documentType();

	SigningStrategy strategy();

}
