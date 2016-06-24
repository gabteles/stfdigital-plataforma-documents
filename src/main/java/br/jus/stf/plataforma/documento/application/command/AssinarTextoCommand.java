package br.jus.stf.plataforma.documento.application.command;

import javax.validation.constraints.NotNull;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@ApiModel(value = "Contém as informações para realizar a assinatura do texto")
public class AssinarTextoCommand {

	@NotNull
	@ApiModelProperty(value = "Identificador do texto", required = true)
	private Long textoId;

	@NotNull
	@ApiModelProperty(value = "Identificador do documento temporário", required = true)
	private String documentoTemporarioId;

	public Long getTextoId() {
		return textoId;
	}

	public String getDocumentoTemporarioId() {
		return documentoTemporarioId;
	}

}
