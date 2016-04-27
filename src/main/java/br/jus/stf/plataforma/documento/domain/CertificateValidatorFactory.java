package br.jus.stf.plataforma.documento.domain;

import br.jus.stf.plataforma.documento.domain.model.pki.Pki;
import br.jus.stf.plataforma.documento.domain.model.validation.CertificateValidator;

public interface CertificateValidatorFactory {

	CertificateValidator createCertificateValidator(Pki pki);

	CertificateValidator createCertificateValidator(Pki[] pkis);

}
