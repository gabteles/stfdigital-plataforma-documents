package br.jus.stf.plataforma.documento.application.command;

import javax.validation.constraints.NotNull;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@ApiModel(value = "Contém as informações para concluir o texto")
public class ConcluirTextoCommand {

	@NotNull
	@ApiModelProperty(value = "Identificador do texto", required = true)
	private Long textoId;

	public Long getTextoId() {
		return textoId;
	}

	public void setTextoId(Long textoId) {
		this.textoId = textoId;
	}

}
