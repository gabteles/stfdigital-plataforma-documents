package br.jus.stf.plataforma.documento.interfaces.dto;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * DTO usado para transferir dados de documentos persistidos.
 * 
 * @author Anderson.Araujo
 * @since 24.02.2016
 *
 */
@ApiModel(value = "DTO usado para transferir dados de documentos persistidos.")
public class DocumentoDto {
	@ApiModelProperty(value = "Id do documento.")
	private Long documentoId;
	
	@ApiModelProperty(value = "Tamanho do documento em bytes.")
	private Long tamanho;
	
	@ApiModelProperty(value = "Quantidade de páginas do documento.")
	private Integer quantidadePaginas;

	public DocumentoDto(Long documentoId, Long tamanho, Integer quantidadePaginas){
		this.documentoId = documentoId;
		this.tamanho = tamanho;
		this.quantidadePaginas = quantidadePaginas;
	}
	
	public Long getDocumentoId() {
		return documentoId;
	}

	public void setDocumentoId(Long documentoId) {
		this.documentoId = documentoId;
	}

	public Long getTamanho() {
		return tamanho;
	}

	public void setTamanho(Long tamanho) {
		this.tamanho = tamanho;
	}

	public Integer getQuantidadePaginas() {
		return quantidadePaginas;
	}

	public void setQuantidadePaginas(Integer quantidadePaginas) {
		this.quantidadePaginas = quantidadePaginas;
	}
}
