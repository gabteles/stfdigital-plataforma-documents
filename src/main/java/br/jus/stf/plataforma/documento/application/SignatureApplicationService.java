package br.jus.stf.plataforma.documento.application;

import java.io.IOException;
import java.security.cert.X509Certificate;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import br.jus.stf.core.shared.documento.DocumentoId;
import br.jus.stf.core.shared.documento.DocumentoTemporarioId;
import br.jus.stf.core.shared.documento.PDFMultipartFile;
import br.jus.stf.plataforma.documento.application.command.UploadDocumentoCommand;
import br.jus.stf.plataforma.documento.domain.CertificateValidationService;
import br.jus.stf.plataforma.documento.domain.PdfSigningSpecificationBuilder;
import br.jus.stf.plataforma.documento.domain.PdfTempDocument;
import br.jus.stf.plataforma.documento.domain.model.Document;
import br.jus.stf.plataforma.documento.domain.model.DocumentoRepository;
import br.jus.stf.plataforma.documento.domain.model.pki.PkiIds;
import br.jus.stf.plataforma.documento.domain.model.signature.DocumentSignature;
import br.jus.stf.plataforma.documento.domain.model.signature.DocumentSigner;
import br.jus.stf.plataforma.documento.domain.model.signature.DocumentSignerFactory;
import br.jus.stf.plataforma.documento.domain.model.signature.DocumentSignerId;
import br.jus.stf.plataforma.documento.domain.model.signature.DocumentSignerRepository;
import br.jus.stf.plataforma.documento.domain.model.signature.HashSignature;
import br.jus.stf.plataforma.documento.domain.model.signature.HashType;
import br.jus.stf.plataforma.documento.domain.model.signature.PreSignature;
import br.jus.stf.plataforma.documento.domain.model.signature.SignedDocument;
import br.jus.stf.plataforma.documento.domain.model.signature.SigningException;
import br.jus.stf.plataforma.documento.domain.model.signature.SigningSpecification;
import br.jus.stf.plataforma.documento.domain.model.validation.CertificateValidation;

@Component
public class SignatureApplicationService {

	@Autowired
	private DocumentSignerRepository documentSignerRepository;

	@Autowired
	private CertificateValidationService certificateValidationService;

	@Autowired
	private DocumentoRepository documentoRepository;

	@Autowired
	private DocumentSignerFactory signerFactory;
	
	@Autowired
	private PdfSigningSpecificationBuilder specBuilder;
	
	@Autowired
	private DocumentoApplicationService documentoApplicationService;

	/**
	 * Recebe o certificado que vai assinar um documento, permitindo a criação
	 * de um assinador de documentos.
	 * 
	 * @param certificate
	 * @param pkiId
	 * @param reason
	 * @return
	 */
	public DocumentSignerId prepareToSign(X509Certificate certificate, PkiIds pkiIds, String reason)
			throws SigningException {
		CertificateValidation validation = certificateValidationService.validate(certificate, pkiIds);
		if (validation.valid()) {
			// Constrói uma especificação de assinatura de PDF.
			SigningSpecification spec = specBuilder.pkcs7Dettached().reason(reason).hashAlgorithm(HashType.SHA256).build();
			DocumentSigner signer = signerFactory.create(documentSignerRepository.nextId(), spec, validation);
			documentSignerRepository.save(signer);
			return signer.id();
		} else {
			return null;
		}
	}

	public void attachToSign(DocumentSignerId signerId, Document document) throws SigningException {
		DocumentSigner signer = documentSignerRepository.findOne(signerId);
		signer.attachDocumentToSign(document);
	}

	public void provideToSign(DocumentSignerId signerId, Long documentId) throws SigningException {
		Document document = retrieve(new DocumentoId(documentId));
		attachToSign(signerId, document);
	}

	private Document retrieve(DocumentoId documentoId) {
		return new PdfTempDocument(documentoRepository.download(documentoId).stream());
	}

	public PreSignature preSign(DocumentSignerId signerId) throws SigningException {
		DocumentSigner signer = documentSignerRepository.findOne(signerId);
		return signer.preSign();
	}

	public DocumentSignature postSign(DocumentSignerId signerId, HashSignature signature) throws SigningException {
		DocumentSigner signer = documentSignerRepository.findOne(signerId);
		return signer.postSign(signature);
	}

	public SignedDocument recoverSignedDocument(DocumentSignerId signerId) {
		DocumentSigner signer = documentSignerRepository.findOne(signerId);
		return signer.recoverSignedDocument();
	}

	public DocumentoTemporarioId saveSigned(DocumentSignerId signerId) {
		SignedDocument signedDocument = recoverSignedDocument(signerId);
		return upload(signerId.id(), signedDocument.document());
	}

	private DocumentoTemporarioId upload(String name, Document document) {
		try {
			MultipartFile file = new PDFMultipartFile(name, IOUtils.toByteArray(document.stream()));
			return new DocumentoTemporarioId(documentoApplicationService.handle(new UploadDocumentoCommand(file)));
		} catch (IOException e) {
			throw new IllegalStateException("Erro ao ler documento para upload.", e);
		}
	}

}
