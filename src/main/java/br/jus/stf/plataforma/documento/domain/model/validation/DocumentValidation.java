package br.jus.stf.plataforma.documento.domain.model.validation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DocumentValidation {

	private List<String> validationErrors = new ArrayList<>();
	
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
