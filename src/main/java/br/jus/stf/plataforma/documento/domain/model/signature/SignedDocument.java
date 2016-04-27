package br.jus.stf.plataforma.documento.domain.model.signature;

import br.jus.stf.plataforma.documento.domain.model.Document;

public interface SignedDocument {
	
	Document document();
	
	DocumentSignature signature();

}
