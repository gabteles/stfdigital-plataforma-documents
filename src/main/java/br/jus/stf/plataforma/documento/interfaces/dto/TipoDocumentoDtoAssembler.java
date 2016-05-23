package br.jus.stf.plataforma.documento.interfaces.dto;

import org.apache.commons.lang3.Validate;
import org.springframework.stereotype.Component;

import br.jus.stf.plataforma.documento.domain.model.TipoDocumento;

/**
 * Assembler de TipoDocumentoDto
 * 
 * @author Tomas.Godoi
 *
 */
@Component
public class TipoDocumentoDtoAssembler {

	public TipoDocumentoDto toDto(TipoDocumento tipoDocumento) {
		Validate.notNull(tipoDocumento);
		
		return new TipoDocumentoDto(tipoDocumento.identity().toLong(), tipoDocumento.descricao());
	}
	
}
