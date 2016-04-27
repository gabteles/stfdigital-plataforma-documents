package br.jus.stf.plataforma.documento.domain.model.validation;

import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CertificateValidation {

	private X509Certificate[] certificateChain;

	private List<String> validationErrors = new ArrayList<>();

	public CertificateValidation() {
	}

	public void associateChain(X509Certificate[] chain) {
		this.certificateChain = chain;
	}
	
	public X509Certificate[] certificateChain() {
		return certificateChain;
	}

	public X509CRL[] crls() {
		return new X509CRL[0];
	}

	public void appendValidationError(String error) {
		validationErrors.add(error);
	}

	public List<String> validationErrors() {
		return Collections.unmodifiableList(validationErrors);
	}

	public boolean valid() {
		return validationErrors.isEmpty();
	}

}
