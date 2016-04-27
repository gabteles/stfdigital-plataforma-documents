package br.jus.stf.plataforma.documento.domain;

import br.jus.stf.plataforma.documento.domain.model.pki.Pki;
import br.jus.stf.plataforma.documento.domain.model.validation.DocumentValidator;

public interface DocumentValidatorFactory {

	DocumentValidator createDocumentSignatureValidator(Pki pki);

	DocumentValidator createDocumentSignatureValidator(Pki[] pkis);

}
