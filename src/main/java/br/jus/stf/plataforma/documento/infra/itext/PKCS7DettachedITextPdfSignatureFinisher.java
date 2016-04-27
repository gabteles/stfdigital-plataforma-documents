package br.jus.stf.plataforma.documento.infra.itext;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CRLException;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfDate;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfSignature;
import com.itextpdf.text.pdf.PdfSignatureAppearance;
import com.itextpdf.text.pdf.PdfString;
import com.itextpdf.text.pdf.security.DigestAlgorithms;
import com.itextpdf.text.pdf.security.ExternalDigest;
import com.itextpdf.text.pdf.security.MakeSignature.CryptoStandard;
import com.itextpdf.text.pdf.security.PdfPKCS7;

import br.jus.stf.plataforma.documento.domain.model.signature.AuthenticatedAttributes;
import br.jus.stf.plataforma.documento.domain.model.signature.HashSignature;
import br.jus.stf.plataforma.documento.domain.model.signature.HashToSign;
import br.jus.stf.plataforma.documento.domain.model.signature.PdfSigningSpecification;
import br.jus.stf.plataforma.documento.domain.model.signature.PreSignature;
import br.jus.stf.plataforma.documento.domain.model.signature.SigningException;
import br.jus.stf.plataforma.documento.domain.model.validation.CertificateValidation;

public class PKCS7DettachedITextPdfSignatureFinisher implements ITextPdfSignatureFinisher {

	private byte[] firstHash;
	private PdfPKCS7 pdfPKCS7;
	private int estimatedSize;

	@Override
	public PreSignature finishPreSignature(final PdfSigningSpecification spec,
			CertificateValidation certificateValidation, PdfSignatureAppearance appearance) throws SigningException {
		try {
			int tamEstimado = ITextPdfSignatureUtil.estimateSignatureSize(certificateValidation.crls(), false, false);

			ExternalDigest externalDigest = new ExternalDigest() {
				@Override
				public MessageDigest getMessageDigest(String hashAlgorithm) throws GeneralSecurityException {
					return DigestAlgorithms.getMessageDigest(hashAlgorithm, null);
				}
			};

			PdfSignature dic = new PdfSignature(PdfName.ADOBE_PPKLITE, PdfName.ADBE_PKCS7_DETACHED);
			dic.setReason(appearance.getReason());
			dic.setDate(new PdfDate(appearance.getSignDate()));
			appearance.setCryptoDictionary(dic);
			HashMap<PdfName, Integer> exc = new HashMap<PdfName, Integer>();
			exc.put(PdfName.CONTENTS, new Integer(tamEstimado * 2 + 2));
			appearance.preClose(exc);

			PdfPKCS7 sgnNew = new PdfPKCS7(null, certificateValidation.certificateChain(), spec.hashType().name(), null,
					externalDigest, false);

			InputStream data = appearance.getRangeStream();
			byte[] primeiroHash = DigestAlgorithms.digest(data,
					externalDigest.getMessageDigest(spec.hashType().name()));

			Calendar cal = Calendar.getInstance();
			sgnNew.setSignDate(cal);
			byte[] authAttrs = sgnNew.getAuthenticatedAttributeBytes(primeiroHash, null,
					ITextPdfSignatureUtil.crlsToByteCollection(certificateValidation.crls()), CryptoStandard.CMS);

			this.firstHash = primeiroHash;
			this.pdfPKCS7 = sgnNew;
			this.estimatedSize = tamEstimado;

			return new PreSignature(new AuthenticatedAttributes(authAttrs),
					new HashToSign(ITextPdfSignatureUtil.applyHash(authAttrs, spec.hashType())), spec.hashType());
		} catch (IOException e) {
			throw new SigningException("Erro ler pré-assinatura do PDF..", e);
		} catch (DocumentException e) {
			throw new SigningException("Erro ao inciar assinatura do PDF.", e);
		} catch (NoSuchAlgorithmException e) {
			throw new SigningException("Erro ao calcular hash do PDF.", e);
		} catch (InvalidKeyException e) {
			throw new SigningException("Erro gerar assinatura PKCS7. Chave não encontrada.", e);
		} catch (NoSuchProviderException e) {
			throw new SigningException("Erro gerar assinatura PKCS7. Provedor não encontrado.", e);
		} catch (CRLException e) {
			throw new SigningException("Erro ao estimar tamanho da assinatura.", e);
		} catch (GeneralSecurityException e) {
			throw new SigningException("Erro genérico de segurança.", e);
		}
	}

	@Override
	public void finishPostSignature(PdfSigningSpecification spec, CertificateValidation certificateValidation,
			PdfSignatureAppearance appearance, HashSignature signature) throws SigningException {
		try {
			PdfPKCS7 sgnNew = pdfPKCS7;
			sgnNew.setExternalDigest(signature.signatureAsBytes(), null, "RSA");

			Collection<byte[]> crls = ITextPdfSignatureUtil.crlsToByteCollection(certificateValidation.crls());

			byte[] encodedSig = sgnNew.getEncodedPKCS7(firstHash, null, null, crls, CryptoStandard.CMS);
			byte[] paddedSig = new byte[estimatedSize];
			System.arraycopy(encodedSig, 0, paddedSig, 0, encodedSig.length);

			PdfDictionary dic2 = new PdfDictionary();
			dic2.put(PdfName.CONTENTS, new PdfString(paddedSig).setHexWriting(true));

			appearance.close(dic2);
		} catch (IOException e) {
			throw new SigningException("Erro ao finalizar montagem do PDF.", e);
		} catch (DocumentException e) {
			throw new SigningException("Erro ao fechar PDF assinado.", e);
		} catch (CRLException e) {
			throw new SigningException("Erro ao converter a CRL.", e);
		}
	}

}
