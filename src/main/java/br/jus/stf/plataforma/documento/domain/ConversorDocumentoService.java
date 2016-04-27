package br.jus.stf.plataforma.documento.domain;

import br.jus.stf.core.shared.documento.DocumentoId;
import br.jus.stf.plataforma.documento.domain.model.DocumentoTemporario;

public interface ConversorDocumentoService {

	DocumentoTemporario converterDocumentoFinal(DocumentoId documentoId);

}
