package br.jus.stf.plataforma.documento.interfaces.dto;

import org.apache.commons.lang3.Validate;
import org.springframework.stereotype.Component;

import br.jus.stf.plataforma.documento.domain.model.TipoModelo;

/**
 * Assembler de TipoModeloDto
 * 
 * @author Tomas.Godoi
 *
 */
@Component
public class TipoModeloDtoAssembler {

	public TipoModeloDto toDto(TipoModelo tipoModelo) {
		Validate.notNull(tipoModelo);
		
		return new TipoModeloDto(tipoModelo.identity().toLong(), tipoModelo.descricao());
	}
	
}
