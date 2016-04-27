package br.jus.stf.plataforma.documento.domain.model.signature;

import br.jus.stf.plataforma.documento.domain.model.Document;
import br.jus.stf.plataforma.documento.domain.model.validation.CertificateValidation;

public class DocumentSigner {

	private DocumentSignerId id;
	private SigningSpecification spec;
	private CertificateValidation certificateValidation;

	private Document signingDocument;
	private SignedDocument signedDocument;

	public DocumentSigner(DocumentSignerId id, SigningSpecification spec, CertificateValidation certificateValidation) {
		this.id = id;
		this.spec = spec;
		this.certificateValidation = certificateValidation;
	}

	public DocumentSignerId id() {
		return id;
	}

	public PreSignature preSign() throws SigningException {
		return spec.strategy().preSign(signingDocument, certificateValidation);
	}

	public void attachDocumentToSign(Document document) {
		this.signingDocument = document;
	}

	public DocumentSignature postSign(HashSignature signature) throws SigningException {
		signedDocument = spec.strategy().postSign(signature, certificateValidation);
		return signedDocument.signature();
	}

	public SignedDocument recoverSignedDocument() {
		return signedDocument;
	}

}
