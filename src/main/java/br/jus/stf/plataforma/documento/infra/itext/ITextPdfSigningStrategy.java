package br.jus.stf.plataforma.documento.infra.itext;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSignatureAppearance;
import com.itextpdf.text.pdf.PdfStamper;

import br.jus.stf.plataforma.documento.domain.PdfFileDocument;
import br.jus.stf.plataforma.documento.domain.model.Document;
import br.jus.stf.plataforma.documento.domain.model.signature.DocumentSignature;
import br.jus.stf.plataforma.documento.domain.model.signature.HashSignature;
import br.jus.stf.plataforma.documento.domain.model.signature.PdfSigningSpecification;
import br.jus.stf.plataforma.documento.domain.model.signature.PdfSigningStrategy;
import br.jus.stf.plataforma.documento.domain.model.signature.PreSignature;
import br.jus.stf.plataforma.documento.domain.model.signature.SignedDocument;
import br.jus.stf.plataforma.documento.domain.model.signature.SigningException;
import br.jus.stf.plataforma.documento.domain.model.validation.CertificateValidation;
import br.jus.stf.plataforma.documento.infra.PdfTempSignedDocument;

public class ITextPdfSigningStrategy implements PdfSigningStrategy {

	private static final String PDF_EXTENSION = ".pdf";
	private static final String TEMP_FILE_PREFIX = "iTextPDFSigning-";

	private ITextPdfSignatureFinisher finisher;

	private PdfSigningSpecification spec;

	private PdfSignatureAppearance appearance;

	public ITextPdfSigningStrategy(ITextPdfSignatureFinisher finisher) {
		this.finisher = finisher;
	}

	@Override
	public void prepareStrategyWith(PdfSigningSpecification spec) {
		this.spec = spec;
	}

	@Override
	public PreSignature preSign(Document document, CertificateValidation certificateValidation)
			throws SigningException {
		try {
			PdfReader reader = new PdfReader(document.stream());
			File tempFile = createPDFTempFile();

			PdfStamper stamper = PdfStamper.createSignature(reader, null, '\0', tempFile);
			appearance = stamper.getSignatureAppearance();

			appearance.setSignDate(Calendar.getInstance());

			appearance.setReason(spec.reason());

			return finisher.finishPreSignature(spec, certificateValidation, appearance);
		} catch (IOException e) {
			throw new SigningException("Erro ao ler PDF.", e);
		} catch (DocumentException e) {
			throw new SigningException("Erro ao carregar PDF.", e);
		}
	}

	@Override
	public SignedDocument postSign(HashSignature signature, CertificateValidation certificateValidation)
			throws SigningException {
		finisher.finishPostSignature(spec, certificateValidation, appearance, signature);

		return new PdfTempSignedDocument(new PdfFileDocument(appearance.getTempFile()), new DocumentSignature());
	}

	private File createPDFTempFile() throws SigningException {
		try {
			return File.createTempFile(TEMP_FILE_PREFIX, PDF_EXTENSION);
		} catch (IOException e) {
			throw new SigningException("Eror ao criar arquivo temporário para assinatura.", e);
		}
	}

}
