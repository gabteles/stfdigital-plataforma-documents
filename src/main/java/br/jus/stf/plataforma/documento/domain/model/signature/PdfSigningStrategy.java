package br.jus.stf.plataforma.documento.domain.model.signature;

public interface PdfSigningStrategy extends SigningStrategy {

	void prepareStrategyWith(PdfSigningSpecification pdfSigningSpecification);
	
}
