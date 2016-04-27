package br.jus.stf.plataforma.documento.infra.itext;

import com.itextpdf.text.pdf.PdfSignatureAppearance;

import br.jus.stf.plataforma.documento.domain.model.signature.HashSignature;
import br.jus.stf.plataforma.documento.domain.model.signature.PdfSigningSpecification;
import br.jus.stf.plataforma.documento.domain.model.signature.PreSignature;
import br.jus.stf.plataforma.documento.domain.model.signature.SigningException;
import br.jus.stf.plataforma.documento.domain.model.validation.CertificateValidation;

public interface ITextPdfSignatureFinisher {

	PreSignature finishPreSignature(PdfSigningSpecification spec, CertificateValidation certificateValidation,
			PdfSignatureAppearance appearance) throws SigningException;

	void finishPostSignature(PdfSigningSpecification spec, CertificateValidation certificateValidation,
			PdfSignatureAppearance appearance, HashSignature signature) throws SigningException;

}
