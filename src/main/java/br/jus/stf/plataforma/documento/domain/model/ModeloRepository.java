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

	ModeloId nextId();

	<T extends Modelo> T save(T modelo);

	Modelo findOne(ModeloId id);
	
	List<Modelo> findAll();

	List<Modelo> findByTiposDocumento(List<TipoDocumentoId> tiposDocumento);
}
