package br.jus.stf.plataforma.documento.domain.model.signature;

import br.jus.stf.plataforma.documento.domain.model.Document;
import br.jus.stf.plataforma.documento.domain.model.validation.CertificateValidation;

public interface SigningStrategy {

	PreSignature preSign(Document document, CertificateValidation certificateValidation) throws SigningException;

	SignedDocument postSign(HashSignature signature, CertificateValidation certificateValidation)
			throws SigningException;

}
