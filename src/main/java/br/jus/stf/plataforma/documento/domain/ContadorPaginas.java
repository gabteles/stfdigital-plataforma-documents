package br.jus.stf.plataforma.documento.domain;

import br.jus.stf.plataforma.documento.domain.model.DocumentoTemporario;
import br.jus.stf.plataforma.documento.infra.Strategy;

public interface ContadorPaginas extends Strategy<ContadorPaginas> {

	Integer contarPaginas(DocumentoTemporario docTemp);

}
