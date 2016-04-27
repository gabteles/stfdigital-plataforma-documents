package br.jus.stf.plataforma.documento.infra;

import br.jus.stf.plataforma.documento.domain.ContadorPaginas;
import br.jus.stf.plataforma.documento.domain.model.DocumentoTemporario;

public class ContadorPaginasDocx implements ContadorPaginas {

	/**
	 * N�o conta as p�ginas do Docx, pois o Onlyoffice n�o preenche esse
	 * metadado, diferentemente do Word.
	 */
	@Override
	public Integer contarPaginas(DocumentoTemporario docTemp) {
		return null;
	}

}
