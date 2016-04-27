package br.jus.stf.plataforma.documento.domain;

import java.io.IOException;

import br.jus.stf.core.shared.documento.DocumentoId;
import br.jus.stf.core.shared.documento.DocumentoTemporarioId;
import br.jus.stf.plataforma.documento.domain.model.Document;

public interface DocumentAdapter {

	Document retrieve(DocumentoId id) throws IOException;

	DocumentoTemporarioId upload(String name, Document document);

	DocumentoId save(DocumentoTemporarioId tempDocument);

}
