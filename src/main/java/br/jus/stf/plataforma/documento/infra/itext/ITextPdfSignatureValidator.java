package br.jus.stf.plataforma.documento.infra.itext;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.cert.CRL;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.security.PdfPKCS7;

import br.jus.stf.plataforma.documento.domain.model.Document;
import br.jus.stf.plataforma.documento.domain.model.certificate.CertificateUtils;
import br.jus.stf.plataforma.documento.domain.model.validation.CertificateValidation;
import br.jus.stf.plataforma.documento.domain.model.validation.CertificateValidator;
import br.jus.stf.plataforma.documento.domain.model.validation.DocumentValidation;
import br.jus.stf.plataforma.documento.domain.model.validation.DocumentValidationException;
import br.jus.stf.plataforma.documento.domain.model.validation.DocumentValidator;
import br.jus.stf.plataforma.documento.infra.configuration.CryptoProvider;

/**
 * Validador de documento pdf que utiliza o iText.
 * 
 * @author Leandro.Oliveira
 * @author Alberto.Soares
 * @author Tomas.Godoi
 *
 */
public class ITextPdfSignatureValidator implements DocumentValidator {

	boolean acceptAtLeastOneValidSignature = true;
	boolean acceptSignatureWithExpiredCertificate = false;
	boolean acceptSignatureWithoutChain = true;
	boolean verifyCRL = false;
	private CertificateValidator certificateValidator;

	public ITextPdfSignatureValidator(CertificateValidator certificateValidator) {
		this.certificateValidator = certificateValidator;
	}

	@Override
	public DocumentValidation validate(Document document) {
		DocumentValidation validation = new DocumentValidation();

		PdfReader reader = null;
		try {
			reader = openPdf(document.stream());

			// Recupera os campos das assinaturas do pdf.
			AcroFields acroFields = reader.getAcroFields();

			validateAcroFields(acroFields);

			List<String> fields = acroFields.getSignatureNames();

			validateFields(fields);

			int validSignatures = 0;
			List<DocumentValidationException> invalidSignatureExceptions = new ArrayList<>();

			// Valida cada campo de assinatura.
			for (String fieldName : fields) {
				// Recupera um PdfPKCS7 para continuar a verificação.
				PdfPKCS7 pdfPKCS7 = acroFields.verifySignature(fieldName, CryptoProvider.provider());

				try {
					validatePKCS7(pdfPKCS7);

					validateField(acroFields, fieldName);

					validateCertificates(pdfPKCS7);

					validSignatures++;
				} catch (DocumentValidationException e) {
					if (!acceptAtLeastOneValidSignature) {
						throw e;
					}
					invalidSignatureExceptions.add(e);
				}

			}
			
			if (validSignatures == 0) {
				throw new DocumentValidationException(
						"Nenhuma assinatura existente no documento foi considerada válida.",
						invalidSignatureExceptions);
			}
		} catch (DocumentValidationException e) {
			validation.appendValidationError(e.getMessage());
		} finally {
			if (reader != null) {
				reader.close();
			}
		}
		return validation;
	}

	private PdfReader openPdf(InputStream pdf) {
		try {
			return new PdfReader(pdf);
		} catch (IOException e) {
			throw new RuntimeException("Falha ao abrir o pdf.", e);
		}
	}

	private void validateAcroFields(AcroFields acroFields) throws DocumentValidationException {
		Optional.ofNullable(acroFields).orElseThrow(
				() -> new DocumentValidationException("Não foi possível recuperar os campos de assinatura do pdf."));
	}

	private void validateFields(List<String> fields) throws DocumentValidationException {
		if (fields == null || fields.isEmpty()) {
			throw new DocumentValidationException("Não foi possível recuperar os campos de assinatura do pdf.");
		}
	}

	private void validatePKCS7(PdfPKCS7 pdfPKCS7) throws DocumentValidationException {
		try {
			// Verifica se o PdfPKCS7 do campo de assinatura foi encontrado
			Optional.ofNullable(pdfPKCS7)
					.orElseThrow(() -> new DocumentValidationException("Assinatura não encontrada no pdf."));

			// Verifica o digesto da assinatura
			if (!pdfPKCS7.verify()) {
				throw new DocumentValidationException("O digesto da assinatura não confere.");
			}
		} catch (GeneralSecurityException e) {
			throw new DocumentValidationException("Erro de segurança ao verificar o digesto da assinatura.", e);
		}
	}

	private void validateField(AcroFields acroFields, String fieldName) throws DocumentValidationException {
		// Verifica se a assinatura compreende o documento todo
		if (!acroFields.signatureCoversWholeDocument(fieldName)) {
			throw new DocumentValidationException("A assinatura não compreende o documento todo.");
		}
	}

	private void validateCertificates(PdfPKCS7 pdfPKCS7) throws DocumentValidationException {
		X509Certificate cert = pdfPKCS7.getSigningCertificate();
		Collection<CRL> crls = pdfPKCS7.getCRLs();
		Calendar signDate = pdfPKCS7.getSignDate();

		CertificateValidation validation;
		if (crls == null) {
			validation = certificateValidator.validateThen(cert, signDate);
		} else {
			validation = certificateValidator.validateThenWithCRLs(cert, signDate,
					crls.stream().map(X509CRL.class::cast).collect(Collectors.toList()));
		}

		if (!validation.valid()) {
			StringBuilder sb = new StringBuilder();
			validation.validationErrors().forEach(e -> sb.append(e));
			sb.append(" . Certificado: " + CertificateUtils.subjectNameAsString(cert));
			throw new DocumentValidationException(sb.toString());
		}

		X509Certificate[] pdfOriginalCertificateChain = (X509Certificate[]) pdfPKCS7.getSignCertificateChain();
		if (!acceptSignatureWithoutChain) {
			if (!Arrays.equals(pdfOriginalCertificateChain, validation.certificateChain())) {
				throw new DocumentValidationException("Cadeia de certificação incompleta.");
			}
		} else {
			if (pdfOriginalCertificateChain.length > validation.certificateChain().length) {
				throw new DocumentValidationException("Cadeia de certificação inválida.");
			} else {
				int i = 0;
				for (X509Certificate c : pdfOriginalCertificateChain) {
					if (!c.equals(validation.certificateChain()[i])) {
						throw new DocumentValidationException("Cadeia de certificação inválida.");
					}
					i++;
				}
			}
		}

	}

}
