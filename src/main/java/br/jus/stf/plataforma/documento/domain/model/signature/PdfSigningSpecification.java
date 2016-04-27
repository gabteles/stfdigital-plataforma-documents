package br.jus.stf.plataforma.documento.domain.model.signature;

import br.jus.stf.plataforma.documento.domain.model.DocumentType;

public class PdfSigningSpecification implements SigningSpecification {

	private PdfSigningStrategy strategy;
	private String reason;
	private HashType hashType;

	public PdfSigningSpecification(PdfSigningStrategy strategy, String reason, HashType hashType) {
		this.strategy = strategy;
		this.reason = reason;
		this.hashType = hashType;

		this.strategy.prepareStrategyWith(this);
	}

	@Override
	public DocumentType documentType() {
		return DocumentType.PDF;
	}

	@Override
	public PdfSigningStrategy strategy() {
		return strategy;
	}

	public String reason() {
		return reason;
	}

	public HashType hashType() {
		return hashType;
	}

}
