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

	TextoId nextId();

	<T extends Texto> T save(T texto);

	Texto findOne(TextoId id);
	
	List<Texto> findAll();
	
}
