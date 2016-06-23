package br.jus.stf.plataforma.documento.interfaces.dto;

import org.springframework.stereotype.Component;

import br.jus.stf.plataforma.documento.domain.model.Documento;

/**
 * Monta um objeto DocumentoDto
 * 
 * @author Anderson.Araujo
 * @since 24.02.2016
 *
 */
@Component
public class DocumentoDtoAssembler {
	public DocumentoDto toDo(Documento documento){
		return new DocumentoDto(documento.identity().toLong(), documento.tamanho(), documento.quantidadePaginas());
	}
}
