package br.jus.stf.plataforma.documento.interfaces.dto;

import org.springframework.stereotype.Component;

/**
 * Monta um objeto DocumentoDto
 * 
 * @author Anderson.Araujo
 * @since 24.02.2016
 *
 */
@Component
public class DocumentoDtoAssembler {
	public DocumentoDto toDo(Long documentoId, Long tamanho, Integer quantidadePaginas){
		return new DocumentoDto(documentoId, tamanho, quantidadePaginas);
	}
}
