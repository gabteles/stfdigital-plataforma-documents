package br.jus.stf.plataforma.documento.infra;

import br.jus.stf.plataforma.documento.domain.ContadorPaginas;
import br.jus.stf.plataforma.documento.domain.model.DocumentoTemporario;

public class ContadorPaginasDocx implements ContadorPaginas {

	/**
	 * Não conta as páginas do Docx, pois o Onlyoffice não preenche esse
	 * metadado, diferentemente do Word.
	 */
	@Override
	public Integer contarPaginas(DocumentoTemporario docTemp) {
		return null;
	}

}
