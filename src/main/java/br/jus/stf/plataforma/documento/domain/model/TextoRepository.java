package br.jus.stf.plataforma.documento.domain.model;

import java.util.List;

import br.jus.stf.core.shared.documento.TextoId;

/**
 * Repositório da entidade Texto.
 * 
 * @author Tomas.Godoi
 *
 */
public interface TextoRepository {

	/**
	 * @return
	 */
	TextoId nextId();

	/**
	 * @param texto
	 * @return
	 */
	<T extends Texto> T save(T texto);

	/**
	 * @param id
	 * @return
	 */
	Texto findOne(TextoId id);
	
	/**
	 * @return
	 */
	List<Texto> findAll();
	
}
