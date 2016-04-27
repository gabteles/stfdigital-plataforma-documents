package br.jus.stf.plataforma.documento.infra;

import br.jus.stf.plataforma.documento.domain.model.Document;
import br.jus.stf.plataforma.documento.domain.model.signature.DocumentSignature;
import br.jus.stf.plataforma.documento.domain.model.signature.SignedDocument;

public class PdfTempSignedDocument implements SignedDocument {

	private Document document;
	private DocumentSignature signature;

	public PdfTempSignedDocument(Document document, DocumentSignature signature) {
		this.document = document;
		this.signature = signature;
	}

	@Override
	public DocumentSignature signature() {
		return signature;
	}

	@Override
	public Document document() {
		return document;
	}

}
