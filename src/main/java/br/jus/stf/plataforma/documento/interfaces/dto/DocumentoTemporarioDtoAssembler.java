package br.jus.stf.plataforma.documento.interfaces.dto;

import org.springframework.stereotype.Component;

import br.jus.stf.core.shared.documento.DocumentoId;

/**
 * Converte um documento em um dto
 * 
 * @author Lucas.Rodrigues
 *
 */
@Component
public class DocumentoTemporarioDtoAssembler {

	public DocumentoTemporarioDto toDto(String tempId, DocumentoId documentoId) {
		return new DocumentoTemporarioDto(tempId, documentoId.toLong());
	}
	
}
