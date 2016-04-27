package br.jus.stf.plataforma.documento.domain.model.validation;

public class CertificateValidationException extends Exception {

	private static final long serialVersionUID = 1L;

	public CertificateValidationException(String msg) {
		super(msg);
	}

	public CertificateValidationException(String msg, Throwable e) {
		super(msg, e);
	}

}
