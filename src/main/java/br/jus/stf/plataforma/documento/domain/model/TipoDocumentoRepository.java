package br.jus.stf.plataforma.documento.domain.model;

import java.util.List;

import org.springframework.data.repository.Repository;

import br.jus.stf.core.shared.documento.TipoDocumentoId;

/**
 * Repositório de Tipo de Documento.
 * 
 * @author Tomas.Godoi
 *
 */
public interface TipoDocumentoRepository extends Repository<TipoDocumento, TipoDocumentoId> {

	/**
	 * @return
	 */
	List<TipoDocumento> findAll();

	/**
	 * @param id
	 * @return
	 */
	TipoDocumento findOne(TipoDocumentoId id);

}
