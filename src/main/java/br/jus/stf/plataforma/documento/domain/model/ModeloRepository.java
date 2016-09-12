package br.jus.stf.plataforma.documento.domain.model;

import java.util.List;

import br.jus.stf.core.shared.documento.ModeloId;
import br.jus.stf.core.shared.documento.TipoDocumentoId;

/**
 * Repositório de Modelo.
 * 
 * @author Tomas.Godoi
 *
 */
public interface ModeloRepository {

	/**
	 * @return
	 */
	ModeloId nextId();

	/**
	 * @param modelo
	 * @return
	 */
	<T extends Modelo> T save(T modelo);

	/**
	 * @param id
	 * @return
	 */
	Modelo findOne(ModeloId id);
	
	/**
	 * @return
	 */
	List<Modelo> findAll();

	/**
	 * @param tiposDocumento
	 * @return
	 */
	List<Modelo> findByTiposDocumento(List<TipoDocumentoId> tiposDocumento);
}
