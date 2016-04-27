package br.jus.stf.plataforma.documento.interfaces.commands;

import org.apache.commons.lang3.Validate;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@ApiModel("Commando para gera��o do documento final a partir do documento edit�vel")
public class GerarDocumentoFinalCommand {

	@ApiModelProperty("O id do documento edit�vel")
	private Long documento;

	public GerarDocumentoFinalCommand(Long documento) {
		Validate.notNull(documento);
		
		this.documento = documento;
	}

	public Long getDocumento() {
		return documento;
	}

	public void setDocumento(Long documento) {
		this.documento = documento;
	}

}
