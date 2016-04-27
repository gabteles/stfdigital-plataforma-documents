package br.jus.stf.plataforma.documento.domain;

import java.security.cert.X509Certificate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.jus.stf.plataforma.documento.domain.model.pki.Pki;
import br.jus.stf.plataforma.documento.domain.model.pki.PkiIds;
import br.jus.stf.plataforma.documento.domain.model.pki.PkiRepository;
import br.jus.stf.plataforma.documento.domain.model.validation.CertificateValidation;
import br.jus.stf.plataforma.documento.domain.model.validation.CertificateValidator;

@Component
public class CertificateValidationService {

	@Autowired
	private PkiRepository pkiRepository;

	@Autowired
	private CertificateValidatorFactory certificateValidatorFactory;

	public CertificateValidation validate(X509Certificate certificate, PkiIds pkiIds) {
		Pki[] pkis = pkiRepository.findAll(pkiIds.ids());
		CertificateValidator validator = certificateValidatorFactory.createCertificateValidator(pkis);
		return validator.validateNow(certificate);
	}

}
