package br.jus.stf.plataforma.documento.infra;

import br.jus.stf.plataforma.documento.domain.ContadorPaginas;
import br.jus.stf.plataforma.documento.domain.model.DocumentoTemporario;

public class ContadorPaginasImpl extends StrategyResolver<String, ContadorPaginas> implements ContadorPaginas {

	@Override
	public Integer contarPaginas(DocumentoTemporario docTemp) {
		return resolveStrategy(docTemp.contentType()).contarPaginas(docTemp);
	}

}
