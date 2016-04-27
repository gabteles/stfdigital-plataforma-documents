package br.jus.stf.plataforma.documento.infra.persistence;

import br.jus.stf.core.shared.documento.DocumentoId;
import br.jus.stf.plataforma.documento.domain.model.ConteudoDocumento;
import br.jus.stf.plataforma.documento.domain.model.DocumentoTemporario;

/**
 * Reposit�rio de conte�do do documento.
 * 
 * @author Tomas.Godoi
 *
 */
public interface ConteudoDocumentoRepository {

	/**
	 * Faz o download do conte�do especificado.
	 * 
	 * @param numeroConteudo
	 * @return
	 */
	ConteudoDocumento downloadConteudo(String numeroConteudo);

	/**
	 * Remove o conte�do de um documento.
	 * 
	 * @param numeroConteudo N�mero do conte�do
	 */
	void deleteConteudo(String numeroConteudo);

	/**
	 * Salva o conte�do do documento tempor�rio com o id especificado,
	 * retornando o n�mero do conte�do do documento.
	 * 
	 * @param documentoId Id do Documento
	 * @param documentoTemporario Documento Tempor�rio cujo documento ser� armazenado
	 * @return N�mero do conte�do do documento
	 */
	String save(DocumentoId documentoId, DocumentoTemporario documentoTemporario);

}