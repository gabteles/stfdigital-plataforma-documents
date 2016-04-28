package br.jus.stf.plataforma.documento.interfaces.validators;

import java.io.IOException;
import java.io.InputStream;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.io.IOUtils;
import org.springframework.web.multipart.MultipartFile;

public class DocumentSignatureValidatorMultipartFile extends GenericSignatureValidator
		implements ConstraintValidator<SignedDocument, MultipartFile> {

	@Override
	public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {
		InputStream is = null;
		try {
			is = value.getInputStream();
			return isValid(is);
		} catch (IOException e) {
			throw new RuntimeException("Erro ao recuperar documento para validação.", e);
		} finally {
			IOUtils.closeQuietly(is);
		}
	}

}
