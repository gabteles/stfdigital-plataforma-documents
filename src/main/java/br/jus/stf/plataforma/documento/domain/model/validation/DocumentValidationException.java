package br.jus.stf.plataforma.documento.domain.model.validation;

import java.util.List;
import java.util.Optional;

public class DocumentValidationException extends Exception {

	private static final long serialVersionUID = 1L;

	private List<DocumentValidationException> causeValidationExceptions;

	public DocumentValidationException(String message) {
		super(message);
	}

	public DocumentValidationException() {
		super();
	}

	public DocumentValidationException(String message, Throwable cause) {
		super(message, cause);
	}

	public DocumentValidationException(Throwable cause) {
		super(cause);
	}

	public DocumentValidationException(String message, List<DocumentValidationException> causeValidationExceptions) {
		super(message);
		this.causeValidationExceptions = causeValidationExceptions;
	}

	@Override
	public String getMessage() {
		StringBuilder sb = new StringBuilder();
		sb.append(super.getMessage());

		Optional.ofNullable(causeValidationExceptions).ifPresent(l -> l.forEach(e -> sb.append(" Erro ao validar campo de assinatura: " + e.getMessage())));

		return sb.toString();
	}

}
