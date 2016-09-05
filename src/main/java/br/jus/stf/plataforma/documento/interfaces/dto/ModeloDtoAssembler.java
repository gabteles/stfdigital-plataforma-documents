package br.jus.stf.plataforma.documento.interfaces.dto;

import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.jus.stf.plataforma.documento.domain.model.Modelo;

/**
 * Assembler de ModeloDto
 * 
 * @author Tomas.Godoi
 *
 */
@Component
public class ModeloDtoAssembler {

	@Autowired
	private TipoDocumentoDtoAssembler tipoDocumentoDtoAssembler;

	/**
	 * @param modelo
	 * @return
	 */
	public ModeloDto toDto(Modelo modelo) {
		Validate.notNull(modelo);

		return new ModeloDto(modelo.identity().toLong(), tipoDocumentoDtoAssembler.toDto(modelo.tipoDocumento()), modelo.nome(),
		        modelo.template().toLong());
	}

}
