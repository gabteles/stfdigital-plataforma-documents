package br.jus.stf.plataforma.documento.domain;

import java.io.InputStream;
import java.util.List;

import br.jus.stf.core.shared.documento.DocumentoId;
import br.jus.stf.core.shared.documento.DocumentoTemporarioId;
import br.jus.stf.plataforma.documento.application.command.SubstituicaoTagTexto;

/**
 * Adaptador para o contexto de documentos.
 * 
 * @author Tomas.Godoi
 *
 */
public interface DocumentoAdapter {

	/**
	 * Recupera o conteúdo de um documento.
	 * 
	 * @param documentoId
	 * @return
	 */
	InputStream recuperarConteudo(DocumentoId documentoId);

	DocumentoId salvar(DocumentoTemporarioId documentoTemporario);

	DocumentoTemporarioId upload(String nome, byte[] documento);

	DocumentoId gerarDocumentoComTags(DocumentoId documento, List<SubstituicaoTagTexto> substituicoes);

}
