package br.jus.stf.plataforma.documento.domain;

import br.jus.stf.core.shared.documento.DocumentoId;
import br.jus.stf.core.shared.documento.TipoDocumentoId;

/**
 * Factory de Modelo.
 * 
 * @author Tomas.Godoi
 *
 */
public interface ModeloFactory {

	/**
	 * Cria um modelo a partir do template padrão.
	 * 
	 * @param tipoDocumento Tipo do Documento
	 * @param nome Nome do modelo
	 * @return
	 */
	DocumentoId criarDocumentoModeloPadrao(TipoDocumentoId tipoDocumento, String nome);

}
