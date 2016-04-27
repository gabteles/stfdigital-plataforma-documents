package br.jus.stf.plataforma.documento.interfaces.validators;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
@Retention(RUNTIME)
@Constraint(validatedBy = { DocumentSignatureValidatorFile.class,
		DocumentSignatureValidatorMultipartFile.class })
public @interface SignedDocument {

	String message() default "Assinatura do documento inválida.";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

}
