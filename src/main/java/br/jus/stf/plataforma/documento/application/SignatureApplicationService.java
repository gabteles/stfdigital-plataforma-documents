package br.jus.stf.plataforma.documento.application;

import java.io.IOException;
import java.security.cert.X509Certificate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.jus.stf.core.shared.documento.DocumentoId;
import br.jus.stf.core.shared.documento.DocumentoTemporarioId;
import br.jus.stf.plataforma.documento.domain.CertificateValidationService;
import br.jus.stf.plataforma.documento.domain.DocumentAdapter;
import br.jus.stf.plataforma.documento.domain.PdfSigningSpecificationBuilder;
import br.jus.stf.plataforma.documento.domain.model.Document;
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
	private DocumentAdapter documentAdapter;

	@Autowired
	private DocumentSignerFactory signerFactory;
	
	@Autowired
	private PdfSigningSpecificationBuilder specBuilder;

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
		try {
			Document document = documentAdapter.retrieve(new DocumentoId(documentId));
			attachToSign(signerId, document);
		} catch (IOException e) {
			throw new RuntimeException("Erro ao recuperar documento.", e);
		}
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
		return documentAdapter.upload(signerId.id(), signedDocument.document());
	}

}
