package br.jus.stf.plataforma.documento.domain.model.signature;

public class SigningException extends Exception {

	private static final long serialVersionUID = 1L;

	public SigningException(Throwable t) {
		super(t);
	}

	public SigningException(String msg) {
		super(msg);
	}

	public SigningException(String msg, Throwable t) {
		super(msg, t);
	}

}
