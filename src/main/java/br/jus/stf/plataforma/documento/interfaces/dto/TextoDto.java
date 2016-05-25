package br.jus.stf.plataforma.documento.interfaces.dto;

import org.apache.commons.lang3.Validate;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@ApiModel("Dto de Texto")
public class TextoDto {

	@ApiModelProperty("Id do texto")
	private Long id;

	@ApiModelProperty("Id do documento")
	private Long documentoId;

	public TextoDto(final Long id, final Long documentoId) {
		Validate.notNull(id);
		Validate.notNull(documentoId);

		this.id = id;
		this.documentoId = documentoId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getDocumentoId() {
		return documentoId;
	}

	public void setDocumentoId(Long documentoId) {
		this.documentoId = documentoId;
	}

}
