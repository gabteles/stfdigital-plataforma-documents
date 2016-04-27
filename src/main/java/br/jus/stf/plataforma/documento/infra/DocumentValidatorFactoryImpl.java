package br.jus.stf.plataforma.documento.infra;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.jus.stf.plataforma.documento.domain.CertificateValidatorFactory;
import br.jus.stf.plataforma.documento.domain.DocumentValidatorFactory;
import br.jus.stf.plataforma.documento.domain.model.pki.CompositePki;
import br.jus.stf.plataforma.documento.domain.model.pki.Pki;
import br.jus.stf.plataforma.documento.domain.model.validation.DocumentValidator;
import br.jus.stf.plataforma.documento.infra.itext.ITextPdfSignatureValidator;

@Component
public class DocumentValidatorFactoryImpl implements DocumentValidatorFactory {

	@Autowired
	private CertificateValidatorFactory certificateValidationFactory;

	@Override
	public DocumentValidator createDocumentSignatureValidator(Pki[] pkis) {
		return createDocumentSignatureValidator(new CompositePki(pkis));
	}

	@Override
	public DocumentValidator createDocumentSignatureValidator(Pki pki) {
		return new ITextPdfSignatureValidator(certificateValidationFactory.createCertificateValidator(pki));
	}

}
