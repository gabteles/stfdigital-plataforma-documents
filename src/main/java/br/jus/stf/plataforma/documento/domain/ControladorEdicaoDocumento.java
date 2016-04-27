package br.jus.stf.plataforma.documento.domain;

import br.jus.stf.core.shared.documento.DocumentoId;
import br.jus.stf.plataforma.documento.domain.model.Edicao;

public interface ControladorEdicaoDocumento {

	Edicao gerarEdicao(DocumentoId id);
	
	void excluirEdicao(String numeroEdicao);
	
	Edicao recuperarEdicao(DocumentoId id);
	
	void ativarEdicao(String numeroEdicao);

}
