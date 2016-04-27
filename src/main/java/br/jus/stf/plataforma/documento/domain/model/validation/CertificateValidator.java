package br.jus.stf.plataforma.documento.domain.model.validation;

import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.util.Calendar;
import java.util.Collection;

public interface CertificateValidator {

	CertificateValidation validateNow(X509Certificate certificate);

	CertificateValidation validateThen(X509Certificate cert, Calendar date);

	CertificateValidation validateThenWithCRLs(X509Certificate cert, Calendar date, Collection<X509CRL> crls);
	
}
