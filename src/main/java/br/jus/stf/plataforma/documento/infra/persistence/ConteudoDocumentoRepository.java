package br.jus.stf.plataforma.documento.infra.persistence;

import br.jus.stf.core.shared.documento.DocumentoId;
import br.jus.stf.plataforma.documento.domain.model.ConteudoDocumento;
import br.jus.stf.plataforma.documento.domain.model.DocumentoTemporario;

/**
 * Repositório de conteúdo do documento.
 * 
 * @author Tomas.Godoi
 *
 */
public interface ConteudoDocumentoRepository {

	/**
	 * Faz o download do conteúdo especificado.
	 * 
	 * @param numeroConteudo
	 * @return
	 */
	ConteudoDocumento downloadConteudo(String numeroConteudo);

	/**
	 * Remove o conteúdo de um documento.
	 * 
	 * @param numeroConteudo Número do conteúdo
	 */
	void deleteConteudo(String numeroConteudo);

	/**
	 * Salva o conteúdo do documento temporário com o id especificado,
	 * retornando o número do conteúdo do documento.
	 * 
	 * @param documentoId Id do Documento
	 * @param documentoTemporario Documento Temporário cujo documento será armazenado
	 * @return Número do conteúdo do documento
	 */
	String save(DocumentoId documentoId, DocumentoTemporario documentoTemporario);

}