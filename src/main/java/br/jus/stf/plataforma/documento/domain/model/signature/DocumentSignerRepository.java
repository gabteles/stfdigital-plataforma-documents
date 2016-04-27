package br.jus.stf.plataforma.documento.domain.model.signature;

public interface DocumentSignerRepository {

	public DocumentSigner save(DocumentSigner signer);

	public DocumentSigner findOne(DocumentSignerId signerId);

	public DocumentSignerId nextId();
}